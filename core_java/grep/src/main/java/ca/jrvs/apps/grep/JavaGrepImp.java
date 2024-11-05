package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaGrepImp implements JavaGrep {

    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    @Override
    public void process() throws IOException {
        // List all files in the root directory
        List<File> files = listFiles(rootPath);

        List<String> matchedLines = new ArrayList<>();
        // Initialize an empty list to store matched lines
        for (File file : files) {
            System.out.println(file.getName());
            List<String> lines = readLines(file);
            for (String line : lines) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File root = new File(rootDir);

        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(listFiles(file.getAbsolutePath()));
                    } else {
                        fileList.add(file);
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (Scanner scanner = new Scanner(inputFile)) {
            // Read each line from the file and add it to the list
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return line.matches(getRegex());
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

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
