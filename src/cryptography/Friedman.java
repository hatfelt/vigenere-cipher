/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import cryptography.Text;
import cryptography.Vigenere;

/**
 * William Friedman's method of finding the key length using indices of coincidence.
 * 
 * @author Hristo Hristov
 */
class Friedman
{
	// Adjust these to estimate longer key lengths
	static final int MIN_KEY_LENGTH = 2;
	static final int MAX_KEY_LENGTH = 10;
	
	/**
	 * Performs Friedman's test. Used to estimate the length of the unknown keyword.
	 * 
	 * @param cipher		Cipher text
	 * @param sampleIoC		The index of coincidence for a big chunk of English
	 * @return				The most congruent key length
	 */
	static int test(String cipher, float sampleIoC)
	{
		float[] avgIoC = new float[MAX_KEY_LENGTH + 1];
		
		System.out.println("Performing Friedman's test...");
		
		for (int len = MIN_KEY_LENGTH; len <= MAX_KEY_LENGTH; len++)
		{
			String[] streams = Vigenere.breakDownCipher(cipher, len);
			
			// Analyse each stream of encrypted characters
			Text[] cosets = new Text[streams.length];
			float indexOfCoincidence = 0.0f;
			
			for (int i = 0; i < streams.length; i++)
			{
				cosets[i] = new Text(streams[i]);
				indexOfCoincidence += cosets[i].getIndexOfCoincidence();
			}
			avgIoC[len] = indexOfCoincidence / len;
			
			System.out.printf("Key length: %2d | Avg Index of Coincidence: %f\n", len, avgIoC[len]);
		}
		
		// The one that's the closest to the IoC of English should be the right key length
		return findClosestIndex(avgIoC, sampleIoC);
	}
	
	/**
	 * Gets the closest value to a number in an array returns the index
	 * 
	 * @param array		Array of floats to scan through
	 * @param value		Number to find closest match for
	 * @return			The array index
	 */
	static int findClosestIndex(float[] array, float value)
	{
		// Initial state
		float difference = Math.abs(array[0] - value);
		int idx = 0;
		
		// Compute closest match
		for (int i = 1; i < array.length; i++)
		{
			float newDifference = Math.abs(array[i] - value);
			
			if (newDifference < difference)
			{
				// Found a closer match
				idx = i;
				difference = newDifference;
			}
		}
		return idx;
	}
}
