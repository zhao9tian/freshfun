package com.quxin.freshfun.service.impl.mail;

import javax.mail.MessagingException;

/**
 * Created by fanyanlin on 2016/11/15.
 */
public class MailSenderFactory {

    /**
     * 发送邮件对象
     */
    private static MailSender mailSender;

    /**
     * 获取发送邮件对象
     * @return
     */
    public synchronized static MailSender getSender(){
        if (mailSender == null){
            mailSender = new MailSender("m18106555694@163.com","freshfun365");
        }
        return mailSender;
    }

    public static void main(String [] args){
        MailSender sender = MailSenderFactory.getSender();
        try {
            sender.send("1260855435@qq.com","来啊","互相伤害啊");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
