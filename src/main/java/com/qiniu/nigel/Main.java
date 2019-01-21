package com.qiniu.nigel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        List<String> configFiles = new ArrayList<String>(){{
            add("resources/qiniu.properties");
            add("resources/.qiniu.properties");
        }};
        String configFilePath = null;
        for (int i = configFiles.size() - 1; i >= 0; i--) {
            File file = new File(configFiles.get(i));
            if (file.exists()) {
                configFilePath = configFiles.get(i);
                break;
            }
        }
        if (configFilePath == null) throw new IOException("there is no config file detected.");
        Config config = new Config(configFilePath);
        String senderAddress = config.getParamValue("sender");
        String senderAccount = config.getParamValue("email-account");
        String senderPassword = config.getParamValue("email-password");
        String SMTPHost = config.getParamValue("smtp-host");
        String recipientAddress = config.getParamValue("recipient-address");
    }
}
