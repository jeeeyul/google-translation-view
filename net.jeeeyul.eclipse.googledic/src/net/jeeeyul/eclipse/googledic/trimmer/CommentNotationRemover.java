package net.jeeeyul.eclipse.googledic.trimmer;

import java.io.StringReader;

import net.jeeeyul.eclipse.googledic.internal.Logger;


public class CommentNotationRemover implements ISourceTextTrimmer {

	@Override
	public String trim(String source) {
		StringReader reader = new StringReader(source);
		StringBuffer result = new StringBuffer();

		int buf;
		Character c = null;
		try {
			while ((buf = reader.read()) != -1) {
				c = (char) buf;
				if (c == '/' || c == '*') {
					continue;
				}
				result.append(c);
			}
		} catch (Exception e) {
			Logger.log(e);
		}

		return result.toString();
	}

}
