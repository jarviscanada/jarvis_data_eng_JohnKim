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
java -jar target/grep-1.0-SNAPSHOT.jar regex rootPath outFile
```


#Implemenation
## Pseudocode
write `process` method pseudocode.

## Performance Issue
(30-60 words)
Discuss the memory issue and how would you fix it

# Test
How did you test your application manually? (e.g. prepare sample data, run some test cases manually, compare result)

# Deployment
How you dockerize your app for easier distribution?

# Improvement
List three things you can improve in this project.