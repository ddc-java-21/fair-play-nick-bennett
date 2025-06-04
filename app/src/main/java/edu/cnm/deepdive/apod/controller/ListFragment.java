package edu.cnm.deepdive.apod.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.adapter.ApodAdapter;
import edu.cnm.deepdive.apod.databinding.FragmentListBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.util.List;

public class ListFragment extends Fragment {

  private static final String TAG = ListFragment.class.getSimpleName();

  private FragmentListBinding binding;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentListBinding.inflate(inflater, container, false);
    // TODO: 6/4/25 Attach event listeners to view widgets.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel
        .getApods()
        .observe(getViewLifecycleOwner(),
            (apods) -> {
              ApodAdapter adapter = new ApodAdapter(requireContext(), apods,
                  (apod, pos) -> navigateToMedia(apod),
                  (apod, pos) -> Log.d(TAG, "Info clicked for " + apod.getDate())
              );
              binding.apods.setAdapter(adapter);
            });
  }

  private void navigateToMedia(Apod apod) {
    if (apod.getMediaType() == null) {
      Snackbar.make(
              binding.getRoot(), R.string.no_media_display, Snackbar.LENGTH_LONG)
          .show();
    } else {
      NavController navController = Navigation.findNavController(
          binding.getRoot());
      switch (apod.getMediaType()) {
        case IMAGE -> navController
            .navigate(ListFragmentDirections.displayImage(apod.getDate()));
        case VIDEO -> navController
            .navigate(ListFragmentDirections.displayVideo(apod.getDate()));
      }
    }
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
