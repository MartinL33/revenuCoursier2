

package fr.modele;

import static fr.modele.SendDataPassord.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.SwingWorker;

import fr.gui.GUI;

public class SendData {



	static void sauvegarde(String mail) {
		
		
		
		if(mail!=null&&!mail.isEmpty()&&mail.length()>0) {
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground()  {
					if(hasInterfaceNet(Value.debug)) {
						envoyerMailSMTP(mail,Value.debug);
					}		
					else {
					    GUI.messageConsole("pas d'interface web, envoie données annulé");
					}
					return null;
				};			
				@Override
				protected void done() {		}			
			}.execute();	
		}
		else {
		    GUI.messageConsole("message vide, envoie données annulé");
		}
	}
	
	private static boolean envoyerMailSMTP(String mail,boolean debug) {

		boolean result = false;

		String MAILER_VERSION = "Java";
		String SMTP_HOST="smtp-mail.outlook.com";
		
		
		
				
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", SMTP_HOST);
		properties.setProperty("mail.smtp.user", LOGIN_SMTP);
		properties.setProperty("mail.from", LOGIN_SMTP);
		properties.setProperty("mail.smtp.auth", "true");

		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.starttls.enable", "true");	
		Session session = Session.getInstance(properties);
		session.setDebug(debug);

		Message message=null;
		try {
			
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(LOGIN_SMTP));
			InternetAddress[] internetAddresses = new InternetAddress[1];
			internetAddresses[0] = new InternetAddress(LOGIN_SMTP);
			message.setRecipients(Message.RecipientType.TO,internetAddresses);
			message.setSubject("ListFactures");			
			message.setText(mail);			
			message.setHeader("X-Mailer", MAILER_VERSION);			
			message.setSentDate(new Date());			
			
			
			
			// 3 -> Envoi du message
			Transport transport = null;
			try {
				transport = session.getTransport("smtp");
				transport.connect(LOGIN_SMTP, PASSFROM_SMTP);
				transport.sendMessage(message, new Address[] { new InternetAddress(LOGIN_SMTP) });
			} catch (MessagingException e) {
				if(debug) e.printStackTrace();
			} finally {
				try {
					if (transport != null) {
						transport.close();
						result = true;
						GUI.messageConsole("mail envoyé");
					}
				} catch (MessagingException e) {
					if(debug) e.printStackTrace();
				}
			}
			
			
			

		} catch (AddressException e1) {			
			if(debug) e1.printStackTrace();
		} catch (MessagingException e1) {			
			if(debug) e1.printStackTrace();
		}
	
		
		return result;

	}
	
	private static boolean hasInterfaceNet(boolean debug) {
		
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			
			if(debug) e.printStackTrace();
			return false;
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface interf = interfaces.nextElement();
			try {
				if (interf.isUp() && !interf.isLoopback())
					return true;
			} catch (SocketException e) {				
				if(debug) e.printStackTrace();				
			}
		}
		return false;
	}
}




