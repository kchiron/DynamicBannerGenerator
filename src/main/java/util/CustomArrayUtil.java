package util;

import java.util.Arrays;

public class CustomArrayUtil {
	public static String[] mergeArray(String[] array1, String[] array2) {
		String[] merged = new String[array1.length+array2.length];
		
		for(int i = 0; i < array1.length; i++)
			merged[i] = array1[i];
		
		for(int i = array1.length; i < array1.length+array2.length; i++)
			merged[i] = array2[i-array1.length];
		
		return merged;
	}
}
