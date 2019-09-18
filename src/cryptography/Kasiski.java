/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cryptography.Util;

/**
 * Kasiski's test is a method of attacking polyalphabetic 
 * substitution ciphers such as the Vigenere cipher.
 * 
 * @author Hristo Hristov
 */
class Kasiski
{
	// Starting factor, essentially the minimum length of the key
	static final int MIN_FACTOR = 2;
	
	// The higher the max factor, the longer key you may break
	static final int MAX_FACTOR = 10;
	
	// Account for uncertainty of key length. The higher, the more possible key lengths assumed
	private static final float KEY_TOLERANCE = 15.0f;
	
	/**
	 * Finds repeating words/patterns of text
	 * 
	 * @param cipher	Cipher text
	 * @return			A map of repeating words
	 */
	static Map<String, Integer> getRepeatingWords(String cipher)
	{
		System.out.printf("Finding repeating words of length [%d,%d] please wait...\n", 
				MIN_FACTOR, MAX_FACTOR);
		
		// Key: word, Value: starting index
		Map<String, Integer> repeatingWords = new LinkedHashMap<String, Integer>();
		int totalWords = 0;
		
		String regex = String.format("(\\S{%d,%d})(?=.*?\\1)", MIN_FACTOR, MAX_FACTOR);
		Matcher m = Pattern.compile(regex).matcher(cipher);
		
		while (m.find())
		{
			if (!repeatingWords.containsKey(m.group()))
			{
				totalWords++;
				System.out.printf("Loading: %5.2f %% | Word #%d: %s\n", 
						Util.getPercentage(m.end(), cipher.length()),
						totalWords, m.group());
				
				// Key: word, Value: starting index
				repeatingWords.put(m.group(), m.start());
			}
		}
		System.out.printf("Found %d repeating words.\n\n", totalWords);
		return repeatingWords;
	}
	
	/**
	 * Calculates distances of repeating words
	 * 
	 * @param cipher			Cipher text
	 * @param repeatingWords	A map of repeating words returned by getRepeatingWords()
	 * @return					A map of distance to first repeating occurence for each word
	 */
	static Map<String, Integer> getDistances(String cipher, Map<String, Integer> repeatingWords)
	{
		// Key: word, Value: distance to first repeated occurrence
		Map<String, Integer> distances = new LinkedHashMap<String, Integer>();
		
		System.out.println("Calculating distance to first repeating occurrence for each word...");
		
		int wordNum = 0;
		for (Entry<String, Integer> entry : repeatingWords.entrySet())
		{
			wordNum++;
			int distance = cipher.indexOf(entry.getKey(), entry.getValue() + entry.getKey().length()) - entry.getValue();
			distances.put(entry.getKey(), distance);
			
			System.out.printf("Distance: %4d | #%d Word: %s\n", distance, wordNum, entry.getKey());
		}
		System.out.println();
		return distances;
	}
	
	/**
	 * Counts how many of the words are factorizable by MIN_FACTOR up to MAX_FACTOR
	 * 
	 * @param distances		A map of distances of words returned by getDistances()
	 * @return				An array of ints representing num of words factorizable by x
	 */
	static int[] factorizeDistances(Map<String, Integer> distances)
	{
		int[] factorCounter = new int[MAX_FACTOR + 1];
		
		System.out.println("Factorizing distances...");
		
		// For every distance
		for (Entry<String, Integer> entry : distances.entrySet())
		{
			// Count up if it's factorizable by i
			for (int i = MIN_FACTOR; i <= MAX_FACTOR; i++)
			{
				if (entry.getValue() % i == 0)
				{
					factorCounter[i]++;
				}
			}
		}
		return factorCounter;
	}
	
	/**
	 * Used to find the most congruent key length of a polyalphabetical cipher /Vigenere/
	 * 
	 * @param factorCounter	Array of ints representing  num of words factorizable by x
	 * @param totalWords	Total number of repeating words
	 * @return				The most congruent key length
	 */
	static int findKeyLength(int[] factorCounter, int totalWords)
	{
		Map<Integer, Integer> factorMap = new LinkedHashMap<Integer, Integer>();
		
		for (int i = MAX_FACTOR; i >= MIN_FACTOR; i--)
		{
			factorMap.put(i, factorCounter[i]);
		}
		
		System.out.println("Sorting factors in descending order...");
		factorMap = Util.sortMap(factorMap, true, false);
		
		for (Entry<Integer, Integer> entry : factorMap.entrySet())
		{
			System.out.printf("Num of words factorizable by %2d : %d (%.2f %%)\n", 
					entry.getKey(), entry.getValue(), Util.getPercentage(entry.getValue(), totalWords));
		}
		System.out.println();

		return (Integer) factorMap.keySet().toArray()[ 0 ];
	}
	
	/**
	 * Used to find the possible key lengths of a polyalphabetical cipher /Vigenere/
	 * 
	 * @param factorCounter	Array of ints representing  num of words factorizable by x
	 * @param totalWords	Total number of repeating words
	 * @return				An array of integers containing possible key lengths or null if couldn't find any
	 */
	static int[] findKeyLengths(int[] factorCounter, int totalWords)
	{
		Map<Integer, Integer> factorMap = new LinkedHashMap<Integer, Integer>();
		
		for (int i = MAX_FACTOR; i >= MIN_FACTOR; i--)
		{
			factorMap.put(i, factorCounter[i]);
		}
		
		System.out.println("Sorting factors in descending order...");
		factorMap = Util.sortMap(factorMap, true, false);
		
		// Initial guess
		float mostCongruent = 0.0f;
		int numOfPossibleLengths = 0;
		
		for (Entry<Integer, Integer> entry : factorMap.entrySet())
		{
			System.out.printf("Num of words factorizable by %2d : %d (%.2f %%)\n", 
					entry.getKey(), entry.getValue(), Util.getPercentage(entry.getValue(), totalWords));
			
			if (mostCongruent == 0.0f)
				mostCongruent = Util.getPercentage(entry.getValue(), totalWords);
			
			// Is this a possible key length?
			if (mostCongruent <= Util.getPercentage(entry.getValue(), totalWords) + KEY_TOLERANCE)
				numOfPossibleLengths++;
		}
		
		if (numOfPossibleLengths > 0)
		{
			int[] keyLengths = new int[numOfPossibleLengths];
			for (int i = 0; i < numOfPossibleLengths; i++)
			{
				keyLengths[i] = (Integer) factorMap.keySet().toArray()[ i ];
			}
			return keyLengths;
		}
		return null;
	}
	
	/**
	 * Performs a Kasiski test. Used to estimate the length of the unknown keyword.
	 * 
	 * @param 	Cipher text
	 * @return 	The most congruent key length
	 */
	static int test(String cipher)
	{
		Map<String, Integer> repeatingWords = getRepeatingWords(cipher);
		Map<String, Integer> distances = getDistances(cipher, repeatingWords);
		int[] factorCounter = factorizeDistances(distances);
		
		return findKeyLength(factorCounter, repeatingWords.size());
	}
}
