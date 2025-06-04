package edu.cnm.deepdive.apod.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.time.LocalDate;

public class ImageFragment extends Fragment {

  private ApodViewModel viewModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // TODO: 6/4/25 Inflate view from layout using binding class; attach event listeners. 
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(getViewLifecycleOwner(), (apod) -> {
          // TODO: 6/4/25 Display image from apod. 
        });
    viewModel.fetch(ImageFragmentArgs.fromBundle(getArguments()).getDate());
  }

  @Override
  public void onDestroyView() {
    // TODO: 6/4/25 Release reference to binding object. 
    super.onDestroyView();
  }
  
}
