/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis2.examples.rest.fileUpload.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;

import org.seasar.remoting.axis2.examples.rest.fileUpload.FileUpload;
import org.seasar.remoting.axis2.examples.rest.fileUpload.FileUploadDto;

public class FileUploadImpl implements FileUpload {

    public void upload(String fileName, DataHandler uploadfile) {
        output(fileName, uploadfile);
    }

    public void uploadByDto(FileUploadDto fileUploadDto) {
        output(fileUploadDto.getFileName(), fileUploadDto.getUploadfile());
    }

    private void output(String fileName, DataHandler dataHandler) {
        File file = new File(fileName);
        System.out.println(file.getAbsolutePath());

        try {
            BufferedInputStream inStream = new BufferedInputStream(
                    dataHandler.getDataSource().getInputStream());
            BufferedOutputStream outStream = new BufferedOutputStream(
                    new FileOutputStream(file));

            byte[] buff = new byte[1024];
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            outStream.flush();

            outStream.close();
            inStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
