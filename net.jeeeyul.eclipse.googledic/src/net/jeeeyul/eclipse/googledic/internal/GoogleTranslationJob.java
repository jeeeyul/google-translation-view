package net.jeeeyul.eclipse.googledic.internal;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jeeeyul.eclipse.googledic.translate.TranslateURL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;

public class GoogleTranslationJob extends Job {
	private ITranslationTarget target;
	private HashSet<ITranslationListener> listeners = new HashSet<ITranslationListener>();

	public void addTranslationListener(ITranslationListener listener) {
		this.listeners.add(listener);
	}

	public void removeTranslationListener(ITranslationListener listener) {
		this.listeners.remove(listener);
	}

	public GoogleTranslationJob(ITranslationTarget targetLanguage) {
		super("Translating through Google"); // $NON-NLS-1$
		this.target = targetLanguage;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Translation", IProgressMonitor.UNKNOWN);

		try {
			HttpClient client = GoogleDicPlugin.getDefault().getSharedClient();

			TranslateURL request = new TranslateURL();
			request.setSourceLanguage(target.getSourceLanguage());
			request.setTargetLanguage(target.getTargetLanguage());
			request.setText(target.getTextToTranslate());

			ContentExchange exchange = new ContentExchange();
			exchange.setURL(request.getURL());
			client.send(exchange);
			exchange.waitForDone();
			String res = exchange.getResponseContent();

			if (res != null) {
				StringBuffer buffer = new StringBuffer();

				Matcher matcher = Pattern
						.compile(
								"\\[\\s*\"(([^\"]|\\\\\")*)\"\\s*(,\"(([^\"]|\\\\\")*)\"){3}\\s*\\]")
						.matcher(res);

				while (matcher.find()) {
					buffer.append(matcher.group(1));
					buffer.append(System.getProperty("line.separator"));
				}

				String result = buffer.toString();

				for (ITranslationListener each : listeners) {
					each.translationCompleted(result);
				}
			}

		} catch (Exception e) {
			return new Status(IStatus.ERROR, GoogleDicPlugin.PLUGIN_ID,
					e.getMessage(), e);
		}
		return Status.OK_STATUS;
	}

}
