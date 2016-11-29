package com.rojsn.searchengine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author oleg
 */
public class XMLUtils {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XMLUtils.class);

    public void storeXmlProperties(
            final Properties sourceProperties,
            final OutputStream out) {
        try {
            sourceProperties.storeToXML(out, "This is easy!");
        } catch (IOException ioEx) {
            LOG.error("ERROR trying to store properties in XML!");
        }
    }

    public void storeXmlPropertiesToFile(
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
}
