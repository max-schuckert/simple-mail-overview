package com.max.mailoverview;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static com.max.mailoverview.EmailParser.sendRecipientsToCSV;

public class Main {

    private static String filesLocation = "";
    private static String mailEnding = "";
    public static void main(String[] args) throws FolderNotFoundException, InterruptedException, IOException {
        List<Email> emailList = EmailParser.getEmailsFromEmlFiles(filesLocation);

        List<Recipient> recipientList = new ArrayList<>();

        for (Email selectedEmail : emailList) {
            if (selectedEmail.getRecipients().stream().anyMatch(recipient -> recipient.getAddress().endsWith(mailEnding))
                    && recipientList.stream().noneMatch(recipient -> recipient.getAddress().equals(selectedEmail.getFromRecipient().getAddress()))
            ) {
                recipientList.add(selectedEmail.getFromRecipient());
            }
        }

        recipientList.sort(Comparator.comparing(Recipient::getAddress));
        recipientList.sort(Comparator.comparing(recipient -> recipient.getName() != null ? recipient.getName() : ""));

        sendRecipientsToCSV(recipientList);
    }
}
