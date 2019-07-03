package fr.ankeraout.mcank.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class contains the utils for serializing and deserializing objects. This
 * class is final and its only constructor is made private, because it should
 * not be instantiated.
 * 
 * @author Ankeraout
 *
 */
public final class SerializationUtils {
	/**
	 * This constructor is kept private to prevent class instantiation.
	 */
	private SerializationUtils() {

	}

	/**
	 * Serializes a Java object and returns its byte array representation.
	 * 
	 * @param o The Java object to serialize. Its type class and the type classes it
	 *          depends on should implement Serializable
	 * @return The byte array representation of the Java object.
	 */
	public static final byte[] serializeObject(Object o) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// This should never happen because we're not doing I/O operations anywhere else
			// than in memory.
			throw new RuntimeException("Unexpected IOException", e);
		}

		return baos.toByteArray();
	}

	/**
	 * Deserializes a Java object and returns its Object representation from its
	 * byte array representation.
	 * 
	 * @param data The byte array representation of the object.
	 * @return The Java object representation of the given data
	 * @throws ClassNotFoundException If the class of the deserialized object could
	 *                                not be found.
	 */
	public static final Object deserializeObject(byte[] data) throws ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois;
		Object tmp;

		try {
			ois = new ObjectInputStream(bais);
			tmp = ois.readObject();
			ois.close();
		} catch (IOException e) {
			// This should never happen because we're not doing I/O operations anywhere else
			// than in memory.
			throw new RuntimeException("Unexpected " + e.getClass().getSimpleName(), e);
		}

		return tmp;
	}
}
