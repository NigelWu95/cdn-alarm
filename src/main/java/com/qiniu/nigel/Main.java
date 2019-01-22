package com.qiniu.nigel;

import com.qiniu.nigel.email.EmailSender;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        List<String> configFiles = new ArrayList<String>(){{
            add("qiniu.properties");
            add(".qiniu.properties");
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
        String accessKey = config.getParamValue("ak");
        String secretKey = config.getParamValue("sk");
        Auth auth = Auth.create(accessKey, secretKey);
        QiniuCdn qiniuCdn = new QiniuCdn();
        qiniuCdn.queryCdnAmounts(auth);

        String senderAddress = config.getParamValue("sender");
        String senderAccount = config.getParamValue("email-account");
        String senderPassword = config.getParamValue("email-password");
        String SMTPHost = config.getParamValue("smtp-host");
        String recipientAddress = config.getParamValue("recipient-address");

        EmailSender emailSender = new EmailSender(SMTPHost, senderAddress, senderAccount, senderPassword);
        emailSender.addRecipient(recipientAddress);
        emailSender.emailText("测试", "简单文本邮件");
    }
}
