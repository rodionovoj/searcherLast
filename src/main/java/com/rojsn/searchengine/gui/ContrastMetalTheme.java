package com.rojsn.searchengine.gui;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;

public class ContrastMetalTheme extends DefaultMetalTheme {

    public String getName() {
        return "Contrast";
    }

    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
    private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
    private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);
    private final ColorUIResource primaryHighlight = new ColorUIResource(102, 102, 102);

    private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
    private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
    private final ColorUIResource controlHighlight = new ColorUIResource(102, 102, 102);

    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override
    public ColorUIResource getPrimaryControlHighlight() {
        return primaryHighlight;
    }

    @Override
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    @Override
    public ColorUIResource getControlHighlight() {
        return super.getSecondary3();
    }

    @Override
    public ColorUIResource getFocusColor() {
        return getBlack();
    }

    @Override
    public ColorUIResource getTextHighlightColor() {
        return getBlack();
    }

    @Override
    public ColorUIResource getHighlightedTextColor() {
        return getWhite();
    }

    @Override
    public ColorUIResource getMenuSelectedBackground() {
        return getBlack();
    }

    @Override
    public ColorUIResource getMenuSelectedForeground() {
        return getWhite();
    }

    @Override
    public ColorUIResource getAcceleratorForeground() {
        return getBlack();
    }

    @Override
    public ColorUIResource getAcceleratorSelectedForeground() {
        return getWhite();
    }

    @Override
    public void addCustomEntriesToTable(UIDefaults table) {

        Border blackLineBorder = new BorderUIResource(new LineBorder(getBlack()));
        Border whiteLineBorder = new BorderUIResource(new LineBorder(getWhite()));

        Object textBorder = new BorderUIResource(new CompoundBorder(
                blackLineBorder,
                new BasicBorders.MarginBorder()));

        table.put("ToolTip.border", blackLineBorder);
        table.put("TitledBorder.border", blackLineBorder);
        table.put("Table.focusCellHighlightBorder", whiteLineBorder);
        table.put("Table.focusCellForeground", getWhite());

        table.put("TextField.border", textBorder);
        table.put("PasswordField.border", textBorder);
        table.put("TextArea.border", textBorder);
        table.put("TextPane.font", textBorder);
    }
}
