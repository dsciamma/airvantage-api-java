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

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class AirVantageApi extends DefaultApi20 {
	
	public static final String API_URL = "https://na.airvantage.net/api";
	
	private static final String AUTHORIZE_URL = API_URL + "/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";
	
	private static final String TOKEN_URL = API_URL + "/oauth/token?grant_type=authorization_code&redirect_uri=%s";

	private OAuthConfig _config;
	
	@Override
	public String getAccessTokenEndpoint() {
	    return String.format(TOKEN_URL, OAuthEncoder.encode(_config.getCallback()));
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
	    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. AirVantage does not support OOB");
	    _config = config;
	    return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(_config.getCallback()));
	}
	
	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

}
