/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.io.File;
import java.util.Scanner;

import cryptography.Text;
import cryptography.Text.AnalysisType;
import cryptography.Kasiski;
import cryptography.Vigenere;

/**
 * Coursework for CSC3621 Cryptography
 * Part I - Exercise 2
 * @author Hristo Hristov /150455606/ b5045560
 */
class Exercise2
{
	private static final File NOVEL_FILE = new File(Text.SAMPLE_FILEPATH);
	private static final File NOVEL_FILE_ENCRYPTED = new File("C:\\Users\\Ici\\Desktop\\novel-encrypted.txt");
	private static final File NOVEL_FILE_DECRYPTED = new File("C:\\Users\\Ici\\Desktop\\novel-decrypted.txt");
	
	private static final File NCL_FILE = new File("C:\\Users\\Ici\\Desktop\\newcastle.txt");
	private static final File NCL_FILE_ENCRYPTED = new File("C:\\Users\\Ici\\Desktop\\newcastle-encrypted.txt");
	
	private static final File CIPHER_FILE = new File("C:\\Users\\Ici\\Desktop\\Exercise2Ciphertext.txt");
	private static final File CIPHER_FILE_DECRYPTED = new File("C:\\Users\\Ici\\Desktop\\Exercise2Ciphertext-decrypted.txt");
	
	/**
	 * Main entry point
	 * @param args
	 */
	public static void main(String[] args)
	{
		String password = getPassword();
		System.out.println("Your password is: " + password);
		
		// Simple test for correctness
		Vigenere.encrypt(NCL_FILE, NCL_FILE_ENCRYPTED, password);
		
		// Encrypt the novel using the password entered
		Vigenere.encrypt(NOVEL_FILE, NOVEL_FILE_ENCRYPTED, password);
		
		// Decrypt the novel now using the same password
		Vigenere.decrypt(NOVEL_FILE_ENCRYPTED, NOVEL_FILE_DECRYPTED, password);
		
		// Compare frequency analyses
		Text novel = new Text(NOVEL_FILE, false);
		Text encryptedNovel = new Text(NOVEL_FILE_ENCRYPTED, false);
		novel.printAnalysis(AnalysisType.ByFrequency);
		System.out.println("Index of coincidence: " + novel.getIndexOfCoincidence());
		encryptedNovel.printAnalysis(AnalysisType.ByFrequency);
		System.out.println("Index of coincidence: " + encryptedNovel.getIndexOfCoincidence());
		
		// Get the cipher from Exercise 2
		Text cipher = new Text(CIPHER_FILE, true);
		System.out.println("Total number of letters: " + cipher.getTotalLetters());
		System.out.println("Index of coincidence: " + cipher.getIndexOfCoincidence());
		
		// Perform a Kasiski test on it
		int keyLengthKasiski = Kasiski.test(cipher.getText());
		System.out.println("[Kasiski] The most congruent key length: " + keyLengthKasiski);
		
		// Perform a Friedman test
		int keyLengthFriedman = Friedman.test(cipher.getText(), novel.getIndexOfCoincidence());
		System.out.println("[Friedman] The most congruent key length: " + keyLengthFriedman);
		
		// Attempt to break the Vigenere cipher
		String[] streams = Vigenere.breakDownCipher(cipher.getText(), keyLengthFriedman);
		String key = Vigenere.cryptAnalyse(novel, streams);
		System.out.println("The key is: " + key);
		
		// Decrypt the Vigenere cipher
		Vigenere.decrypt(CIPHER_FILE, CIPHER_FILE_DECRYPTED, key); // voila
	}
	
	/**
	 * Asks the user to enter a password used to encrypt and decrypt a Vigenere cipher
	 * @return	The password entered
	 */
	private static String getPassword()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter password to encrypt Vigenere cipher with: ");
		
		String password = scanner.nextLine();
		while (!password.matches("[a-zA-Z]+"))
		{
		    System.out.println("Please enter a valid password! (Only letters allowed) : ");
		    password = scanner.nextLine();
		}
		
		password = password.trim(); // perhaps unnecessary because of the upcoming regex
		password = password.toLowerCase();
		password = password.replaceAll("\\s+", ""); // get rid of whitespace
		
		scanner.close(); // um.. this would close the input stream, do I care?
		return password;
	}
}
