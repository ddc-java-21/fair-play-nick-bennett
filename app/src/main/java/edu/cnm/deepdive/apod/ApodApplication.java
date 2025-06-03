package edu.cnm.deepdive.apod;

import android.app.Application;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.apod.service.ApodProxy;
import edu.cnm.deepdive.apod.service.ApodService;

public class ApodApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ApodProxy.setContext(this);
    ApodService.setContext(this);
    Picasso.setSingletonInstance(new Picasso.Builder(this).build());
  }

}
