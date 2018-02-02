package map.gui;

import djf.AppTemplate;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.data.mapData;
import map.transactions.ChangeTextFont_Transaction;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jtps.jTPS;
import map.data.DraggableLine;
import map.data.DraggableText;

/**
 *
 * @author McKillaGorilla
 */
public class FontController {

    private AppTemplate app;
    private mapData mapManager;

    public FontController(AppTemplate initApp) {
        app = initApp;
        mapManager = (mapData) app.getDataComponent();
    }

    public void setFontColorFromColorPicker(Color color) {
        if (mapManager.getSelectedDraggableNode() instanceof DraggableText) {
            DraggableText draggableText = (DraggableText) mapManager.getSelectedDraggableNode();
            draggableText.setStroke(color);
        }
    }

    public void processChangeFont() {
        mapData data = (mapData) app.getDataComponent();
        if (data.isTextSelected()) {
            Text selectedText = (Text) data.getSelectedNode();
            mapWorkspace workspace = (mapWorkspace) app.getWorkspaceComponent();
            Font currentFont = workspace.getCurrentFontSettings();
            ChangeTextFont_Transaction transaction = new ChangeTextFont_Transaction(selectedText, currentFont);
            jTPS tps = app.getTPS();
            tps.addTransaction(transaction);
        }
    }

    public void rotateLabel() {
        DraggableText draggableText = (DraggableText) mapManager.getSelectedDraggableNode();

        if (draggableText.getIsForLine() == false) {
            if (draggableText.getRotate() == 0) {
                draggableText.setRotate(-90);
            } else if (draggableText.getRotate() == -90) {
                draggableText.setRotate(0);
            }
        }
    }

}
