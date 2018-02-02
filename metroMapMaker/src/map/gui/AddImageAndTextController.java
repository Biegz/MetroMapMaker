package map.gui;

import djf.AppTemplate;
import djf.ui.AppDialogs;
import map.data.DraggableImage;
import map.data.DraggableText;
import map.data.mapData;
import static map.mapPropertyType.DEFAULT_NODE_X;
import static map.mapPropertyType.DEFAULT_NODE_Y;
import map.transactions.AddNode_Transaction;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import jtps.jTPS;
import map.data.DraggableImage2;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class AddImageAndTextController {

    private AppTemplate app;

    public AddImageAndTextController(AppTemplate initApp) {
        app = initApp;
    }

    public void processAddImage() {
        // ASK THE USER TO SELECT AN IMAGE
        Image imageToAdd = promptForImage();
        if (imageToAdd != null) {
            DraggableImage2 imageViewToAdd = new DraggableImage2();
            imageViewToAdd.setImage(imageToAdd);
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            imageViewToAdd.xProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
            imageViewToAdd.yProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));

            // MAKE AND ADD THE TRANSACTION
            addNodeTransaction(imageViewToAdd);
        }
    }
    
    public void processAddImageBackground2(){
        
    }

    public void processAddImageBackground() {
        mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
        // ASK THE USER TO SELECT AN IMAGE
        Image imageToAdd = promptForImage();
        if (imageToAdd != null) {
            //DraggableImage2 imageViewToAdd = new DraggableImage2();
            //imageViewToAdd.setImage(imageToAdd);
            BackgroundImage bi = new BackgroundImage(imageToAdd, null, null, null, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
            
            workspace.getCanvas().setBackground(new Background(bi));
            
            //PropertiesManager props = PropertiesManager.getPropertiesManager();
            //imageViewToAdd.xProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
            //imageViewToAdd.yProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));

            // MAKE AND ADD THE TRANSACTION
            //addNodeTransaction(imageViewToAdd);
            //addNodeTransaction(bi);
        }
    }

    public void processAddText() {
        // ASK THE USER FOR TEXT
        String textInput = AppDialogs.showTextInputDialog(app.getGUI().getWindow(), "ENTER_TEXT_TITLE", "ENTER_TEXT_CONTENT");

        // MAKE AND ADD THE TRANSACTION
        if ((textInput != null) && (textInput.length() > 0)) {
            DraggableText textToAdd = new DraggableText(textInput);
            textToAdd.setIsStart();
            textToAdd.setIsLonelyLabel();
            textToAdd.setIsForStation();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            textToAdd.xProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
            textToAdd.yProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
            textToAdd.setOnMouseClicked(e -> {
                processTextClick(e);
            });
            addNodeTransaction(textToAdd);
        }
    }

    public void processAddLineLabel(String textInput) {
        if ((textInput != null) && (textInput.length() > 0)) {
            DraggableText textToAdd = new DraggableText(textInput);
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            textToAdd.xProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_X)));
            textToAdd.yProperty().set(Double.parseDouble(props.getProperty(DEFAULT_NODE_Y)));
            textToAdd.setOnMouseClicked(e -> {
                processTextClick(e);
            });
            addNodeTransaction(textToAdd);
        }
    }

    public void processTextClick(MouseEvent me) {
        if (me.getClickCount() > 1) {
            String textInput = AppDialogs.showTextInputDialog(app.getGUI().getWindow(), "ENTER_TEXT_TITLE", "ENTER_TEXT_CONTENT");
            if (textInput != null) {
                Text textControl = (Text) me.getSource();
                textControl.setText(textInput);
            }
        }
    }

    private void addNodeTransaction(Node nodeToAdd) {
        mapData data = (mapData) app.getDataComponent();
        AddNode_Transaction transaction = new AddNode_Transaction(data, nodeToAdd);
        jTPS tps = app.getTPS();
        tps.addTransaction(transaction);
    }

    private Image promptForImage() {
        // SETUP THE FILE CHOOSER FOR PICKING IMAGES
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./images/"));
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.BMP");
        FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterBMP, extFilterGIF, extFilterJPG, extFilterPNG);
        fileChooser.setSelectedExtensionFilter(extFilterPNG);

        // OPEN THE DIALOG
        File file = fileChooser.showOpenDialog(null);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return image;
        } catch (IOException ex) {
            AppDialogs.showMessageDialog(app.getGUI().getWindow(), "ERROR LOADING IMAGE TITLE", "ERROR LOADING IMAGE CONTENT");
            return null;
        }
    }

}
