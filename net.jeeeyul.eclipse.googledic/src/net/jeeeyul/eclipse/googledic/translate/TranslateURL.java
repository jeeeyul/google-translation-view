package net.jeeeyul.eclipse.googledic.translate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.UrlEncoded;

public class TranslateURL {
	private Map<String, Object> parameters;

	public TranslateURL() {
		parameters = new HashMap<String, Object>();
		parameters.put("hl", "ko");
		parameters.put("ie", "UTF-8");
		parameters.put("oe", "UTF-8");
		parameters.put("multires", 0);
		parameters.put("sc", 0);
		parameters.put("client", "t");
		setSourceLanguage("auto");
	}

	public void setSourceLanguage(String sourceLanguage) {
		parameters.put("sl", sourceLanguage);
	}

	public void setTargetLanguage(String targetLanguage) {
		parameters.put("tl", targetLanguage);
	}

	public void setText(String text) {
		String trimmed = text.replaceAll("[\\s\\t\\r\\n]+", " ");
		parameters.put("text", trimmed);
	}

	public String getURL() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://translate.google.com/translate_a/t?");

		Iterator<String> keyIter = parameters.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			buffer.append(key);
			buffer.append("=");
			buffer.append(getEncoded(parameters.get(key)));
			if (keyIter.hasNext()) {
				buffer.append("&");
			}
		}

		return buffer.toString();
	}

	private String getEncoded(Object o) {
		if (o == null) {
			return "";
		} else {
			return UrlEncoded.encodeString(o.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		TranslateURL request = new TranslateURL();
		request.setText("Who is jeeeyul?");
		request.setTargetLanguage("ko");

		HttpClient client = new HttpClient();
		client.start();

		ContentExchange exchange = new ContentExchange();
		String url = request.getURL();
		System.out.println(url);
		exchange.setURL(url);

		client.send(exchange);
		exchange.waitForDone();
		System.out.println(exchange.getResponseContent());

		client.stop();

	}
}
