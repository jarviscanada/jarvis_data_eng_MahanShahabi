package ca.jrvs.apps.grep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc{

  @Override
  public boolean matchJpeg(String filename) {
    // regular expression pattern
    String regex = "^.+\\.jpe?g$";

    // compile regular expression into a Pattern object
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    // Create a Matcher object for matching against input string
    Matcher matcher = pattern.matcher(filename);

    // conditional to check if input string contains jpg or jpeg
    return matcher.find();
  }

  @Override
  public boolean matchIp(String ip) {
    // regular expression pattern
    String regex = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";

    // compile regular expression into a Pattern object
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    // Create a Matcher object for matching against input string
    Matcher matcher = pattern.matcher(ip);

    // outputs true if ip contains valid IP address range and false otherwise
    return matcher.find();
  }

  @Override
  public boolean isEmptyLine(String line) {
    // regular expression pattern
    String regex = "^\\s*$";

    // compile regular expression into a Pattern object
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    // Create a Matcher object for matching against input string
    Matcher matcher = pattern.matcher(line);

    // outputs true if line is empty
    System.out.println(matcher.find());
    return matcher.find();
  }
}
