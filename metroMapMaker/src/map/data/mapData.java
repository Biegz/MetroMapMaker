package map.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static map.data.mapState.SIZING_SHAPE;
import map.gui.mapWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import javafx.scene.image.Image;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import static map.data.mapState.SELECTING_NODE;
import map.transactions.AddStation_Transaction;
import map.transactions.AddNode_Transaction;
import map.transactions.AddLine_Transaction;
import javafx.scene.text.Text;
import jtps.jTPS;
import map.gui.AddImageAndTextController;
import map.gui.LineController;
import map.gui.StationController;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapData implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES

    ImagePattern ip;
    Image i;
    Image img;

    // THESE ARE THE NODES IN THE LOGO
    ObservableList<Node> mapNodes;

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Shape newShape;

    // THIS IS THE NODE CURRENTLY SELECTED
    Node selectedNode;

    // CURRENT STATE OF THE APP
    mapState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    // USE THIS WHEN THE NODE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public mapData(AppTemplate initApp) {
        // KEEP THE APP FOR LATER
        app = initApp;

        // NO SHAPE STARTS OUT AS SELECTED
        newShape = null;
        selectedNode = null;

        // THIS IS FOR THE SELECTED SHAPE
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(0.8);
        dropShadowEffect.setColor(Color.valueOf("#f7d6c8"));
        dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
        dropShadowEffect.setRadius(15);
        highlightedEffect = dropShadowEffect;
    }

    public ObservableList<Node> getMapNodes() {
        return mapNodes;
    }

    public void setMapNodes(ObservableList<Node> initLogoNodes) {
        mapNodes = initLogoNodes;
    }

    public void removeSelectedNode() {
        if (selectedNode != null) {
            mapNodes.remove(selectedNode);
            selectedNode = null;
        }
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        setState(SELECTING_NODE);
        newShape = null;
        selectedNode = null;

        mapNodes.clear();
        ((mapWorkspace) app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        ((mapWorkspace) app.getWorkspaceComponent()).initDebugText();
    }

    public Color getBackgroundColor() {
        return (Color) ((mapWorkspace) app.getWorkspaceComponent()).getCanvas().getBackground().getFills().get(0).getFill();
    }

    public void setBackgroundColor(Color color) {
        Pane canvas = ((mapWorkspace) app.getWorkspaceComponent()).getCanvas();
        BackgroundFill fill = new BackgroundFill(color, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
    }

    public void selectSizedShape() {
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
        }
        selectedNode = newShape;
        highlightNode(selectedNode);
        newShape = null;
        if (state == SIZING_SHAPE) {
            state = ((Draggable) selectedNode).getStartingState();
        }
    }

    public void unhighlightNode(Node node) {
        node.setEffect(null);
    }

    public void highlightNode(Node node) {
        node.setEffect(highlightedEffect);
    }

    public void startNewEllipse(int x, int y) {
        DraggableEllipse newEllipse = new DraggableEllipse();
        newEllipse.start(x, y);
        newShape = newEllipse;
        initNewShape();
    }

    public void makeNewLine(double x1, double y1, double x2, double y2, String lineName, Color colorResult) {
        LineController lineController = new LineController(app);

        DraggableText newText = new DraggableText(lineName);
        newText.start((int) x1, (int) y1);
        newShape = newText;
        initNewShape();

        DraggableText newText2 = new DraggableText(lineName);
        newText2.start((int) x2, (int) y2);
        newShape = newText2;
        initNewShape();

        DraggableLine newLine = new DraggableLine();

        newLine.setAssociatedStartLabel(newText);
        newLine.setAssociatedEndLabel(newText2);

//        newLine.setStartX(newText.getX() + newText.getLayoutBounds().getWidth()+10);
//        newLine.setStartY(newText.getY());
//        newLine.setEndX(newText2.getX() - newText2.getLayoutBounds().getWidth()/3);
//        newLine.setEndY(newText2.getY());
        newLine.getPoints().addAll(new Double[]{
            newText.getX() + newText.getLayoutBounds().getWidth() + 10, //x1
            newText.getY(),
            newText2.getX() - newText2.getLayoutBounds().getWidth() / 3,
            newText2.getY()
        });

        newLine.setStroke(colorResult);
        newLine.setStrokeWidth(5.0);
        newShape = newLine;

        newText.setAssociatedLine(newLine);
        newText.setIsStart();
        newText2.setAssociatedLine(newLine);
        newText2.setIsEnd();

        newText.setIsForLine();
        newText2.setIsForLine();

        initNewShape();

        lineController.addToLineList(lineName);          //adds to observable list
        lineController.addToListOfLineObjects(newLine);  //adds to oject array list

        jTPS tps = app.getTPS();
        mapData data = (mapData) app.getDataComponent();
        AddLine_Transaction newTransaction = new AddLine_Transaction(data, newLine, newText, newText2);
        tps.addTransaction(newTransaction);

//        AddImageAndTextController textController = new AddImageAndTextController(app);
//        newText.setOnMouseClicked(e ->{
//            textController.processTextClick(e);
//        });
    }

    public void makeNewStation(int x, int y, String stationName) {
        StationController stationController = new StationController(app);

        DraggableText newText = new DraggableText(stationName);
        newText.start(x, y);
        newShape = newText;
        initNewShape();

        DraggableEllipse newEllipse = new DraggableEllipse();
        newEllipse.setCenterX(newText.getX() + newText.getLayoutBounds().getWidth() / 1.75);
        newEllipse.setCenterY(newText.getY() - 21);
        newEllipse.setStroke(rgb(0, 0, 0));
        newEllipse.setRadiusX(8.0);
        newEllipse.setRadiusY(8.0);
        newEllipse.setFill(rgb(255, 255, 255));
        newShape = newEllipse;
        initNewShape();

        newEllipse.setAssociatedLabel(newText);
        newText.setAssociatedEllipse(newEllipse);
        newText.setIsStart();

        newText.setIsForStation();

        stationController.addToStationList(stationName);
        stationController.addToListOfStationObjects(newEllipse);

        jTPS tps = app.getTPS();
        mapData data = (mapData) app.getDataComponent();
        AddStation_Transaction newTransaction = new AddStation_Transaction(data, newEllipse, newText);
        tps.addTransaction(newTransaction);
    }

    public void makeNewText(int x, int y, String text) {
        DraggableText newText = new DraggableText(text);
        newText.start(x, y);
        newShape = newText;
        initNewShape();

        newText.setIsLonelyLabel();
        newText.setIsForStation();
        newText.setIsStart();

    }

    public void makeNewImage(int x, int y) {
        mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
        
        DraggableImage newImage = new DraggableImage();
        newImage.start(x, y);
        newShape = newImage;
        initNewImage();

        newImage.setWidth(workspace.getCanvas().getWidth());
        newImage.setHeight(workspace.getCanvas().getHeight());

        //setState(mapState.SELECTING_SHAPE);

        ((Shape) getNewShape()).setFill(getImagePattern());

    }
    
    public void initNewImage() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedNode != null) {
            unhighlightNode((Shape) selectedNode);
            selectedNode = null;
        }

        // ADD THE SHAPE TO THE CANVAS
        mapNodes.add(newShape);
        
        jTPS tps = app.getTPS();
        mapData data = (mapData) app.getDataComponent();
        AddNode_Transaction newTransaction = new AddNode_Transaction(data, newShape);
        tps.addTransaction(newTransaction);

    }

    public void initNewShape() {
        // DESELECT THE SELECTED SHAPE IF THERE IS ONE
        if (selectedNode != null) {
            unhighlightNode(selectedNode);
            selectedNode = null;
        }

        // USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        // GO INTO SHAPE SIZING MODE
        //state = mapState.SIZING_SHAPE;
        // FINALLY, ADD A TRANSACTION FOR ADDING THE NEW SHAPE
        jTPS tps = app.getTPS();
        mapData data = (mapData) app.getDataComponent();
        AddNode_Transaction newTransaction = new AddNode_Transaction(data, newShape);
        tps.addTransaction(newTransaction);
    }

    public Shape getNewShape() {
        return newShape;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(Node initSelectedNode) {
        selectedNode = initSelectedNode;
    }

    public Node selectTopNode(int x, int y) {
        Node node = getTopNode(x, y);
        if (node == selectedNode) {
            return node;
        }

        if (selectedNode != null) {
            unhighlightNode(selectedNode);
        }
        if (node != null) {
            highlightNode(node);
            mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
            workspace.loadSelectedNodeSettings(node);
        }
        selectedNode = node;
        if (node != null) {
            ((Draggable) node).setStart(x, y);
        }
        return node;
    }

    public boolean isShape(Draggable node) {
        return ((node.getNodeType() == Draggable.ELLIPSE)
                || (node.getNodeType() == Draggable.LINE)
                || (node.getNodeType() == Draggable.TEXT));
    }

    public Draggable getSelectedDraggableNode() {
        if (selectedNode == null) {
            return null;
        } else {
            return (Draggable) selectedNode;
        }
    }

    public Node getTopNode(int x, int y) {
        for (int i = mapNodes.size() - 1; i >= 0; i--) {
            Node node = (Node) mapNodes.get(i);
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public mapState getState() {
        return state;
    }

    public void setState(mapState initState) {
        state = initState;
    }

    public boolean isInState(mapState testState) {
        return state == testState;
    }

    // METHODS NEEDED BY TRANSACTIONS
    public void moveNodeToFront(Node nodeToMove) {
        int currentIndex = mapNodes.indexOf(nodeToMove);
        if (currentIndex >= 0) {
            mapNodes.remove(currentIndex);
            if (mapNodes.isEmpty()) {
                mapNodes.add(nodeToMove);
            } else {
                ArrayList<Node> temp = new ArrayList();
                temp.add(nodeToMove);
                for (Node node : mapNodes) {
                    temp.add(node);
                }
                mapNodes.clear();
                for (Node node : temp) {
                    mapNodes.add(node);
                }
            }
        }
    }

    public void moveNodeToBack(Node nodeToMove) {
        int currentIndex = mapNodes.indexOf(nodeToMove);
        if (currentIndex >= 0) {
            mapNodes.remove(currentIndex);
            mapNodes.add(nodeToMove);
        }
    }

    public void moveNodeToIndex(Node nodeToMove, int index) {
        int currentIndex = mapNodes.indexOf(nodeToMove);
        int numberOfNodes = mapNodes.size();
        if ((currentIndex >= 0) && (index >= 0) && (index < numberOfNodes)) {
            // IS IT SUPPOSED TO BE THE LAST ONE?
            if (index == (numberOfNodes - 1)) {
                mapNodes.remove(currentIndex);
                mapNodes.add(nodeToMove);
            } else {
                mapNodes.remove(currentIndex);
                mapNodes.add(index, nodeToMove);
            }
        }
    }

    public void removeNode(Node nodeToRemove) {
        int currentIndex = mapNodes.indexOf(nodeToRemove);
        if (currentIndex >= 0) {
            mapNodes.remove(currentIndex);
        }
    }

    public void addNode(Node nodeToAdd) {
        int currentIndex = mapNodes.indexOf(nodeToAdd);
        if (currentIndex < 0) {
            mapNodes.add(nodeToAdd);
        }
    }

    public int getIndexOfNode(Node node) {
        return mapNodes.indexOf(node);
    }

    public void addNodeAtIndex(Node node, int nodeIndex) {
        mapNodes.add(nodeIndex, node);
    }

    public boolean isTextSelected() {
        if (selectedNode == null) {
            return false;
        } else {
            return (selectedNode instanceof Text);
        }
    }

    public void setImagePattern(Image i) {
        this.ip = new ImagePattern(i);
    }

    public ImagePattern getImagePattern() {
        return ip;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public Image getImage() {
        return img;
    }

}
