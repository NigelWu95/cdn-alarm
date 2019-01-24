package com.qiniu.nigel.common;

import com.qiniu.nigel.email.EmailSender;

import java.io.File;
import java.io.IOException;

public class LogRegisterTask extends java.util.TimerTask {

    private EmailSender emailSender;
    private String path;
    private File file;

    public LogRegisterTask(EmailSender emailSender, String path) throws IOException {
        this.emailSender = emailSender;
        this.path = path;
        this.file = new File(path);
        boolean success = false;
        while (!file.exists() && !success) {
            success = file.createNewFile();
        }
        emailSender.setLogFile(file);
    }

    @Override
    public void run() {
        boolean success = false;
        while (file.exists() && file.isFile() && !success) {
            success = file.delete();
        }
        try {
            success = false;
            while (!file.exists() && !success) {
                success = file.createNewFile();
            }
            emailSender.setLogFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}