package com.mail;

import java.io.IOException;

import com.model.Mail;
import com.model.entity.MailItem;

public interface MailManager {
	void toDBQueue(Mail mail) throws IOException;

	void toSendQueue(MailItem mail) throws IOException;

}
