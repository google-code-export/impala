/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.impalaframework.util.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Implementation of {@link SerializationStreamFactory} which simply wraps the output and input
 * in {@link ObjectOutputStream} and {@link ObjectInputStream}s, respectively.
 * 
 * @author Phil Zoio
 */
public class DefaultSerializationStreamFactory implements SerializationStreamFactory {

    public ObjectOutputStream getOutputStream(OutputStream output) throws IOException {
        return new ObjectOutputStream(output);
    }
    
    public ObjectInputStream getInputStream(InputStream input) throws IOException {
        return new ObjectInputStream(input);
    }
    
}
