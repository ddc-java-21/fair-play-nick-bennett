package edu.cnm.deepdive.apod.service;

import android.annotation.SuppressLint;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.model.Apod;
import io.reactivex.rxjava3.core.Single;
import java.lang.reflect.Type;
import java.time.LocalDate;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApodProxy {

  Context[] contexts = new Context[1];

  @GET("planetary/apod")
  Single<Apod> get(@Query("date") LocalDate date, @Query("api_key") String apiKey);

  @GET("planetary/apod")
  Single<Apod[]> get(
      @Query("start_date") LocalDate startDate, @Query("end_date") LocalDate endDate,
      @Query("api_key") String apiKey);

  @GET
  Call<ResponseBody> download(@Url String url);

  static ApodProxy getInstance() {
    return Holder.INSTANCE;
  }

  static void setContext(Context context) {
    contexts[0] = context;
  }

  class Holder {

    private static final ApodProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
          .create();
      Interceptor loggingInterceptor = new HttpLoggingInterceptor()
          .setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(loggingInterceptor)
          .build();
      INSTANCE = new Retrofit.Builder()
          .client(client)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
          .baseUrl(contexts[0].getString(R.string.base_url))
          .build()
          .create(ApodProxy.class);
    }

    private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

      @Override
      public LocalDate deserialize(JsonElement jsonElement, Type type,
          JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDate.parse(jsonElement.getAsString());
      }

    }

  }

}
