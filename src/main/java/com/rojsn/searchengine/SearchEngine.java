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
import javax.swing.tree.DefaultMutableTreeNode;
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
    public static final String BASE_DOC_FOLDER = "base_folder";
    public static String BASE_FOLDER;
    private int WIDTH_OF_SEARCH;
    private int LEFT_OFFSET_SEARCH;
    private int RIGHT_OFFSET_SEARCH;
    private final String WIDTH = "width";
    private final String LEFT_OFFSET = "left_offset";
    private final String RIGHT_OFFSET = "right_offset";
    private int MAX_SIZE_OF_TEXT;
    private final String MAX_SIZE = "max_size";
    private final String MASKS_ALIAS = "masks";
    private static String MASKS = "";
    private static String encoding = "windows-1251";
    private Map<String, List<FormattedMatch>> mapOfFiles = new HashMap<>();
    public static boolean CASE_SENSITIVE_VALUE = true;
    private String CASE_SENSITIVE = "case_sensitive";

    public SearchEngine() {
        init();
    }
    
    public Map<String, List<FormattedMatch>> getMapFiles() {
        return mapOfFiles;
    }
    
    private void init() {
        try {
            InputStream cfg = new FileInputStream("tika-config.xml");
            Properties pref = new Properties();
            pref.loadFromXML(cfg);
            BASE_FOLDER = (String) pref.getProperty(BASE_DOC_FOLDER);
            MASKS = (String) pref.getProperty(MASKS_ALIAS);
            WIDTH_OF_SEARCH = Integer.parseInt((String) pref.getProperty(WIDTH, "200"));
            LEFT_OFFSET_SEARCH = Integer.parseInt((String) pref.getProperty(LEFT_OFFSET, "200"));
            RIGHT_OFFSET_SEARCH = Integer.parseInt((String) pref.getProperty(RIGHT_OFFSET, "200"));
            MAX_SIZE_OF_TEXT = Integer.parseInt((String) pref.getProperty(MAX_SIZE, "10000000"));
            encoding = System.lineSeparator().equals("\r\n") ? "windows-1251" : "UTF-8";  
            CASE_SENSITIVE_VALUE = Boolean.parseBoolean((String) pref.getProperty(CASE_SENSITIVE, "true"));

        } catch (IOException | NumberFormatException e) {
            LOG.error("count=" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SearchEngine we = new SearchEngine();
        we.init();
        File baseFile = new File(BASE_FOLDER);
        if (baseFile.isDirectory()) {
            we.fillOperatedFileNames(baseFile, "чебурек");
        }       
    }

    public void fillOperatedFileNames(File baseFile, String regexp, boolean isCaseSensitive) {
        CASE_SENSITIVE_VALUE = isCaseSensitive;
        List<File> list = Arrays.asList(baseFile.listFiles());
        String[] extensions = MASKS.split(", ");        
        for (File file: list) {
            if (file.isFile()) {
                for (String extension: extensions) {
                    if (file.getName().contains(extension)) {
                        extractContentDocx(new ArrayList<>(), file.getAbsolutePath(), regexp);
                    }
                }
            } else {
                fillOperatedFileNames(file.getAbsoluteFile(), regexp, isCaseSensitive);
            }
        }
    }

    public void fillOperatedFileNames(File baseFile, String regexp) {
        List<File> list = Arrays.asList(baseFile.listFiles());
        String[] extensions = MASKS.split(", ");        
        for (File file: list) {
            if (file.isFile()) {
                for (String extension: extensions) {
                    if (file.getName().contains(extension)) {
                        extractContentDocx(new ArrayList<>(), file.getAbsolutePath(), regexp);
                    }
                }
            } else {
                fillOperatedFileNames(file.getAbsoluteFile(), regexp);
            }
        }
    }

    private void extractContentDocx(List<FormattedMatch> list, String fullFileName, String regexp) {
        try {
            search(list, regexp, fullFileName);
        } catch (IOException | SAXException | TikaException e) {
            LOG.error(e.getMessage());
        }
    }

    private void search(List<FormattedMatch> matches, String regexp, String fileName) throws UnsupportedEncodingException, IOException, SAXException, TikaException {        
        String text = parseToPlainText(fileName);
        Pattern pattern = Pattern.compile(CASE_SENSITIVE_VALUE ? regexp : regexp.toUpperCase());
        Matcher matcher = pattern.matcher(CASE_SENSITIVE_VALUE ? text: text.toUpperCase());           
        int index = 1;
        while (matcher.find()) {
            FormattedMatch fm = new FormattedMatch();
            fm.setStart((matcher.start() < LEFT_OFFSET_SEARCH ? 0 : (matcher.start() - LEFT_OFFSET_SEARCH)));
            fm.setEnd((matcher.end() < RIGHT_OFFSET_SEARCH) ? matcher.end() + RIGHT_OFFSET_SEARCH : matcher.end());
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
    
    public void createNodes(DefaultMutableTreeNode top) {
              
        DefaultMutableTreeNode document = null;
        DefaultMutableTreeNode matchNode = null;             
        Set keySet = mapOfFiles.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String fileName = (String) it.next();       
            document = new DefaultMutableTreeNode(fileName);
            top.add(document);
            List<FormattedMatch> matches = mapOfFiles.get(fileName);
            for (FormattedMatch match: matches) {   
                match.setFileName(fileName);
                matchNode = new DefaultMutableTreeNode(match);
                document.add(matchNode);
            }           
        }         
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
}
