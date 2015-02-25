package com.example.vivantsample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.vivantsample.data.Galleries;
import com.google.gson.Gson;

public class JsonLoader {

	public Galleries loadJsonData() {
		
		Galleries response = null;
		
		Gson gson = new Gson();
		String data = readUrl(Global.jsonUrl);
		
		if (data == null)
			return null;
		
		response = gson.fromJson(data, Galleries.class);

		return response;
	}
	
	public String readUrl(String urlString) {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1) {
	            buffer.append(chars, 0, read); 
	        System.out.println(new String(chars)); }

	        return buffer.toString();
	    } catch(Exception e) {
	    	return null;
	    } finally {
	    	try {
	        if (reader != null)
	            reader.close();
	    	} catch(Exception e) {}
	    }
	}
	
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		
		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(getClass().getSimpleName() +
						":Error " + statusCode + " for URL " + url);
				return null;
			}
			
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();
		}
		catch (Exception e) {
			getRequest.abort();
			System.out.println(getClass().getSimpleName() + ":Error for URL " + url);
		}
		return null;

	}
}
