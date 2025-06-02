package edu.cnm.deepdive.apod.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.cnm.deepdive.apod.model.Apod;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApodService {

  private final ApodProxy proxy;
  private final String apiKey;
  private final Scheduler scheduler;

  public ApodService() throws IOException {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
        .create();
    Interceptor loggingInterceptor = new HttpLoggingInterceptor()
        .setLevel(Level.NONE);
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();
    proxy = new Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(getLocalProperty("base_url"))
        .build()
        .create(ApodProxy.class);
    apiKey = getLocalProperty("api_key"); // FIXME: 6/2/25 Read from string resources.
    scheduler = Schedulers.io();
  }

  public Single<Apod> getApod(LocalDate date) throws IOException {
    return proxy
        .get(date, apiKey)
        .subscribeOn(scheduler);
  }

  public Single<Apod[]> getApods(LocalDate startDate, LocalDate endDate) throws IOException {
    return proxy
        .get(startDate, endDate, apiKey)
        .subscribeOn(scheduler);
  }

  private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return LocalDate.parse(jsonElement.getAsString());
    }

  }

}
