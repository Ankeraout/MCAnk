package fr.ankeraout.mcank.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * This class is an extension of {@link DataOutputStream}. It contains methods
 * specific to Classicube client communication.
 * 
 * @author Ankeraout
 *
 */
public class ClassicubeOutputStream extends DataOutputStream {
	/**
	 * Creates a new instance of {@link ClassicubeOutputStream} with the given
	 * output stream.
	 * 
	 * @param outputStream The {@link OutputStream} to which the data will be
	 *                     written.
	 */
	public ClassicubeOutputStream(OutputStream outputStream) {
		super(outputStream);
	}

	/**
	 * Writes a Classicube string to the underlying output stream. A Classicube
	 * string is always a 64 character wide, US-ASCII encoded string. If the length
	 * of the string is less than 64, then it is padded with space characters. If
	 * the length of the string exceeds 64, then it is truncated.
	 * 
	 * @return The Classicube string read from the underlying output stream.
	 * 
	 * @throws IOException if an exception occurs while writing data from the
	 *                     stream.
	 */
	public void writeClassicubeString(String string) throws IOException {
		byte[] data = string.getBytes(Charset.forName("US-ASCII"));
		
		this.write(data, 0, Math.min(data.length, 64));
		
		for(int i = data.length; i < 64; i++) {
			this.writeByte(' ');
		}
	}

}
