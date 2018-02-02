/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.transactions;

import jtps.jTPS_Transaction;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.data.mapData;

/**
 *
 * @author austin
 */
public class AddLine_Transaction implements jTPS_Transaction {
    private mapData data;
    private DraggableLine line;
    private DraggableText startLabel;
    private DraggableText endLabel;
    
    public AddLine_Transaction(mapData initData, DraggableLine line, DraggableText startLabel, DraggableText endLabel) {
        data = initData;
        this.line = line;
        this.startLabel = startLabel;
        this.endLabel = endLabel;
        
    }

    @Override
    public void doTransaction() {
        data.addNode(line);
        data.addNode(startLabel);
        data.addNode(endLabel);
    }

    @Override
    public void undoTransaction() {
        data.removeNode(line); 
        data.removeNode(startLabel);
        data.removeNode(endLabel);
    }
}
