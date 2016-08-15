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

import java.io.FileFilter;
import java.io.File;

/**
 * ImageFilter implements a FileFilter for image files.
 */

public class ImageFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        String extension = null;
        String pathString = pathname.getName();
        int dotLocation = pathString.lastIndexOf(".");
        if ((dotLocation > 0) && (dotLocation < pathString.length() - 1)) {
            extension = pathString.substring(dotLocation + 1).toLowerCase();
        }
        if (extension != null) {
            if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")
                    || extension.equals("bmp") || extension.equals("gif")) {
                return true;
            }
        }
        return false;
    }
}
