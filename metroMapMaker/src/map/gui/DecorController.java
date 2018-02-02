/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import map.data.DraggableText;
import map.data.mapData;

/**
 *
 * @author austin
 */
public class DecorController {

    private AppTemplate app;
    private mapData mapManager;

    public DecorController(AppTemplate initApp) {
        app = initApp;
        mapManager = (mapData) app.getDataComponent();
    }

    public void setImageBackground() {

    }
    
    public void addImageOverlay(){
        
    }

    public void addLabel() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("You must enter text for the label.");
        alert.setContentText("Try again.");

        Dialog dialog = new Dialog();
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Set the text of the new label.");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, ButtonType.OK);

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();
        //HBox hbox2 = new HBox();

        Label lineLbl = new Label("Enter the label text:\t");
        Label errorLbl = new Label();
        TextField lineTf = new TextField();
        hbox1.getChildren().addAll(lineLbl, lineTf, errorLbl);
        vbox1.getChildren().addAll(hbox1);
        dialog.getDialogPane().setContent(vbox1);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            String text = lineTf.getText();

            if (!(workspace.getStationsList().contains(text))) {
                mapManager.makeNewText(25, 25, text);
            } else {
                dialog.close();
                alert.showAndWait();
            }
            workspace.reloadWorkspace(mapManager);
        }
    }
    
    public void removeElement(){
        if(mapManager.getSelectedDraggableNode() instanceof DraggableText){
            DraggableText draggableLabel = (DraggableText)mapManager.getSelectedDraggableNode();
            if(draggableLabel.getIsLonelyLabel()){
                mapManager.removeNode(draggableLabel);
            }
        }
    }

}
