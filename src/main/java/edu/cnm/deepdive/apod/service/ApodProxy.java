package edu.cnm.deepdive.apod.service;

import edu.cnm.deepdive.apod.model.Apod;
import java.net.URL;
import java.time.LocalDate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApodProxy {

  @GET("planetary/apod")
  Call<Apod> get(@Query("date") LocalDate date, @Query("api_key") String apiKey);

  // TODO: 5/28/25 Add request for range of dates.

  @GET
  Call<ResponseBody> download(@Url String url);

}
