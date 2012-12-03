package net.jeeeyul.eclipse.googledic.trimmer;

import java.io.StringReader;

import net.jeeeyul.eclipse.googledic.internal.Logger;


public class CamelSplitter implements ISourceTextTrimmer {

	@Override
	public String trim(String source) {
		StringReader reader = new StringReader(source);
		StringBuffer result = new StringBuffer();

		int buf;
		Character c = null;
		Character last = null;
		try {
			while ((buf = reader.read()) != -1) {
				c = (char) buf;
				if (last != null && isLowerCase(last) && isUpperCase(c)) {
					result.append(" ");
				}
				result.append(c);
				last = c;
			}
		} catch (Exception e) {
			Logger.log(e);
		}

		return result.toString();
	}

	private boolean isLowerCase(char c) {
		return 'a' <= c && c <= 'z';
	}

	private boolean isUpperCase(char c) {
		return 'A' <= c && c <= 'Z';
	}

}
