package edu.cnm.deepdive.apod.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.apod.adapter.ApodAdapter;
import edu.cnm.deepdive.apod.databinding.FragmentListBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.util.List;

public class ListFragment extends Fragment {

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
            (apods) -> binding.apods.setAdapter(new ApodAdapter(requireContext(), apods)));
  }

  @Override
  public void onDestroyView() {
    // TODO: 6/4/25 Set reference to view binding object to null.
    super.onDestroyView();
  }

}
