package net.jeeeyul.eclipse.googledic.trimmer.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import net.jeeeyul.eclipse.googledic.IExtensionPointConstants;
import net.jeeeyul.eclipse.googledic.IGoogleDicPreferenceConstants;
import net.jeeeyul.eclipse.googledic.internal.GoogleDicPlugin;
import net.jeeeyul.eclipse.googledic.trimmer.ITrimmerConfiguration;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class TrimmerConfiguration implements IPropertyChangeListener, ITrimmerConfiguration {
	private IPreferenceStore preferenceStore;

	private List<SourceTextTrimmerProxy> trimmerProxyList;

	public List<SourceTextTrimmerProxy> getTrimmerProxyList() {
		if (this.trimmerProxyList == null) {
			rebuildTrimmerProxyList();
		}
		return this.trimmerProxyList;
	}

	public TrimmerConfiguration() {
		this.preferenceStore = GoogleDicPlugin.getDefault().getPreferenceStore();
		this.preferenceStore.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(IGoogleDicPreferenceConstants.EXCLUDE_TRIMMER))
			rebuildTrimmerProxyList();
	}

	private void rebuildTrimmerProxyList() {
		List<String> excludes = Arrays.asList(this.preferenceStore.getString(
				IGoogleDicPreferenceConstants.EXCLUDE_TRIMMER).split(","));

		this.trimmerProxyList = new ArrayList<SourceTextTrimmerProxy>();
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(
				IExtensionPointConstants.ID_TRIMMER_EXT_PT);

		IConfigurationElement[] configs = point.getConfigurationElements();
		EachConfig: for (IConfigurationElement eachConfig : configs) {
			try {
				String name = eachConfig.getAttribute(IExtensionPointConstants.ATTR_TRIMMER_NAME);
				if (excludes.indexOf(name) != -1) {
					continue EachConfig;
				}

				SourceTextTrimmerProxy proxy = new SourceTextTrimmerProxy(eachConfig);
				this.trimmerProxyList.add(proxy);
			} catch (Exception e) {
				GoogleDicPlugin.getDefault().getLog().log(
						new Status(IStatus.ERROR, GoogleDicPlugin.PLUGIN_ID, e.getMessage(), e));
			}
		}
	}

	public void dispose() {
		this.preferenceStore.removePropertyChangeListener(this);
	}
}
