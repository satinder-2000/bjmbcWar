/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.AccessType;
import bjm.bc.model.ExpenseParty;
import bjm.bc.model.RevenueParty;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@Stateless
public class EmailerEjb implements EmailerEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(EmailerEjb.class.getName());
    
    @Resource(name = "mail/bjmbc")
    Session session;
    
    @Resource(name="accessCreateURL")
    String accessCreateURL;

    @Override
    public void sendRevenuePartyRegistrationEmail(RevenueParty rp) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<h2>Dear, "+rp.getName()+ "</h2>");
        htmlMsg.append("<p>Congratulations on registering yourself successfully as "+AccessType.REVENUE_PARTY.toString()+".</p>");
        htmlMsg.append("<p>As a final step, please create your account password by following the link below:</p>");
        htmlMsg.append("<p>"+accessCreateURL+"</p>");
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

    @Override
    public void sendExpensePartyRegistrationEmail(ExpenseParty ep) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void sendAccessCreatedEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
