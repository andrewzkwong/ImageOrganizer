/* 
 * Copyright 2016 Andrew Z Kwong.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.imageorganizer;

import static com.imageorganizer.FolderBinding.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;


/**
 * AddDialogController is the controller for the dialog box
 * through which you add folder bindings
 * 
 * @author Andrew Z Kwong
 */

public class AddDialogController implements Initializable {

    @FXML
    private Button setFolderButton;
    @FXML
    private Button setKeyButton;
    @FXML
    private Button okAddDialog;
    @FXML
    private Button cancelAddDialog;
    @FXML
    private TextField folderNameField;
    @FXML
    private TextField keyNameField;
    @FXML
    private AnchorPane topPaneAddDialog;

    private EventHandler<KeyEvent> keyEventHandler;
    private File tempDestination;
    private KeyCode tempKeyCode;
    private FolderBinding folderBinding;

    @FXML
    private void setFolderAction(ActionEvent action) {
        DirectoryChooser destinationChooser = new DirectoryChooser();
        tempDestination = destinationChooser.showDialog(keyNameField.getScene().getWindow());
        if (testWritable(tempDestination) == false) {
            try{
            tempDestination = null;
            FXMLLoader fxmlFolders = new FXMLLoader(getClass().getResource("ErrorWindow.fxml"));
            Scene scene = new Scene((Parent) fxmlFolders.load());
            ErrorWindowController errorController
                    = fxmlFolders.<ErrorWindowController>getController();
            errorController.setErrorText("Cannot write to this folder");
            Stage errorStage = new Stage();
            errorStage.initOwner(keyNameField.getScene().getWindow());
            errorStage.initModality(Modality.WINDOW_MODAL);
            errorStage.setScene(scene);
            errorStage.setResizable(false);
            errorStage.setTitle("Error");
            errorStage.showAndWait();
            }catch (IOException e){
                System.out.println("Missing FXML file");
            }
        } else {
            folderNameField.setText(tempDestination.toString());
        }
        if ((tempKeyCode != null) && (tempDestination != null)) {
            okAddDialog.setDisable(false);
        }
    }

    @FXML
    private void setKeyAction(ActionEvent action) {
        keyNameField.setText("Press Key Now");
        topPaneAddDialog.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                tempKeyCode = keyEvent.getCode();
                keyNameField.setText(tempKeyCode.getName());
                if ((tempKeyCode != null) && (tempDestination != null)) {
                    okAddDialog.setDisable(false);
                }
                keyEvent.consume();
                topPaneAddDialog.removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
        });
    }

    @FXML
    private void finishAddAction(ActionEvent action) {
        folderBinding.addDestinationMap(tempDestination, tempKeyCode);
        Stage stage = (Stage) okAddDialog.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelAddAction(ActionEvent action) {
        Stage stage = (Stage) cancelAddDialog.getScene().getWindow();
        stage.close();
    }

    public void passFolderBinding(FolderBinding passedBinding) {
        folderBinding = passedBinding;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        folderNameField.setEditable(false);
        keyNameField.setEditable(false);
        okAddDialog.setDisable(true);
    }
}