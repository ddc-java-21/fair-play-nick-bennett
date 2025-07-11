package edu.cnm.deepdive.fairplay.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.fairplay.model.Apod;
import edu.cnm.deepdive.fairplay.service.ApodService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ApodViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private static final String TAG = ApodViewModel.class.getSimpleName();

  private final MutableLiveData<Apod> apod;
  private final MutableLiveData<List<Apod>> apods;
  private final MutableLiveData<Uri> downloadedImage;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final ApodService apodService;

  public ApodViewModel(@NotNull Application application) {
    super(application);
    apod = new MutableLiveData<>();
    apods = new MutableLiveData<>();
    downloadedImage = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    apodService = ApodService.getInstance();
    LocalDate today = LocalDate.now();
    LocalDate lastMonth = today.minusMonths(1);
    fetch(lastMonth, today);
  }

  public LiveData<Apod> getApod() {
    return apod;
  }

  public void setApod(Apod apod) {
    this.apod.setValue(apod);
  }

  public LiveData<List<Apod>> getApods() {
    return apods;
  }

  public LiveData<Uri> getDownloadedImage() {
    return downloadedImage;
  }

  public void clearDownloadedImage() {
    downloadedImage.setValue(null);
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetch(LocalDate date) {
    throwable.setValue(null);
    apodService
        .getApod(date)
        .subscribe(
            apod::postValue,
            this::postThrowable,
            pending
        );
  }

  public void fetch(LocalDate startDate, LocalDate endDate) {
    throwable.setValue(null);
    apodService
        .getApods(startDate, endDate)
        .subscribe(
            apods::postValue,
            this::postThrowable,
            pending
        );
  }

  public void downloadImage(String title, URL url) {
    throwable.setValue(null);
    clearDownloadedImage();
    apodService
        .downloadImage(title, url)
        .subscribe(
            downloadedImage::postValue,
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NotNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
