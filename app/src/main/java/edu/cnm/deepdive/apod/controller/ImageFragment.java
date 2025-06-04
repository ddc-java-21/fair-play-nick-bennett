package edu.cnm.deepdive.apod.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import edu.cnm.deepdive.apod.databinding.FragmentImageBinding;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.time.LocalDate;

public class ImageFragment extends Fragment {

  private FragmentImageBinding binding;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentImageBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(getViewLifecycleOwner(), (apod) -> Picasso.get()
            .load(Uri.parse(apod.getUrl().toString()))
            .into(new ImageFinalizer()));
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private class ImageFinalizer implements Target {

    @Override
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
      binding.image.setImageBitmap(bitmap);
      binding.loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable drawable) {
      // TODO: 6/4/25 Handle as appropriate.
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {
      // TODO: 6/4/25 Handle as appropriate.
    }

  }

}
