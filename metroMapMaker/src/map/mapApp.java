package map;

import java.util.Locale;

import map.data.mapData;
import map.file.mapFiles;
import map.gui.mapWorkspace;
import djf.AppTemplate;
import map.gui.mapClipboard;
import djf.ui.mapWelcome;

/**
 * This class serves as the application class for our goLogoLoApp program. Note that much of its behavior is inherited
 * from AppTemplate, as defined in the Desktop Java Framework. This app starts by loading all the app-specific messages
 * like icon files and tooltips and other settings, then the full User Interface is loaded using those settings. Note
 * that this is a JavaFX application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mapApp extends AppTemplate {
    /**
     * This hook method must initialize all three components in the proper order ensuring proper dependencies are
     * respected, meaning all proper objects are already constructed when they are needed for use, since some may need
     * others for initialization.
     */
    @Override
    public void buildAppComponentsHook() {
        
        // CONSTRUCT ALL THREE COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, AND THE DATA COMPONENT NEEDS THE
        // FILE COMPONENT SO WE MUST BE CAREFUL OF THE ORDER
        fileComponent = new mapFiles();
        dataComponent = new mapData(this);
        workspaceComponent = new mapWorkspace(this);
        clipboardComponent = new mapClipboard(this);
    }

    /**
     * This is where program execution begins. Since this is a JavaFX app it will simply call launch, which gets JavaFX
     * rolling, resulting in sending the properly initialized Stage (i.e. window) to the start method inherited from
     * AppTemplate, defined in the Desktop Java Framework.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }
}