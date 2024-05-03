package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.BasicConfigurator;

public class JavaGrepLambdaImp extends JavaGrepImp{

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }

    /**
     * Implement using lambda and stream APIs
     */
    @Override
    public void process() throws IOException {
        List<String> matchedLines = listFiles(this.rootPath).stream()
                .flatMap(file -> readLines(file).stream())
                .filter(line -> containsPattern(line))
                .collect(Collectors.toList());

        writeToFile(matchedLines);
    }

    /**
     * Implement using lambda and stream APIs
     */
    @Override
    public List<File> listFiles(String rootDir) {
        // Convert the rootDir string to a Path object
        Path directory = Paths.get(rootDir);

        try {
            return Files.walk(directory).filter(path -> Files.isRegularFile(path)).map(path -> path.toFile()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Implement using lambda and stream APIs
     */
    @Override
    public List<String> readLines(File inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found: " + inputFile.getAbsolutePath(), e);
        }
    }

    /**
     * Implement using lambda and stream APIs
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
     * Implement using lambda and stream APIs
     */
    @Override
    public void writeToFile(List<String> lines) throws IOException {
        File outFile = new File(this.outFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.outFile))) {
            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException("Error writing to file: " + outFile.getAbsolutePath(), e);
                }
            });
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
}
