package edu.cnm.deepdive.apod.service;

import android.annotation.SuppressLint;
import android.content.Context;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.model.Apod;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ApodService {

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
