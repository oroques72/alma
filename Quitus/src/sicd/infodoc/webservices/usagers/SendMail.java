package sicd.infodoc.webservices.usagers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {


	
	private static SendMail instance = new SendMail();
	
	

	private SendMail() {

		

	}

	public static SendMail getInstance() {

		return instance;
	}

	public static void main(String args[]) {
//System.out.print(new Date().toString());
		//SendMail.getInstance().sendMail2("Test", "olivier.roques@univ-toulouse.fr", null);

	}

	
	
	
	public void sendMail(String subject, String to,String contents,String fileName,String host_quitus_dir ,String SMTP_LOGIN,String SMTP_PASSWD,String SMTP_HOST,String MAIL_LOGO_PATH) {

		try {
			DataSource fds=null;
			String from = SMTP_LOGIN;

			
			Properties props = System.getProperties();

			
			props.setProperty("mail.smtp.host", SMTP_HOST);
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.port", "25");

			Session session = Session.getInstance(props);
			

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			//Header Date
			message.setSentDate(new Date());

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			
			
			messageBodyPart.setContent(contents, "text/html; charset=UTF-8");

			// add it
			multipart.addBodyPart(messageBodyPart);

			// second part (the image)
			if (MAIL_LOGO_PATH != null){
			messageBodyPart = new MimeBodyPart();
			fds = new FileDataSource(MAIL_LOGO_PATH);

			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<image>");
			// add image to the multipart
			multipart.addBodyPart(messageBodyPart);

			}
			
			if (fileName != null) {
				
				try{
				// second part (the file)
				messageBodyPart = new MimeBodyPart();
				fds = new FileDataSource(host_quitus_dir + "/"+ fileName);

				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setFileName(fileName);

				// add image to the multipart
				multipart.addBodyPart(messageBodyPart);
				}catch(Throwable e){
				e.printStackTrace();
					
				}
			}
			
			

			// put everything together
			message.setContent(multipart);

			// Now set the actual message
			// message.setText(text);

			// Send message

			Transport transport = null;

			transport = session.getTransport("smtp");

			transport.connect(SMTP_LOGIN, SMTP_PASSWD);

			transport.sendMessage(message, new Address[] { new InternetAddress(to), new InternetAddress(from) });
			if (transport != null)
				transport.close();

			System.out.println("Sent message successfully....");

		} catch (Throwable e) {
			System.out.println("SendMail : Erreur envoi mail");
			e.printStackTrace();
		}
	}


}