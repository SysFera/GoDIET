package com.sysfera.godiet.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {

	/**
	 * Convert an inpustream in string. Close the stream !
	 * 
	 * @param stream
	 * @return
	 * @throws IOException 
	 */
	public static String streamToString(InputStream stream) throws IOException {
		try {
			if(stream == null)
			{
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} finally {
			if(stream != null)
			{
				try {
					stream.close();
				} catch (IOException e) {
					
				}
			}
			
		}
	}
}
