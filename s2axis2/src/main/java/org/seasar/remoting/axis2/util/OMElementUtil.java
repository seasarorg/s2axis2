package org.seasar.remoting.axis2.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;

public class OMElementUtil {

    public static final String DEFAULT_ENCODE = "UTF-8";

    public OMElementUtil() {}

    public static String toString(OMElement om) {

        XMLStreamReader reader = om.getXMLStreamReader();
        String enc = reader.getCharacterEncodingScheme();

        return toString(om, enc);

    }

    public static String toString(OMElement om, String enc) {

        String str = null;

        if (enc == null || enc.equals("")) {
            enc = DEFAULT_ENCODE;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            om.serialize(baos);
        }
        catch (XMLStreamException ex) {
            str = om.toString();
        }

        if (str == null) {
            try {
                str = baos.toString(enc);
            }
            catch (UnsupportedEncodingException ex) {
                str = baos.toString();
            }
        }

        return str;

    }

}
