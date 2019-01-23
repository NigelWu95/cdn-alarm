package com.qiniu.nigel.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message.*;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

//    private Properties emailProps;
//    private String SMTPHost;
    //发件人地址
    private String senderAddress;
    //发件人账户名
    private String senderAccount;
    //发件人账户密码
    private String senderPassword;

    private Session session;
    private MimeMessage message;

    public EmailSender(String SMTPHost, String senderAddress, String senderAccount, String senderPassword) {
        //邮件 smtp 服务域名
//        this.SMTPHost = SMTPHost;
        this.senderAddress = senderAddress;
        this.senderAccount = senderAccount;
        this.senderPassword = senderPassword;
        //连接邮件服务器的参数配置
        Properties emailProps = new Properties();
        //设置用户的认证方式
        emailProps.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        emailProps.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        emailProps.setProperty("mail.smtp.host", SMTPHost);
        //创建定义整个应用程序所需的环境信息的 Session 对象
        session = Session.getInstance(emailProps);
        //创建一封邮件的实例对象
        message = new MimeMessage(session);
    }

    public void addRecipient(String emailAddress) throws MessagingException {
        // 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
        // MimeMessage.RecipientType.TO
        // MimeMessage.RecipientType.CC
        // MimeMessage.RecipientType.BCC
        message.addRecipient(RecipientType.TO, new InternetAddress(emailAddress));
    }

    public void emailText(String subject, String content) throws MessagingException {
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //防止成为垃圾邮件，披上outlook的马甲
        message.addHeader("Mailer","Foxmail for Mac version 1.2.14017");
        //设置发件人地址
        message.setFrom(new InternetAddress(senderAddress));
        //设置邮件主题
        message.setSubject("<h2>" + subject + "</h2></br>","UTF-8");
        //设置邮件正文
        message.setContent("<p>" + content + "</p>", "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        message.setSentDate(new Date());
        //根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        //如果只想发送给指定的人，可以如下写法
        //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

        //5、关闭邮件连接
        transport.close();
    }
}
