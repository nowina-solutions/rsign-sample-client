/*
 * Copyright 2019 Nowina Solutions
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package lu.nowina.rsign.client;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RSignAuthorization implements ClientHttpRequestInterceptor {

	final String domain;

	final String user;

	final RSAPrivateKey privateKey;

	final ObjectMapper objectMapper = new ObjectMapper();

	public RSignAuthorization(String domain, String user, RSAPrivateKey privateKey) {
		this.domain = domain;
		this.user = user;
		this.privateKey = privateKey;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().add(HttpHeaders.AUTHORIZATION, generateAuthorizationHeader());
		return execution.execute(request, body);
	}

	private String generateAuthorizationHeader() throws IOException {

		final Map<String, String> headers = new HashMap<>();
		headers.put("typ", "JWT"); // Tokens are expected to be JWT tokens.
		headers.put("alg", "RS512"); // Algorithm is expected to be RS512.

		final String kid = domain + "/" + user;
		headers.put("kid", kid);

		final RsaSigner signer = new RsaSigner(privateKey, "SHA512withRSA"); // SHA512withRSA corresponds to RS512

		final int jwtMaxDuration = 15000;

		JWTPayload payload = new JWTPayload();
		payload.setExp((System.currentTimeMillis() + jwtMaxDuration) / 1000);
		payload.setIss(kid);
		payload.setJti(UUID.randomUUID().toString());

		final Jwt jwt = JwtHelper.encode(objectMapper.writeValueAsString(payload), signer, headers);

		return ("Bearer " + jwt.getEncoded());
	}

}
