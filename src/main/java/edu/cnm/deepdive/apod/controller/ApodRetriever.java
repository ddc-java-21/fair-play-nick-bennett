package edu.cnm.deepdive.apod.controller;

import edu.cnm.deepdive.apod.model.Apod;
import edu.cnm.deepdive.apod.service.ApodService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import retrofit2.Response;

@Command(name = "apod", requiredOptionMarker = '*', sortSynopsis = false, sortOptions = false)
public class ApodRetriever implements Callable<Integer> {

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

  @Override
  public Integer call() {
    try {
      ApodService service = new ApodService();
      Apod apod = service.getApod(date);
      return 0;
    } catch (IOException e) {
      return 1;
    } catch (RuntimeException e) {
      return 2;
    }
  }

}
