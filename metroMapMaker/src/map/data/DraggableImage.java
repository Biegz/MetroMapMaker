package map.data;

import javafx.scene.image.Image;
import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class DraggableImage extends Rectangle implements Draggable {
    double startX;
    double startY;

    public DraggableImage() {
        setX(0.0);
        setY(0.0);
        setWidth(0.0);
        setHeight(0.0);
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;
    }

    @Override
    public mapState getStartingState() {
        return mapState.STARTING_RECTANGLE;
    }

    @Override
    public void start(int x, int y) {
        startX = x;
        startY = y;
        setX(x);
        setY(y);

        
    }

    @Override
    public void drag(int x, int y) {

        //code below drags from center
//        double diffX = x - (getX() + (getWidth() / 2));
//        double diffY = y - (getY() + (getHeight() / 2));
//        double newX = getX() + diffX;
//        double newY = getY() + diffY;
//        xProperty().set(newX);
//        yProperty().set(newY);
//        startX = x;
//        startY = y;

        
        
        //drags from point clicked
        double diffX = x - startX;
        double diffY = y - startY;
        double newX = getX() + diffX;
        double newY = getY() + diffY;
        xProperty().set(newX);
        yProperty().set(newY);

        
        startX = x;
        startY = y;
        
        

    }

    public String cT(double x, double y) {
        return "(x,y): (" + x + "," + y + ")";
    }

    @Override
    public void size(int x, int y) {
        double width = x - getX();
        widthProperty().set(width);
        double height = y - getY();
        heightProperty().set(height);

    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        xProperty().set(initX);
        yProperty().set(initY);
        widthProperty().set(initWidth);
        heightProperty().set(initHeight);
    }

//    @Override
//    public String getShapeType() {
//        return IMAGE;
//    }

//    @Override
//    public void startDrag(int x, int y) {
//        startX = x;
//        startY = y;
//    }

    @Override
    public Draggable makeClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNodeType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStart(int initX, int initY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setImage(Image i){
        
    }
}
