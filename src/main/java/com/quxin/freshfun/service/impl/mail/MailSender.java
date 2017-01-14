package com.quxin.freshfun.service.impl.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by fanyanlin on 2016/11/15.
 * 发送邮件
 */
public class MailSender {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 发送邮件的props文件
     */
    private final transient Properties props = System.getProperties();
    /**
     * 邮件服务器登录验证
     */
    private transient MailAuthenticator authenticator;

    /**
     * 邮箱session
     */
    private transient Session session;

    private String userName;

    private String password;

    private String host = "smtp.qq.com";


    /**
     * 初始化邮件发送器
     * @param smtpHostName SMTP邮件服务器地址
     * @param username 发送邮件的用户名(地址)
     * @param password 发送邮件的密码
     */
    public MailSender(final String smtpHostName, final String username, final String password) {
        init(username, password);
    }

    /**
     * 初始化邮件发送器
     *
     * @param username 发送邮件的用户名(地址)，并以此解析SMTP服务器地址
     * @param password 发送邮件的密码
     */
    public MailSender(final String username, final String password) {
        //通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
        //final String smtpHostName = "smtp." + username.split("@")[1];
        init(username, password);
    }

    /**
     * 初始化
     * @param username 发送邮件的用户名(地址)
     * @param password 密码
     */
    private void init(String username, String password) {
        // 初始化props
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.starttls.enable",true);
        //props.put("mail.smtp.host", smtpHostName);

        this.userName = username;
        this.password = password;
        // 验证
        //authenticator = new MailAuthenticator(username, password);
        // 创建session
        session = Session.getInstance(props);
    }

    /**
     * 发送邮件
     *
     * @param recipient 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void send(String recipient, String subject, Object content)  {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        String nick="";
        try {
            nick=MimeUtility.encodeText("悦选美食订单");

        message.setFrom(new InternetAddress(nick+" <"+userName+">"));
        // 设置收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");

        Transport transport = session.getTransport();

        transport.connect(host,userName, password);
        // 发送
        transport.sendMessage(message, message.getAllRecipients());

        transport.close();

        } catch (UnsupportedEncodingException e) {
            logger.error("设置邮件发件人异常", e);
        } catch (NoSuchProviderException e) {
            logger.error("发送邮件异常",e);
        } catch (MessagingException e) {
            logger.error("发送邮件异常",e);
        }
    }

}
