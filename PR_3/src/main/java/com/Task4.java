package com;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.atomic.AtomicInteger;

class File {
    private final String fileType;
    private final int fileSize;

    public File(String fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }
}

class FileGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public Observable<File> generateFile() {
        return Observable.fromCallable(() -> {
            try {
                String[] fileTypes = { "XML", "JSON", "XLS" };
                String fileType = fileTypes[(int) (Math.random() * 3)];
                int fileSize = (int) (Math.random() * 91) + 10;
                Thread.sleep((long) (Math.random() * 901) + 100);
                return new File(fileType, fileSize);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        })
                .repeat()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}


class FileQueue {
    private final int capacity;
    private final Observable<File> fileObservable;
    public FileQueue(int capacity) {
        this.capacity = capacity;
        this.fileObservable = new FileGenerator().generateFile()
                .replay(capacity) 
                .autoConnect();
    }

    public Observable<File> getFileObservable() {
        return fileObservable;
    }
}

class FileProcessor {
    private final String supportedFileType;
    public FileProcessor(String supportedFileType) {
        this.supportedFileType = supportedFileType;
    }
    public Completable processFiles(Observable<File> fileObservable) {
        return fileObservable
                .filter(file -> file.getFileType().equals(supportedFileType))
                .flatMapCompletable(file -> {
                    long processingTime = file.getFileSize() * 7;
                    return Completable
                            .fromAction(() -> {
                                Thread.sleep(processingTime);
                                System.out.println("Processed " +
                                        supportedFileType + " file with size " + file.getFileSize());
                            })
                            .subscribeOn(Schedulers.io()) 
                            .observeOn(Schedulers.io());
                })
                .onErrorComplete();
    }
}

public class Task4 {
    public static void main(String[] args) {
        int queueCapacity = 5;
        FileQueue fileQueue = new FileQueue(queueCapacity);
        String[] supportedFileTypes = { "XML", "JSON", "XLS" };
        for (String fileType : supportedFileTypes) {
            new FileProcessor(fileType)
                    .processFiles(fileQueue.getFileObservable())
                    .subscribe(
                            () -> {
                            }, 
                            throwable -> System.err.println("Error processing file: " + throwable));
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
