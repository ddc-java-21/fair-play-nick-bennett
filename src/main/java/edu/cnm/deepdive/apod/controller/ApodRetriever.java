package edu.cnm.deepdive.apod.controller;

import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.service.ApodService;
import edu.cnm.deepdive.apod.view.ApodView;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Uses NASA APOD API to retrieve one or more Astronomy Pictures of the Day, displaying the textual
 * information (by default) and optionally downloading any images.
 */
@Command(name = "apod", requiredOptionMarker = '*', sortSynopsis = false, sortOptions = false)
public class ApodRetriever implements Callable<Integer> {

  private static final Pattern FILENAME_PATTERN = Pattern.compile("^.*/([^/]+\\.([^/.]+))$");
  private static final String PROVIDED_FILENAME_FORMAT = "%1$s.%2$s";

  private final ApodService service;
  private final ApodView view;
  private final PrintStream out;

  @Parameters(
      index = "0",
      description = "date (in YYYY-MM-DD format) of desired Astronomy Picture of the Day"
  )
  private LocalDate date;

  @Option(
      names = {"-m", "--mute"},
      description = "flag to mute (silence) display of Astronomy Picture of the Day attributes"
  )
  private boolean mute;

  @Option(
      names = {"--std-def"}, arity = "0..1", paramLabel = "FILE",
      description = "name (without extension) of standard-definition output file"
  )
  private String stdDefOutput;

  @Option(
      names = {"--high-def"}, arity = "0..1", paramLabel = "FILE",
      description = "name (without extension) of high-definition output file"
  )
  private String highDefOutput;

  @Option(
      names = {"-?", "-h", "--help"}, usageHelp = true, description = "display this help and exit")
  private boolean help;

  /**
   * Initializes this instance to use the specified {@link ApodService}, {@link ApodView}, and
   * {@link PrintStream}.
   *
   * @param service {@code ApodService} used to communicate with NASA APOD API.
   * @param view    {@code ApodView} used to render an {@link Apod} instance to a {@code String}.
   * @param out     {@code PrintStream} to which rendered output is sent.
   */
  public ApodRetriever(ApodService service, ApodView view, PrintStream out) {
    this.service = service;
    this.view = view;
    this.out = out;
  }

  /**
   * Retreives {@link Apod} instance from NASA APOD API, prints its attributes, and optionally
   * downloads the image(s).
   *
   * @return Result code, where zero (0) indicates no error, and non-zero indicates some error
   * condition.
   */
  @Override
  public Integer call() {
    try {
      Apod apod = service.getApod(date);
      if (!mute) {
        String representation = view.render(apod);
        out.println(representation);
      }
      downloadImage(stdDefOutput, apod.getUrl());
      downloadImage(highDefOutput, apod.getHdurl());
      return 0;
    } catch (IOException e) {
      return 1;
    } catch (RuntimeException e) {
      return 2;
    }
  }

  private void downloadImage(String downloadOption, URL imageUrl) throws IOException {
    if (downloadOption != null) {
      Matcher matcher = FILENAME_PATTERN.matcher(imageUrl.toString());
      if (matcher.matches()) {
        String filename = (downloadOption.isBlank())
            ? matcher.group(1)
            : PROVIDED_FILENAME_FORMAT.formatted(downloadOption, matcher.group(2));
        Path output = Paths.get(filename);
        InputStream input = service.getImageStream(imageUrl);
        Files.copy(input, output, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

}
