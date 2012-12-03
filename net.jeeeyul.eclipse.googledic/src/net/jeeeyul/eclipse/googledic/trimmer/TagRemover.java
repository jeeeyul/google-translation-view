package net.jeeeyul.eclipse.googledic.trimmer;

import java.io.IOException;
import java.io.StringReader;

import net.jeeeyul.eclipse.googledic.internal.Logger;

public class TagRemover implements ISourceTextTrimmer {
	@Override
	public String trim(String source) {
		StringReader reader = new StringReader(source);
		StringBuffer result = new StringBuffer();
		try {
			int current;
			while ((current = reader.read()) != -1) {
				char c = (char) current;
				if (c == '<') {
					c = eatTag(reader);
				} else {
					result.append(c);
				}

			}
		} catch (Exception e) {
			Logger.log(e);
		}
		return result.toString();
	}

	private char eatTag(StringReader reader) throws IOException {
		int current;
		char c;
		LookUpClosingTag: while ((current = reader.read()) != -1) {
			c = (char) current;
			if (c == '>') {
				break LookUpClosingTag;
			}
		}
		return 0;
	}
}
