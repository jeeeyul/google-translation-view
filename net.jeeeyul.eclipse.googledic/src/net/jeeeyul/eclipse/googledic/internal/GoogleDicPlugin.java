package net.jeeeyul.eclipse.googledic.internal;

import net.jeeeyul.eclipse.googledic.trimmer.ITrimmerConfiguration;
import net.jeeeyul.eclipse.googledic.trimmer.internal.TrimmerConfiguration;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GoogleDicPlugin extends AbstractUIPlugin {

	// The shared instance
	private static GoogleDicPlugin plugin;

	// The plug-in ID
	public static final String PLUGIN_ID = "net.jeeeyul.eclipse.googledic";

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static GoogleDicPlugin getDefault() {
		return GoogleDicPlugin.plugin;
	}

	private HttpClient sharedClient;

	ITrimmerConfiguration trimmerConfiguration;

	/**
	 * The constructor
	 */
	public GoogleDicPlugin() {
	}

	public HttpClient getSharedClient() throws Exception {
		if (sharedClient == null) {
			sharedClient = new HttpClient();
			sharedClient.start();
		}
		return sharedClient;
	}

	public ITrimmerConfiguration getTrimmerConfiguration() {
		if (this.trimmerConfiguration == null) {
			this.trimmerConfiguration = new TrimmerConfiguration();
		}
		return this.trimmerConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		GoogleDicPlugin.plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.trimmerConfiguration != null) {
			this.trimmerConfiguration.dispose();
		}

		if (sharedClient != null) {
			if (!sharedClient.isStopped() && !sharedClient.isStopping()) {
				sharedClient.stop();
			}
		}

		GoogleDicPlugin.plugin = null;
		super.stop(context);
	}

}
