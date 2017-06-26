package com.mail;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Julia on 09.06.2017.
 */
public class JaxbDataFormat  implements DataFormat{

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {

    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        return null;
    }
}
