package com.rojsn.searchengine;

import java.util.regex.Matcher;

/**
 *
 * @author sbt-rodionov-oy
 */
class FormattedMatch {

    private String fileName;
    private int start;
    private int end;
    private int index;
    private String textMatch;

//    public FormattedMatch(Matcher matcher) {
//        this.setFileName(fileName);
//            this.setStart((matcher.start() < WIDTH_OF_SEARCH ? 0 : (matcher.start() - WIDTH_OF_SEARCH)));
//            this.setEnd(matcher.end() + WIDTH_OF_SEARCH);
//            String dd = text.substring(fm.getStart(), fm.getEnd());
//            this.setTextMatch(dd);
//            this.setIndex(index);
//    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the textMatch
     */
    public String getTextMatch() {
        return textMatch;
    }

    /**
     * @param textMatch the textMatch to set
     */
    public void setTextMatch(String textMatch) {
        this.textMatch = textMatch;
    }
    
    public String toString() {
        return "\n" + this.getFileName() + ":\n совпадение №" + this.getIndex() + " в диапазоне: от " +  this.getStart() + " до " + this.getEnd() + ":\n" + this.textMatch;        
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
