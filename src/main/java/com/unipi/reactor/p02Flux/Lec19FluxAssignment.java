package com.unipi.reactor.p02Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Lec19FluxAssignment {

    public static void main(String[] args) {
        FileReaderService readerService = new FileReaderService();

        Path path = Paths.get("src/main/resources/assignment/sec01/file03.txt");
        readerService.read(path)
                // Simulate a random error to check if BufferReader closes at the end
                .map(s -> {
                    Integer integer = Util.faker().random().nextInt(0, 10);
                    if(integer > 8)
                        throw new RuntimeException("Intended Error");
                    return s;
                })
                .take(20) // Take only 20 lines
                .subscribe(Util.subscriber()); // Subscriber
    }
}

class FileReaderService {

    // Open Buffer Reader
    private Callable<BufferedReader> openReader(Path path) {
        return () -> Files.newBufferedReader(path);
    }

    private BiFunction<BufferedReader, SynchronousSink<String>, BufferedReader> read() {
        return (br, sink) -> {
            try {
                String line = br.readLine();
                System.out.println("Reading...");
                if(Objects.isNull(line))
                    sink.complete();
                else
                    sink.next(line);
            } catch (IOException e) {
                sink.error(e);
            }
            return br;
        };
    }

    // Close Buffer Reader
    private Consumer<BufferedReader> closeReader() {
        return br -> {
            try {
                br.close();
                System.out.println("-- Closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public Flux<String> read(Path path) {
        return Flux.generate(
                openReader(path),
                read(),
                closeReader()
        );
    }
}
