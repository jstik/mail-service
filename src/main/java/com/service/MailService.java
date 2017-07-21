package com.service;

import com.model.Mail;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Julia on 06.06.2017.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MailService {
    @WebMethod
   String sendEmail(@WebParam(name="mail") Mail mail) throws JAXBException, IOException;
}
