/**
 * Package to encapsulate cryptography exercises
 */
package cryptography;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cryptography.Util;

/**
 * Represents an instance of plaintext.
 * Used to perform a frequency analysis on its contents.
 * 
 * @author Hristo Hristov
 */
class Text
{
	static final String SAMPLE_FILEPATH = "C:\\Users\\Ici\\Desktop\\pg1661.txt";
	
	// Used to count the number of occurrences for each letter
	private int[] m_iLetterCounter = new int[Util.ALPHABET_SIZE];
	private int m_iTotalLetters = 0;
	
	// Used to keep the contents of a text file
	private String m_strText;
	
	enum AnalysisType
	{
		Alphabetical,
		ByFrequency
	}
	
	/**
	 * Used to count the num of occurences for each letter in a string
	 * @param str	String of text to iterate over
	 */
	Text(String str)
	{
		// For each character in the string
		for (char ch : str.toCharArray())
		{
			// Skip if it's a non-alphabetical character
			if (ch < 'a' || ch > 'z')
				continue;
			
			// Count occurrence
			m_iLetterCounter[ch - 'a']++;
		}
		m_iTotalLetters = str.length();
	}
	
	/**
	 * Used to count the num of occurrences for each letter in a text file
	 * @param file			Text file to iterate over
	 * @param keepContents	Keep the contents of the file in a member string
	 */
	Text(File file, boolean keepContents)
	{
		try
		{
			System.out.printf("Opening \"%s\" for reading... ", file.getPath());
			if (keepContents) m_strText = new String();
			
			// Open text file for reading
			FileReader reader = new FileReader(file);
			
			// Go through every letter
			int ch;
			while ((ch = reader.read()) != -1)
			{
				ch = Character.toLowerCase(ch);
				
				// Don't care about non-alpha chars
				if (ch < 'a' || ch > 'z')
				{
					continue;
				}
				
				// Count occurrence of letter
				m_iLetterCounter[ch - 'a']++;
				
				if (keepContents) m_strText += Character.toString((char)ch);
			}
			
			System.out.println("finished reading file.");
			
			// Count the total number of letters
			int sum = 0;
			for (int i : m_iLetterCounter)
				sum += i;
			
			m_iTotalLetters = sum;
			reader.close();
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}
	
	/**
	 * Prints a frequency analysis
	 * @param type
	 */
	void printAnalysis(AnalysisType type)
	{
		if (m_iTotalLetters == 0)
		{
			System.out.println("Couldn't analyse. No letters were counted.");
			return;
		}
		
		switch (type)
		{
		case Alphabetical:
			System.out.println();
			System.out.println("Freq Analysis: Alphabetical");
			System.out.println("---------------------------");
			System.out.println("Letter | Count | Frequency");
			
			for (int i = 0; i < m_iLetterCounter.length; i++)
			{
				System.out.printf("%6c | %5d | %5.2f %%\n", 
						(char)(i + 'a'), m_iLetterCounter[i], 
						Util.getFrequency(m_iLetterCounter[i], m_iTotalLetters));
			}
			
			System.out.println();
			break;
		
		case ByFrequency:
			Map<Character, Integer> map = new LinkedHashMap<Character, Integer>();
			
			for (int i = 0; i < m_iLetterCounter.length; i++)
			{
				map.put((char)(i + 'a'), m_iLetterCounter[i]);
			}
			map = Util.sortMap(map, true, false);
			
			System.out.println();
			System.out.println("Freq Analysis: Sorted by %");
			System.out.println("---------------------------");
			System.out.println("Letter | Count | Frequency");
			
			for (Entry<Character, Integer> entry : map.entrySet())
			{
				System.out.printf("%6c | %5d | %5.2f %%\n", 
						entry.getKey(), entry.getValue(), 
						Util.getFrequency(entry.getValue(), m_iTotalLetters));
			}
			System.out.println();
			break;
		}
	}
	
	/**
	 * Returns the num of occurrences of a given letter
	 * @param x		Letter
	 * @return		Num of occurrences counted
	 */
	int getLetterCount(int x)
	{
		return m_iLetterCounter[ x ];
	}
	
	/**
	 * @return Total number of letters counted
	 */
	int getTotalLetters()
	{
		return m_iTotalLetters;
	}
	
	/**
	 * @return The string of text collected during file read
	 */
	String getText()
	{
		return m_strText;
	}
	
	/**
	 * @return	The index of coincidence
	 */
	float getIndexOfCoincidence()
	{
		float ioc = 0.0f;
		
		for (int i = 0; i < m_iLetterCounter.length; i++)
		{
			ioc += Math.pow(Util.getFrequency(m_iLetterCounter[i], m_iTotalLetters) / 100.0f, 2.0);
		}
		
		return ioc;
	}
}
