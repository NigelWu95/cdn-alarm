package com.qiniu.nigel;

import com.qiniu.nigel.cdn.QuotaAndSurplus;
import com.qiniu.nigel.cdn.Refresh;
import com.qiniu.nigel.common.Config;
import com.qiniu.nigel.email.EmailSender;
import com.qiniu.storage.Configuration;
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

        String senderAddress = config.getParamValue("sender");
        String senderAccount = config.getParamValue("email-account");
        String senderPassword = config.getParamValue("email-password");
        String SMTPHost = config.getParamValue("smtp-host");
        String recipientAddress = config.getParamValue("recipient-address");
        EmailSender emailSender = new EmailSender(SMTPHost, senderAddress, senderAccount, senderPassword);
        File file = new File("log.txt");
        emailSender.setLogFile(file);

        String accessKey = config.getParamValue("ak");
        String secretKey = config.getParamValue("sk");
        Auth auth = Auth.create(accessKey, secretKey);
        Refresh refresh = new Refresh(new Configuration(), auth);
        QuotaAndSurplus quotaAndSurplus = refresh.queryQuotaAndSurplus();
        int urlSurplusDay = quotaAndSurplus.urlSurplusDay;
        // 告警时间单位间隔
        int seconds = 60 * 1000;
        // 告警时间间隔初始值，间隔 10 分钟查询一次
        int interval = 30 * seconds;
        int count = 0;

        while (true) {
            if (urlSurplusDay == 0) {
                count = 0;
                interval = 60 * seconds;
                emailSender.addRecipient(recipientAddress);
                emailSender.addRecipient("tswork@qiniu.com");
                emailSender.emailText("七牛 CDN 刷新额度预警", "美篇 URL 刷新额度剩余：" + urlSurplusDay);
            } else if (urlSurplusDay < 100) {
                count++;
                interval = seconds;
                if (count <= 5 || count % 30 == 0) {
                    emailSender.addRecipient(recipientAddress);
                    emailSender.emailText("七牛 CDN 刷新额度预警", "美篇 URL 刷新额度剩余：" + urlSurplusDay);
                }
            } else if (urlSurplusDay < 500) {
                count++;
                interval = 2 * seconds;
                if (count <= 5 || count % 15 == 0) {
                    emailSender.addRecipient(recipientAddress);
                    emailSender.emailText("七牛 CDN 刷新额度预警", "美篇 URL 刷新额度剩余：" + urlSurplusDay);
                }
            } else if (urlSurplusDay < 1000) {
                count++;
                interval = 3 * seconds;
                if (count <= 5 || count % 10 == 0) {
                    emailSender.addRecipient(recipientAddress);
                    emailSender.emailText("七牛 CDN 刷新额度预警", "美篇 URL 刷新额度剩余：" + urlSurplusDay);
                }
            } else if (urlSurplusDay < 10000 ) {
                count = 0;
                interval = 5 * seconds;
            } else if (urlSurplusDay < 20000 ) {
                count = 0;
                interval = 10 * seconds;
            } else {
                count = 0;
            }
            Thread.sleep(interval);
            quotaAndSurplus = refresh.queryQuotaAndSurplus();
            urlSurplusDay = quotaAndSurplus.urlSurplusDay;
        }

    }
}
