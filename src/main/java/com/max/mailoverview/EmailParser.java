package com.max.mailoverview;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.converter.EmailConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailParser {

    private EmailParser(){}
    public static List<Email> getEmailsFromEmlFiles(String emlFolderPath) throws FolderNotFoundException, InterruptedException {
        List<String> fileNames = getEmailFileNames(emlFolderPath);

        return convertFileToEmail(emlFolderPath, fileNames);
    }

    public static void sendRecipientsToCSV(List<Recipient> recipientList) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./recipients.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL);

        csvPrinter.printRecord("status", "name", "mail");

        for(Recipient recipient : recipientList){
            csvPrinter.printRecord("Not Done", recipient.getName(), recipient.getAddress());
        }

        csvPrinter.close();
        writer.close();
    }

    private static List<String> getEmailFileNames(String emlFolderPath) throws FolderNotFoundException {
        List<String> fileNames = new ArrayList<>();

        File folder = new File(emlFolderPath);
        File[] files = folder.listFiles();

        if(files == null){
            throw new FolderNotFoundException();
        }

        for (File file: files) {
            if( file.isFile()) fileNames.add(file.getName());
        }

        return fileNames;
    }

    private static List<Email> convertFileToEmail(String directory, List<String> fileNames) throws InterruptedException {
        List<Email> emailList = new ArrayList<>();

        Counter counter = new Counter();
        int maxItems = fileNames.size();

        Thread progressBarThread = new Thread(new ProgressBar(maxItems, counter));
        progressBarThread.start();

        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        fileNames.forEach(fileName -> threadPool.submit(() -> {
            emailList.add(EmailConverter.emlToEmail(new File(directory + "/" + fileName)));
            counter.add();
        }));

        threadPool.close();
        progressBarThread.join();
        return emailList;
    }

}
