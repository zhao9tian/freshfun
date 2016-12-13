package com.quxin.freshfun.service.impl.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
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


    /**
     * 初始化邮件发送器
     * @param smtpHostName SMTP邮件服务器地址
     * @param username 发送邮件的用户名(地址)
     * @param password 发送邮件的密码
     */
    public MailSender(final String smtpHostName, final String username, final String password) {
        init(username, password, smtpHostName);
    }

    /**
     * 初始化邮件发送器
     *
     * @param username 发送邮件的用户名(地址)，并以此解析SMTP服务器地址
     * @param password 发送邮件的密码
     */
    public MailSender(final String username, final String password) {
        //通过邮箱地址解析出smtp服务器，对大多数邮箱都管用
        final String smtpHostName = "smtp." + username.split("@")[1];
        init(username, password, smtpHostName);
    }

    /**
     * 初始化
     * @param username 发送邮件的用户名(地址)
     * @param password 密码
     * @param smtpHostName SMTP主机地址
     */
    private void init(String username, String password, String smtpHostName) {
        // 初始化props
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostName);
        // 验证
        authenticator = new MailAuthenticator(username, password);
        // 创建session
        session = Session.getInstance(props, authenticator);
    }

    /**
     * 发送邮件
     *
     * @param recipient 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(String recipient, String subject, Object content)
            throws AddressException, MessagingException {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        String nick="";
        try {
            nick=MimeUtility.encodeText("悦选美食订单");
        } catch (UnsupportedEncodingException e) {
            logger.error("设置邮件发件人异常",e);
        }
        message.setFrom(new InternetAddress(nick+" <"+authenticator.getUsername()+">"));
        // 设置收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");
        // 发送
        Transport.send(message);
    }

}
