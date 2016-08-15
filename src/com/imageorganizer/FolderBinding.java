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

import static java.nio.file.StandardCopyOption.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.Random;
import javafx.scene.input.KeyCode;

/**
 * FolderBinding is a class that manages folders and provides the map 
 * to various folder bindings.
 */

public class FolderBinding {

    private File sourceFolder;
    private LinkedHashMap<KeyCode, File> destinationMap;
    private File[] imageFiles;
    private int currentImageIndex;
    private int lastImageIndex;

    public FolderBinding() {
        destinationMap = new LinkedHashMap();
    }

    public void setSourceFolder(File folder) {
        sourceFolder = folder;
        imageFiles = sourceFolder.listFiles(new ImageFilter());
        currentImageIndex = 0;
        lastImageIndex = imageFiles.length;
    }

    public File getCurrentImage() {
        if (currentImageIndex < lastImageIndex) {
            return imageFiles[currentImageIndex];
        } else {
            return null;
        }
    }

    //returns 0 if unbound key or no image in display
    //returns 1 if successful placement
    //returns 2 if already existing
    public int putImage(KeyCode key, boolean replace) throws IOException {
        if ((destinationMap.get(key) == null) || currentImageIndex>lastImageIndex-1) {
            return 0;
        } else {
            File destination = new File(destinationMap.get(key), imageFiles[currentImageIndex].getName());
            if (destination.exists() && replace == false) {
                return 2;
            } else {
                Files.move(imageFiles[currentImageIndex].toPath(), destination.toPath(), REPLACE_EXISTING);
                currentImageIndex++;
                return 1;
            }
        }
    }
    
    public void skipImage(){
        currentImageIndex++;
    }
    
    public String getImageName(){
        return imageFiles[currentImageIndex].getName();
    }
    
    public void addDestinationMap(File destination, KeyCode key) {
        destinationMap.put(key, destination);
    }

    public Set<Map.Entry<KeyCode, File>> getTableList() {
        return destinationMap.entrySet();
    }
    
    public void removeKey(KeyCode key){
        destinationMap.remove(key);
    }

    public static boolean testWritable(File directory) {
        boolean success = true;
        try {
            File testWrite = new File(directory, Double.toString((new Random()).nextDouble()).substring(3));
            while (testWrite.exists()) {
                testWrite = new File(directory, Double.toString((new Random()).nextDouble()).substring(3));
            }
            testWrite.createNewFile();
            testWrite.delete();
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
}
