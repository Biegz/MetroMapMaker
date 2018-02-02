/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map.transactions;

import javafx.scene.Node;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import map.data.DraggableEllipse;
import map.data.DraggableText;
import map.data.mapData;

/**
 *
 * @author austin
 */
public class AddNode_Transaction implements jTPS_Transaction{

    private mapData data;
    private Node node;
    
    public AddNode_Transaction(mapData initData, Node initNode) {
        data = initData;
        node = initNode;
    }

    @Override
    public void doTransaction() {
        data.addNode(node);

    }

    @Override
    public void undoTransaction() {
        data.removeNode(node); 

    }
}

