package map.transactions;

import djf.AppTemplate;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.gui.ShapeController;

/**
 *
 * @author McKillaGorilla
 */
public class MoveNode_Transaction implements jTPS_Transaction {
    
    AppTemplate app;

    private DraggableText node;
    private double newX;
    private double newY;
    private double oldX;
    private double oldY;

    public MoveNode_Transaction(DraggableText initNode, double initNewX, double initNewY, double initOldX, double initOldY, AppTemplate initApp) {
        node = initNode;
        newX = initNewX;
        newY = initNewY;
        oldX = initOldX;
        oldY = initOldY;
        
        this.app = initApp;
    }

    @Override
    public void doTransaction() {
        ShapeController shapeController = new ShapeController(app);


        node.setX(newX);
        node.setY(newY);
        
        if(node.getIsLonelyLabel() == false){
            shapeController.dragLineEnd(node);
            shapeController.dragStationOnLine(node);
        }
        

    }

    @Override
    public void undoTransaction() {
        ShapeController shapeController = new ShapeController(app);

        node.setX(oldX);
        node.setY(oldY);
        
        shapeController.dragLineEnd(node);
        shapeController.dragStationOnLine(node);


    }
}
