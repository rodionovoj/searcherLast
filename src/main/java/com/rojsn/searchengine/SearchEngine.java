package com.rojsn.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class SearchEngine {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SearchEngine.class);
    private final String BASE_DOC_FOLDER = "base_folder";
    private static String BASE_FOLDER;
    private int WIDTH_OF_SEARCH;
    private final String WIDTH = "width";
    private int MAX_SIZE_OF_TEXT;
    private final String MAX_SIZE = "max_size";
    private final String MASKS_ALIAS = "masks";
    private static String MASKS = "";
    private static String encoding = "windows-1251";
    private List<String> fileList = new ArrayList<>();
    private Map<String, List<FormattedMatch>> mapOfFiles = new HashMap<>();

    private void init() {
        try {
            InputStream cfg = new FileInputStream("tika-config.xml");
            Properties pref = new Properties();
            pref.loadFromXML(cfg);
            BASE_FOLDER = (String) pref.getProperty(BASE_DOC_FOLDER);
            MASKS = (String) pref.getProperty(MASKS_ALIAS);
            WIDTH_OF_SEARCH = Integer.parseInt((String) pref.getProperty(WIDTH, "200"));
            MAX_SIZE_OF_TEXT = Integer.parseInt((String) pref.getProperty(MAX_SIZE, "10000000"));
            encoding = System.lineSeparator().equals("\r\n") ? "windows-1251" : "UTF-8";            

        } catch (IOException | NumberFormatException e) {
            LOG.error("count=" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        SearchEngine we = new SearchEngine();
        we.init();
        File baseFile = new File(BASE_FOLDER);
        if (baseFile.isDirectory()) {
            we.fillOperatedFileNames(baseFile);
            we.fill("чебурек");
//            we.showContent();
            we.writeXML();
        }       
    }


    private void fillOperatedFileNames(File baseFile) {

        List<File> list = Arrays.asList(baseFile.listFiles());
        String[] extensions = MASKS.split(", ");        
        list.stream().forEach((file) -> {
            if (file.isFile()) {
                for (String extension: extensions) {
                    if (file.getName().contains(extension)) {
                        fileList.add(file.getAbsolutePath());
                    }
                }
            } else {
                fillOperatedFileNames(file.getAbsoluteFile());
            }
        });
    }

    private void fill(String regexp) {
        fileList.stream().forEach((fileName) -> {
            String[] extensions = MASKS.split(", ");
            for (String extension: extensions) {
                if (fileName.contains(extension)) 
                {
                    extractContentDocx(fileName, regexp);
                }
            }
        });
    }

    private void search(String fileName, String regexp, String text) throws UnsupportedEncodingException {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        List<FormattedMatch> matches = new ArrayList<>();
        int index = 1;
        while (matcher.find()) {
            FormattedMatch fm = new FormattedMatch();
            fm.setFileName(fileName);           
            fm.setStart(
                (matcher.start() < WIDTH_OF_SEARCH ? 0 : (matcher.start() - WIDTH_OF_SEARCH))
            );
            fm.setEnd(
                (matcher.end() < WIDTH_OF_SEARCH) ? matcher.end() + WIDTH_OF_SEARCH : matcher.end()
            );
            String dd = "";
            if (fm.getEnd() > text.length()) {
                dd = text.substring(fm.getStart());
            } else {
                dd = text.substring(fm.getStart(), fm.getEnd());
            }
            
            fm.setTextMatch(dd);
            fm.setIndex(index);
            matches.add(fm);
            index++;
            mapOfFiles.put(fileName, matches);
        }
    }

    private void showContent() {
        Set keySet = mapOfFiles.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            List<FormattedMatch> listOfMatches = (List<FormattedMatch>) mapOfFiles.get(key);
            listOfMatches.stream().forEach((fm) -> {
                System.out.println(fm.toString());
            });
        }
    }

    public String identifyLanguage(String text) {
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        return identifier.getLanguage();
    }

    public String parseToPlainText(String fileName) throws IOException, SAXException, TikaException {
//    TikaConfig config = new TikaConfig("tika-config.xml");
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
        Detector detector = tikaConfig.getDetector();
        Parser autoDetectParser = new AutoDetectParser(tikaConfig);
        BodyContentHandler handler = new BodyContentHandler(MAX_SIZE_OF_TEXT);
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(fileName)) {
            parser.parse(stream, handler, metadata);
//            autoDetectParser.parse(stream, handler, metadata);
            return handler.toString();
        }
    }

//    public String parseToStringExample() throws IOException, SAXException, TikaException {
//        Tika tika = new Tika();
//        tika.setMaxStringLength(MAX_SIZE_OF_TEXT);
//        try (InputStream stream = new FileInputStream("d:/d.doc")) {
//            return tika.parseToString(stream);
//        }
//    }
    private void extractContentDocx(String fullFileName, String regexp) {
        try {
            search(fullFileName, regexp, parseToPlainText(fullFileName));
        } catch (IOException | SAXException | TikaException e) {
            LOG.error(e.getMessage());
        }
    }
    
    private void writeXML() {
        
        Set keySet = mapOfFiles.keySet();
        Iterator it = keySet.iterator();        
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(System.out, encoding);
            writer.writeStartDocument(encoding, "1.0");
            writer.writeStartElement("documents");                        
            while (it.hasNext()) {
                String key = (String) it.next();
                List<FormattedMatch> listOfMatches = (List<FormattedMatch>) mapOfFiles.get(key);  
                for (FormattedMatch fm : listOfMatches) {
                    writer.writeStartElement("item");
                    writer.writeAttribute("title", fm.getFileName());   
                    writer.writeAttribute("index", String.valueOf(fm.getIndex()));                   
                    writer.writeAttribute("start_position", String.valueOf(fm.getStart()));                   
                    writer.writeAttribute("end_position", String.valueOf(fm.getEnd()));                   
                    writer.writeStartElement("description");
                    writer.writeCData(fm.getTextMatch());
                    writer.writeEndElement();//description
                    writer.writeEndElement();//item
                }
            }
            writer.writeEndDocument();//documents
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            LOG.error(e.getMessage());       
        }
    }
}
