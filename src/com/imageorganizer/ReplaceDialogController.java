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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ReplaceDialogController is the controller class for the ReplaceDialog.
 *
 */
public class ReplaceDialogController implements Initializable {

    @FXML
    private Button replaceButton;
    @FXML
    private Button skipButton;

    private FolderBinding folderBinding;
    private KeyCode keyCode;

    @FXML
    private void skipAction(ActionEvent action) {
        folderBinding.skipImage();
        Stage stage = (Stage) replaceButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void replaceAction(ActionEvent action) {
        try {
            folderBinding.putImage(keyCode, true);
            Stage stage = (Stage) replaceButton.getScene().getWindow();
            stage.close();
        } catch (IOException missingFolder) {
            try {
                //Makes error window for missing folder.
                FXMLLoader fxmlFolders = new FXMLLoader(getClass().getResource("ErrorWindow.fxml"));
                Scene scene = new Scene((Parent) fxmlFolders.load());
                ErrorWindowController errorController
                        = fxmlFolders.<ErrorWindowController>getController();
                errorController.setErrorText("Either the destination folder or the image you're trying to move "
                        + "\n has been moved or deleted. \n Restarting the program is suggested.");
                Stage manageStage = new Stage();
                manageStage.initOwner(skipButton.getScene().getWindow());
                manageStage.initModality(Modality.WINDOW_MODAL);
                manageStage.setScene(scene);
                manageStage.setResizable(false);
                manageStage.showAndWait();
            } catch (IOException missingFXML) {
                System.err.println("Caught IOException from no ErrorWindow.fxml");
            }
        }
    }

    public void passFolderBinding(FolderBinding passedBinding) {
        folderBinding = passedBinding;
    }

    public void passKeyCode(KeyCode passedKeyCode) {
        keyCode = passedKeyCode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
