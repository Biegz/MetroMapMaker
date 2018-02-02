package map.gui;

import djf.AppTemplate;
import djf.components.AppClipboardComponent;
import map.data.Draggable;
import map.data.mapData;
import map.transactions.Paste_Transaction;
import map.transactions.RemoveNode_Transaction;
import javafx.scene.Node;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class mapClipboard implements AppClipboardComponent {
    private AppTemplate app;
    private mapData data;
    private Node nodeOnClipboard;
    
    public mapClipboard(AppTemplate initApp) {
        app = initApp;
        data = (mapData)app.getDataComponent();
        nodeOnClipboard = null;
    }
    
    @Override
    public void cut() {
        // CHECK AND SEE IF THERE IS A DRAGGABLE SELECTED
        Node testNode = (Node)data.getSelectedDraggableNode();
        
        // IF NO, DO NOTHING
        if (testNode == null)
            return;

        // ALSO ASSIGN IT TO THE DRAGGABLEONCLIPBOARD
        nodeOnClipboard = testNode;
        
        // ADD A TRANSACTION TO REMOVE IT
        RemoveNode_Transaction transaction = new RemoveNode_Transaction(data, testNode);
        jTPS tps = app.getTPS();
        tps.addTransaction(transaction);
                
        // AND MAKE IT UNSELECTED
        data.unhighlightNode(testNode);
        data.setSelectedNode(null);
    }

    @Override
    public void copy() {
        // GET THE SELECTED NODE
        Node node = data.getSelectedNode();
        
        // IF IT'S NULL, RETURN
        if (node == null)
            return;
        
        // IF IT'S NOT, MAKE A CLONE
        Draggable draggableNode = (Draggable)node;
        nodeOnClipboard = (Node)draggableNode.makeClone();
    }

    @Override
    public void paste() {
        if (nodeOnClipboard != null) {
            mapData data = (mapData)app.getDataComponent();
            Paste_Transaction transaction = new Paste_Transaction(data, nodeOnClipboard);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
            Node selectedNode = data.getSelectedNode();
            if (selectedNode != null) {
                data.unhighlightNode(nodeOnClipboard);
            }
            data.setSelectedNode(nodeOnClipboard);
            data.highlightNode(nodeOnClipboard);
            nodeOnClipboard = null;
        }
    }    
}