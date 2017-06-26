package com.service;

import com.model.Mail;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Julia on 06.06.2017.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC,use=SOAPBinding.Use.LITERAL,
        parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
public interface MailService {
    @WebMethod
   String sendEmail(Mail mail) throws JAXBException, IOException;
}
