package edu.cnm.deepdive.apod;

import edu.cnm.deepdive.apod.controller.ApodRetriever;
import java.io.IOException;
import picocli.CommandLine;

public class Main {

  public static void main(String[] args) throws IOException {
    System.exit(new CommandLine(new ApodRetriever()).execute(args));
  }

}
