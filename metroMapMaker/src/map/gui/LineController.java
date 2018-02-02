/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import map.data.Draggable;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.data.mapData;
import map.data.mapState;

/**
 *
 * @author austin
 */
public class LineController {

    ArrayList<String> lines2 = new ArrayList();

    ColorPicker lineColorPicker;
    CheckBox roundedLine;

    AppTemplate app;
    mapData mapManager;

    int lastSelectedNode;
    int currentSelectedNode;

    public LineController(AppTemplate app) {
        this.app = app;
        mapManager = (mapData) app.getDataComponent();

    }

    public void addLine() {
        lineColorPicker = new ColorPicker(rgb(0, 0, 0));
        Dialog dialog = new Dialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Set the name and color of the new line.");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, ButtonType.OK);

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        Label lineLbl = new Label("Enter the line name:\t");
        Label colorLbl = new Label("Choose the line color:\t");
        TextField lineTf = new TextField();
        hbox1.getChildren().addAll(lineLbl, lineTf);
        hbox2.getChildren().addAll(colorLbl, lineColorPicker);
        vbox1.getChildren().addAll(hbox1, hbox2);

        dialog.getDialogPane().setContent(vbox1);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            String text = lineTf.getText();
            Color colorResult = lineColorPicker.getValue();
            setLineColorPicker(colorResult);
            mapManager.makeNewLine(50, 150, 200, 150, text, colorResult);

            mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
            workspace.reloadWorkspace(mapManager);
        }

    }

    public void removeLine() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
            DraggableLine draggableLine = (DraggableLine) mapManager.getSelectedDraggableNode();
            workspace.getListOfLineObjects().remove(draggableLine);
            mapManager.removeSelectedNode();
            mapManager.removeNode(draggableLine.getAssociatedStartLabel());
            mapManager.removeNode(draggableLine.getAssociatedEndLabel());
            workspace.getLinesList().remove(draggableLine.getAssociatedStartLabel().getText());

        }
    }

    public void addToLineList(String line) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.getLinesList().add(line);
    }

    public void addToListOfLineObjects(DraggableLine line) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.getListOfLineObjects().add(line);
    }

    public void focusLineFromCB(int index) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        //System.out.println("String Array Size: " + workspace.getLinesList().size());
        //System.out.println("Lines Array Size: " + workspace.getListOfLineObjects().size());
        if (workspace.getListOfLineObjects().size() > 1) {
            DraggableLine previousDraggableLine = (DraggableLine) workspace.getListOfLineObjects().get(lastSelectedNode);
            mapManager.unhighlightNode(previousDraggableLine);
        }

        DraggableLine draggableLine = (DraggableLine) workspace.getListOfLineObjects().get(index);
        mapManager.setSelectedNode(draggableLine);
        mapManager.highlightNode(draggableLine);
        saveCurrentSelectedNodeIndex(index);

        saveLastSelectedNodeIndex(index);

    }

    public void editLineDialog() {
        DraggableLine draggableLine = (DraggableLine) mapManager.getSelectedDraggableNode();
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("You must give the line a name.");
        alert.setContentText("Try again.");

        roundedLine = new CheckBox("Rouned Line");
        if (draggableLine.getStrokeLineCap().equals(draggableLine.getStrokeLineCap().SQUARE)) {
            roundedLine.setSelected(false);
        } else if (draggableLine.getStrokeLineCap().equals(draggableLine.getStrokeLineCap().ROUND)) {
            roundedLine.setSelected(true);
        }
        lineColorPicker = new ColorPicker(rgb(0, 0, 0));
        Dialog dialog = new Dialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Change the name and color of the line.");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, ButtonType.OK);

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();

        Label lineLbl = new Label("New line name:\t");
        Label colorLbl = new Label("New line color:\t");
        TextField lineTf = new TextField(draggableLine.getAssociatedStartLabel().getText());
        hbox1.getChildren().addAll(lineLbl, lineTf);
        hbox2.getChildren().addAll(colorLbl, lineColorPicker);
        hbox3.getChildren().add(roundedLine);
        vbox1.getChildren().addAll(hbox1, hbox2, hbox3);

        dialog.getDialogPane().setContent(vbox1);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            int x = workspace.getLinesList().indexOf(draggableLine.getAssociatedStartLabel().getText());

            String text = lineTf.getText();
            Color colorResult = lineColorPicker.getValue();

            if (!(text.isEmpty())) {
                draggableLine.getAssociatedStartLabel().setText(text);
                draggableLine.getAssociatedEndLabel().setText(text);

                workspace.getLinesList().set(x, text);

                draggableLine.setStroke(colorResult);
                if (roundedLine.isSelected()) {
                    draggableLine.setStrokeLineCap(StrokeLineCap.ROUND);
                } else if (roundedLine.isSelected() == false) {
                    draggableLine.setStrokeLineCap(StrokeLineCap.SQUARE);
                }
            } else if (text.isEmpty()) {
                dialog.close();
                alert.showAndWait();
            }

            workspace.reloadWorkspace(mapManager);
        }

    }

    public void processSelectLineThickness() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        int outlineThickness = (int) workspace.getLineThicknessSlider().getValue();

        DraggableLine draggableLine = (DraggableLine) mapManager.getSelectedDraggableNode();
        draggableLine.setStrokeWidth(outlineThickness);
        app.getGUI().updateToolbarControls(false);
    }

    //SETTERS AND SAVER METHODS
    public void setLineColorFromColorPicker(Color color) {
        if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
            DraggableLine draggableLine = (DraggableLine) mapManager.getSelectedDraggableNode();
            draggableLine.setStroke(color);
        }
    }
    
    public void listAllStationsInLine(){
        mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
        DraggableLine draggableLine = (DraggableLine)mapManager.getSelectedDraggableNode();
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("All stations in the selected line:");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, ButtonType.OK);

        VBox vbox1 = new VBox();
        
        String list = "";
        
        for(int i = 0; i < draggableLine.getLineStationsObservableList().size(); i++){
            String str = (String)draggableLine.getLineStationsObservableList().get(i);
            list += "- " + str + "\n";
        }
        
        Text text = new Text(list);

        vbox1.getChildren().addAll(text);

        dialog.getDialogPane().setContent(vbox1);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            workspace.reloadWorkspace(mapManager);
        }
    }

    public void setLineColorPicker(Color color) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.getLineColorPicker().setValue(color);
    }

    //saves last selected node Index from the combobox
    public void saveLastSelectedNodeIndex(int index) {
        this.lastSelectedNode = index;
    }

    public void saveCurrentSelectedNodeIndex(int index) {
        this.currentSelectedNode = index;
    }

    public void snapLineEndToGrid() {
        ShapeController shapeController = new ShapeController(app);

        DraggableText selectedDraggableNode = (DraggableText) mapManager.getSelectedDraggableNode();

        if (selectedDraggableNode.getIsForLine() == true && selectedDraggableNode.getIsLonelyLabel() == false) {

            //Checks which node clicked is the start node or the end node and then drags the line end accordingly
            if (selectedDraggableNode.getIsStart() == true && selectedDraggableNode.getIsLonelyLabel() == false) {
                //FOR POLY LINE START POINT
                ObservableList obs = selectedDraggableNode.getAssociatedLine().getPoints();
                selectedDraggableNode.getAssociatedLine().getPoints().set(0, Math.floor(selectedDraggableNode.getAssociatedLine().getPoints().get(0)/20)*20);
                selectedDraggableNode.getAssociatedLine().getPoints().set(1, Math.floor(selectedDraggableNode.getAssociatedLine().getPoints().get(1)/20)*20);
                
                selectedDraggableNode.setX(selectedDraggableNode.getAssociatedLine().getPoints().get(0) - selectedDraggableNode.getLayoutBounds().getWidth()-4);
                selectedDraggableNode.setY(selectedDraggableNode.getAssociatedLine().getPoints().get(1));
                
            } else {
                //FOR POLYLINE END POINT
                ObservableList obs = selectedDraggableNode.getAssociatedLine().getPoints();
                selectedDraggableNode.getAssociatedLine().getPoints().set(obs.size()-2, Math.floor(selectedDraggableNode.getAssociatedLine().getPoints().get(obs.size()-2)/20)*20);
                selectedDraggableNode.getAssociatedLine().getPoints().set(obs.size()-1, Math.floor(selectedDraggableNode.getAssociatedLine().getPoints().get(obs.size()-1)/20)*20);
                
                selectedDraggableNode.setX(selectedDraggableNode.getAssociatedLine().getPoints().get(obs.size()-2) + selectedDraggableNode.getLayoutBounds().getWidth());
                selectedDraggableNode.setY(selectedDraggableNode.getAssociatedLine().getPoints().get(obs.size()-1));
            }

            app.getGUI().updateToolbarControls(false);
        } else {

            //DRAGGING A STATION
            if (selectedDraggableNode.getIsStart() == true && selectedDraggableNode.getIsLonelyLabel() == false) {

                DraggableEllipse draggableEllipse = (DraggableEllipse) selectedDraggableNode.getAssociatedEllipse();
                DraggableText draggableText = (DraggableText) selectedDraggableNode;

                //bindEllipseToText(draggableEllipse, draggableText);

            }
        }
        
        

    }

}
