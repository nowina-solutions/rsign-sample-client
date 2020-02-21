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

public class SignatureAppearance {

	private int page;

	private float x, y;

	private String signatureTextBackColor, signatureTextFont, signatureText, signatureTextColor;
	
	public SignatureAppearance() {
		super();
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getSignatureTextBackColor() {
		return signatureTextBackColor;
	}

	public void setSignatureTextBackColor(String signatureTextBackColor) {
		this.signatureTextBackColor = signatureTextBackColor;
	}

	public String getSignatureTextFont() {
		return signatureTextFont;
	}

	public void setSignatureTextFont(String signatureTextFont) {
		this.signatureTextFont = signatureTextFont;
	}

	public String getSignatureText() {
		return signatureText;
	}

	public void setSignatureText(String signatureText) {
		this.signatureText = signatureText;
	}

	public String getSignatureTextColor() {
		return signatureTextColor;
	}

	public void setSignatureTextColor(String signatureTextColor) {
		this.signatureTextColor = signatureTextColor;
	}

}
