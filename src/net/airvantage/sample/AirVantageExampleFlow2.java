/**
 * Copyright (c) 2013 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * 		David Sciamma  - initial API and implementation
 */
package net.airvantage.sample;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Resource Owner Flow example
 * --------------
 * This basic main class shows how to connect to AirVantage API and make
 * authentified API calls using the OAuth Resource Owner flow
 * --------------
 * This example depends on the following libraries:
 * - Apache Commons IO 
 * - Apache HTTP Client and its dependencies (commons codec, commons logging)
 * - JSON.org
 */
public class AirVantageExampleFlow2 {

	public static void main(String[] args) {

		String apiUrl = "https://na.airvantage.net/api";
		// Replace these with your own api key and secret
		String apiKey = "your_app_id";
		String apiSecret = "your_api_secret";

		String login = null;
		String password = null;
		String access_token = null;

		Scanner in = new Scanner(System.in);

		System.out.println("=== AirVantage's OAuth Workflow ===");
		System.out.println();

		// Obtain User/Password
		System.out.println("Enter your login:");
		System.out.print(">>");
		login = in.nextLine();
		System.out.println();
		System.out.println("...and your password:");
		System.out.print(">>");
		password = in.nextLine();
		System.out.println();

		// Get the Access Token
		System.out.println("Getting the Access Token...");
		System.out.println(apiUrl
				+ "/oauth/token?grant_type=password&username=" + login
				+ "&password=" + password + "&client_id=" + apiKey
				+ "&client_secret=" + apiSecret);
		try {
			access_token = Request
					.Get(apiUrl + "/oauth/token?grant_type=password&username="
							+ login + "&password=" + password + "&client_id="
							+ apiKey + "&client_secret=" + apiSecret).execute()
					.handleResponse(new ResponseHandler<String>() {

						public String handleResponse(final HttpResponse response)
								throws IOException {
							StatusLine statusLine = response.getStatusLine();
							HttpEntity entity = response.getEntity();
							if (statusLine.getStatusCode() >= 300) {
								throw new HttpResponseException(statusLine
										.getStatusCode(), statusLine
										.getReasonPhrase());
							}
							if (entity == null) {
								throw new ClientProtocolException(
										"Response contains no content");
							}

							try {
								String content = IOUtils.toString(entity
										.getContent());
								JSONObject result = new JSONObject(content);
								return result.getString("access_token");
							} catch (JSONException e) {
								throw new ClientProtocolException(
										"Malformed JSON", e);
							}
						}

					});

			System.out.println("Got the Access Token!");
			System.out.println("(if you're curious it looks like this: "
					+ access_token + " )");
			System.out.println();

			// Now let's go and ask for a protected resource!
			System.out
					.println("Now we're going to get info about the current user...");

			JSONObject result = Request
					.Get(apiUrl + "/v1/users/current?access_token="
							+ access_token).execute()
					.handleResponse(new ResponseHandler<JSONObject>() {

						public JSONObject handleResponse(
								final HttpResponse response) throws IOException {
							StatusLine statusLine = response.getStatusLine();
							HttpEntity entity = response.getEntity();
							if (statusLine.getStatusCode() >= 300) {
								throw new HttpResponseException(statusLine
										.getStatusCode(), statusLine
										.getReasonPhrase());
							}
							if (entity == null) {
								throw new ClientProtocolException(
										"Response contains no content");
							}

							try {
								String content = IOUtils.toString(entity
										.getContent());
								return new JSONObject(content);
							} catch (JSONException e) {
								throw new ClientProtocolException(
										"Malformed JSON", e);
							}
						}

					});

			System.out.println("Got it! Let's see what we found...");
			System.out.println();
			System.out.println(result.toString());

			System.out.println();
			System.out
					.println("That's it man! Go and build something awesome with AirVantage! :)");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
