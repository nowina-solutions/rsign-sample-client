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

import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

public class RSignClient {

	private final RestTemplate restTemplate;
	
	private final String baseUrl;

	public RSignClient(String baseUrl, String domain, String user, RSAPrivateKey privateKey) {
		this.baseUrl = baseUrl;

		RestTemplate template = new RestTemplate();
		template.setErrorHandler(new RSignErrorHandler(false));
		template.setInterceptors(Arrays.asList(new RSignAuthorization(domain, user, privateKey)));

		this.restTemplate = template;
	}

	private UriBuilder apiUrl() {
		return new DefaultUriBuilderFactory(this.baseUrl).builder();
	}

	public Optional<User> searchUserByEmail(String email) {

		RequestEntity<Void> req = RequestEntity
				.get(apiUrl().path("/users/search/email").queryParam("email", email).build()).build();

		ResponseEntity<User> response = restTemplate.exchange(req, User.class);
		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			return Optional.empty();
		} else {
			User user = response.getBody();
			return Optional.of(user);
		}
	}

	public void createDocumentGroupTranslation(String key, String language, String value) {

		Translation tr = new Translation();
		tr.setLocale(language);
		tr.setValue(value);

		RequestEntity<List<Translation>> req = RequestEntity
				.post(apiUrl().path("/document-groups/translations").queryParam("i18nKey", key).build())
				.body(Arrays.asList(tr));

		restTemplate.exchange(req, Void.class);

	}

	public void createDocumentTranslation(String key, String language, String value) {

		Translation tr = new Translation();
		tr.setLocale(language);
		tr.setValue(value);

		RequestEntity<List<Translation>> req = RequestEntity
				.post(apiUrl().path("/documents/translations").queryParam("i18nKey", key).build())
				.body(Arrays.asList(tr));

		restTemplate.exchange(req, Void.class);

	}

	public Optional<DocumentGroup> getDocumentGroup(String groupId) {

		RequestEntity<Void> req = RequestEntity.get(apiUrl().path("/document-groups/").path(groupId).build()).build();

		ResponseEntity<DocumentGroup> response = restTemplate.exchange(req, DocumentGroup.class);
		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			return Optional.empty();
		} else {
			DocumentGroup group = response.getBody();
			return Optional.of(group);
		}
	}
	
	public Optional<byte[]> getDocumentContent(String documentId) {
		
		RequestEntity<Void> req = RequestEntity.get(apiUrl().path("/documents/").path(documentId).path("/content").build()).build();
		ResponseEntity<byte[]> response = restTemplate.exchange(req, byte[].class);
		if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
			return Optional.empty();
		} else {
			byte[] data = response.getBody();
			return Optional.of(data);
		}
	}

	public String createDocumentGroup(User userInfo, String title, byte[] pdfContent, Optional<SignatureAppearance> info) {

		User user = searchUserByEmail(userInfo.getEmail()).orElseGet(() -> {

			User newUser = new User();
			newUser.setEmail(userInfo.getEmail());
			newUser.setPhoneNumber(userInfo.getPhoneNumber());
			newUser.setFirstName(userInfo.getFirstName());
			newUser.setLastName(userInfo.getLastName());

			RequestEntity<User> req = RequestEntity.post(apiUrl().path("/users/signatories").build()).body(newUser);

			restTemplate.exchange(req, Void.class);

			return newUser;
		});

		String uuid = UUID.randomUUID().toString();

		String contentRef = uploadDocumentContent(uuid, pdfContent);

		createDocumentGroupTranslation(uuid, "", title);
		createDocumentGroup(uuid);
		createDocumentTranslation(uuid, "", title);
		createDocument(uuid, user.getId(), contentRef, info);

		lockDocumentGroup(uuid);

		return uuid;
	}

	private void lockDocumentGroup(String uuid) {
		RequestEntity<Void> req = RequestEntity
				.post(apiUrl().path("/document-groups/").path(uuid).path("/lock").build()).build();

		restTemplate.exchange(req, Void.class);
	}

	private String uploadDocumentContent(String uuid, byte[] pdfContent) {
		RequestEntity<byte[]> req = RequestEntity.post(apiUrl().path("/documents/content").build())
				.contentType(MediaType.APPLICATION_PDF).body(pdfContent);

		String result = restTemplate.exchange(req, String.class).getBody();
		return result.substring(1, result.length() -1);
	}

	private void createDocument(String uuid, String signatoryUserId, String contentRef, Optional<SignatureAppearance> info) {
		Document doc = new Document();
		doc.setExternalId(uuid);
		doc.setI18nKey(uuid);
		doc.setAllowedSignatureModes(Arrays.asList("SMS"));
		doc.setContentReference(contentRef);

		Signature signature = new Signature();
		signature.setUserId(signatoryUserId);
		
		info.ifPresent(i -> {
			signature.setSignatureInfos(Arrays.asList(i));
		});
		
		doc.setSignatories(Arrays.asList(signature));

		RequestEntity<Document> req = RequestEntity
				.post(apiUrl().path("/documents").queryParam("documentGroupExternalId", uuid).build()).body(doc);
		restTemplate.exchange(req, Void.class);

	}

	private void createDocumentGroup(String uuid) {
		DocumentGroup group = new DocumentGroup();
		group.setCallback("https://www.google.com");
		group.setExternalId(uuid);
		group.setI18nKey(uuid);

		RequestEntity<DocumentGroup> req = RequestEntity.post(apiUrl().path("/document-groups").build()).body(group);
		restTemplate.exchange(req, Void.class);
	}

}
