package edu.cnm.deepdive.apod.controller;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ImageFragment extends Fragment implements MenuProvider {

  private FragmentImageBinding binding;
  private Apod apod;

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
    this.apod = apod;
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

  private class GalleryImageFinalizer implements Target {

    private final Apod apod;

    private GalleryImageFinalizer(Apod apod) {
      this.apod = apod;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
      ContentValues values = new ContentValues();
      values.put(MediaColumns.DISPLAY_NAME, apod.getTitle().strip());
      values.put(MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/APOD");
      values.put(MediaColumns.IS_PENDING, 1);

      ContentResolver resolver = requireContext().getContentResolver();
      Uri uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
      if (uri != null) {
        try (OutputStream out = resolver.openOutputStream(uri)) {

        } catch (IOException e) {
          throw new RuntimeException(e); // FIXME: 6/6/25 Notify user of failure.
        }
      } else {
        // We should never get here!
        // FIXME: 6/6/25 Notify user of failure.
      }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable drawable) {
      // TODO: 6/6/25 Display a snackbar to user, indicating failure.
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {
      // TODO: 6/6/25 Obtain URIs, random filenames, etc.
    }

  }

}
