package org.impalaframework.util.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public interface SerializationStreamFactory {

	ObjectOutputStream getOutputStream(OutputStream output) throws IOException;

	ObjectInputStream getInputStream(InputStream input) throws IOException;

}