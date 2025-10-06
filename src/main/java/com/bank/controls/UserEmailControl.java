package com.bank.controls;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.bank.dao.ConnectionProvider;
import com.bank.model.UserModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


public class UserEmailControl {
	public static String generateUserReport(String accId, String filePath) {
	    String userEmail = null;

	    try (Connection con = ConnectionProvider.getCon()) {
	        Document doc = new Document();
	        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
	        doc.open();

	        String profileSql = "SELECT account_holder_name, account_holder_email, branch_id, branch_name, account_type, balance, status FROM customer WHERE account_holder_number = ?";
	        PreparedStatement profileStmt = con.prepareStatement(profileSql);
	        profileStmt.setString(1, accId);
	        ResultSet profileRs = profileStmt.executeQuery();

	        if (profileRs.next()) {
	            userEmail = profileRs.getString("account_holder_email"); 

	            doc.add(new Paragraph("User Profile"));
	            doc.add(new Paragraph("Name: " + profileRs.getString("account_holder_name")));
	            doc.add(new Paragraph("Email: " + userEmail));
	            doc.add(new Paragraph("Branch ID: " + profileRs.getString("branch_id")));
	            doc.add(new Paragraph("Branch Name: " + profileRs.getString("branch_name")));
	            doc.add(new Paragraph("Account Type: " + profileRs.getString("account_type")));
	            doc.add(new Paragraph("Balance: ₹" + profileRs.getDouble("balance")));
	            doc.add(new Paragraph("Status: " + (profileRs.getBoolean("status") ? "Active" : "Inactive")));
	            doc.add(new Paragraph("\n"));
	        }

	        String txnSql = "SELECT transaction_id, transaction_type, amount, transaction_date FROM transaction WHERE account_holder_number = ? ORDER BY transaction_date DESC";
	        PreparedStatement txnStmt = con.prepareStatement(txnSql);
	        txnStmt.setString(1, accId);
	        ResultSet txnRs = txnStmt.executeQuery();

	        doc.add(new Paragraph("Transaction History"));
	        while (txnRs.next()) {
	            doc.add(new Paragraph("--------------------------------------------------"));
	            doc.add(new Paragraph("Transaction ID: " + txnRs.getString("transaction_id")));
	            doc.add(new Paragraph("Type: " + txnRs.getString("transaction_type")));
	            doc.add(new Paragraph("Amount: ₹" + txnRs.getDouble("amount")));
	            doc.add(new Paragraph("Date: " + txnRs.getTimestamp("transaction_date")));
	        }

	        doc.close();
	        System.out.println("PDF generated at: " + filePath);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return userEmail;
	}

	public static void generateAndSendReport(String accId) {
	    String filePath = "UserReport_" + accId + ".pdf";
//	    new File("reports").mkdirs();
	    String email = generateUserReport(accId, filePath);

	    if (email != null && !email.isEmpty()) {
	        sendEmailWithAttachment(email, "Your Account Statement", "your latest account report.", filePath);
	    } else {
	        System.out.println("Email not found for account ID: " + accId);
	    }
	}

	
	public static void sendEmailWithAttachment(String toEmail, String subject, String body, String filePath) {
		UserModel user = new UserModel();
		
	    final String fromEmail = user.getUserEmail();
	    final String password = "pwll olnn wqmb umyo";

	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(fromEmail, password);
	        }
	    });

	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(fromEmail));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	        message.setSubject(subject);

	        MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setText(body);

	        MimeBodyPart attachmentPart = new MimeBodyPart();
	        attachmentPart.attachFile(new File(filePath));

	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(textPart);
	        multipart.addBodyPart(attachmentPart);

	        message.setContent(multipart);

	        Transport.send(message);
	        System.out.println("Email sent to " + toEmail);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
