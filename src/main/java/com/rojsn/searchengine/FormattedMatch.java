package com.rojsn.searchengine;

import java.util.regex.Matcher;

/**
 *
 * @author sbt-rodionov-oy
 */
class FormattedMatch {

    private int start;
    private int end;
    private int index;
    private String textMatch;

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
        return ":\n Совпадение №" + this.getIndex() + " в диапазоне: от " +  this.getStart() + " до " + this.getEnd() + ":\n" + this.textMatch;        
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
