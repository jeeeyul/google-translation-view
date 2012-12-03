package net.jeeeyul.eclipse.googledic;

import org.eclipse.osgi.util.NLS;

/**
 * @since 1.1
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "net.jeeeyul.eclipse.googledic.messages"; //$NON-NLS-1$
	public static String GoogleDicView_2;
	public static String GoogleDicView_3;
	public static String GoogleDicView_5;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
