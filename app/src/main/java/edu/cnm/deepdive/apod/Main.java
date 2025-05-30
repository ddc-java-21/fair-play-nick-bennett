package edu.cnm.deepdive.apod;

import edu.cnm.deepdive.apod.controller.ApodRetriever;
import edu.cnm.deepdive.apod.service.ApodService;
import edu.cnm.deepdive.apod.view.ApodView;
import java.io.IOException;
import picocli.CommandLine;

public class Main {

  public static void main(String[] args) throws IOException {
    new CommandLine(
        new ApodRetriever(new ApodService(), new ApodView(), System.out)).execute(args);
  }

}
