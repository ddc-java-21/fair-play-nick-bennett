package edu.cnm.deepdive.apod.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.apod.databinding.ItemApodBinding;
import edu.cnm.deepdive.apod.model.Apod;

public class ApodAdapter extends Adapter<ViewHolder> {


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  private static class Holder extends ViewHolder {

    private final ItemApodBinding binding;

    Holder(@NonNull ItemApodBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(int position, Apod apod) {
      binding.title.setText(apod.getTitle());
      // TODO: 6/3/25 Set content of binding.thumbnail and binding.date
    }

  }

}
