package edu.cnm.deepdive.apod.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.databinding.FragmentImageBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;

public class ImageFragment extends Fragment implements MenuProvider {

  private FragmentImageBinding binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentImageBinding.inflate(inflater, container, false);
    requireActivity().addMenuProvider(this, getViewLifecycleOwner(), State.RESUMED);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ApodViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(getViewLifecycleOwner(), this::displayApod);
  }


  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.detail_actions, menu);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = false;
    if (menuItem.getItemId() == R.id.show_info) {
      handled = true;
      Navigation.findNavController(binding.getRoot())
          .navigate(ImageFragmentDirections.displayInfo());
    }
    return handled;
  }

  private void displayApod(Apod apod) {
    //noinspection DataFlowIssue
    ((AppCompatActivity) requireActivity())
        .getSupportActionBar()
        .setTitle(apod.getTitle());
    Picasso.get()
        .load(Uri.parse(apod.getUrl().toString()))
        .into(new ImageFinalizer());
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
