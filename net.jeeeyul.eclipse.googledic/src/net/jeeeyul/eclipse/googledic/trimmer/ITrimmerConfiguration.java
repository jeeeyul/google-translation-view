package net.jeeeyul.eclipse.googledic.trimmer;

import java.util.List;

import net.jeeeyul.eclipse.googledic.trimmer.internal.SourceTextTrimmerProxy;


public interface ITrimmerConfiguration {

	public abstract List<SourceTextTrimmerProxy> getTrimmerProxyList();

	public abstract void dispose();

}