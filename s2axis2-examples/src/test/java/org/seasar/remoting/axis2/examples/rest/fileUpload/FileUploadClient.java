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
package org.seasar.remoting.axis2.examples.rest.fileUpload;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.description.java2wsdl.Java2WSDLUtils;
import org.seasar.remoting.axis2.examples.rest.fileUpload.FileUpload;
import org.seasar.remoting.axis2.examples.rest.fileUpload.FileUploadDto;

public class FileUploadClient {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        String path = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");

        File file = createFile(path + separator + "サンプル.txt", "ファイルの内容です。");
        OMElement payload = getPayload(file);

        EndpointReference targetEPR = new EndpointReference(
                "http://localhost:8080/s2axis2-examples/services/FileUpload");

        Options options = new Options();
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
        options.setProperty(Constants.Configuration.ENABLE_REST,
                Constants.VALUE_TRUE);
        options.setProperty(Constants.Configuration.HTTP_METHOD,
                Constants.Configuration.HTTP_METHOD_POST);

        options.setTo(targetEPR);
        options.setAction("urn:uploadByDto");

        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        sender.sendRobust(payload);

        file.delete();
    }

    private static File createFile(String fileName, String data)
            throws IOException {

        File file = new File(fileName);

        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.flush();
        writer.close();

        return file;
    }

    private static OMElement getPayload(File file) throws Exception {

        String className = FileUpload.class.getName();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        StringBuffer nsBuff;
        try {
            nsBuff = Java2WSDLUtils.schemaNamespaceFromClassName(className,
                    loader);
        } catch (Exception ex) {
            throw ex;
        }
        String schemaTargetNameSpace = nsBuff.toString();

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(schemaTargetNameSpace, null);
        OMElement method = fac.createOMElement("uploadByDto", omNs);

        // dto
        OMElement omDto = fac.createOMElement("fileUploadDto", omNs, method);
        omDto.addAttribute("type", FileUploadDto.class.getName(), null);

        // fileName
        OMElement omFileName = fac.createOMElement("fileName", null, omDto);
        omFileName.setText(file.getName());

        // uploadfile
        DataSource dataSource = new FileDataSource(file);
        DataHandler handler = new DataHandler(dataSource);
        OMElement omUploadfile = fac.createOMElement("uploadfile", null, omDto);
        OMText textNode = fac.createOMText(handler, true);
        omUploadfile.addChild(textNode);

        return method;
    }

}
