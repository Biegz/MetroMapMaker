/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui;

import djf.AppTemplate;
import djf.ui.AppGUI;
import djf.ui.FileController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 *
 * @author austin
 */
public class mapWelcome {

    Image iconImage;

    ImageView welcomeLabel;
    Hyperlink newHl;
    Hyperlink loadHl;
    Hyperlink exitHl;

    Label recentLabel;
    Hyperlink example1;
    Hyperlink example2;
    Hyperlink example3;
    Hyperlink example4;
    Hyperlink example5;

    AppTemplate app;
    AppGUI gui;

    Scene welcomeScene;
    Stage welcomeStage;
    BorderPane bp;

    public mapWelcome(AppGUI gui) {
        //this.app = app;
        this.gui = gui;
    }

    public void initWelcome() {

        initLayout();
        initStyle();
        initController();

        welcomeStage.showAndWait();
    }

    public void initLayout() {
        iconImage = new Image("file:images/logo.png");
        bp = new BorderPane();

        welcomeLabel = new ImageView(iconImage);
        
        bp.setLeft(welcomeLabel);

        VBox vbox1 = new VBox();
        newHl = new Hyperlink("Create New Map");
        exitHl = new Hyperlink("Exit");
        vbox1.getChildren().addAll(newHl, exitHl);
        bp.setCenter(vbox1);

        VBox vbox2 = new VBox();

        HBox hbox1 = new HBox();
        recentLabel = new Label("Recent Maps");
        hbox1.getChildren().add(recentLabel);
        hbox1.setPadding(new Insets(0, 0, 0, 30));

        HBox hbox2 = new HBox();
        example1 = new Hyperlink("example1");
        example2 = new Hyperlink("example2");
        example3 = new Hyperlink("example3");
        example4 = new Hyperlink("example4");
        example5 = new Hyperlink("example5");
        hbox2.getChildren().addAll(example1, example2, example3, example4, example5);
        hbox2.setSpacing(15.0);
        hbox2.setPadding(new Insets(0, 0, 0, 30));
        vbox2.getChildren().addAll(hbox1, hbox2);
        bp.setBottom(vbox2);

        welcomeStage = new Stage();
        welcomeStage.setScene(new Scene(bp, 550, 300));

    }

    public void initController() {
        newHl.setOnAction(e -> {
            gui.getFileController().processNewRequestFromWelcome();
            welcomeStage.close();

        });
        exitHl.setOnAction(e -> {
            welcomeStage.close();
        });

        example1.setOnAction(e -> {
            gui.getFileController().processLoadRecentRequest(example1.getText());
            welcomeStage.close();

        });
        example2.setOnAction(e -> {
            welcomeStage.close();

        });
        example3.setOnAction(e -> {
            welcomeStage.close();

        });
        example4.setOnAction(e -> {
            welcomeStage.close();

        });
        example5.setOnAction(e -> {
            welcomeStage.close();

        });

    }

    public void initStyle() {

        //bp.getStyleClass().add(CLASS_WELCOME);

    }
    

}
