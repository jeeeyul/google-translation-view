package net.jeeeyul.eclipse.googledic.trimmer.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jeeeyul.eclipse.googledic.IExtensionPointConstants;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

public class TextSelectionTrimmerContributionItem extends
		CompoundContributionItem implements IWorkbenchContribution {
	private static final String COMMAND_ID = "net.jeeeyul.eclipse.googledic.toggleTextSelectionTrimmer";

	private IServiceLocator serviceLocator;

	@Override
	protected IContributionItem[] getContributionItems() {
		List<IContributionItem> result = new ArrayList<IContributionItem>();

		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(IExtensionPointConstants.ID_TRIMMER_EXT_PT);
		IConfigurationElement[] configs = point.getConfigurationElements();

		for (IConfigurationElement eachConfig : configs) {
			String name = eachConfig.getAttribute("name");

			HashMap<String, String> commandParams = new HashMap<String, String>();
			commandParams.put("trimmerName", name);

			CommandContributionItemParameter param = new CommandContributionItemParameter(
					serviceLocator, null, COMMAND_ID, SWT.CHECK);
			CommandContributionItem item = new CommandContributionItem(param);
			result.add(item);
		}

		return result.toArray(new IContributionItem[result.size()]);
	}

	@Override
	public void initialize(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

}
