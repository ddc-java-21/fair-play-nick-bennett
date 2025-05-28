package edu.cnm.deepdive.apod.view;

import edu.cnm.deepdive.apod.model.Apod;
import java.util.ResourceBundle;

public class ApodView {

  private static final String BUNDLE_NAME = "strings";
  private static final String ATTRIBUTES_FORMAT_KEY = "attributes_format";

  private final String attributesFormat;

  public ApodView() {
    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    attributesFormat = bundle.getString(ATTRIBUTES_FORMAT_KEY);
  }

  public String render(Apod apod) {
    return attributesFormat.formatted(apod.getDate(), apod.getTitle(), apod.getExplanation(),
        apod.getMediaType(), apod.getUrl(), apod.getHdurl(), apod.getCopyright());
  }

}
