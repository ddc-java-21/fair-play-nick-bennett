package edu.cnm.deepdive.apod.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.apod.model.Apod;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApodService {

  private final ApodProxy proxy;
  private final String apiKey;

  public ApodService() throws IOException {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        // TODO: 5/27/25 Add a type adapter for LocalDate
        .create();
    Interceptor loggingInterceptor = new HttpLoggingInterceptor()
        .setLevel(Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();
    proxy = new Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(getLocalProperty("base_url"))
        .build()
        .create(ApodProxy.class);
    apiKey = getLocalProperty("api_key");
  }

  public Apod getApod(LocalDate date) throws IOException {
    return proxy.get(date, apiKey)
        .execute()
        .body();
  }

  private String getLocalProperty(String key) throws IOException {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("local.properties")) {
      Properties props = new Properties();
      props.load(input);
      return props.getProperty(key);
    }
  }

}
