# Core Java - Grep Application

# Introduction
The Java Grep App is a command-line tool that mimics the grep command. It searches for regex patterns in text files within a specified directory and its subdirectories, outputting matched lines to a file. Developed in Java, it leverages core Java libraries for file I/O, regex matching, and logging with SLF4J. The app uses Stream APIs for memory efficiency and is built with Maven, packaged with Docker, and uploaded to Docker Hub for ease of use.

# Quick Start
Follow these steps to quickly get started with the Java Grep App:

1. Build the project using Maven:
```
mvn clean compile package
```
2. Run the application:
```
java -jar target/grep-1.0-SNAPSHOT.jar "your-regex-pattern" "path/to/rootDir" "path/to/outputFile"
```
Replace "your-regex-pattern", "path/to/rootDir", and "path/to/outputFile" with your desired regex pattern, root directory, and output file path respectively.

#Implemenation
## Pseudocode
1. Initialize an empty list to store matched lines.
2. Get a list of all files in the root directory and its subdirectories.
3. For each file in the list:
   a. Read each line of the file.
   b. For each line:
      i. If the line matches the regex pattern:
         - Add the line to the list of matched lines.
4. Write all matched lines to the output file.

## Performance Issue
When processing large datasets that exceed the available physical memory, the application may encounter a "GC overhead limit exceeded" error due to excessive garbage collection. To mitigate this, the Stream API can be used to process data in a more memory-efficient manner by leveraging lazy evaluation. Do this by using the LambdaGrepStreamImp.java over the defaulted
JavaGrepImp.java.

# Test
The Java Grep App was tested by preparing sample text files with various content and running test cases with different regex patterns and directories. The output was compared with the actual grep command in Linux to ensure accuracy. Edge cases such as empty files, large files, and files with no matching lines were tested to verify robustness.

# Deployment
To deploy the Java Grep App using Docker, follow these steps:

1. Build a new Docker image locally:
```
docker build -t your_docker_id/grep .
```

2. Run the Docker container:
```
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
your_docker_id/grep "your-regex-pattern" "path/to/rootDir" "path/to/outputFile"
```

3. Push your image to Docker Hub for easier distribution and pulling later:
```
docker push your_docker_id/grep
```

# Improvement
1. Add more comprehensive unit tests to cover edge cases.
2. Switch the default implementation to the grep LambdaGrepStreamImp.Java for greater memory efficiency.
