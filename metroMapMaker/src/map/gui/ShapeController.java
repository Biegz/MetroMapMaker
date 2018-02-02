package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import map.data.Draggable;
import map.data.mapData;
import map.data.mapState;
import static map.data.mapState.DRAGGING_NODE;
import static map.data.mapState.DRAGGING_NOTHING;
import static map.data.mapState.SELECTING_NODE;
import static map.data.mapState.SIZING_SHAPE;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import jtps.jTPS;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.transactions.AddStationToLine_Transaction;
import map.transactions.AddStation_Transaction;
import map.transactions.MoveNode_Transaction;

/**
 *
 * @author McKillaGorilla
 */
public class ShapeController {

    AppTemplate app;
    mapData dataManager;

    double oldX;
    double oldY;

    public ShapeController(AppTemplate initApp) {
        app = initApp;
        dataManager = (mapData) app.getDataComponent();
    }

    /**
     * This method handles the response for selecting either the selection or
     * removal tool.
     */
    public void processSelectSelectionTool() {
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        // CHANGE THE STATE
        dataManager.setState(mapState.SELECTING_NODE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * This method handles a user request to remove the selected node.
     */
    public void processRemoveSelectedNode() {
        // REMOVE THE SELECTED NODE IF THERE IS ONE
        dataManager.removeSelectedNode();

        // ENABLE/DISABLE THE PROPER BUTTONS
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }

    /**
     * This method processes a user request to start drawing a rectangle.
     */
    public void processSelectRectangleToDraw() {
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.CROSSHAIR);

        // CHANGE THE STATE
        dataManager.setState(mapState.STARTING_RECTANGLE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    public void processSelectLineToDraw() {

    }

    /**
     * This method provides a response to the user requesting to start drawing
     * an ellipse.
     */
    public void processSelectEllipseToDraw() {
        // CHANGE THE CURSOR
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.CROSSHAIR);

        // CHANGE THE STATE
        dataManager.setState(mapState.STARTING_ELLIPSE);

        // ENABLE/DISABLE THE PROPER BUTTONS
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y, int clickCount) {
        mapData dataManager = (mapData) app.getDataComponent();
        LineController lineController = new LineController(app);
        StationController stationController = new StationController(app);
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        if (clickCount == 1) {

            if (dataManager.getTopNode(x, y) instanceof Line) {
                return;
            }

            if (dataManager.isInState(mapState.ADDING_STATION_TO_LINE)) {
                System.out.println("Break");

                Node node = dataManager.selectTopNode(x, y);

                if (node instanceof DraggableEllipse) {
                    //stationController.addStationToLine(((DraggableEllipse) node), workspace.getLineComboBox().getSelectionModel().getSelectedIndex());

                    jTPS tps = app.getTPS();
                    mapData data = (mapData) app.getDataComponent();
                    AddStationToLine_Transaction newTransaction = new AddStationToLine_Transaction(app, data, (DraggableEllipse) node,  workspace.getLineComboBox().getSelectionModel().getSelectedIndex());
                    tps.addTransaction(newTransaction);
                }

                if (!(node instanceof DraggableEllipse)) {
                    dataManager.setState(mapState.SELECTING_NODE);
                }

            }

            if (dataManager.isInState(mapState.REMOVING_STATION_FROM_LINE)) {
                Node node = dataManager.selectTopNode(x, y);

                if (node instanceof DraggableEllipse) {
                    stationController.removeStationFromLine(((DraggableEllipse) node), workspace.getLineComboBox().getSelectionModel().getSelectedIndex());
                }

                if (!(node instanceof DraggableEllipse)) {
                    dataManager.setState(mapState.SELECTING_NODE);
                }
            }

            if (dataManager.isInState(SELECTING_NODE)) {
                LayerController layerController = new LayerController(app);
                // SELECT THE TOP NODE
                Node node = dataManager.selectTopNode(x, y);
                Scene scene = app.getGUI().getPrimaryScene();

                if (dataManager.getSelectedDraggableNode() instanceof DraggableLine) {
                    layerController.processMoveSelectedNodeToBack();
                }

                // AND START DRAGGING IT
                if (node != null) {

                    scene.setCursor(Cursor.MOVE);
                    dataManager.setState(mapState.DRAGGING_NODE);

                    if (dataManager.getSelectedDraggableNode() instanceof DraggableText) {
                        DraggableText text = (DraggableText) dataManager.getSelectedDraggableNode();
                        oldX = text.getX();
                        oldY = text.getY();
                    }
                    app.getGUI().updateToolbarControls(false);
                } else {
                    dataManager.setSelectedNode(null);

                    scene.setCursor(Cursor.DEFAULT);
                    dataManager.setState(DRAGGING_NOTHING);
                    app.getWorkspaceComponent().reloadWorkspace(dataManager);
                }
            } else if (dataManager.isInState(mapState.STARTING_ELLIPSE)) {
                dataManager.startNewEllipse(x, y);
            }
            workspace.reloadWorkspace(dataManager);
        }

        if (clickCount > 1) {
            if (dataManager.isInState(SELECTING_NODE)) {
                Node node = dataManager.selectTopNode(x, y);
                Scene scene = app.getGUI().getPrimaryScene();

                if (node != null && node instanceof Text) {
                    //System.out.println("yo buddy");
                    //processEditText();
                }
            }
        }

        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        mapData dataManager = (mapData) app.getDataComponent();

        if (dataManager.isInState(SIZING_SHAPE)) {
            Draggable newDraggableShape = (Draggable) dataManager.getNewShape();
            newDraggableShape.size(x, y);
        } else if (dataManager.isInState(DRAGGING_NODE)) {
            if (dataManager.getSelectedNode() instanceof Text) {

                DraggableText selectedDraggableNode = (DraggableText) dataManager.getSelectedNode();
                DraggableLine draggableLine = (DraggableLine) selectedDraggableNode.getAssociatedLine();

                jTPS tps = app.getTPS();
                MoveNode_Transaction newTransaction = new MoveNode_Transaction(selectedDraggableNode, selectedDraggableNode.getX(), selectedDraggableNode.getY(), oldX, oldY, app);
                tps.addTransaction(newTransaction);

                selectedDraggableNode.drag(x, y);

                //DRAGGING STATION ON LINE
                dragStationOnLine(selectedDraggableNode);

                dragLineEnd(selectedDraggableNode);

            }

            if (dataManager.getSelectedDraggableNode() instanceof ImageView) {
                Draggable selectedDraggableNode = (Draggable) dataManager.getSelectedNode();
                selectedDraggableNode.drag(x, y);
            }
        }

    }

    /**
     * Respond to mouse button release on the rendering surface, which we call
     * canvas, but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        mapData dataManager = (mapData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            dataManager.selectSizedShape();
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mapState.DRAGGING_NODE)) {
            dataManager.setState(SELECTING_NODE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mapState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_NODE);
        }
    }

    public void processEditText() {
        //mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();

        TextInputDialog dialog = new TextInputDialog("Enter Text");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Enter to change Text.");
        dialog.setContentText("Enter:");
        Optional<String> result = dialog.showAndWait();
        Text input = new Text(result.get());

        DraggableText node = (DraggableText) dataManager.getSelectedNode();

        if (node.getIsForLine() == true) {

            node.getAssociatedLine().getAssociatedStartLabel().setText(input.getText());
            node.getAssociatedLine().getAssociatedEndLabel().setText(input.getText());

        } else {
            node.setText(input.getText());
        }

//        int size = Integer.parseInt(workspace.getFontSizeCB().getSelectionModel().getSelectedItem().toString());
//        String family = workspace.getFontCB().getSelectionModel().getSelectedItem().toString();
//
//        ((Text) node).setFont(Font.font(family, FontWeight.BOLD, size));
//        
//        if(workspace.getIsBold() == true){
//            ((Text) node).setFont(Font.font(family, FontWeight.BOLD, size));
//        }
    }

    public void dragStationOnLine(DraggableText selectedDraggableNode) {

        //dragging station on line
        //make sure to check if the text node isnt for a line but also the lines list of stations contains the station 
        //that is about to be dragged
        if (selectedDraggableNode.getIsForLine() == false && selectedDraggableNode.getIsLonelyLabel() == false) {
            if (((DraggableEllipse) selectedDraggableNode.getAssociatedEllipse()).getParentLinesList().isEmpty() == false) {
                if (selectedDraggableNode.getIsStart() == true) {

                    DraggableEllipse station = (DraggableEllipse) selectedDraggableNode.getAssociatedEllipse();

                    for (DraggableLine line : station.getParentLinesList()) {

                        line.getPoints().set(line.getLineStationsObservableList().indexOf(station.getAssociatedLabel().getText()) * 2 + 2, station.getX() + station.getRadiusX());
                        line.getPoints().set(line.getLineStationsObservableList().indexOf(station.getAssociatedLabel().getText()) * 2 + 3, station.getY() + station.getRadiusX());

                    }
                }
            }
        }
    }

    public void dragLineEnd(DraggableText selectedDraggableNode) {

        if (selectedDraggableNode.getIsForLine() == true && selectedDraggableNode.getIsLonelyLabel() == false) {

            //Checks which node clicked is the start node or the end node and then drags the line end accordingly
            if (selectedDraggableNode.getIsStart() == true && selectedDraggableNode.getIsLonelyLabel() == false) {

                //FOR POLY LINE START POINT
                selectedDraggableNode.getAssociatedLine().getPoints().set(0, selectedDraggableNode.getX() + selectedDraggableNode.getLayoutBounds().getWidth() + 10);
                selectedDraggableNode.getAssociatedLine().getPoints().set(1, selectedDraggableNode.getY());

            } else {

                //FOR POLYLINE END POINT
                selectedDraggableNode.getAssociatedLine().getPoints().set(selectedDraggableNode.getAssociatedLine().getPoints().size() - 2, selectedDraggableNode.getX() - selectedDraggableNode.getLayoutBounds().getWidth() / 3);
                selectedDraggableNode.getAssociatedLine().getPoints().set(selectedDraggableNode.getAssociatedLine().getPoints().size() - 1, selectedDraggableNode.getY());

            }

            app.getGUI().updateToolbarControls(false);
        } else {

            //DRAGGING A STATION
            if (selectedDraggableNode.getIsStart() == true && selectedDraggableNode.getIsLonelyLabel() == false) {

                DraggableEllipse draggableEllipse = (DraggableEllipse) selectedDraggableNode.getAssociatedEllipse();
                DraggableText draggableText = (DraggableText) selectedDraggableNode;

                bindEllipseToText(draggableEllipse, draggableText);

            }
        }
    }

    public void bindEllipseToText(DraggableEllipse station, DraggableText label) {

        //RIGHT
        if (station.getLabelPosition() == 2) {
            station.setCenterX(label.getX() - label.getLayoutBounds().getWidth() / 1.75);
            station.setCenterY(label.getY());
        }
        //TOP
        if (station.getLabelPosition() == 3) {
            station.setCenterX(label.getX() + label.getLayoutBounds().getWidth() / 3);
            station.setCenterY(label.getY() + 21);
        }
        //LEFT
        if (station.getLabelPosition() == 4) {
            station.setCenterX(label.getX() + label.getLayoutBounds().getWidth() * 1.25);
            station.setCenterY(label.getY());
        }
        //BOTTOM
        if (station.getLabelPosition() == 1) {
            station.setCenterX(label.getX() + label.getLayoutBounds().getWidth() / 1.75);
            station.setCenterY(label.getY() - 21);
        }

    }

    public void bindTextToEllipse(DraggableText label, DraggableEllipse station) {

        //RIGHT
        if (station.getLabelPosition() == 2) {
            label.setX(station.getCenterX() + station.getLayoutBounds().getWidth() / 1.75);
            label.setY(station.getCenterY());
        }
        //TOP
        if (station.getLabelPosition() == 3) {
            label.setX(station.getCenterX() - station.getLayoutBounds().getWidth() / 3);
            label.setY(station.getCenterY() - 21);
        }
        //LEFT
        if (station.getLabelPosition() == 4) {
            label.setX(station.getCenterX() - station.getLayoutBounds().getWidth() * 2.25);
            label.setY(station.getCenterY());
        }
        //BOTTOM
        if (station.getLabelPosition() == 1) {
            label.setX(station.getCenterX() - label.getLayoutBounds().getWidth() / 3);//+ station.getLayoutBounds().getWidth() / 1.75);
            label.setY(station.getCenterY() + 21);
        }

    }

}
