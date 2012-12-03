package net.jeeeyul.eclipse.googledic.internal;

import java.util.Map;

import net.jeeeyul.eclipse.googledic.IGoogleDicPreferenceConstants;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

public class ToggleDirectionHandler extends AbstractHandler implements
		IElementUpdater {
	public static final String COMMAND_ID = "net.jeeeyul.eclipse.googledic.toggleDirection";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (!(part instanceof GoogleDicView)) {
			return null;
		}

		GoogleDicView view = (GoogleDicView) part;
		view.toggleDirection();

		ICommandService service = (ICommandService) PlatformUI.getWorkbench()
				.getService(ICommandService.class);
		service.refreshElements(COMMAND_ID, null);

		return null;
	}

	@Override
	public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
		int style = GoogleDicPlugin.getDefault().getPreferenceStore().getInt(IGoogleDicPreferenceConstants.DIRECTION);
		element.setChecked(style == SWT.HORIZONTAL);
	}
}
