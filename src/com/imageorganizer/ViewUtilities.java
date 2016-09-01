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

import java.io.File;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *ViewUtilities is the utility class that provides
access to static methods that help with
managing the view for this application.
 * 
 * 
 */

public final class ViewUtilities {
    public static void bindTableColumnWidths(TableColumn folders, TableColumn keys, TableView table) {
        folders.prefWidthProperty().bind(table.widthProperty().multiply(0.75));
        keys.prefWidthProperty().bind(table.widthProperty().subtract(folders.widthProperty()));
    }

    public static void fillTableColumns(TableColumn folders, TableColumn keys) {
        folders.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry, String> p) {
                return new SimpleStringProperty(p.getValue().getValue().toString());
            }
        });
        keys.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<KeyCode, File>, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<KeyCode, File>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey().getName());
            }
        });
    }
    
    public static void createNewWindow(Scene scene, Node node, String titleText){
        Stage newStage = new Stage();
        newStage.initOwner(node.getScene().getWindow());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle(titleText);
        newStage.showAndWait();
    }
}
