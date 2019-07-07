package fr.ankeraout.mcank.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * This class is an extension of {@link DataInputStream}. It contains methods
 * specific to Classicube client communication.
 * 
 * @author Ankeraout
 *
 */
public class ClassicubeInputStream extends DataInputStream {
	/**
	 * This is a small buffer used by the
	 * {@link ClassicubeInputStream#readClassicubeString()} method. It will contain
	 * all the data of the read string. It is placed as a class attribute in order
	 * to prevent the {@link ClassicubeInputStream#readClassicubeString()} method
	 * from allocating a byte array every time it is executed.
	 */
	private byte[] buffer;

	/**
	 * Creates a new instance of {@link ClassicubeInputStream} with the given input
	 * stream.
	 * 
	 * @param inputStream The {@link InputStream} from which the data will be read.
	 */
	public ClassicubeInputStream(InputStream inputStream) {
		super(inputStream);

		this.buffer = new byte[64];
	}

	/**
	 * Reads a Classicube string from the underlying input stream. A Classicube
	 * string is always a 64 character wide, US-ASCII encoded string. If the length
	 * of the string is less than 64, then it is padded with space characters. This
	 * method reads the string and removes the padding characters.
	 * 
	 * @return The Classicube string read from the underlying input stream.
	 * 
	 * @throws IOException if an exception occurs while reading data from the stream.
	 */
	public String readClassicubeString() throws IOException {
		int readByteCount = 0;
		
		while(readByteCount < 64) {
			readByteCount += this.read(this.buffer, readByteCount, 64 - readByteCount);
		}
		
		return new String(this.buffer, Charset.forName("US-ASCII")).trim();
	}

}
