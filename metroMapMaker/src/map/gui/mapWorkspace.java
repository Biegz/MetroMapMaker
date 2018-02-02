package map.gui;

import com.sun.glass.events.KeyEvent;
import djf.ui.mapWelcome;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import map.data.mapData;
import static map.data.mapData.BLACK_HEX;
import static map.data.mapData.WHITE_HEX;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.language.AppLanguageSettings;
import static djf.language.AppLanguageSettings.FILE_PROTOCOL;
import static djf.language.AppLanguageSettings.PATH_IMAGES;
import static djf.ui.AppGUI.DISABLED;
import static djf.ui.AppGUI.ENABLED;
import djf.ui.FileController;
import java.awt.event.KeyListener;

import static map.css.mapStyle.*;
import map.data.Draggable;
import static map.mapPropertyType.ADD_ELLIPSE;
import static map.mapPropertyType.ADD_IMAGE;
import static map.mapPropertyType.ADD_RECTANGLE;
import static map.mapPropertyType.ADD_TEXT;
import static map.mapPropertyType.BACKGROUND_COLOR;
import static map.mapPropertyType.BACKGROUND_COLOR_TEXT;
import static map.mapPropertyType.BOLD_TEXT;
import static map.mapPropertyType.FILL_COLOR;
import static map.mapPropertyType.FILL_COLOR_TEXT;
import static map.mapPropertyType.FONT_FAMILY_COMBO_BOX_OPTIONS;
import static map.mapPropertyType.FONT_SIZE_COMBO_BOX_OPTIONS;
import static map.mapPropertyType.ITALICIZE_TEXT;
import static map.mapPropertyType.MOVE_TO_BACK;
import static map.mapPropertyType.MOVE_TO_FRONT;
import static map.mapPropertyType.OUTLINE_COLOR;
import static map.mapPropertyType.OUTLINE_COLOR_TEXT;
import static map.mapPropertyType.OUTLINE_THICKNESS;
import static map.mapPropertyType.OUTLINE_THICKNESS_TEXT;
import static map.mapPropertyType.REMOVE;
import static map.mapPropertyType.SELECT;
import static map.mapPropertyType.SNAPSHOT;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import properties_manager.PropertiesManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import jtps.jTPS;
import map.data.DraggableEllipse;
import map.data.DraggableLine;
import map.data.DraggableText;
import map.data.mapState;
import map.transactions.AddStationToLine_Transaction;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapWorkspace extends AppWorkspaceComponent {

    ScrollPane mainLeftPane;

    ObservableList<String> lines = FXCollections.observableArrayList();
    ArrayList<DraggableLine> lines2 = new ArrayList();

    ObservableList<String> stations = FXCollections.observableArrayList();
    ArrayList<DraggableEllipse> stations2 = new ArrayList();

    ObservableList<String> fontSizes = FXCollections.observableArrayList("4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
    ObservableList<String> fontFamilies = FXCollections.observableArrayList("Arial", "Courier", "Impact", "Times New Roman", "Verdana");

    //LINE PANE
    VBox linePane;
    //hbox1
    Label cbLineLabel;
    ComboBox cbLine;

    Button editLineBtn;

    ColorPicker colorPicker;
    //hbox2
    Button addLine;
    Button removeLine;
    Button addStation;
    Button removeStation;
    Button pref;
    //hbox3
    Slider lineThicknessSlider;

    //STATION PANE
    VBox stationPane;
    //hbox1
    Label cbStationLabel;
    ComboBox cbStation;
    ColorPicker colorPicker2;
    //hbox2
    Button addStation2;
    Button removeStation2;
    Button snap;
    Button moveLabel;
    Button rotateLabel;
    //hbox3
    Slider radiusSlider;

    //DIRECTION PANE
    HBox directionPane;
    //vbox1
    ComboBox cbStation1;
    ComboBox cbStation2;
    //vbox2
    Button getRoute;

    //DECOR PANE
    VBox decorPane;
    //hbox1
    Label decorLabel;
    ColorPicker colorPicker3;
    //hbox2
    Button setImageBackground;
    Button addImage;
    Button addLabel;
    Button removeElement;

    //FONT PANE
    VBox fontPane;
    //hbox1
    Label fontLabel;
    ColorPicker colorPicker4;
    //hbox2
    ToggleButton boldButton;
    ToggleButton italicsButton;
    ComboBox fontFamilyComboBox;
    ComboBox fontSizeComboBox;

    //NAVIGATION PANE
    VBox navigationPane;
    //hbox1
    Label navigationLabel;
    CheckBox showGrid;
    //hbox2
    Button zoomIn;
    Button zoomOut;
    Button increaseMapSize;
    Button decreaseMapSize;

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;

    // IMAGE/TEXT PANE
    //HBox imageTextPane;
    //Button addImageButton;
    //Button addTextButton;
    // BACKGROUND COLOR PANE
    VBox backgroundColorPane;
    Label backgroundColorLabel;
    ColorPicker backgroundColorPicker;

    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    StackPane stackPane;
    Group innerGroup;
    Group outerGroup;
    Pane canvas;
    ScrollPane canvasHolder;

    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    mapWelcome mapWelcomeStage;

    double panCountX = 0;
    double panCountY = 0;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public mapWorkspace(AppTemplate initApp) {

        // KEEP THIS FOR LATER
        app = initApp;

        // KEEP THE GUI FOR LATER
        gui = app.getGUI();

        //mapWelcomeStage = new mapWelcome(app, gui);
        //mapWelcomeStage.initWelcome();
        // LAYOUT THE APP
        initLayout();

        // HOOK UP THE CONTROLLERS
        initControllers();

        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
    }

    public Pane getCanvas() {
        return canvas;
    }

    // HELPER SETUP METHOD
    private void initLayout() {

        app.getGUI().getPrimaryScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    panCountY += 20;
                    outerGroup.translateYProperty().set(panCountY);
                    //panCountY += 10;
                    break;
                case A:
                    panCountX += 20;
                    outerGroup.translateXProperty().set(panCountX);
                    //panCountX -= 10;
                    break;
                case S:
                    panCountY -= 20;
                    outerGroup.translateYProperty().set(panCountY);
                    //panCountY -= 10;
                    break;
                case D:
                    panCountX -= 20;
                    outerGroup.translateXProperty().set(panCountX);
                    //panCountX += 10;
                    break;

            }
        });

        // WE'LL USE THIS TO GET TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
        editToolbar = new VBox();
        mainLeftPane = new ScrollPane();

        // IMAGE/TEXT TOOLBAR
//        imageTextPane = new HBox();
//        addImageButton = gui.initPaneChildButton(imageTextPane, ADD_IMAGE.toString(), ENABLED);
//        addTextButton = gui.initPaneChildButton(imageTextPane, ADD_TEXT.toString(), ENABLED);
        // EDIT TEXT TOOLBAR
        // BACKGROUND COLOR TOOLBAR
        backgroundColorPane = new VBox();
        backgroundColorLabel = new Label(props.getProperty(BACKGROUND_COLOR_TEXT));
        backgroundColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
        backgroundColorPane.getChildren().add(backgroundColorLabel);
        backgroundColorPane.getChildren().add(backgroundColorPicker);

        // NOW ORGANIZE THE EDIT TOOLBAR
        editToolbar.getChildren().add(getLinePane());
        editToolbar.getChildren().add(getStationPane());
        editToolbar.getChildren().add(getDirectionPane());
        editToolbar.getChildren().add(getDecorPane());
        editToolbar.getChildren().add(getFontPane());
        editToolbar.getChildren().add(getNavigationPane());

        mainLeftPane.setContent(editToolbar);

        // WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        stackPane = new StackPane();
        canvasHolder = new ScrollPane();
        innerGroup = new Group();
        outerGroup = new Group();
        canvas = new Pane();

        BackgroundFill fill = new BackgroundFill(Color.WHITE, null, null);
        Background background = new Background(fill);
        canvas.setBackground(background);
        debugText = new Text();
        debugText.setText("");
        debugText.setStroke(Color.BLUE);
        canvas.getChildren().add(debugText);
        debugText.setX(500);
        debugText.setY(500);

        // AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
        mapData data = (mapData) app.getDataComponent();
        data.setMapNodes(canvas.getChildren());

        //canvas.setMinWidth(1900);
        //canvas.setMinHeight(1400);
        workspace = new BorderPane();

        canvas.setPrefSize(400, 400);
        innerGroup.getChildren().add(canvas);
        outerGroup.getChildren().add(innerGroup);
        stackPane.getChildren().add(outerGroup);
        canvasHolder.setContent(stackPane);
        canvasHolder.setFitToHeight(true);
        canvasHolder.setFitToWidth(true);
        canvasHolder.setPannable(false);

        // AND NOW SETUP THE WORKSPACE
        ((BorderPane) workspace).setLeft(mainLeftPane);
        ((BorderPane) workspace).setCenter(canvasHolder);

        // GIVE THE LABELS TO THE LANGUAGE MANAGER
        AppLanguageSettings language = app.getLanguageSettings();
        language.addLabeledControl(BACKGROUND_COLOR, backgroundColorLabel);
    }

    public ToggleButton initToggleButton(Pane parent, String name, boolean enabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // LOAD THE ICON FROM THE PROVIDED FILE
        String iconProperty = name + "_ICON";
        String tooltipProperty = name + "_TOOLTIP";
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(iconProperty);
        Image buttonImage = new Image(imagePath);

        // NOW MAKE THE BUTTON
        ToggleButton button = new ToggleButton();
        button.setDisable(!enabled);
        button.setGraphic(new ImageView(buttonImage));
        String tooltipText = props.getProperty(tooltipProperty);
        Tooltip buttonTooltip = new Tooltip(tooltipText);
        button.setTooltip(buttonTooltip);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        AppLanguageSettings languageSettings = app.getLanguageSettings();
        languageSettings.addLabeledControl(name, button);

        // ADD IT TO THE PANE
        parent.getChildren().add(button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }

    private ComboBox initComboBox(String comboPropertyList) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> comboOptions = props.getPropertyOptionsList(comboPropertyList);
        ObservableList oList = FXCollections.observableList(comboOptions);
        ComboBox cBox = new ComboBox(oList);
        cBox.getSelectionModel().selectFirst();
        return cBox;
    }

    // HELPER SETUP METHOD
    private void initControllers() {
        DecorController decorController = new DecorController(app);
        FileController fileController = new FileController(app);
        StationController stationController = new StationController(app);
        LineController lineController = new LineController(app);
        FontController fontController = new FontController(app);
        NavigateController navController = new NavigateController(app);
        mapData mapManager = (mapData) app.getDataComponent();
        BackgroundOutlineFillController backgroundOutlineFillController = new BackgroundOutlineFillController(app);
        AddImageAndTextController addImageAndTextController = new AddImageAndTextController(app);

        if (fileController.isSaved() == false) {
            app.getGUI().getWindow().setOnCloseRequest(e -> {
                ButtonType Yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType No = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(AlertType.WARNING,
                        "Make sure to save your work before closing application! " + "\n\nIs your work saved?",
                        No,
                        Yes);
                alert.setTitle("Date format warning");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == Yes) {
                    app.getGUI().getWindow().close();
                }
                if (result.isPresent() && result.get() == No) {
                    e.consume();
                }
            });
        }

        addLine.setOnAction(e -> {
            lineController.addLine();
        });

        editLineBtn.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
                lineController.editLineDialog();
            }

        });

        removeLine.setOnAction(e -> {
            lineController.removeLine();
        });

        cbLine.setOnAction(e -> {
            lineController.focusLineFromCB(cbLine.getSelectionModel().getSelectedIndex());

            reloadWorkspace(mapManager);
        });

        addStation.setOnAction(e -> {

            if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {

                Scene scene = app.getGUI().getPrimaryScene();
                scene.setCursor(Cursor.HAND);
                mapManager.setState(mapState.ADDING_STATION_TO_LINE);

            }


            //stationController.addStationToLine(cbLine.getSelectionModel().getSelectedIndex());
        });

        removeStation.setOnAction(e -> {
            //stationController.removeStationFromLine();

            if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {

                Scene scene = app.getGUI().getPrimaryScene();
                scene.setCursor(Cursor.HAND);
                mapManager.setState(mapState.REMOVING_STATION_FROM_LINE);

            }
        });

        addStation2.setOnAction(e -> {
            stationController.makeNewStation();
        });

        removeStation2.setOnAction(e -> {
            stationController.removeStation();
        });

        cbStation.setOnAction(e -> {
            stationController.focusStationFromCB(cbStation.getSelectionModel().getSelectedIndex());
        });

        // NOW CONNECT THE BUTTONS TO THEIR HANDLERS
        // FIRST THE SHAPE TOOLBAR
        ShapeController shapeController = new ShapeController(app);

        canvas.setOnMousePressed(e -> {
            shapeController.processCanvasMousePress((int) e.getX(), (int) e.getY(), e.getClickCount());
        });
        canvas.setOnMouseReleased(e -> {
            shapeController.processCanvasMouseRelease((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMouseDragged(e -> {
            shapeController.processCanvasMouseDragged((int) e.getX(), (int) e.getY());
        });
        // AND THEN THE FONT TOOLBAR
        fontFamilyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fontController.processChangeFont();
            }
        });
        fontSizeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                fontController.processChangeFont();
            }
        });
        boldButton.setOnAction(e -> {
            fontController.processChangeFont();
        });
        italicsButton.setOnAction(e -> {
            fontController.processChangeFont();
        });

        // THE BACKGROUND TOOLBAR
//        backgroundColorPicker.setOnAction(e -> {
//            backgroundOutlineFillController.processSelectBackgroundColor();
//        });
        colorPicker.setOnAction(e -> {
            lineController.setLineColorFromColorPicker(colorPicker.getValue());
        });

        lineThicknessSlider.valueProperty().addListener(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
                lineController.processSelectLineThickness();

            }
        });

        radiusSlider.valueProperty().addListener(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableEllipse) {
                stationController.processSelectStationRadius();
            }
        });

        colorPicker2.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableEllipse) {
                stationController.changeStationFillColor(colorPicker2.getValue());
            }
        });

        moveLabel.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableEllipse) {
                stationController.processMoveStationLabel();

                DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();
                draggableEllipse.increaseLabelPosition();

                System.out.println(draggableEllipse.getLabelPosition());
            }
        });

        rotateLabel.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableText) {
                fontController.rotateLabel();
            }
        });

        addLabel.setOnAction(e -> {
            addImageAndTextController.processAddText();
        });

        removeElement.setOnAction(e -> {
            decorController.removeElement();
        });

        increaseMapSize.setOnAction(e -> {
            navController.increaseSizeCount();
            navController.refreshZoomedMap();
            navController.updateGridLines();

        });

        decreaseMapSize.setOnAction(e -> {
            navController.decreaseSizeCount();
            navController.refreshZoomedMap();
            navController.updateGridLines();

        });

        zoomIn.setOnAction(e -> {
            navController.increaseZoomCount();
            navController.refreshZoomedMap();

        });

        zoomOut.setOnAction(e -> {
            navController.decreaseZoomCount();
            navController.refreshZoomedMap();

        });

        showGrid.setOnAction(e -> {
            if (showGrid.isSelected()) {
                navController.showGridLines();
            } else {
                navController.hideGridLines();
            }

        });

        snap.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof Ellipse) {
                stationController.snapStationToGrid();
            } else if (mapManager.getSelectedDraggableNode() instanceof Text) {
                lineController.snapLineEndToGrid();
            }
        });

        colorPicker3.setOnAction(e -> {

            canvas.setBackground(new Background(new BackgroundFill(colorPicker3.getValue(), null, null)));
            //backgroundOutlineFillController.processSelectBackgroundColor(colorPicker.getValue());
        });

        setImageBackground.setOnAction(e -> {
            addImageAndTextController.processAddImageBackground();

        });

        addImage.setOnAction(e -> {
            addImageAndTextController.processAddImage();
        });

        removeElement.setOnAction(e -> {
            Node node = mapManager.getSelectedNode();

            mapManager.removeNode(node);

        });

        //FONT PANE
        colorPicker4.setOnAction(e -> {
            fontController.setFontColorFromColorPicker(colorPicker4.getValue());
        });

        fontFamilyComboBox.setOnAction(e -> {
            fontController.processChangeFont();
        });

        pref.setOnAction(e -> {
            if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
                lineController.listAllStationsInLine();
            }

        });

//        boldButton.setOnAction(e ->{
//            fontController.processChangeFont();
//        });
//
//
//        italicsButton.setOnAction(e -> {
//            fontController.processChangeFont();
//        });

        // THEN THE ADD IMAGE AND TEXT TOOLBAR
//        AddImageAndTextController addImageAndTextController = new AddImageAndTextController(app);
//        addImageButton.setOnAction(e -> {
//            addImageAndTextController.processAddImage();
//        });
//        addTextButton.setOnAction(e -> {
//            addImageAndTextController.processAddText();
//        });
    }

    // HELPER METHOD
    public void loadSelectedNodeSettings(Node node) {
        if (node != null) {
            mapData data = (mapData) app.getDataComponent();
            if (data.isShape((Draggable) node)) {
                Shape shape = (Shape) node;
                Color fillColor = (Color) shape.getFill();
                Color strokeColor = (Color) shape.getStroke();
                double lineThickness = shape.getStrokeWidth();
            }
        }
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
        // NOTE THAT EACH CLASS SHOULD CORRESPOND TO
        // A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
        // CSS FILE

        //canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
        //LINE PANE
        linePane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        //hbox1
        cbLineLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        colorPicker.getStyleClass().add(CLASS_BUTTON);
        //hbox3
        lineThicknessSlider.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);

        //STATION PANE
        stationPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        //hbox1
        cbStationLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        colorPicker2.getStyleClass().add(CLASS_BUTTON);
        //hbox3
        radiusSlider.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);

        //DIRECTION PANE
        directionPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);

        //DECOR PANE
        decorPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        decorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        colorPicker3.getStyleClass().add(CLASS_BUTTON);

        //FONT PANE
        fontPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        fontLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        colorPicker4.getStyleClass().add(CLASS_BUTTON);

        //NAVIGATION PANE
        navigationPane.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        navigationLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);

    }

    /**
     * This function reloads all the controls for editing logos in the
     * workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {

        mapData mapManager = (mapData) app.getDataComponent();
        LineController lineController = new LineController(app);

        if (mapManager.getSelectedDraggableNode() instanceof DraggableLine) {
            DraggableLine draggableLine = (DraggableLine) mapManager.getSelectedDraggableNode();

            colorPicker.setValue((Color) draggableLine.getStroke());

            //int x = getLinesList().indexOf(draggableLine.getAssociatedStartLabel().getText());
            cbLine.setValue(draggableLine.getAssociatedStartLabel().getText());
            //editLineBtn.setDisable(DISABLED);

            if (draggableLine.getListOfLineStations().isEmpty() == false) {
                cbStation.setItems(draggableLine.getLineStationsObservableList());
            }

        }

        if (!(mapManager.getSelectedDraggableNode() instanceof DraggableLine)) {
            cbStation.setItems(getStationsList());
        }

        if (mapManager.getSelectedDraggableNode() instanceof DraggableText) {
            DraggableText text = (DraggableText) mapManager.getSelectedDraggableNode();

            if (text.getStroke() == null) {
                colorPicker4.setValue(Color.BLACK);
            } else {
                colorPicker4.setValue((Color) text.getStroke());
            }

        }

        if (mapManager.getSelectedDraggableNode() instanceof DraggableEllipse) {
            DraggableEllipse draggableEllipse = (DraggableEllipse) mapManager.getSelectedDraggableNode();

            colorPicker2.setValue((Color) draggableEllipse.getFill());

        }

    }

    @Override
    public void resetLanguage() {
        // WE'LL NEED TO RELOAD THE CONTROLS WITH TEXT
        // THAT ARE NOT BUTTONS HERE, LIKE LABELS AND COMBO BOXES

    }

    public VBox getLinePane() {
        linePane = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();

        //hbox1
        cbLineLabel = new Label("Metro Lines: ");
        cbLine = new ComboBox(lines);
        cbLine.setMaxWidth(20);
        colorPicker = new ColorPicker();
        editLineBtn = new Button("Edit Line");
        //editLineBtn.setDisable(ENABLED);

        hbox1.getChildren().addAll(cbLineLabel, cbLine, editLineBtn);
        hbox1.setSpacing(5);
        hbox1.setPadding(new Insets(0, 0, 1, 0)); //Insets(top,right,bottom,left)

        //hbox2
        addLine = new Button("+");
        removeLine = new Button("-");
        addStation = new Button("Add Station");
        removeStation = new Button("Remove Station");
        pref = new Button("=");
        hbox2.getChildren().addAll(addLine, removeLine, addStation, removeStation, pref);
        hbox2.setPadding(new Insets(0, 0, 2, 0));

        //hbox3
        lineThicknessSlider = new Slider(4, 10, 1);
        hbox3.getChildren().addAll(lineThicknessSlider);
        hbox3.setPadding(new Insets(0, 0, 0, 40));

        linePane.getChildren().addAll(hbox1, hbox2, hbox3);

        return linePane;
    }

    public VBox getStationPane() {
        stationPane = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();

        //hbox1
        cbStationLabel = new Label("Metro Stations: ");
        cbStation = new ComboBox();
        cbStation.setMaxWidth(20);
        colorPicker2 = new ColorPicker();
        hbox1.getChildren().addAll(cbStationLabel, cbStation, colorPicker2);
        hbox1.setSpacing(5);
        hbox1.setPadding(new Insets(0, 0, 1, 0));

        //hbox2
        addStation2 = new Button("+");
        removeStation2 = new Button("-");
        snap = new Button("Snap");
        moveLabel = new Button("Move Label");
        rotateLabel = new Button("Rotate\nLabel");
        hbox2.getChildren().addAll(addStation2, removeStation2, snap, moveLabel, rotateLabel);
        hbox2.setPadding(new Insets(0, 0, 2, 0));

        //hbox3
        radiusSlider = new Slider(4, 16, 1);
        hbox3.getChildren().addAll(radiusSlider);
        hbox3.setPadding(new Insets(0, 0, 0, 40));

        stationPane.getChildren().addAll(hbox1, hbox2, hbox3);

        return stationPane;
    }

    public HBox getDirectionPane() {
        directionPane = new HBox();

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        //vbox1
        cbStation1 = new ComboBox();
        cbStation2 = new ComboBox();
        vbox1.getChildren().addAll(cbStation1, cbStation2);

        //vbox2
        getRoute = new Button("Find\nRoute");
        vbox2.getChildren().add(getRoute);

        directionPane.getChildren().addAll(vbox1, vbox2);

        return directionPane;
    }

    public VBox getDecorPane() {
        decorPane = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        //hbox1
        decorLabel = new Label("Decor");
        colorPicker3 = new ColorPicker();
        hbox1.setSpacing(108);
        hbox1.getChildren().addAll(decorLabel, colorPicker3);

        //hbox2
        setImageBackground = new Button(" Set Image \nBackground");
        addImage = new Button(" Add \nImage");
        addLabel = new Button(" Add \nLabel");
        removeElement = new Button(" Remove \nElement");
        hbox2.getChildren().addAll(setImageBackground, addImage, addLabel, removeElement);

        decorPane.getChildren().addAll(hbox1, hbox2);

        return decorPane;
    }

    public VBox getFontPane() {
        fontPane = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        //hbox1
        fontLabel = new Label("Font");
        colorPicker4 = new ColorPicker();
        hbox1.getChildren().addAll(fontLabel, colorPicker4);
        hbox1.setSpacing(125);

        //hbox2
        boldButton = new ToggleButton("B");
        italicsButton = new ToggleButton("I");
        fontFamilyComboBox = new ComboBox(fontFamilies);
        //fontFamilyComboBox.getSelectionModel().select(0);
        fontFamilyComboBox.setMaxWidth(20);
        fontSizeComboBox = new ComboBox(fontSizes);
        //fontSizeComboBox.getSelectionModel().select(0);
        fontSizeComboBox.setMaxWidth(20);
        hbox2.getChildren().addAll(boldButton, italicsButton, fontFamilyComboBox, fontSizeComboBox);

        fontPane.getChildren().addAll(hbox1, hbox2);

        return fontPane;
    }

    public VBox getNavigationPane() {
        navigationPane = new VBox();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        //hbox1
        navigationLabel = new Label("Navigation");
        showGrid = new CheckBox("Show Grid");
        hbox1.getChildren().addAll(navigationLabel, showGrid);
        hbox1.setSpacing(80);
        //hbox2
        zoomIn = new Button("In");
        zoomOut = new Button("Out");
        increaseMapSize = new Button("Expand Map");
        decreaseMapSize = new Button("Contract Map");
        hbox2.getChildren().addAll(zoomIn, zoomOut, decreaseMapSize, increaseMapSize);

        navigationPane.getChildren().addAll(hbox1, hbox2);

        return navigationPane;
    }

    public Font getCurrentFontSettings() {

        String fontFamily = fontFamilyComboBox.getSelectionModel().getSelectedItem().toString();
        int fontSize = Integer.valueOf(fontSizeComboBox.getSelectionModel().getSelectedIndex());
        FontWeight weight = FontWeight.NORMAL;
        if (boldButton.isPressed()) {
            weight = FontWeight.BOLD;
        }
        FontPosture posture = FontPosture.REGULAR;
        if (italicsButton.isPressed()) {
            posture = FontPosture.ITALIC;
        }
        Font newFont = Font.font(fontFamily, weight, posture, fontSize);
        return newFont;
    }

    public ColorPicker getLineColorPicker() {
        return colorPicker;
    }

    public ObservableList getLinesList() {
        return lines;
    }

    public ArrayList getListOfLineObjects() {
        return lines2;
    }

    public ObservableList getStationsList() {
        return stations;
    }

    public ArrayList getListOfStationObjects() {
        return stations2;
    }

    public ComboBox getLineComboBox() {
        return cbLine;
    }

    public ComboBox getStationComboBox() {
        return cbStation;
    }

    public ColorPicker getBackgroundColorPicker() {
        return backgroundColorPicker;
    }

    public Slider getLineThicknessSlider() {
        return lineThicknessSlider;
    }

    public Slider getRadiusSlider() {
        return radiusSlider;
    }

    public ScrollPane getCanvasHolder() {
        return canvasHolder;
    }

    public Group getOuterGroup() {
        return outerGroup;
    }

    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
        //debugText.setText(text);
    }

    public void initDebugText() {
        canvas.getChildren().add(debugText);
    }

}
