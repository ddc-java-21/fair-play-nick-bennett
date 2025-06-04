package edu.cnm.deepdive.apod.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.apod.R;
import edu.cnm.deepdive.apod.databinding.ItemApodBinding;
import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.model.Apod.MediaType;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ApodAdapter extends Adapter<ViewHolder> {

  private final List<Apod> apods;
  private final OnApodClickListener onThumbnailClickListener;
  private final OnApodClickListener onInfoClickListener;
  private final LayoutInflater inflater;
  private final DateTimeFormatter formatter;

  public ApodAdapter(@NonNull Context context, @NonNull List<Apod> apods,
      @NonNull OnApodClickListener onThumbnailClickListener,
      @NonNull OnApodClickListener onInfoClickListener) {
    this.apods = apods;
    inflater = LayoutInflater.from(context);
    this.onThumbnailClickListener = onThumbnailClickListener;
    this.onInfoClickListener = onInfoClickListener;
    formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
    ItemApodBinding binding = ItemApodBinding.inflate(inflater, viewGroup, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    ((Holder) viewHolder).bind(position, apods.get(position));
  }

  @Override
  public int getItemCount() {
    return apods.size();
  }

  private class Holder extends ViewHolder {

    private final ItemApodBinding binding;

    Holder(@NonNull ItemApodBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(int position, Apod apod) {
      binding.title.setText(apod.getTitle());
      binding.date.setText(formatter.format(apod.getDate()));
      if (apod.getMediaType() == MediaType.IMAGE) {
        Picasso.get()
            .load(Uri.parse(apod.getUrl().toString()))
            .into(binding.thumbnail);
        binding.mediaTypeThumbnail.setImageResource(R.drawable.photo_camera);
      } else {
        // TODO: 6/4/25 Load video thumbnail for Youtube video.
        binding.thumbnail.setImageResource(R.drawable.video_camera);
        binding.mediaTypeThumbnail.setImageResource(R.drawable.video_camera);
      }
      binding.thumbnail.setContentDescription(apod.getTitle()); // TODO: 6/4/25 Include more info.
      binding.thumbnail.setOnClickListener(
          (v) -> onThumbnailClickListener.onApodClick(apod, position));
      binding.info.setOnClickListener((v) -> onInfoClickListener.onApodClick(apod, position));
    }

  }

  @FunctionalInterface
  public interface OnApodClickListener {

    void onApodClick(Apod apod, int position);

  }

}
