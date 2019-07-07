package fr.ankeraout.mcank.util;

/***
 * This class contains utility methods for working with strings. This class
 * cannot be instantiated, therefore it does not contain any constructor that is
 * not private.
 * 
 * @author Ankeraout
 *
 */
public final class StringUtils {
	/**
	 * This array of characters contains the list of all the hexadecimal characters.
	 */
	private static final char[] hexadecimalCharacters = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * The only constructor of this class is private because this class cannot be
	 * instantiated.
	 */
	private StringUtils() {
		// This will never be called
	}

	/**
	 * Returns the hexadecimal representation of the array of bytes. Every byte in
	 * the array is written as 2 hexadecimal characters. For example, if the input
	 * is this array: [0x3f, 0x98] then the output will be: "3f98".
	 * 
	 * @param array The array that contains the data that we want represented as an
	 *              hexadecimal string.
	 * @return A hexadecimal {@link String} value representing the input byte array.
	 */
	public static String arrayToHex(byte[] array) {
		// We use a StringBuilder because according to the documentation of the Java
		// language, it is faster than concatenating String objects.
		StringBuilder stringBuilder = new StringBuilder();

		// For every byte in the byte array
		for (byte b : array) {
			// Ensures byteValue is positive
			int byteValue = ((int)b) & 0xff;
			
			// Append the character for the most significant 4 bits of the byte.
			stringBuilder.append(StringUtils.hexadecimalCharacters[byteValue >> 4]);
			
			// Append the character for the least significant 4 bits of the byte.
			stringBuilder.append(StringUtils.hexadecimalCharacters[byteValue & 0x0f]);
		}

		// Return the final String object.
		return stringBuilder.toString();
	}
}
