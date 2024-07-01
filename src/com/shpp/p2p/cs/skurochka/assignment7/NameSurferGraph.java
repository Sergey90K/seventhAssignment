package com.shpp.p2p.cs.skurochka.assignment7;

/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;
import com.shpp.p2p.cs.skurochka.assignment7.NameSurferConstants;
import com.shpp.p2p.cs.skurochka.assignment7.NameSurferEntry;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NameSurferGraph extends GCanvas
        implements NameSurferConstants, ComponentListener {
    private ArrayList<NameSurferEntry> entryList = new ArrayList();

    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
        update();
    }

    /**
     * Clears the list of name surfer entries stored inside this class.
     */
    public void clear() {
        entryList.clear();
        update();
    }

    /* Method: addEntry(entry) */

    /**
     * Adds a new NameSurferEntry to the list of entries on the display.
     * Note that this method does not actually draw the graph, but
     * simply stores the entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        if (entry != null) {
            entryList.add(entry);
        }
        update();
    }

    /**
     * Updates the display image by deleting all the graphical objects
     * from the canvas and then reassembling the display according to
     * the list of entries. Your application must call update after
     * calling either clear or addEntry; update is also called whenever
     * the size of the canvas changes.
     */
    public void update() {
        removeAll();
        drawTopHorizontalLine();
        drawBottomHorizontalLine();
        drawAllVerticalLines();
        startDrawGraph();
    }

    public void deleteNameFromGraph(String name){
        for (int i = 0; i< entryList.size(); i++){
            if(name.toLowerCase().equals(entryList.get(i).getName().toLowerCase())){
                entryList.remove(entryList.get(i));
                update();
                break;
            }
        }
    }

    private void startDrawGraph(){
        if(entryList.size() != 0){
            drawGraph(entryList);
        }
    }

    private void drawGraph(ArrayList<NameSurferEntry> entryList) {
        double stepX = getWidth() / (double) NDECADES;
        double scaleY = (getHeight() - 2 * GRAPH_MARGIN_SIZE) / (double) MAX_RANK;
        for (int i = 0; i < entryList.size(); i++) {
            NameSurferEntry currentNameSurferEntry = entryList.get(i);
            for (int j = 0; j < NDECADES; j++) {
                int rank = checkRank(currentNameSurferEntry.getRank(j));
                if (j != 0) {
                    int previousRank = checkRank(currentNameSurferEntry.getRank(j - 1));
                    drawLine(stepX * (j - 1),
                            GRAPH_MARGIN_SIZE + previousRank * scaleY,
                            stepX * j, GRAPH_MARGIN_SIZE + rank + scaleY,
                            getColor(i));
                }
                drawLabel(getStringRank(rank) + currentNameSurferEntry.getName(),
                        stepX * j,
                        GRAPH_MARGIN_SIZE + rank * scaleY,
                        getColor(i));
                drawPoint(stepX * j - DIAMETER_POINT /2.0 ,
                        (GRAPH_MARGIN_SIZE + rank * scaleY) - DIAMETER_POINT/2.0,
                        DIAMETER_POINT,
                        getColor(i));
            }
        }
    }

    private void drawPoint(double startX, double startY, double diameterPoint, Color color){
        GOval gOval = new GOval(startX, startY, diameterPoint,diameterPoint);
        gOval.setFilled(true);
        gOval.setColor(color);
        add(gOval);
    }

    private String getStringRank(int rank) {
        return rank == MAX_RANK ? "*" : " " + rank;
    }

    private void drawLabel(String text, double startX, double startY, Color color) {
        double fontScale = (getWidth() * getHeight()) / (double) (APPLICATION_WIDTH * APPLICATION_HEIGHT);
        GLabel gLabel = new GLabel(text, startX, startY);
        gLabel.setFont(new Font("Arial", Font.BOLD, (int) (12 * fontScale)));
        gLabel.setColor(color);
        gLabel.sendToFront();
        add(gLabel);
    }

    private Color getColor(int numberOfLine) {
        Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};
        return colors[numberOfLine % colors.length];
    }

    private void drawLine(double startX, double startY, double endX, double endY, Color color) {
        GLine gLine = new GLine(startX, startY, endX, endY);
        gLine.setColor(color);
        add(gLine);
    }

    private int checkRank(int rank) {
        return rank == 0 ? MAX_RANK : rank;
    }

    private void drawAllVerticalLines() {
        int startDecade;
        String nameDecade;
        int widthBetweenLines = getWidth() / NDECADES;
        for (int i = 0; i < NDECADES; i++) {
            startDecade = START_DECADE + STEP_DECADE * i;
            nameDecade = Integer.toString(startDecade);
            drawLine(widthBetweenLines * i, 0, widthBetweenLines * i, getHeight());
            drawLabel(nameDecade, widthBetweenLines * i, getHeight());
        }
    }

    private void drawLabel(String labelText, double startX, double startY) {
        GLabel label = new GLabel(labelText);
        add(label, startX, startY - label.getDescent());
    }

    private void drawBottomHorizontalLine() {
        drawHorizontalLine(getHeight() - GRAPH_MARGIN_SIZE);
    }

    private void drawTopHorizontalLine() {
        drawHorizontalLine(GRAPH_MARGIN_SIZE);
    }

    private void drawHorizontalLine(double height) {
        drawLine(0, height, getWidth(), height);
    }

    private void drawLine(double startX, double startY, double endX, double endY) {
        GLine line = new GLine(startX, startY, endX, endY);
        add(line);
    }

    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        update();
    }

    public void componentShown(ComponentEvent e) {
    }
}
