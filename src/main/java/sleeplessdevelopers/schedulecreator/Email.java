package sleeplessdevelopers.schedulecreator;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
public class Email {
    public static void main(String [] args)
    {
        Properties prop = new Properties();
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","465");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {//ENCRYPT PASSWORD BEFORE PUSH
                return new PasswordAuthentication("No.Reply.Schedule.Creator@gmail.com",serverMailPassword());
            }
        });
        Message mes = new MimeMessage(session);
        //mes.setText();

        try{
            mes.setSubject("Test Email Subject");
            mes.setContent("<h1>Test Email</h1>","text/html");
            Address addressTo = new InternetAddress("FontanaTJ20@gcc.edu");
            mes.setRecipient(Message.RecipientType.TO,addressTo);
            // MimeBodyPart messageBodyPart = new MimeBodyPart();
            // messageBodyPart.setContent(
//            MimeBodyPart attachment = new MimeBodyPart("<h1>Test Email</h1>","text/html");
//            attachment.attachFile(new File("static/somename.pdf"));
//            MimeMultipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            multipart.addBodyPart(attachment);
//            mes.setContent(multipart);
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

}
