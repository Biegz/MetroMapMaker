/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.transactions;

import djf.AppTemplate;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import map.data.DraggableEllipse;
import map.data.mapData;
import map.gui.StationController;

/**
 *
 * @author austin
 */
public class AddStationToLine_Transaction implements jTPS_Transaction {
    private AppTemplate app;
    private mapData mapManager;
    private DraggableEllipse station;
    private int i;
    
    public AddStationToLine_Transaction(AppTemplate app,mapData initData, DraggableEllipse station, int i){
        this.app = app;
        this.mapManager = initData;
        this.station = station;
        this.i = i;
        
}
    
    
    public void doTransaction(){
        StationController stationController = new StationController(app);
        stationController.addStationToLine(station, i);
    }
    
    public void undoTransaction(){
        StationController stationController = new StationController(app);
        stationController.removeStationFromLine(station, i);
    }
}
