/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cryptography.Util;

/**
 * The Caesar cipher, also known as a shift cipher, is one of the simplest forms 
 * of encryption. It is a substitution cipher where each letter in the original 
 * message (called the plaintext) is replaced with a letter corresponding to a 
 * certain number of letters up or down in the alphabet.
 * 
 * @author Hristo Hristov
 */
class Caesar
{
	/**
	 * Attempts a monoalphabetic cipher-attack based on a statistical frequency analysis.
	 * Used to find the shift value of a Caesar cipher.
	 * 
	 * @param 	sample	A big chunk of English text
	 * @param 	cipher	The encrypted string of text
	 * @return			The most congruent shift value for the cipher based on the stats
	 */
	static int cryptAnalyse(Text sample, Text cipher)
	{
		System.out.println("Cryptanalysing monoalphabetical cipher...");
		
		float[] deviation = new float[Util.ALPHABET_SIZE];
		
		// Used to sort deviation values
		Map<Integer, Float> deviationMap = new LinkedHashMap<Integer, Float>();
		
		// s means shift, l means letter
		for (int s = 0; s < Util.ALPHABET_SIZE; s++)
		{
			for (int l = 0; l < Util.ALPHABET_SIZE; l++)
			{
				deviation[s] += Math.abs(
					Util.getFrequency(sample.getLetterCount(l), sample.getTotalLetters()) 
				  - Util.getFrequency(cipher.getLetterCount((l + s) % Util.ALPHABET_SIZE), 
						  			  cipher.getTotalLetters()));
			}
			System.out.printf("Shifting the cipher by %2d deviates it from the norm by %6.2f %%\n", s, deviation[s]);
			
			// Used to sort deviation values
			deviationMap.put(s, deviation[s]);
		}
		
		System.out.println("\nSorting shift values by deviation...");
		deviationMap = Util.sortMap(deviationMap, true, true);
		
		for (Entry<Integer, Float> entry : deviationMap.entrySet())
		{
			System.out.printf("Shift: %2d | Deviation: %4.2f %%\n", 
					entry.getKey(), entry.getValue());
		}
		System.out.println();
		
		return (Integer) deviationMap.keySet().toArray()[0];
	}
	
	/**
	 * Decrypts a a monoalphabetic caesar cipher given a shift value
	 * 
	 * @param encryptedFile		File to decrypt
	 * @param decryptedFile		File to store the decrypted version at
	 * @param shift				Shift value to use
	 */
	static void decrypt(File encryptedFile, File decryptedFile, int shift)
	{
		try
		{
			System.out.printf("Opening \"%s\" for decryption... ", encryptedFile.getPath());
			
			// Open cipher for decryption
			FileReader reader = new FileReader(encryptedFile);
			
			// Create a file to store the decrypted version
			FileWriter writer = new FileWriter(decryptedFile);
			
			// Go through every letter of the cipher text
			int ch;
			while ((ch = reader.read()) != -1)
			{
				// Not sure if lowercasing is necessary, but I'll keep it
				ch = Character.toLowerCase(ch);
				
				// Keep numbers, spaces, punc. intact
				// assuming they're not encrypted
				if (ch < 'a' || ch > 'z')
				{
					writer.write(ch);
					continue;
				}
				
				// Perform the shift and write to the new file
				int decryptedch = ch - shift;
				
				decryptedch = Util.normalizeLetter(decryptedch);
				
				writer.write(decryptedch);
			}
			
			System.out.println("finished decrypting file.");
			System.out.printf("Decrypted version stored at: \"%s\"\n", decryptedFile.getPath());
			
			reader.close();
			writer.flush();
			writer.close();
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}
}
