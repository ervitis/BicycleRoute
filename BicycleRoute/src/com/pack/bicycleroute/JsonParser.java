package com.pack.bicycleroute;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
	public static JSONObject getJSONfromURL(String url){
		InputStream inputStream = null;
		JSONObject jsonArray = null;
		String result;
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
		} catch (Exception ex){
			Log.e("HttpConnection", ex.getMessage());
			return null;
		}
		
		try{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			
			while ( (line = bufferedReader.readLine()) != null ){
				stringBuilder.append(line + "\n");
			}
			inputStream.close();
			result = stringBuilder.toString();
		} catch (Exception ex){
			Log.e("stringBuilder", ex.getMessage());
			return null;
		}
		
		try{
			jsonArray = new JSONObject(result);
		} catch (Exception ex){
			Log.e("jsonArray", ex.getMessage());
			return null;
		}
		
		return jsonArray;
	}
	
	public static InputStream retrieveStream(String url){
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		
		try{
			HttpResponse response = client.execute(httpGet);
			final int statusCode = response.getStatusLine().getStatusCode();
			
			if ( statusCode != HttpStatus.SC_OK ){
				return null;
			}
			
			HttpEntity getResponseEntity = response.getEntity();
			return getResponseEntity.getContent();
		} catch (Exception ex){
			httpGet.abort();
		}
		
		return null;
	}
}
