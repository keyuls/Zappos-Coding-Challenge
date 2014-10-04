package com.zappos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonConnection {
	public static final String BASEURL = "http://api.zappos.com/Search?key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";

	public static String httpPost(String urlStr) throws IOException {
		
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
		if (conn.getResponseCode() != 200) 
		{
		    throw new IOException(conn.getResponseMessage());
		}
	
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) 
		{
			sb.append(line);
		}
		br.close();
	
		conn.disconnect();
		return sb.toString();
	}
	
	public static JSONObject parseReply(String reply) throws ParseException
	{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(reply);
		JSONObject object = (JSONObject)obj;
		return object;
	}

	public static JSONArray getResults(JSONObject reply)
	{
		Object resultObject = reply.get("results");
		JSONArray resultArray = (JSONArray)resultObject;
		return resultArray;
	}
	
}
