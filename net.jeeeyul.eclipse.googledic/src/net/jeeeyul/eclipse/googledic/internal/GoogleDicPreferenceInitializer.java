package net.jeeeyul.eclipse.googledic.internal;

import java.util.Locale;

import net.jeeeyul.eclipse.googledic.IGoogleDicPreferenceConstants;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

public class GoogleDicPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = GoogleDicPlugin.getDefault().getPreferenceStore();
		store.setDefault(IGoogleDicPreferenceConstants.SOURCE_LANGUAGE, "");

		store.setDefault(IGoogleDicPreferenceConstants.TARGET_LANGUAGE, Locale.getDefault().getLanguage());
		store.setDefault(IGoogleDicPreferenceConstants.DIRECTION, SWT.VERTICAL);
		store.setDefault(IGoogleDicPreferenceConstants.SOURCE_RATIO, 50);
		store.setDefault(IGoogleDicPreferenceConstants.TRANSLATE_RATIO, 50);
	}
}
