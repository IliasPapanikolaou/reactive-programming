package com.unipi.reactor.p01Mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Lec09Assignment {

    public static void main(String[] args) {

        // Subscribers - Consumers

        // Read
        FileService.read("file01.txt")
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete());

        // Read file 2: Intended file not found exception
        FileService.read("file02.txt")
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete());

        // Write file 2
        FileService.write("file02.txt", "Hello from file3")
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        ()-> System.out.println("File created successfully"));

        // Read file 2
        FileService.read("file02.txt")
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        Util.onComplete());

        // Delete file 2
        FileService.delete("file02.txt")
                .subscribe(
                        Util.onNext(),
                        Util.onError(),
                        ()-> System.out.println("File deleted successfully"));

    }
}

class FileService {

    private static final Path PATH = Paths.get("src/main/resources/assignment/sec01/");

    // Publisher
    public static Mono<String> read(String filename) {
        return Mono.fromSupplier(() -> readFile(filename));
    }

    public static Mono<Void> write(String filename, String content) {
        return Mono.fromRunnable(() -> writeFile(filename, content));
    }

    public static Mono<Void> delete(String filename) {
        return Mono.fromRunnable(() -> deleteFile(filename));
    }

    private static String readFile(String fileName) {
        // Java 11: Reads all the bytes and converts them to String
        try {
            return Files.readString(PATH.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeFile(String fileName, String content) {
        try {
            Files.writeString(PATH.resolve(fileName), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile(String filename) {
        try {
            Files.delete(PATH.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
