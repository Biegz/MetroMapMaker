package map.transactions;

import map.data.mapData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import map.data.DraggableEllipse;
import map.data.DraggableText;

/**
 *
 * @author McKillaGorilla
 */
public class AddStation_Transaction implements jTPS_Transaction {
    private mapData data;
    private DraggableEllipse station;
    private DraggableText label;
    
    public AddStation_Transaction(mapData initData, DraggableEllipse station, DraggableText label) {
        data = initData;
        this.station = station;
        this.label = label;
    }

    @Override
    public void doTransaction() {
        data.addNode(station);
        data.addNode(label);
    }

    @Override
    public void undoTransaction() {
        data.removeNode(station); 
        data.removeNode(label);
    }
}