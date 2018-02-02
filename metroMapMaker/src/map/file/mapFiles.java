package map.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import map.data.mapData;
import map.data.DraggableEllipse;
import map.data.Draggable;
import static map.data.Draggable.LINE;
import map.data.DraggableLine;
import map.data.DraggableText;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapFiles implements AppFileComponent {

    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_ALPHA = "alpha";

    static final String JSON_TEXT = "text";
    static final String JSON_STRING = "string value";

    static final String JSON_IS_FOR_LINE = "is for line";
    static final String JSON_IS_START = "is start";
    static final String JSON_ASSOCIATED_LINE = "associated line";
    static final String JSON_ASSOCIATED_ELLIPSE = "associated ellipse";

    static final String JSON_SHAPES = "shapes";
    static final String JSON_SHAPE = "shape";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";

    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";

    /**
     * This method is for saving user work, which in the case of this
     * application means the data that together draws the logo.
     *
     * @param data The data management component for this application.
     *
     * @param filePath Path (including file name/extension) to where to save the
     * data to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
        mapData dataManager = (mapData) data;

        // FIRST THE BACKGROUND COLOR
        Color bgColor = dataManager.getBackgroundColor();
        JsonObject bgColorJson = makeJsonColorObject(bgColor);

        // NOW BUILD THE JSON OBJCTS TO SAVE
        //JsonArrayBuilder arrayBuilderForShapes = Json.createArrayBuilder();
        JsonArrayBuilder arrayBuilderForText = Json.createArrayBuilder();
        JsonArrayBuilder arrayBuilderForLine = Json.createArrayBuilder();
        JsonArrayBuilder arrayBuilderForStation = Json.createArrayBuilder();
        
        

        ObservableList<Node> nodes = dataManager.getMapNodes();
        for (Node node : nodes) {

            if (node instanceof DraggableText) {

                //Text text = (Text) node;
                DraggableText draggableText = ((DraggableText) node);
                
                String type = draggableText.getNodeType();
                String str = draggableText.getText();
                double x = draggableText.getX();
                double y = draggableText.getY();
                double width = draggableText.getWidth();
                double height = draggableText.getHeight();

                Boolean isForLine = draggableText.getIsForLine();
                Boolean isStart = draggableText.getIsStart();

                JsonObject associatedEllipseJson = makeJsonEllipseObject(draggableText);
                //JsonObject associatedLineJson = makeJsonLineObject(draggableText.getAssociatedLine());

                JsonObject textJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add(JSON_STRING, str)
                        .add(JSON_IS_FOR_LINE, isForLine)
                        .add(JSON_IS_START, isStart)
                        .add(JSON_X, x)
                        .add(JSON_Y, y).build();
                arrayBuilderForText.add(textJson);
            }


            if(node instanceof DraggableEllipse){
                DraggableEllipse ellipse = (DraggableEllipse)node;
                
                String type = ellipse.getNodeType();
                double centerX = ellipse.getCenterX();
                double centerY = ellipse.getCenterY();
                double radiusX = ellipse.getRadiusX();
                double radiusY = ellipse.getRadiusY();
                Color color = (Color)ellipse.getFill();
                
                JsonObject fillColorJson = makeJsonColorObject((Color) ellipse.getFill());
                
                
                if(ellipse.getAssociatedLine() == null){
                    return;
                }else {
                    DraggableLine associatedLine = (DraggableLine)ellipse.getAssociatedLine();
                }
                DraggableText setAssociatedText = (DraggableText)ellipse.getAssociatedLabel();
                
                JsonObject stationJson = Json.createObjectBuilder()
                        .add(JSON_TYPE, type)
                        .add("centerX", centerX)
                        .add("centerY", centerY)
                        .add("radiusX", radiusX)
                        .add("radiusY", radiusY)
                        .add("color", fillColorJson).build();
                arrayBuilderForText.add(stationJson);
            }

        }
        JsonArray textArray = arrayBuilderForText.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BG_COLOR, bgColorJson)
                .add(JSON_TEXT, arrayBuilderForText)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    private JsonObject makeJsonColorObject(Color color) {
        JsonObject colorJson = Json.createObjectBuilder()
                .add(JSON_RED, color.getRed())
                .add(JSON_GREEN, color.getGreen())
                .add(JSON_BLUE, color.getBlue())
                .add(JSON_ALPHA, color.getOpacity()).build();
        return colorJson;
    }

    private JsonObject makeJsonEllipseObject(DraggableText text) {
        JsonObject fillColorJson = makeJsonColorObject((Color) text.getAssociatedEllipse().getFill());

        JsonObject ellipseJson = Json.createObjectBuilder()
                .add("centerX", text.getAssociatedEllipse().getCenterX())
                .add("centerY", text.getAssociatedEllipse().getCenterY())
                .add("radiusX", text.getAssociatedEllipse().getRadiusX())
                .add("radiusY", text.getAssociatedEllipse().getRadiusY())
                .add("fill", fillColorJson).build();
        return ellipseJson;

    }

    private JsonObject makeJsonLineObject(DraggableLine line) {
        JsonObject strokeColor = makeJsonColorObject((Color) line.getStroke());

        JsonObject lineJson = Json.createObjectBuilder()
                .add("startX", line.getPoints().get(0))
                .add("startY", line.getPoints().get(1))
                .add("endX", line.getPoints().get(2))
                .add("endY", line.getPoints().get(3))
                .add("stroke", strokeColor)
                .add("stroke weight", line.getStrokeWidth()).build();
        return lineJson;

    }

    /**
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
        mapData dataManager = (mapData) data;
        dataManager.resetData();

        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        // LOAD THE BACKGROUND COLOR
        Color bgColor = loadColor(json, JSON_BG_COLOR);
        dataManager.setBackgroundColor(bgColor);

        // AND NOW LOAD ALL THE SHAPES
        JsonArray jsonShapeArray = json.getJsonArray(JSON_SHAPES);
        for (int i = 0; i < jsonShapeArray.size(); i++) {
            JsonObject jsonShape = jsonShapeArray.getJsonObject(i);
            Shape shape = loadShape(jsonShape);
            dataManager.addNode(shape);
        }

        //NOW LOAD ALL OF THE TEXT
        JsonArray jsonTextArray = json.getJsonArray(JSON_TEXT);
        for (int i = 0; i < jsonShapeArray.size(); i++) {
            JsonObject jsonText = jsonTextArray.getJsonObject(i);
            DraggableText text = (DraggableText) loadText(jsonText);
            dataManager.addNode(text);

        }
    }

    private double getDataAsDouble(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber) value;
        return number.bigDecimalValue().doubleValue();
    }

    private Text loadText(JsonObject jsonText) {

        String str = jsonText.getString(JSON_STRING);
        DraggableText text = new DraggableText(str);

        //JsonObject jsonEllipse = jsonText.getJsonObject(JSON_ASSOCIATED_ELLIPSE);
        //DraggableEllipse ellipse = new DraggableEllipse();
        //double centerX = getDataAsDouble(jsonEllipse, "centerX");
        //double centerY = getDataAsDouble(jsonEllipse, "centerY");
        //double radiusX = getDataAsDouble(jsonEllipse, "radiusX");
        //double radiusY = getDataAsDouble(jsonEllipse, "radiusY");

        //Color fillColor = loadColor(jsonEllipse, "fill");

        //ellipse.centerXProperty().bind(text.xProperty());
        //ellipse.centerYProperty().bind(text.yProperty());
        //ellipse.setRadiusX(radiusX);
        //ellipse.setRadiusX(radiusY);
        //ellipse.setCenterX(centerX);
        //ellipse.setCenterY(centerY+21);
        //ellipse.setFill(fillColor);

        double x = getDataAsDouble(jsonText, JSON_X);
        double y = getDataAsDouble(jsonText, JSON_Y);

        Boolean isForLine = jsonText.getBoolean(JSON_IS_FOR_LINE);
        Boolean isStart = jsonText.getBoolean(JSON_IS_START);

        if (isForLine == true) {
            text.setIsForLine();
        } else if (isForLine == false) {
            text.setIsForStation();
        }
        if (isStart == true) {
            text.setIsStart();
        } else if (isForLine == false) {
            text.setIsEnd();
        }

        //text.setAssociatedEllipse(ellipse);

        text.setX(x);
        text.setY(y);

        return text;
    }

    private Shape loadShape(JsonObject jsonShape) {
        // FIRST BUILD THE PROPER SHAPE TYPE
        String type = jsonShape.getString(JSON_TYPE);
        Shape shape;
        if (type.equals(LINE)) {
            shape = new DraggableLine();
        } else {
            shape = new DraggableEllipse();
            //shape.getAs
        }

        // THEN LOAD ITS FILL AND OUTLINE PROPERTIES
        Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
        Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
        double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
        shape.setFill(fillColor);
        shape.setStroke(outlineColor);
        shape.setStrokeWidth(outlineThickness);

        // AND THEN ITS DRAGGABLE PROPERTIES
        double x = getDataAsDouble(jsonShape, JSON_X);
        double y = getDataAsDouble(jsonShape, JSON_Y);
        double width = getDataAsDouble(jsonShape, JSON_WIDTH);
        double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
        Draggable draggableShape = (Draggable) shape;
        draggableShape.setLocationAndSize(x, y, width, height);

        // ALL DONE, RETURN IT
        return shape;
    }

    private Color loadColor(JsonObject json, String colorToGet) {
        JsonObject jsonColor = json.getJsonObject(colorToGet);
        double red = getDataAsDouble(jsonColor, JSON_RED);
        double green = getDataAsDouble(jsonColor, JSON_GREEN);
        double blue = getDataAsDouble(jsonColor, JSON_BLUE);
        double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
        Color loadedColor = new Color(red, green, blue, alpha);
        return loadedColor;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        // WE ARE NOT USING THIS, THOUGH PERHAPS WE COULD FOR EXPORTING
        // IMAGES TO VARIOUS FORMATS, SOMETHING OUT OF THE SCOPE
        // OF THIS ASSIGNMENT
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        // AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
    
    //            else if (node instanceof DraggableEllipse) {
//                //Shape shape = (Shape) node;
//                DraggableEllipse draggableShape = ((DraggableEllipse) node);
//                String type = draggableShape.getNodeType();
//                double x = draggableShape.getX();
//                double y = draggableShape.getY();
//                double width = draggableShape.getWidth();
//                double height = draggableShape.getHeight();
//                JsonObject fillColorJson = makeJsonColorObject((Color) draggableShape.getFill());
//                JsonObject outlineColorJson = makeJsonColorObject((Color) draggableShape.getStroke());
//                double outlineThickness = draggableShape.getStrokeWidth();
//
//                JsonObject shapeJson = Json.createObjectBuilder()
//                        .add(JSON_TYPE, type)
//                        .add(JSON_X, x)
//                        .add(JSON_Y, y)
//                        .add(JSON_WIDTH, width)
//                        .add(JSON_HEIGHT, height)
//                        .add(JSON_FILL_COLOR, fillColorJson)
//                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
//                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
//                arrayBuilderForShapes.add(shapeJson);
//            }
}
