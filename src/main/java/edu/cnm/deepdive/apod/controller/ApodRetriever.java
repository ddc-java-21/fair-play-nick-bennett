package edu.cnm.deepdive.apod.controller;

import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.service.ApodService;
import edu.cnm.deepdive.apod.view.ApodView;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import retrofit2.Response;

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

  public ApodRetriever(ApodService service, ApodView view, PrintStream out) {
    this.service = service;
    this.view = view;
    this.out = out;
  }

  @Override
  public Integer call() {
    try {
      Apod apod = service.getApod(date);
      if (!mute) {
        String representation = view.render(apod);
        out.println(representation);
      }
      if (stdDefOutput != null) {
        Matcher matcher = FILENAME_PATTERN.matcher(stdDefOutput);
        if (matcher.matches()) {
          String stdDefFilename = (stdDefOutput.isBlank())
              ? matcher.group(1)
              : PROVIDED_FILENAME_FORMAT.formatted(stdDefOutput, matcher.group(2));
        }
      }
      return 0;
    } catch (IOException e) {
      return 1;
    } catch (RuntimeException e) {
      return 2;
    }
  }

}
