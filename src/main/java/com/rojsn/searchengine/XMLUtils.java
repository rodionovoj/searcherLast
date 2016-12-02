package com.rojsn.searchengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oleg
 */
public class XMLUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XMLUtils.class);

    public static void storeXmlProperties(
            final Properties sourceProperties,
            final OutputStream out) {
        try {
            sourceProperties.storeToXML(out, "Parameters for application");
        } catch (IOException ioEx) {
            LOG.error("ERROR trying to store properties in XML!");
        }
    }

    public static void storeXmlPropertiesToFile(
            final Properties sourceProperties,
            final String pathAndFileName) {
        try (FileOutputStream fos = new FileOutputStream(pathAndFileName)) {
            storeXmlProperties(sourceProperties, fos);
        } catch (FileNotFoundException fnfEx) {
            LOG.error("ERROR writing to " + pathAndFileName);
        } catch (IOException ioEx) {
            LOG.error("ERROR trying to write XML properties to file " + pathAndFileName);
        }
    }
    
    public static void saveProperty(String key, String value) {
        
        Properties props = new Properties();
        String fileName = "tika-config.xml";
            try {
                props.loadFromXML(new FileInputStream(fileName));
                props.setProperty(key, value);
                storeXmlPropertiesToFile(props, fileName);        
            } catch (FileNotFoundException ex) {
                Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    
    public static void main(String[] arg) throws Exception{
        
        Properties props = new Properties();
        props.loadFromXML(new FileInputStream("tika-config.xml"));
        props.setProperty("base_folder", "dfs");
        storeXmlPropertiesToFile(props, "tika-config.xml");
        
    
//        String xmlString = setupXMLBuffer(
//              new StringBuffer("<firstName>")
//                .append(customer.firstName)
//       .append("</firstName>")
//       .append("<lastName>")
//       .append(customer.lastName)
//       .append("</lastName>")
//  // etc...
//       .toString()
//        );
//bufferedWriter.write(xmlString);
// other file I/O code
    }
}
