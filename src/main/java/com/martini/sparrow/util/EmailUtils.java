package com.martini.sparrow.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * @author martini at 2020/11/2 15:15
 */
public class EmailUtils {
    // private static Properties properties = null;
    // static {
    //     properties = new Properties();
    //     try {
    //         properties.load(EmailUtils.class.getClassLoader().getResourceAsStream("prop/email.properties"));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    public static boolean sendEmail(String[] to, String title, String content) {
        try {
            SimpleEmail mail = new SimpleEmail();
            mail.setSslSmtpPort("994");
            mail.setHostName("xxx.xxx.xxx.xxx");
            mail.setAuthentication("xxxxxx@163.com", "xxxxxx");
            mail.setFrom("xxxxxx@163.com");
            mail.addTo(to);
            mail.setCharset("UTF-8");
            mail.setContent(content, "text/html;charset=utf-8");
            mail.setSubject(title);
            mail.setSentDate(new Date());
            mail.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean sendVerificationEmail(String[] to, String title, String content, String placeholder, String uuid) {
        return sendEmail(to, title, content);
    }
}
