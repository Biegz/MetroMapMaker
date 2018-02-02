package map.gui;

import djf.AppTemplate;
import java.io.File;
import map.data.Draggable;
import map.data.mapData;
import map.transactions.ChangeBackgroundColor_Transaction;
import map.transactions.ChangeShapeFillColor_Transaction;
import map.transactions.ChangeShapeOutlineColor_Transaction;
import map.transactions.ChangeShapeOutlineThickness_Transaction;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import jtps.jTPS;

/**
 *
 * @author McKillaGorilla
 */
public class BackgroundOutlineFillController {
    AppTemplate app;
    mapData mapManager;
    
    public BackgroundOutlineFillController(AppTemplate initApp) {
        
        this.app = initApp;
        mapManager = (mapData)app.getDataComponent();
    }

    /**
     * This method processes a user request to select the 
     * background color.
     */
    public void processSelectBackgroundColor(Color color) {
	mapWorkspace workspace = (mapWorkspace)app.getWorkspaceComponent();
	//Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (color != null) {
            Pane canvas = workspace.getCanvas();
            
//            BackgroundFill bf = new BackgroundFill(color, null, null);
//            Background b = new Background(bf);
//            canvas.setBackground(b);
            mapManager.setBackgroundColor(color);

	    ChangeBackgroundColor_Transaction transaction = new ChangeBackgroundColor_Transaction(canvas, color);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    public void setImageBackground(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("image files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(app.getGUI().getWindow());

        Image i = new Image(file.toURI().toString());

        mapManager.setImage(i);
        mapManager.setImagePattern(i);
        mapManager.makeNewImage(0, 0);

 
        mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(mapManager);
    }
            

    
           
}
