/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.io.File;

import cryptography.Text;
import cryptography.Caesar;

/**
 * Coursework for CSC3621 Cryptography
 * Part I - Exercise 1
 * @author Hristo Hristov /150455606/ b5045560
 */
class Exercise1
{
	private static final File NOVEL_FILE = new File(Text.SAMPLE_FILEPATH);
	private static final File CIPHER_FILE = new File("C:\\Users\\Ici\\Desktop\\Exercise1Ciphertext.txt");
	private static final File CIPHER_FILE_DECRYPTED = new File("C:\\Users\\Ici\\Desktop\\Exercise1Ciphertext-decrypted.txt");
	
	/**
	 * Main entry point
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Frequency analyse a novel
		Text novel = new Text(NOVEL_FILE, false);
		System.out.println("Total number of letters: " + novel.getTotalLetters());
		novel.printAnalysis(Text.AnalysisType.ByFrequency);
		System.out.println("Index of coincidence: " + novel.getIndexOfCoincidence());
		
		// Frequency analyse a cipher
		Text cipher = new Text(CIPHER_FILE, false);
		System.out.println("Total number of letters: " + cipher.getTotalLetters());
		cipher.printAnalysis(Text.AnalysisType.ByFrequency);
		System.out.println("Index of coincidence: " + cipher.getIndexOfCoincidence());
		
		// Attempt to break the cipher
		int shift  = Caesar.cryptAnalyse(novel, cipher);
		System.out.println("The most congruent shift value appears to be: " + shift);
		Caesar.decrypt(CIPHER_FILE, CIPHER_FILE_DECRYPTED, shift);
	}
}
