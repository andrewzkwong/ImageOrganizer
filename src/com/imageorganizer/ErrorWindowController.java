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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * ErrorWindowController is the controller for error windows.
 *
 */
public class ErrorWindowController implements Initializable {

    @FXML
    private Button okButton;
    @FXML
    private Text errorText;
    /**
     * Initializes the controller class.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    
    public void setErrorText(String text){
        errorText.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}