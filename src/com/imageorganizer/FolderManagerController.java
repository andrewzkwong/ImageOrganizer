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

import static com.imageorganizer.ViewUtilities.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.collections.FXCollections;

/**
 * FolderManagerController is the controller class for FolderManager.
 */
public class FolderManagerController implements Initializable {

    @FXML
    private TableView tableViewManage;
    @FXML
    private TableColumn tableFolderManage;
    @FXML
    private TableColumn tableKeyManage;

    private FolderBinding folderBinding;

    @FXML
    private void openAddDialog(ActionEvent action) {
        try {
            FXMLLoader fxmlAddDialog = new FXMLLoader(getClass().getResource("AddDialog.fxml"));
            Scene scene = new Scene((Parent) fxmlAddDialog.load());
            AddDialogController addDialogController
                    = fxmlAddDialog.<AddDialogController>getController();
            addDialogController.passFolderBinding(folderBinding);
            Stage addStage = new Stage();
            addStage.initOwner(tableViewManage.getScene().getWindow());
            addStage.initModality(Modality.WINDOW_MODAL);
            addStage.setScene(scene);
            addStage.setResizable(false);
            addStage.setTitle("Add Destination Folder");
            addStage.showAndWait();
            tableViewManage.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
        } catch (IOException e) {
            System.err.println("Caught IOException from no AddDialog.fxml");
        }
    }

    @FXML
    private void deleteFocusedRow(ActionEvent action) {
        folderBinding.removeKey(((Map.Entry<KeyCode, File>) tableViewManage.getSelectionModel().getSelectedItem()).getKey());
        tableViewManage.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
    }

    @FXML
    private void closeWindow(ActionEvent action) {
        Stage stage = (Stage) tableViewManage.getScene().getWindow();
        stage.close();
    }

    public void passFolderBinding(FolderBinding passedBinding) {
        folderBinding = passedBinding;
        tableViewManage.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
        fillTableColumns(tableFolderManage, tableKeyManage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bindTableColumnWidths(tableFolderManage, tableKeyManage, tableViewManage);
    }
}
