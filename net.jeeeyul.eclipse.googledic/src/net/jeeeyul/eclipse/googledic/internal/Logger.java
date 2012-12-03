package net.jeeeyul.eclipse.googledic.internal;



import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Logger {
	public static void log(Exception e) {
		GoogleDicPlugin.getDefault().getLog().log(
				new Status(IStatus.ERROR, GoogleDicPlugin.PLUGIN_ID, e.getMessage(), e));
	}
}
