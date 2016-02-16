package org.summer.dp.cms.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SmtpMailService {
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;	
	
	/**
	 * 
	 * @param receiver 收件人称呼
	 * @param receiverAddress 收件人邮箱地址
	 * @param sender 发件人称呼
	 * @param senderMailAddress 发件人的邮箱地址
	 * @param subject 主题
	 * @param content 发送的文本内容
	 */
	public void sendTextMail(String receiver,String receiverAddress,
			String sender,String subject,String content){

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			
			InternetAddress receiveAddress = new InternetAddress();
			receiveAddress.setAddress(receiverAddress);
			receiveAddress.setPersonal(receiver);
			
			InternetAddress fromAddress = new InternetAddress();
			fromAddress.setPersonal(sender);
			fromAddress.setAddress(javaMailSender.getUsername());
			
			messageHelper.setTo(receiveAddress);//接受者     
			messageHelper.setFrom(fromAddress);//发送者
			messageHelper.setSubject(subject);//主题  
			messageHelper.setText(content, true);  
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	
	}

}
