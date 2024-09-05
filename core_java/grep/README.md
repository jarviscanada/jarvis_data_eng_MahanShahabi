# Introduction
The grep app is a Java implementation of the widely 
used Linux grep feature. Made in Intellij, it utilizes
Java, and it's associated file, path, list, 
BufferedReader/Writer, and regex libraries to find 
specified keywords within all files of a specified 
directory and prints them in an output file in a 
specified location. It is now dockerized for consumer
use.

# Quick Start
you use the grep app by specifying 3 arguments for
the program to take in before running it:
1. A regex representing the word(s) you want to look for
2. A file path to specify which files to search
3. The name of the out file and the directory it should be made in

# Implementation

Implemented the ```process``` method using the following
pseudocode

## Psuedocode
```bash
Method process
    Initialize an empty list for matchedLines
    
    Call listFiles with rootPath to get a list of files
    Loop through each file in the list of files
        Call readLines with the current file to get all lines 
        from the file
        Loop through each line in the list of lines
            If containsPattern returns true for the line
                Add the line to the matchedLines list
    
    Call writeToFile with matchedLines to write all matched lines to an output file
End Method
```

## Performance Issues
Currently, the implementation has a memory issue 
where if the heap memory is smaller than the size of
the file being searched, it will give an OutOfMemoryError
because there is no more memory in the heap for JVM
to allocate. This can be fixed by using streams and
lambda expressions to read line by line as opposed to
reading the file all at once to use less memory in
the heap.

# Test
The application was tested by running it on its own
src file to search for IllegalArgumentExceptions.
This allows easy verification and modification to see
how the program works.

# Deployment
The deployment was done through docker by dockerizing 
the app. This was done by first registering a Docker
hub account and then making a Dockerfile which tells
docker what image to use (openjdk:8-alpine) as well as
which jar files to copy (the ones matching pattern grep*.jar)
and using the ```ENTRYPOINT``` instruction to tell Docker
to execute the app packaged in the jar file whenever the
container starts.

# Improvement
1. utilize streams and lambda expressions
2. optimize the process function so that it does not
use an embedded for loop
3. Improve exception handling to better catch errors