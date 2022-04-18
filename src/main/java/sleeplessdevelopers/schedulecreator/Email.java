package sleeplessdevelopers.schedulecreator;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
public class Email {
    private final File FILE;
    private final InternetAddress ADVISOREMAIL;
    private final String STUDENTID;

    public Email(String file, String advisorEmail, int studentID) throws AddressException {
        FILE= new File(file);
        ADVISOREMAIL = new InternetAddress(advisorEmail);
        STUDENTID = Integer.toString(studentID);
    }

    private Session setupMail(){
        Properties prop = new Properties();
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","465");

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("No.Reply.Schedule.Creator@gmail.com",serverMailPassword());
            }
        });
    }
    public void sendMail(){
        Session session = setupMail();
        Message mes = new MimeMessage(session);
        try{
            mes.setSubject("Newly Generated Schedule for Student: " + STUDENTID);
//            mes.setContent("<h2>The following student is seeking approval for their schedule</h2> <p>Attached" +
//                    " to this email is a pdf version of the student's schedule</p> <p> --Created with Schedule Creator--</p>"
//                    ,"text/html");
            mes.setRecipient(Message.RecipientType.TO,ADVISOREMAIL);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h2>The following student is seeking approval for their schedule</h2> <p>Attached" +
                            " to this email is a pdf version of the student's schedule</p> <p> --Created with Schedule Creator--</p>"
                    ,"text/html");
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(FILE);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachment);
            mes.setContent(multipart);
            Transport.send(mes);

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static String serverMailPassword(){
        // encrypted with https://www.stringencrypt.com (v1.4.0) [Java]
        String Password = "";

        int ZHRBD[] = { 0x0039, 0x0060, 0x0029, 0x0056, 0x003D, 0x003E, 0x003A, 0x006D,
                0x002E, 0x0043 };

        for (int eLGiQ = 0, HKAnd = 0; eLGiQ < 10; eLGiQ++)
        {
            HKAnd = ZHRBD[eLGiQ];
            HKAnd --;
            HKAnd -= eLGiQ;
            Password = Password + (char)(HKAnd & 0xFFFF);
        }

        return Password;
    }

    public static void main(String [] args) {
        String f = "test.pdf";
        Email e = null;
        try {
            e = new Email(f, "No.Reply.Schedule.Creator@gmail.com", 111111);
            e.sendMail();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
