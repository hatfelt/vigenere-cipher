/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A wrapper for utility functions used throughout cryptography tutorials.
 * 
 * @author Hristo Hristov
 */
class Util
{
	// The English alphabet consists of 26 letters
	static final int ALPHABET_SIZE = 26;
	
	/**
	 * Calculates a percentage
	 * @return (amount/total) * 100
	 */
	static float getPercentage(int amount, int total)
	{
		return (float) amount / (float) total * 100.0f;
	}
	
	/**
	 * A synonym for getPercentage();
	 */
	static float getFrequency(int amount, int total)
	{
		return getPercentage(amount, total);
	}
	
	/**
	 * @param letter	Letter to normalize
	 * @return			Normalized letter
	 */
	static int normalizeLetter(int letter)
	{
		while (letter < 'a')
		{
			letter += ALPHABET_SIZE;
		}
		while (letter > 'z')
		{
			letter -= ALPHABET_SIZE;
		}
		return letter;
	}
	
	/**
	 * Used to sort a map
	 * 
	 * @param unsortMap		An unsorted map
	 * @param byValue		If true, sorts by value, else sorts by key
	 * @param asc			If true, sorts in asc order, else sorts in desc
	 * @return
	 */
	static <K extends Comparable<K>, V extends Comparable<V>> 
			Map<K, V> sortMap(Map<K, V> unsortMap, final boolean byValue, final boolean asc)
	{
		List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<K, V>>()
		{
			public int compare(Entry<K, V> o1,
					Entry<K, V> o2)
			{
				if (asc) // Ascending
				{
					if (byValue)
					{
						return o1.getValue().compareTo(o2.getValue());
					}
					else // By key
					{
						return o1.getKey().compareTo(o2.getKey());
					}
				}
				else // Descending
				{
					if (byValue)
					{
						return o2.getValue().compareTo(o1.getValue());
					}
					else // By key
					{
						return o2.getKey().compareTo(o1.getKey());
					}
				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (Entry<K, V> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
