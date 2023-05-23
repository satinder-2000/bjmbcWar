/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.AccessType;
import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueParty;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author user
 */
@Stateless
public class EmailerEjb implements EmailerEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(EmailerEjb.class.getName());
    
    @Resource(name = "mail/bjmbc")
    Session session;
    
    @Resource(name = "WebURI")
    String webURI;
    
    @Resource(name="accessCreateURI")
    String accessCreateURI;
    
    @Resource(name="loginURI")
    String loginURI;

    @Override
    public void sendRevenuePartyRegistrationEmail(RevenueParty rp) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<h2>Dear, "+rp.getName()+ "</h2>");
        htmlMsg.append("<p>Congratulations on registering yourself successfully as "+AccessType.REVENUE_PARTY.toString()+".</p>");
        htmlMsg.append("<p>As a final step, please create your account password by following the link below:</p>");
        String accessCreate=String.format(accessCreateURI, rp.getEmail(), AccessType.REVENUE_PARTY.toString());
        htmlMsg.append(webURI).append(accessCreateURI).append(rp.getEmail()).append("&accessType="+AccessType.REVENUE_PARTY.toString());
        htmlMsg.append("<p>"+accessCreate+"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmbc.net Admin</p>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent( htmlMsg.toString(), "text/html; charset=utf-8" );
            multipart.addBodyPart(htmlPart);
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(rp.getEmail()));
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            Logger.getLogger(EmailerEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendExpensePartyRegistrationEmail(ExpenseParty ep) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<h2>Dear, "+ep.getName()+ "</h2>");
        htmlMsg.append("<p>Congratulations on registering yourself successfully as "+AccessType.EXPENSE_PARTY.toString()+".</p>");
        htmlMsg.append("<p>As a final step, please create your account password by following the link below:</p>");
        String accessCreate=String.format(accessCreateURI, ep.getEmail(), AccessType.EXPENSE_PARTY.toString());
        htmlMsg.append(webURI).append(accessCreateURI).append(ep.getEmail()).append("&accessType="+AccessType.EXPENSE_PARTY.toString());
        htmlMsg.append("<p>"+accessCreate+"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmbc.net Admin</p>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent( htmlMsg.toString(), "text/html; charset=utf-8" );
            multipart.addBodyPart(htmlPart);
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(ep.getEmail()));
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            Logger.getLogger(EmailerEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendAccessCreatedEmail(String email) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<h2>Dear, "+email+ "</h2>");
        htmlMsg.append("<p>Congratulations on creating your access successfully on bjmbc.net.</p>");
        htmlMsg.append("<p>We look forward to a long parthership for the functioning of the portal.</p>");
        htmlMsg.append("<p>Kindly, use the link below to login and resume your activities:</p>");
        htmlMsg.append("<p><a href='+loginURI='>"+webURI+loginURI+"</a></p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmbc.net Admin</p>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent( htmlMsg.toString(), "text/html; charset=utf-8" );
            multipart.addBodyPart(htmlPart);
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            Logger.getLogger(EmailerEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void sendExpensePartyAccountOverdrawnEmail(ExpenseParty ep) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setSubject("Warning - Account is overdrawn!!");
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<h2>Dear, "+ep.getName()+ "</h2>");
        htmlMsg.append("<p>\"Just to let you know that your Account is overdrawn. Please take action urgently to top it up.\\n\\n\"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmbc.net Admin</p>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            
            htmlPart.setContent( htmlMsg.toString(), "text/html; charset=utf-8" );
            multipart.addBodyPart(htmlPart);
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            Logger.getLogger(EmailerEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
