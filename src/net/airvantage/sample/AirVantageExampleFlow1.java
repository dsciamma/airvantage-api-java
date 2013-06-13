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

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Authorization Code Flow example
 * --------------
 * This basic main class shows how to connect to AirVantage API and make
 * authentified API calls using the OAuth Authorization Code flow
 * --------------
 * This example depends on the following libraries:
 * - Scribe 
 */
public class AirVantageExampleFlow1 {

	private static final Token EMPTY_TOKEN = null;

	public static void main(String[] args) {
		
		// Replace these with your own api key and secret
		String apiKey = "your_app_id";
		String apiSecret = "your_api_secret";
		
		OAuthService service = new ServiceBuilder().provider(AirVantageApi.class)
				.apiKey(apiKey).apiSecret(apiSecret)
				.callback("http://www.example.com/oauth_callback/").build();
		
		Scanner in = new Scanner(System.in);

		System.out.println("=== AirVantage's OAuth Workflow ===");
		System.out.println();

		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if you're curious it looks like this: "
				+ accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to get info about the current user...");
		OAuthRequest request = new OAuthRequest(Verb.GET,
				AirVantageApi.API_URL + "/v1/users/current");
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Let's see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out
				.println("That's it man! Go and build something awesome with AirVantage! :)");

	}
}
