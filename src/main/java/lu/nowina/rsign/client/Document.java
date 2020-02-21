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

import java.util.List;

public class Document {

	private String externalId;
	private String i18nKey;
	private List<Signature> signatories;
	private List<String> allowedSignatureModes;
	private String contentReference;

	public Document() {
		super();
	}
	
	@Override
	public String toString() {
		return "Document [externalId=" + externalId + ", i18nKey=" + i18nKey + ", signatories=" + signatories
				+ ", allowedSignatureModes=" + allowedSignatureModes + ", contentReference=" + contentReference + "]";
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getI18nKey() {
		return i18nKey;
	}

	public void setI18nKey(String i18nKey) {
		this.i18nKey = i18nKey;
	}

	public List<Signature> getSignatories() {
		return signatories;
	}

	public void setSignatories(List<Signature> signatories) {
		this.signatories = signatories;
	}

	public List<String> getAllowedSignatureModes() {
		return allowedSignatureModes;
	}

	public void setAllowedSignatureModes(List<String> allowedSignatureModes) {
		this.allowedSignatureModes = allowedSignatureModes;
	}

	public String getContentReference() {
		return contentReference;
	}

	public void setContentReference(String contentReference) {
		this.contentReference = contentReference;
	}

}
