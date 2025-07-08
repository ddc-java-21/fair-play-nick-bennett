package edu.cnm.deepdive.fairplay.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.net.URL;
import java.time.LocalDate;

@Entity(tableName = "apod")
public class Apod {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "apod_id")
  private long id;

  @Expose(serialize = false, deserialize = true)
  @ColumnInfo(name = "date_created")
  private final LocalDate date;

  @Expose(serialize = false, deserialize = true)
  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private final String title;

  @Expose(serialize = false, deserialize = true)
  private final String explanation;

  @Expose(serialize = false, deserialize = true)
  private final String copyright;

  @Expose(serialize = false, deserialize = true)
  private final URL url;

  @Expose(serialize = false, deserialize = true)
  private final URL hdurl;

  @Expose(serialize = false, deserialize = true)
  @SerializedName("media_type")
  @ColumnInfo(name = "media_type")
  private final MediaType mediaType;

  Apod(LocalDate date, String title, String explanation, String copyright, URL url, URL hdurl,
      MediaType mediaType) {
    this.date = date;
    this.title = title;
    this.explanation = explanation;
    this.copyright = copyright;
    this.url = url;
    this.hdurl = hdurl;
    this.mediaType = mediaType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getTitle() {
    return title;
  }

  public String getExplanation() {
    return explanation;
  }

  public String getCopyright() {
    return copyright;
  }

  public URL getUrl() {
    return url;
  }

  public URL getHdurl() {
    return hdurl;
  }

  public MediaType getMediaType() {
    return mediaType;
  }

  public enum MediaType {

    @SerializedName("image")
    IMAGE,
    @SerializedName("video")
    VIDEO;

  }

}
