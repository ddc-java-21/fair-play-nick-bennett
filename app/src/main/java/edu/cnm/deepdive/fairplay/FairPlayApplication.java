package edu.cnm.deepdive.fairplay;

import android.app.Application;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.HiltAndroidApp;
import edu.cnm.deepdive.fairplay.service.ApodProxy;
import edu.cnm.deepdive.fairplay.service.ApodService;

@HiltAndroidApp
public class FairPlayApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ApodProxy.setContext(this);
    ApodService.setContext(this);
    Picasso.setSingletonInstance(new Picasso.Builder(this).build());
  }

}
