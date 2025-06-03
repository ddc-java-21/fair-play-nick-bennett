package edu.cnm.deepdive.apod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.apod.databinding.ItemApodBinding;
import edu.cnm.deepdive.apod.model.Apod;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ApodAdapter extends Adapter<ViewHolder> {

  private final List<Apod> apods;
  private final LayoutInflater inflater;
  private final DateTimeFormatter formatter;

  public ApodAdapter(Context context, List<Apod> apods) {
    this.apods = apods;
    inflater = LayoutInflater.from(context);
    formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
    ItemApodBinding binding = ItemApodBinding.inflate(inflater, viewGroup, false);
    return new Holder(binding, formatter);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    ((Holder) viewHolder).bind(position, apods.get(position));
  }

  @Override
  public int getItemCount() {
    return apods.size();
  }

  private static class Holder extends ViewHolder {

    private final ItemApodBinding binding;
    private final DateTimeFormatter formatter;

    Holder(@NonNull ItemApodBinding binding, DateTimeFormatter formatter) {
      super(binding.getRoot());
      this.binding = binding;
      this.formatter = formatter;
    }

    void bind(int position, Apod apod) {
      binding.title.setText(apod.getTitle());
      binding.date.setText(formatter.format(apod.getDate()));
      // TODO: 6/3/25 Set content of binding.thumbnail
    }

  }

}
