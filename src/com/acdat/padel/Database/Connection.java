package com.acdat.padel.Database;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Connection {
	private URI uri;
	public static final int NETWORK_OK = HttpURLConnection.HTTP_OK;
	public static final int NETWORK_NOT_COMPLETED= HttpURLConnection.HTTP_ACCEPTED;
	public static final int NETWORK_ERROR= 400;
	public static final int NETWORK_NOT_FOUND = HttpURLConnection.HTTP_NOT_FOUND;
	public static final int NETWORK_CONFLICT = HttpURLConnection.HTTP_CONFLICT;
	
	public Connection(String host, String path) {
		try {
			this.uri = URIUtils.createURI("http", host, 80, path, null, null);
		} catch (URISyntaxException e) {
			Log.e("Error", e.getMessage());
		}
	}

	public InputStreamReader makePostRequest(String dato)
			throws ClientProtocolException, IOException {
		HttpClient client = null;
		HttpPost petition;
		HttpResponse response;
		InputStreamReader in = null;

		if (uri != null) {
			client = new DefaultHttpClient();
			petition = new HttpPost(uri);
			StringEntity entity = new StringEntity(dato);
			petition.setEntity(entity);
			response = client.execute(petition);
			if (response.getStatusLine().getStatusCode() == NETWORK_OK)
				in = new InputStreamReader(response.getEntity().getContent());
		}

		return in;
	}

	public InputStreamReader makeGetRequest()
			throws ClientProtocolException, IOException {
		HttpClient client;
		HttpGet petition;
		HttpResponse response;
		InputStreamReader in = null;

		if (uri != null) {
			client = new DefaultHttpClient();
			petition = new HttpGet(uri);
			response = client.execute(petition);
			if (response.getStatusLine().getStatusCode() == NETWORK_OK)
				in = new InputStreamReader(response.getEntity().getContent());
		}

		return in;
	}

	public InputStreamReader makePutRequest(String text)
			throws ClientProtocolException, IOException {
		HttpClient client = null;
		HttpPut petition;
		HttpResponse response;
		InputStreamReader in = null;

		if (uri != null) {
			client = new DefaultHttpClient();
			petition = new HttpPut(uri);
			StringEntity entity = new StringEntity(text);
			petition.setEntity(entity);
			response = client.execute(petition);
			if (response.getStatusLine().getStatusCode() == NETWORK_OK)
				in = new InputStreamReader(response.getEntity().getContent());
		}

		return in;
	}

	public InputStreamReader makeDeleteRequest()
			throws ClientProtocolException, IOException {
		HttpClient client = null;
		HttpDelete petition;
		HttpResponse response;
		InputStreamReader in = null;

		if (uri != null) {
			client = new DefaultHttpClient();
			petition = new HttpDelete(uri);
			Log.i("info", uri.toString());
			response = client.execute(petition);
			if (response.getStatusLine().getStatusCode() == NETWORK_OK)
				in = new InputStreamReader(response.getEntity().getContent());
		}
		return in;
	}
}
