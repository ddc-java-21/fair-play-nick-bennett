package edu.cnm.deepdive.apod.service;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.model.Apod;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ApodService {

  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final int BUFFER_SIZE = 16_384;

  @SuppressLint("StaticFieldLeak")
  private static Context context;

  private final ApodProxy proxy;
  private final String apiKey;
  private final Scheduler scheduler;

  private ApodService() throws IOException {
    proxy = ApodProxy.getInstance();
    apiKey = context.getString(R.string.api_key);
    scheduler = Schedulers.io();
  }

  public static void setContext(Context context) {
    ApodService.context = context;
  }

  public Single<Apod> getApod(LocalDate date) {
    return proxy
        .get(date, apiKey)
        .subscribeOn(scheduler);
  }

  public Single<List<Apod>> getApods(LocalDate startDate, LocalDate endDate) {
    return proxy
        .get(startDate, endDate, apiKey)
        .subscribeOn(scheduler)
        .map(Arrays::asList);
  }

  /** @noinspection BlockingMethodInNonBlockingContext*/
  public Completable downloadImage(String title, URL url) {
    return Completable.create((CompletableEmitter emitter) -> {
      Response<ResponseBody> response = proxy.download(url.toString()).execute();
      if (response.isSuccessful()) {
        ContentValues values = new ContentValues();
        values.put(MediaColumns.DISPLAY_NAME, title);
        values.put(MediaColumns.RELATIVE_PATH,
            context.getString(R.string.apod_directory_format, Environment.DIRECTORY_PICTURES));
        values.put(MediaColumns.IS_PENDING, 1);
        String mimeType = response.headers().get(CONTENT_TYPE_HEADER);
        if (mimeType != null) {
          values.put(MediaColumns.MIME_TYPE, mimeType);
        }
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
          //noinspection DataFlowIssue
          try (
              ResponseBody responseBody = response.body();
              InputStream input =  responseBody.byteStream();
              OutputStream output = resolver.openOutputStream(uri);
          ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) >= 0) {
              output.write(buffer, 0, bytesRead);
            }
            values.clear();
            values.put(MediaColumns.IS_PENDING, 0);
            resolver.update(uri, values, null, null);
          } catch (IOException e) {
            resolver.delete(uri, null, null);
            emitter.onError(e);
          }
        } else {
          emitter.onError(new RuntimeException());
        }
      } else {
        emitter.onError(new IOException(response.message()));
      }
      emitter.onComplete();
    })
        .subscribeOn(scheduler);
  }

  public static ApodService getInstance() {
    return Holder.INSTANCE;
  }

  private static class Holder {

    @SuppressLint("StaticFieldLeak")
    private static final ApodService INSTANCE;

    static {
      try {
        INSTANCE = new ApodService();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
