/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.gui;

import djf.AppTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.shape.Line;
import map.data.mapData;

/**
 *
 * @author austin
 */
public class NavigateController {

    private AppTemplate app;
    private mapData mapManager;

    double zoomCount = 1;
    double sizeCount = 400;

    double panCountX;
    double panCountY;
    
    Boolean hasGridLines = false;
    

    ObservableList<Line> gridLines; //= FXCollections.observableArrayList(); 

    public NavigateController(AppTemplate initApp) {
        this.app = initApp;
        mapManager = (mapData) app.getDataComponent();
    }

    public void refreshZoomedMap() {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        workspace.getCanvas().setScaleX(zoomCount);
        workspace.getCanvas().setScaleY(zoomCount);

        workspace.getCanvas().setPrefWidth(sizeCount);
        workspace.getCanvas().setPrefHeight(sizeCount);
    }

    public void showGridLines() {
        hasGridLines = true;
        gridLines = FXCollections.observableArrayList();


        mapData mapManager = (mapData) app.getDataComponent();
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        for (int i = 20; i < sizeCount; i += 20) {//workspace.getCanvas().getHeight()
            Line line = new Line(0, i, sizeCount, i);
            line.setStroke(Color.GREY);
            gridLines.add(line);
        }

        for (int i = 20; i < sizeCount; i += 20) {//workspace.getCanvas().getWidth()
            Line line = new Line(i, 0, i,sizeCount );//workspace.getCanvas().getHeight()
            line.setStroke(Color.GREY);
            gridLines.add(line);
        }

        mapManager.getMapNodes().addAll(0, gridLines);

    }

    public void hideGridLines() {
        //gridLines = FXCollections.observableArrayList();
        
        mapData mapManager = (mapData) app.getDataComponent();
        
        //mapManager.getMapNodes().removeAll(gridLines);

        mapManager.getMapNodes().removeAll(gridLines);
        
        

    }
    
    public void updateGridLines(){
        if(hasGridLines){
            hideGridLines();
            showGridLines();
        }
        
        
        
    }
    
    

    public double getZoomCount() {
        return zoomCount;
    }

    public void increaseZoomCount() {
        zoomCount += 0.1;
    }

    public void decreaseZoomCount() {
        if (zoomCount < 0.11) {
            zoomCount = 0.1;
        } else {
            zoomCount -= 0.1;
        }

    }

    public double getSizeCount() {
        return sizeCount;
    }

    public void increaseSizeCount() {
        sizeCount += 100;
    }

    public void decreaseSizeCount() {
        sizeCount -= 100;
    }

}
