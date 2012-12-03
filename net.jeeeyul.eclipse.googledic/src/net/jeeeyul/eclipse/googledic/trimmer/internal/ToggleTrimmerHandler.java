package net.jeeeyul.eclipse.googledic.trimmer.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jeeeyul.eclipse.googledic.IGoogleDicPreferenceConstants;
import net.jeeeyul.eclipse.googledic.internal.GoogleDicPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

public class ToggleTrimmerHandler extends AbstractHandler implements
		IElementUpdater {

	@Override
	public void updateElement(UIElement element,
			@SuppressWarnings("rawtypes") Map parameters) {
		String trimmerName = (String) parameters.get("trimmerName");
		List<String> list = getExcludeList();
		element.setChecked(!list.contains(trimmerName));
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String trimmerName = event.getParameter("trimmerName");
		List<String> list = getExcludeList();

		if (list.contains(trimmerName)) {
			list.remove(trimmerName);
		} else {
			list.add(trimmerName);
		}

		GoogleDicPlugin
				.getDefault()
				.getPreferenceStore()
				.setValue(IGoogleDicPreferenceConstants.EXCLUDE_TRIMMER,
						join(list, ","));

		return null;
	}

	private List<String> getExcludeList() {
		String exString = GoogleDicPlugin.getDefault().getPreferenceStore()
				.getString(IGoogleDicPreferenceConstants.EXCLUDE_TRIMMER);

		ArrayList<String> result = new ArrayList<String>();
		result.addAll(Arrays.asList(exString.split(",")));
		return result;
	}

	private String join(Collection<String> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}
}
