package edu.cnm.deepdive.apod;

import android.os.Bundle;

import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ApodViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main); // FIXME: 6/2/25 Use view binding when relevant.
    viewModel = new ViewModelProvider(this)
        .get(ApodViewModel.class);
    viewModel.fetch(LocalDate.now());
    viewModel
        .getApod()
        .observe(this, (apod) ->
            Log.d(TAG, apod.getTitle()));
  }


}

