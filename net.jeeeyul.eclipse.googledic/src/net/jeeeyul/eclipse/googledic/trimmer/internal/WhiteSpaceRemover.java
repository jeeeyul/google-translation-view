package net.jeeeyul.eclipse.googledic.trimmer.internal;

import java.io.StringReader;

import net.jeeeyul.eclipse.googledic.internal.Logger;
import net.jeeeyul.eclipse.googledic.trimmer.ISourceTextTrimmer;


public class WhiteSpaceRemover implements ISourceTextTrimmer {

	@Override
	public String trim(String source) {
		StringReader reader = new StringReader(source);
		StringBuffer result = new StringBuffer();

		Character last = null;
		Character current;
		int buf;

		try {
			while ((buf = reader.read()) != -1) {
				current = (char) buf;
				if (last != null) {
					if (isWhiteSpace(last) && isWhiteSpace(current)) {
						continue;
					}
				}

				result.append(current);
				last = current;
			}
		} catch (Exception e) {
			Logger.log(e);
		}

		return result.toString().trim();
	}

	private boolean isWhiteSpace(Character last) {
		return last == ' ' || last == '\t' || last == '\r' || last == '\n';
	}

}
