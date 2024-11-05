package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LambdaGrepStreamImp extends JavaGrepImp {

    @Override
    public void process() throws IOException {
        List<File> files = listFiles(getRootPath());
        List<String> matchedLines = new ArrayList<>();

        for (File file : files) {
            try (Stream<String> lines = Files.lines(file.toPath())) {
                lines.filter(this::containsPattern)
                        .forEach(matchedLines::add);
            }
        }

        writeToFile(matchedLines);
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