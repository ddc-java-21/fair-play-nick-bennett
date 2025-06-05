package edu.cnm.deepdive.apod.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.apod.databinding.FragmentVideoBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.viewmodel.ApodViewModel;

public class VideoFragment extends Fragment {

  private FragmentVideoBinding binding;

  @SuppressLint("SetJavaScriptEnabled")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentVideoBinding.inflate(inflater, container, false);
    binding.content.setWebViewClient(new Client());
    WebSettings settings = binding.content.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
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

  private void displayApod(Apod apod) {
    //noinspection DataFlowIssue
    ((AppCompatActivity) requireActivity())
        .getSupportActionBar()
        .setTitle(apod.getTitle());
    binding.content.loadUrl(apod.getUrl().toString());
  }

  private class Client extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      binding.loadingIndicator.setVisibility(View.GONE);
    }
  }

}
