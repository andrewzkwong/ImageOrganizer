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
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ImageOrganizerController is the controller class for the main window.
 */
public class ImageOrganizerController implements Initializable {

    @FXML
    private AnchorPane tableAnchor;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageViewer;
    @FXML
    private TableView tableDestinationFolders;
    @FXML
    private AnchorPane imageViewAnchor;
    @FXML
    private TableColumn tableFolder;
    @FXML
    private TableColumn tableKey;
    @FXML
    private SplitPane splitTableImage;
    @FXML
    private Text fileNameText;

    private FolderBinding folderBinding;
    private ObservableList<Map.Entry<KeyCode, File>> tableData;

    @FXML
    private void openPane(ActionEvent event) {
        splitTableImage.setDividerPositions(0.3);
    }

    @FXML
    private void chooseSourceFolder(ActionEvent event) {
        DirectoryChooser sourceChooser = new DirectoryChooser();
        File sources = sourceChooser.showDialog(rootPane.getScene().getWindow());
        folderBinding.setSourceFolder(sources);
        if (folderBinding.getCurrentImage() != null) {
            imageViewer.setImage(new Image(folderBinding.getCurrentImage().toURI().toString()));
            fileNameText.setText(folderBinding.getImageName());
        } else {
            imageViewer.setImage(null);
        }
    }

    @FXML
    private void openAddDialog(ActionEvent event) {
        try {
            FXMLLoader fxmlAddDialog = new FXMLLoader(getClass().getResource("AddDialog.fxml"));
            Scene scene = new Scene((Parent) fxmlAddDialog.load());
            AddDialogController addDialogController
                    = fxmlAddDialog.<AddDialogController>getController();
            addDialogController.passFolderBinding(folderBinding);
            createNewWindow(scene, (Node) rootPane, "Add Destination Folder");
            tableDestinationFolders.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
        } catch (IOException e) {
            System.err.println("Caught IOException from no AddDialog.fxml");
        }
    }

    @FXML
    private void openManageWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlFolders = new FXMLLoader(getClass().getResource("FolderManager.fxml"));
            Scene scene = new Scene((Parent) fxmlFolders.load());
            FolderManagerController foldersController
                    = fxmlFolders.<FolderManagerController>getController();
            foldersController.passFolderBinding(folderBinding);
            createNewWindow(scene, (Node) rootPane, "Folder Manager");
            tableDestinationFolders.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
        } catch (IOException e) {
            System.err.println("Caught IOException from no FolderManager.fxml ");
            e.printStackTrace();
        }
    }

    @FXML
    private void sortImage(KeyEvent keyPressed) {
        try {
            int success = folderBinding.putImage(keyPressed.getCode(), false);
            if (success == 1) {
                if (folderBinding.getCurrentImage() == null) {
                    imageViewer.setImage(null);
                } else {
                    imageViewer.setImage(new Image(folderBinding.getCurrentImage().toURI().toString()));
                }
            } else if (success == 2) {
                FXMLLoader fxmlReplaceDialog = new FXMLLoader(getClass().getResource("ReplaceDialog.fxml"));
                Scene scene = new Scene((Parent) fxmlReplaceDialog.load());
                ReplaceDialogController replaceDialogController
                        = fxmlReplaceDialog.<ReplaceDialogController>getController();
                replaceDialogController.passFolderBinding(folderBinding);
                replaceDialogController.passKeyCode(keyPressed.getCode());
                createNewWindow(scene, (Node) rootPane, "Name Already Exists");
                imageViewer.setImage(new Image(folderBinding.getCurrentImage().toURI().toString()));
            }
        } catch (IOException missingFolder) {
            try {
                //Makes error window for missing folder.
                FXMLLoader fxmlFolders = new FXMLLoader(getClass().getResource("ErrorWindow.fxml"));
                Scene scene = new Scene((Parent) fxmlFolders.load());
                ErrorWindowController errorController
                        = fxmlFolders.<ErrorWindowController>getController();
                errorController.setErrorText("Either the destination folder or the image you're trying to move "
                        + "\n has been moved or deleted");
                createNewWindow(scene, (Node) rootPane, "Error");
            } catch (IOException missingFXML) {
                System.err.println("Caught IOException from no ErrorWindow.fxml");
            }
        }
        keyPressed.consume();
    }

    @FXML
    private void closeProgram(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bindTableColumnWidths(tableFolder, tableKey, tableDestinationFolders);
        imageViewer.fitWidthProperty().bind(imageViewAnchor.widthProperty());
        imageViewer.fitHeightProperty().bind(imageViewAnchor.heightProperty());
        SplitPane.setResizableWithParent(tableAnchor, Boolean.FALSE);
        folderBinding = new FolderBinding();
        tableDestinationFolders.setItems(FXCollections.observableArrayList(folderBinding.getTableList()));
        fillTableColumns(tableFolder, tableKey);
    }
}