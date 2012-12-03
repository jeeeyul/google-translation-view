package net.jeeeyul.eclipse.googledic.trimmer.internal;

import net.jeeeyul.eclipse.googledic.trimmer.ISourceTextTrimmer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class SourceTextTrimmerProxy implements ISourceTextTrimmer {
	private ISourceTextTrimmer implementation;
	private String name;

	public SourceTextTrimmerProxy(IConfigurationElement config) throws CoreException {
		super();
		this.name = config.getAttribute("name");
		this.implementation = (ISourceTextTrimmer) config.createExecutableExtension("class");
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String trim(String source) {
		return this.implementation.trim(source);
	}

}
