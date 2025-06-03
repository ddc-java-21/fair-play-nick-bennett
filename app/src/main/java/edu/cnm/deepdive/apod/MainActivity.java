package edu.cnm.deepdive.apod;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.apod.adapter.ApodAdapter;
import edu.cnm.deepdive.apod.databinding.ActivityMainBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;

  private ApodViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());

    viewModel = new ViewModelProvider(this)
        .get(ApodViewModel.class);
    LocalDate today = LocalDate.now();
    viewModel.fetch(today.minusMonths(1), today);
    viewModel
        .getApods()
        .observe(this, (apods) -> binding.apods.setAdapter(new ApodAdapter(this, apods)));
  }


}

