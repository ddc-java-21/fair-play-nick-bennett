package edu.cnm.deepdive.apod.view;

import edu.cnm.deepdive.apod.model.Apod;
import java.util.ResourceBundle;

public class ApodView {

  private static final String BUNDLE_NAME = "strings";
  private static final String ATTRIBUTES_FORMAT_KEY = "attributes_format";
  private static final String NO_CONTENT_KEY = "no_content";

  private final String attributesFormat;
  private final String noContent;

  public ApodView() {
    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    attributesFormat = bundle.getString(ATTRIBUTES_FORMAT_KEY);
    noContent = bundle.getString(NO_CONTENT_KEY);
  }

  public String render(Apod apod) {
    return attributesFormat.formatted(
        apod.getDate(),
        apod.getTitle(),
        apod.getExplanation(),
        apod.getMediaType(),
        ifNull(apod.getUrl(), noContent),
        ifNull(apod.getHdurl(), noContent),
        ifNull(apod.getCopyright(), noContent)
    );
  }

  private static Object ifNull(Object preferred, Object alternative) {
    return (preferred != null) ? preferred : alternative;
  }
}
