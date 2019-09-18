/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Vigenere cipher is a method of encrypting alphabetic text by using 
 * a series of interwoven Caesar ciphers based on the letters of a keyword. 
 * It is a form of polyalphabetic substitution.
 * 
 * @author Hristo Hristov
 */
class Vigenere
{
	/**
	 * Encrypts a text file using a Vigenere cipher
	 * 
	 * @param fileToEncrypt		File to encrypt
	 * @param encryptedFile		File to store the encrypted version at
	 * @param key				Key to use
	 */
	static void encrypt(File fileToEncrypt, File encryptedFile, String key)
	{
		try
		{
			System.out.printf("Opening \"%s\" for encryption... ", fileToEncrypt.getPath());
			
			// Open file for encryption
			FileReader reader = new FileReader(fileToEncrypt);
			
			// Create a file to store the encrypted version
			FileWriter writer = new FileWriter(encryptedFile);
			
			// Go through every letter of the original text
			int ch;
			int chNum = 0;
			int[] shifts = breakDownKey(key);
			
			while ((ch = reader.read()) != -1)
			{
				ch = Character.toLowerCase(ch);
				
				// Keep numbers, spaces, punc. intact
				if (ch < 'a' || ch > 'z')
				{
					writer.write(ch);
					continue;
				}
				
				// Perform the shift and write to the new file
				int encryptedCh = ch + shifts[chNum % key.length()];
				encryptedCh = Util.normalizeLetter(encryptedCh);
				
				writer.write(encryptedCh);
				chNum++;
			}
			
			System.out.println("finished encrypting file.");
			System.out.printf("Encrypted version stored at: \"%s\"\n", encryptedFile.getPath());
			
			reader.close();
			writer.flush();
			writer.close();
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}
	
	/**
	 * Decrypts a text file encrypted using a Vigenere cipher
	 * 
	 * @param encryptedFile		File to decrypt
	 * @param decryptedFile		File to store the decrypted version at
	 * @param key				Key to use
	 */
	static void decrypt(File encryptedFile, File decryptedFile, String key)
	{
		try
		{
			System.out.printf("Opening \"%s\" for decryption... ", encryptedFile.getPath());
			
			// Open file for decryption
			FileReader reader = new FileReader(encryptedFile);
			
			// Create a file to store the decrypted version
			FileWriter writer = new FileWriter(decryptedFile);
			
			// Go through every letter of the cipher text
			int ch;
			int chNum = 0;
			int[] shifts = breakDownKey(key);
			
			while ((ch = reader.read()) != -1)
			{
				ch = Character.toLowerCase(ch);
				
				// Keep numbers, spaces, punc. intact
				if (ch < 'a' || ch > 'z')
				{
					writer.write(ch);
					continue;
				}
				
				// Perform the shift and write to the new file
				int decryptedCh = ch - shifts[chNum % key.length()];
				decryptedCh = Util.normalizeLetter(decryptedCh);
				
				writer.write(decryptedCh);
				chNum++;
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
	
	/**
	 * Breaks down a Vigenere key into an array of ints containing shift values.
	 * 
	 * @param key	Vigenere key as a string
	 * @return		Shift values as an array of ints
	 */
	static int[] breakDownKey(String key)
	{
		int[] shifts = new int[key.length()];
		
		for (int i = 0; i < key.length(); i++)
		{
			shifts[i] = key.charAt(i) - 'a';
		}
		
		return shifts;
	}
	
	/**
	 * Breaks down a polyalphabetical cipher into several monoalphabetical ones (streams)
	 * 
	 * @param cipher		Vigenere cipher to break down
	 * @param keyLength		Key length of the vigenere cipher
	 * @return				Streams (cosets) of monoalphabetical (Caesar) ciphers
	 */
	static String[] breakDownCipher(String cipher, int keyLength)
	{
		String[] streams = new String[keyLength];
		int numOfCh = 0;
		
		for (char ch : cipher.toCharArray())
		{
			streams[numOfCh % keyLength] += Character.toString(ch);
			numOfCh++;
		}
		return streams;
	}
	
	/**
	 * Used to find out the key of a Vigenere cipher
	 * 
	 * @param sample	A big chunk of English text
	 * @param streams	Streams of several monoalphabetical ciphers broken down by breakDownCipher()
	 * @return			A string to the key/password
	 */
	static String cryptAnalyse(Text sample, String[] streams)
	{
		// Analyse each stream of encrypted characters
		Text[] cosets = new Text[streams.length];
		int[] keys = new int[streams.length];
		int[] letters = new int[streams.length];
		
		System.out.println("Cryptanalysing vigenere cipher...");
		
		for (int i = 0; i < streams.length; i++)
		{
			cosets[i] = new Text(streams[i]);
			keys[i] = Caesar.cryptAnalyse( sample, cosets[i] );
			
			letters[i] = 'a' + keys[i];
			letters[i] = Util.normalizeLetter( letters[i] );
		}
		
		// Print the shift values used to encrypt the cipher
		System.out.print("Shift values used: ");
		for (int key : keys)
		{
			System.out.printf("%d ", key);
		}
		System.out.println();;
		
		// Construct the actual keyword/password and return it
		String key = new String();
		
		for (int letter : letters)
		{
			key += Character.toString((char) letter);
		}
		return key;
	}
}
