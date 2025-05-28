package edu.cnm.deepdive.apod.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.cnm.deepdive.apod.model.Apod;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
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
    apiKey = getLocalProperty("api_key");
  }

  public Apod getApod(LocalDate date) throws IOException {
    Response<Apod> response = proxy.get(date, apiKey).execute();
    if (!response.isSuccessful()) {
      throw new RuntimeException();
    }
    return response.body();
  }

  public InputStream getImageStream(URL url) throws IOException {
    Response<ResponseBody> response = proxy.download(url.toString()).execute();
    if (!response.isSuccessful()) {
      throw new RuntimeException();
    }
    //noinspection resource,DataFlowIssue
    return response.body().byteStream();
  }

  private String getLocalProperty(String key) throws IOException {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("local.properties")) {
      Properties props = new Properties();
      props.load(input);
      return props.getProperty(key);
    }
  }

  private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return LocalDate.parse(jsonElement.getAsString());
    }

  }

}
