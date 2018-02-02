/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.data;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author austin
 */
public class DraggableLine extends Polyline implements Draggable {

    double startX;
    double startY;

    Polyline line;

    Text associatedStartLabel;
    Text associatedEndLabel;

    ArrayList<DraggableEllipse> lineStations = new ArrayList();
    ObservableList<String> lineStations2 = FXCollections.observableArrayList();

    public DraggableLine() {

//        setStartX(0.0);
//        setStartY(0.0);
//        startX = 0;
//        startY = 0;
    }

    @Override
    public Draggable makeClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public mapState getStartingState() {
        return null;
    }

    @Override
    public void start(int x, int y) {
        startX = x;
        startY = y;
//        setStartX(x);
//        setStartY(y);
//        setEndX(x + 400);
//        setEndY(y);

    }

    @Override
    public void drag(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void size(int x, int y) {
//        setEndX(x);
//        setEndY(y);
    }

    @Override
    public double getX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public double getY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public void setStart(int initX, int initY) {
        startX = initX;
        startY = initY;
    }

    public void setAssociatedStartLabel(Text text) {
        this.associatedStartLabel = text;
    }

    public void setAssociatedEndLabel(Text text) {
        this.associatedEndLabel = text;
    }

    public void addToListOfLineStations(DraggableEllipse station) {
        lineStations.add(station);
    }

    public void removeFromListOfLineStations(DraggableEllipse station) {
        lineStations.remove(station);
    }

    public void addToObservableListOfStations(String text) {
        lineStations2.add(text);
    }

    public void removeFromObservableListOfStations(String text) {
        lineStations2.remove(text);
    }

    public ObservableList getLineStationsObservableList() {
        return lineStations2;
    }

    public Text getAssociatedStartLabel() {
        return associatedStartLabel;
    }

    public Text getAssociatedEndLabel() {
        return associatedEndLabel;
    }

    public ArrayList getListOfLineStations() {
        return lineStations;
    }
    
        @Override
    public String getNodeType() {
        return LINE;
    }

}
