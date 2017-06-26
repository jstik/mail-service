package com.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Julia on 07.06.2017.
 */

@XmlRootElement
public class XmlChild {

    @XmlElement
    String name = "name";

    @XmlElement
    String id ="id";
}
