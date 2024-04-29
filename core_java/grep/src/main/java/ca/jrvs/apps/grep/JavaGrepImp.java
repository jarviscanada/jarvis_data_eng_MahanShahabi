package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  /**
   * Top level search workflow.
   * Method ran by main to run the grep app. searches through specified root path
   * and finds each line in every file within the path that matches the specified
   * regex and outputs the matched lines into the specified output file.
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    List<File> files = listFiles(this.rootPath);
    for (File file : files) {
      List<String> lines = readLines(file);
      for (String line : lines) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir
   * @return
   */
  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<>();

    // Convert the rootDir string to a Path object
    Path directory = Paths.get(rootDir);

    // Traverse the directory with Files.walkFileTree()
    try {
      Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          files.add(file.toFile());
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return files;
  }

  /**
   * Read a file and return all the lines
   *
   * Explain FileReader, BufferedReader, and character encoding:
   * We use BufferedReader over FileReader due to the BufferedReader's access to the
   * readLine() method. But we pass in a FileReader as an argument since BufferedReader
   * takes in a reader as a parameter, and we only have the File.
   *
   * @param inputFile
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();
    try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lines;
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(this.regex);

    // Create a Matcher object for matching against input string
    Matcher matcher = pattern.matcher(line);

    // conditional to check if input string contains jpg or jpeg
    return matcher.find();
  }

  /**
   * Write lines to a file
   * <p>
   * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter
   * we use BufferedWriter over FileOutputStream and OutputStreamWriter due to it's buffering
   * capabilities improving efficiency but also for it's access to the write() method which
   * was designed to work with texts whereas FileOutputStream and OutputStreamWriter's
   * methods are intended to work with raw bytes
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.outFile))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine(); // add a newline character after each line
      }
    }
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }
}
