package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaGrepStreamImp extends JavaGrepImp {

    @Override
    public List<File> listFiles(String rootDir) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDir))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsPattern(String line) {
        return Pattern.compile(getRegex()).matcher(line).find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getOutFile()))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        LambdaGrepStreamImp lambdaGrepStreamImp = new LambdaGrepStreamImp();
        lambdaGrepStreamImp.setRegex(args[0]);
        lambdaGrepStreamImp.setRootPath(args[1]);
        lambdaGrepStreamImp.setOutFile(args[2]);

        try {
            lambdaGrepStreamImp.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}