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

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RSignErrorHandler extends DefaultResponseErrorHandler {

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
			return false;
		}
		return super.hasError(response);
	}
	
	@Override
	protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
		Error error = objectMapper.readValue(response.getBody(), Error.class);
		throw new RSignException(error);
	}
	
}
