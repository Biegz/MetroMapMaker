package map.data;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import javafx.scene.text.Text;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class DraggableText extends Text implements Draggable {

    double startX;
    double startY;

    DraggableLine associatedLine;

    Boolean isStart;
    Boolean isForLine;
    Boolean isLonelyLabel = false;

    Ellipse associatedEllipse;

    public DraggableText(String initText) {
        super(initText);

        setX(0.0);
        setY(0.0);
        //setWidth(0.0);
        //setHeight(0.0);
        setOpacity(1.0);
        startX = 0.0;
        startY = 0.0;
    }

    @Override
    public DraggableText makeClone() {
        DraggableText cloneText = new DraggableText(getText());
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        cloneText.setX(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
        cloneText.setY(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
        cloneText.setFont(getFont());
        cloneText.setOpacity(getOpacity());
        cloneText.setFill(getFill());
        cloneText.setStroke(getStroke());
        cloneText.setStrokeWidth(getStrokeWidth());
        return cloneText;
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
    public void setStart(int initStartX, int initStartY) {
        startX = initStartX;
        startY = initStartY;
    }

    @Override
    public void drag(int x, int y) {
        //double diffX = x - (getX() + (getWidth()/2));
        //double diffY = y - (getY() + (getHeight()/2));
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
        // WE DON'T CARE ABOUT THIS FOR TEXT
    }

    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
        xProperty().set(initX);
        yProperty().set(initY);
        // WE DON'T CARE ABOUT HTE SIZE
    }

    @Override
    public String getNodeType() {
        return TEXT;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    public void startDrag(int x, int y) {

    }

    public void setAssociatedLine(DraggableLine line) {
        this.associatedLine = line;
    }

    public void setIsStart() {
        this.isStart = true;
    }

    public void setIsEnd() {
        this.isStart = false;
    }

    public void setAssociatedEllipse(Ellipse station) {
        this.associatedEllipse = station;
    }

    public void setIsForLine() {
        this.isForLine = true;
    }

    public void setIsForStation() {
        this.isForLine = false;
    }

    public Boolean getIsForLine() {
        return isForLine;
    }
    
    public void setIsLonelyLabel(){
        this.isLonelyLabel = true;
    }
    
    public Boolean getIsLonelyLabel(){
        return isLonelyLabel;
    }

    public DraggableLine getAssociatedLine() {
        return associatedLine;
    }

    public boolean getIsStart() {
        return isStart;
    }

    public Ellipse getAssociatedEllipse() {
        return associatedEllipse;
    }

}
