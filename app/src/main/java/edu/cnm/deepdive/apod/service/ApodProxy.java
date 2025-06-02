package edu.cnm.deepdive.apod.service;

import edu.cnm.deepdive.apod.model.Apod;
import io.reactivex.rxjava3.core.Single;
import java.time.LocalDate;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApodProxy {

  @GET("planetary/apod")
  Single<Apod> get(@Query("date") LocalDate date, @Query("api_key") String apiKey);

  @GET("planetary/apod")
  Single<Apod[]> get(
      @Query("start_date") LocalDate startDate, @Query("end_date") LocalDate endDate,
      @Query("api_key") String apiKey);

  @GET
  Single<ResponseBody> download(@Url String url);

}
