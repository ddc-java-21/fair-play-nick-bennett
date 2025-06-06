package edu.cnm.deepdive.apod.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.apod.databinding.FragmentInfoBinding;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class InfoFragment extends BottomSheetDialogFragment {

  private FragmentInfoBinding binding;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentInfoBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(getViewLifecycleOwner(), (apod) -> {
          DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
          binding.date.setText(apod.getDate().format(formatter));
          binding.title.setText(apod.getTitle().strip());
          binding.description.setText(apod.getExplanation().strip());
          if (apod.getCopyright() != null && !apod.getCopyright().isBlank()) {
            binding.copyright.setText(apod.getCopyright().strip());
          } else {
            binding.copyrightLayout.setVisibility(View.GONE);
          }
          // TODO: 6/5/25 Populate view widgets with data from apod.
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

}
