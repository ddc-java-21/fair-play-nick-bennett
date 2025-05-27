package edu.cnm.deepdive.apod.service;

import edu.cnm.deepdive.apod.model.Apod;
import java.time.LocalDate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodProxy {

  @GET("planetary/apod")
  Call<Apod> get(@Query("date") LocalDate date, @Query("api_key") String apiKey);

}
