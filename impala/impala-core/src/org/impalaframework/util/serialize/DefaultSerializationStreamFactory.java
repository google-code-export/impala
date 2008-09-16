package org.impalaframework.util.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class DefaultSerializationStreamFactory implements SerializationStreamFactory {

	public ObjectOutputStream getOutputStream(OutputStream output) throws IOException {
		return new ObjectOutputStream(output);
	}
	
	public ObjectInputStream getInputStream(InputStream input) throws IOException {
		return new ObjectInputStream(input);
	}
	
}
