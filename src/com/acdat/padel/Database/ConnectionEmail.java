package com.acdat.padel.Database;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class ConnectionEmail {
	private URI uri;

	public ConnectionEmail(String host, String path, ArrayList<NameValuePair> params) {
		String query = null;
		
		if (params != null)
			query = URLEncodedUtils.format(params, HTTP.UTF_8);
			
		try {
			this.uri = URIUtils.createURI(
					"http",
					host, 
					80, 
					path, 
					query, 
					null);
		} catch (URISyntaxException e) {
			Log.e("Error", e.getMessage());
			this.uri = null;
		}
	}
	
	public InputStreamReader makePostRequest(){
		HttpClient client;
		HttpPost petition;
		HttpResponse response;
		InputStreamReader in = null;
		int r;
		
		if (uri != null)
			try {
				client = new DefaultHttpClient();
				petition = new HttpPost(uri);
				response = client.execute(petition);
				r = response.getStatusLine().getStatusCode();
				if ( r == HttpURLConnection.HTTP_OK)
					in = new InputStreamReader(response.getEntity().getContent());
				else {
					Log.e("Error", "Código de error: " + r);
				}			
			} catch (ClientProtocolException e) {
				Log.e("Error", e.getMessage());
			} catch (IOException e) {
				Log.e("Error", e.getMessage());
			}
		return in;		
	}

	public InputStreamReader makeGetRequest(){
		HttpClient client;
		HttpGet petition;
		HttpResponse response;
		InputStreamReader in = null;
		int r;
		
		if (uri != null)
			try {
				client = new DefaultHttpClient();
				petition = new HttpGet(uri);
				response = client.execute(petition);
				r = response.getStatusLine().getStatusCode();
				if ( r == HttpURLConnection.HTTP_OK)
					in = new InputStreamReader(response.getEntity().getContent());
				else {
					Log.e("Error", "Código de error: " + r);
				}			
			} catch (ClientProtocolException e) {
				Log.e("Error", e.getMessage());
			} catch (IOException e) {
				Log.e("Error", e.getMessage());
			}
		return in;		
	}
}
