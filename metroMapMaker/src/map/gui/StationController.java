/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jtps.jTPS;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.data.mapData;
import map.transactions.AddLine_Transaction;
import map.transactions.AddStationToLine_Transaction;

/**
 *
 * @author austin
 */
public class StationController {

    AppTemplate app;
    mapData mapManager;

    DraggableLine line;

    int currentSelectedNodeIndex;
    int lastSelectedNodeIndex;
    
  

    public StationController(AppTemplate app) {
        this.app = app;
        mapManager = (mapData) app.getDataComponent();
    }

    public void addStationToLine(DraggableEllipse station, int index) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.HAND);

        DraggableLine draggableLine = (DraggableLine) workspace.getListOfLineObjects().get(index);

//        System.out.println("Size of list before addition: " + draggableLine.getListOfLineStations().size());
//
//        draggableLine.addToListOfLineStations(station);
//        draggableLine.addToObservableListOfStations(station.getAssociatedLabel().getText());
//
//        System.out.println("Size of list after addition: " + draggableLine.getListOfLineStations().size());
//
//        System.out.println("Line Name: " + ((DraggableLine) workspace.getListOfLineObjects().get(index)).getAssociatedStartLabel().getText());
//
//        System.out.println("Station Name: " + station.getAssociatedLabel().getText());
        //Proof of concept
        //ObservableList obs = draggableLine.getPoints();
        int listSize = draggableLine.getPoints().size();
        double dist = 1000000000;
        double x = station.getX();
        double y = station.getY();
        int smallestInd = 0;

        for (int i = 0; i < listSize - 3; i += 2) {
//            double temp1 = Math.sqrt(Math.pow((draggableLine.getPoints().get(i) - x), 2)) + Math.sqrt(Math.pow((draggableLine.getPoints().get(i + 1) - y), 2));
//            double temp2 = Math.sqrt(Math.pow((draggableLine.getPoints().get(i + 2) - x), 2)) + Math.sqrt(Math.pow((draggableLine.getPoints().get(i + 3) - y), 2));
//            double temp3 = temp1 + temp2;
//

            double temp1 = (draggableLine.getPoints().get(i) + draggableLine.getPoints().get(i + 2)) / 2;
            double temp2 = (draggableLine.getPoints().get(i + 1) + draggableLine.getPoints().get(i + 3)) / 2;
            double temp3 = Math.sqrt(Math.pow(temp1 - x, 2) + Math.pow(temp2 - y, 2));

            if (dist > temp3) {
                smallestInd = i;
                dist = temp3;
            }
        }
        int i = smallestInd / 2;

        draggableLine.getLineStationsObservableList().add(i, station.getAssociatedLabel().getText());
        draggableLine.getListOfLineStations().add(i, station);

        draggableLine.getPoints().add(smallestInd + 2, station.getX() + station.getRadiusX());
        draggableLine.getPoints().add(smallestInd + 3, station.getY() + station.getRadiusX());

        station.setAssociatedLine(draggableLine);
        station.addToParentLinesList(draggableLine);

        workspace.reloadWorkspace(mapManager);
        


    }

    public void removeStationFromLine(DraggableEllipse station, int index) {

        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        DraggableLine draggableLine = (DraggableLine) workspace.getListOfLineObjects().get(index);

        draggableLine.getPoints().remove(station.getX() + station.getRadiusX());
        draggableLine.getPoints().remove(station.getY() + station.getRadiusX());

        draggableLine.getListOfLineStations().remove(station);
        draggableLine.getLineStationsObservableList().remove(station.getAssociatedLabel().getText());
        
        //mapManager.makeNewStation((int)station.getX(), (int)station.getY(), station.getAssociatedLabel().getText());
        mapManager.removeNode(station);
        mapManager.removeNode(station.getAssociatedLabel());
        
        
    }

    public void makeNewStation() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("The station you entered has already been created.");
        alert.setContentText("Try again.");

        Dialog dialog = new Dialog();
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Set the name of the new station.");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, ButtonType.OK);

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();
        //HBox hbox2 = new HBox();

        Label lineLbl = new Label("Enter the station name:\t");
        Label errorLbl = new Label();
        TextField lineTf = new TextField();
        hbox1.getChildren().addAll(lineLbl, lineTf, errorLbl);
        vbox1.getChildren().addAll(hbox1);
        dialog.getDialogPane().setContent(vbox1);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            String text = lineTf.getText();

            if (!(workspace.getStationsList().contains(text))) {
                mapManager.makeNewStation(50, 50, text);
            } else {
                dialog.close();
                alert.showAndWait();
            }
            workspace.reloadWorkspace(mapManager);
        }
    }

    public void removeStation() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        if (mapManager.getSelectedDraggableNode() instanceof DraggableEllipse) {
            DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();
            workspace.getListOfStationObjects().remove(draggableEllipse);

            if (!(draggableEllipse.getParentLinesList().isEmpty())) {
                draggableEllipse.getAssociatedLine().getPoints().remove(draggableEllipse.getX() + draggableEllipse.getRadiusX());
                draggableEllipse.getAssociatedLine().getPoints().remove(draggableEllipse.getY() + draggableEllipse.getRadiusX());

            }
            mapManager.removeSelectedNode();
            mapManager.removeNode(draggableEllipse.getAssociatedLabel());
            workspace.getStationsList().remove(draggableEllipse.getAssociatedLabel().getText());

        }
    }

    public void changeStationFillColor(Color color) {
        DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();
        draggableEllipse.setFill(color);

    }

    public void addToStationList(String station) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.getStationsList().add(station);
    }

    public void addToListOfStationObjects(DraggableEllipse station) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.getListOfStationObjects().add(station);
    }

    public void focusStationFromCB(int index) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        if (workspace.getListOfStationObjects().size() > 1) {
            DraggableEllipse previousDraggableEllipse = (DraggableEllipse) workspace.getListOfStationObjects().get(lastSelectedNodeIndex);
            mapManager.unhighlightNode(previousDraggableEllipse);
        }

        DraggableEllipse draggableEllipse = (DraggableEllipse) workspace.getListOfStationObjects().get(index);
        mapManager.setSelectedNode(draggableEllipse);
        mapManager.highlightNode(draggableEllipse);

        saveCurrentSelectedNodeIndex(index);
        saveLastSelectedNodeIndex(index);

        workspace.reloadWorkspace(mapManager);
    }

    public void processSelectStationRadius() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        int radius = (int) workspace.getRadiusSlider().getValue();

        DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();
        draggableEllipse.setRadiusX(radius);
        draggableEllipse.setRadiusY(radius);
    }

    public void processMoveStationLabel() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();
        
        //RIGHT
        if(draggableEllipse.getLabelPosition() == 1){
            draggableEllipse.getAssociatedLabel().setX(draggableEllipse.getCenterX() + draggableEllipse.getRadiusX() + draggableEllipse.getAssociatedLabel().getLayoutBounds().getWidth()/3);
            draggableEllipse.getAssociatedLabel().setY(draggableEllipse.getAssociatedLabel().getY() - draggableEllipse.getRadiusY() * 2);
        }
        //TOP
        if(draggableEllipse.getLabelPosition() == 2){
            draggableEllipse.getAssociatedLabel().setX(draggableEllipse.getCenterX() - draggableEllipse.getAssociatedLabel().getLayoutBounds().getWidth()/2.5);
            draggableEllipse.getAssociatedLabel().setY(draggableEllipse.getAssociatedLabel().getY() - draggableEllipse.getRadiusY() * 2.5);
        }
        //LEFT
        if(draggableEllipse.getLabelPosition() == 3){
            draggableEllipse.getAssociatedLabel().setX(draggableEllipse.getCenterX() - draggableEllipse.getRadiusX() - draggableEllipse.getAssociatedLabel().getLayoutBounds().getWidth()*1.25);
            draggableEllipse.getAssociatedLabel().setY(draggableEllipse.getCenterY() + 2);
        }
        //BOTTOM
        if(draggableEllipse.getLabelPosition() == 4){
            draggableEllipse.getAssociatedLabel().setX(draggableEllipse.getCenterX() - draggableEllipse.getAssociatedLabel().getLayoutBounds().getWidth()/1.75);
            draggableEllipse.getAssociatedLabel().setY(draggableEllipse.getAssociatedLabel().getY() + draggableEllipse.getRadiusY() * 2.5);
        }

    }

    //saves last selected node Index from the combobox
    public void saveLastSelectedNodeIndex(int index) {
        this.lastSelectedNodeIndex = index;
    }

    public void saveCurrentSelectedNodeIndex(int index) {
        this.currentSelectedNodeIndex = index;
    }
    
    public void snapStationToGrid(){
        ShapeController shapeController = new ShapeController(app);
        
        DraggableEllipse station = (DraggableEllipse) mapManager.getSelectedDraggableNode();
        DraggableText label = (DraggableText)station.getAssociatedLabel();
        
        
        
        station.setCenterX(((int)station.getCenterX() /20)*20);
        station.setCenterY(((int)station.getCenterY() /20)*20);
        shapeController.dragStationOnLine(label);
        shapeController.bindTextToEllipse(label, station);
        
    }

}
