package net.jeeeyul.eclipse.googledic.internal;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.jeeeyul.eclipse.googledic.IGoogleDicPreferenceConstants;
import net.jeeeyul.eclipse.googledic.ILanguageConstants;
import net.jeeeyul.eclipse.googledic.Messages;
import net.jeeeyul.eclipse.googledic.trimmer.internal.SourceTextTrimmerProxy;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class GoogleDicView extends ViewPart implements ITranslationTarget, ITranslationListener {
	private class LanguageSelectorListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			handleLanguageChanged();
			String s = getSourceDocument().get();
			getSourceDocument().set(s);
		}
	}

	private class SourceDocumentChangingListener implements IDocumentListener {
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {

		}

		@Override
		public void documentChanged(DocumentEvent event) {
			handleSourceTextChanged(GoogleDicView.this.sourceDocument.get());
		}
	}

	private class TextSelectionListener implements ISelectionListener {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				handleTextSelection(textSelection);
			}
		}

	}

	public static final String ID = "net.jeeeyul.eclipse.googledic.internal.GoogleDicView"; //$NON-NLS-1$

	private Composite composite;

	private Combo fromLanguageSelector;
	private LanguageSelectorListener languageSelectorListener;
	private IDocument sourceDocument;
	private SourceDocumentChangingListener sourceListener;
	private TextViewer sourceTextViewer;
	private SashForm textSash;
	private TextSelectionListener textSelectionListener;
	private Combo toLanguageSelector;
	private IDocument translatedDocument;
	private TextViewer translatedTextViewer;
	private GoogleTranslationJob translationJob;

	private GridLayout createNoSpacingGridLayout(int num, boolean same) {
		GridLayout layout = new GridLayout(num, same);
		layout.marginHeight = layout.marginWidth = 0;

		return layout;
	}

	@Override
	public void createPartControl(Composite parent) {
		hookTextSelection();

		this.composite = new Composite(parent, SWT.NORMAL);
		this.composite.setLayout(createNoSpacingGridLayout(1, false));

		Composite toolComposite = new Composite(this.composite, SWT.NORMAL);
		toolComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		createTools(toolComposite);
		this.textSash = createTextViewers(this.composite, getPreference().getInt(IGoogleDicPreferenceConstants.DIRECTION));

		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				saveRatio();
			}
		});
	}

	private SashForm createTextViewers(Composite composite, int style) {
		SashForm parent = new SashForm(composite, style);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		parent.setLayout(new FillLayout());

		this.sourceTextViewer = new TextViewer(parent, SWT.NORMAL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		this.sourceTextViewer.setDocument(getSourceDocument());
		this.sourceDocument.addDocumentListener(getSourceListener());

		this.translatedTextViewer = new TextViewer(parent, SWT.NORMAL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		this.translatedTextViewer.setDocument(getTranslatedDocument());

		int sourceRatio = getPreference().getInt(IGoogleDicPreferenceConstants.SOURCE_RATIO);
		int translateRatio = getPreference().getInt(IGoogleDicPreferenceConstants.TRANSLATE_RATIO);
		parent.setWeights(new int[] { sourceRatio, translateRatio });

		return parent;
	}

	private void createTools(Composite toolComposite) {
		toolComposite.setLayout(new GridLayout(4, false));

		new Label(toolComposite, SWT.NORMAL).setText(Messages.GoogleDicView_2);
		this.fromLanguageSelector = new Combo(toolComposite, SWT.NORMAL | SWT.READ_ONLY);

		new Label(toolComposite, SWT.NORMAL).setText(Messages.GoogleDicView_3);
		this.toLanguageSelector = new Combo(toolComposite, SWT.NORMAL | SWT.READ_ONLY);

		for (int i = 0; i < ILanguageConstants.LANGUAGE_DISPLAY_NAMES.length; i++) {
			String displayName = ""; //$NON-NLS-1$
			if (i == 0) {
				displayName = Messages.GoogleDicView_5;
				this.fromLanguageSelector.add(displayName);
			}

			else {
				Locale locale = null;
				String code = ILanguageConstants.LANGUAGE_CODES[i];
				if (code.contains("-")) { //$NON-NLS-1$
					String[] p = code.split("-"); //$NON-NLS-1$
					locale = new Locale(p[0], p[1]);
					displayName = MessageFormat.format("{0}({1})", locale.getDisplayLanguage(), locale.getDisplayCountry()); //$NON-NLS-1$
				} else {
					locale = new Locale(code);
					displayName = locale.getDisplayLanguage();
				}

				this.fromLanguageSelector.add(displayName);
				this.toLanguageSelector.add(displayName);
			}
		}

		String from = getPreference().getString(IGoogleDicPreferenceConstants.SOURCE_LANGUAGE);
		String to = getPreference().getString(IGoogleDicPreferenceConstants.TARGET_LANGUAGE);

		List<String> list = Arrays.asList(ILanguageConstants.LANGUAGE_CODES);
		int fromIndex = list.indexOf(from);
		int toIndex = list.indexOf(to);

		this.fromLanguageSelector.select(fromIndex);
		this.toLanguageSelector.select(toIndex - 1);

		this.fromLanguageSelector.addSelectionListener(getLanguageSelectorListener());
		this.toLanguageSelector.addSelectionListener(getLanguageSelectorListener());
	}

	@Override
	public void dispose() {
		unhookTextSelection();
		super.dispose();
	}

	public LanguageSelectorListener getLanguageSelectorListener() {
		if (this.languageSelectorListener == null) {
			this.languageSelectorListener = new LanguageSelectorListener();
		}
		return this.languageSelectorListener;
	}

	private IPreferenceStore getPreference() {
		return GoogleDicPlugin.getDefault().getPreferenceStore();
	}

	public IDocument getSourceDocument() {
		if (this.sourceDocument == null) {
			this.sourceDocument = new Document();
		}
		return this.sourceDocument;
	}

	@Override
	public String getSourceLanguage() {
		String from = getPreference().getString(IGoogleDicPreferenceConstants.SOURCE_LANGUAGE);
		return from;
	}

	public SourceDocumentChangingListener getSourceListener() {
		if (this.sourceListener == null) {
			this.sourceListener = new SourceDocumentChangingListener();
		}
		return this.sourceListener;
	}

	public TextViewer getSourceTextViewer() {
		return this.sourceTextViewer;
	}

	@Override
	public String getTargetLanguage() {
		String to = getPreference().getString(IGoogleDicPreferenceConstants.TARGET_LANGUAGE);
		return to;
	}

	public TextSelectionListener getTextSelectionListener() {
		if (this.textSelectionListener == null) {
			this.textSelectionListener = new TextSelectionListener();
		}
		return this.textSelectionListener;
	}

	@Override
	public String getTextToTranslate() {
		return this.sourceDocument.get();
	}

	public IDocument getTranslatedDocument() {
		if (this.translatedDocument == null) {
			this.translatedDocument = new Document();
		}
		return this.translatedDocument;
	}

	public GoogleTranslationJob getTranslationJob() {
		if (this.translationJob == null) {
			this.translationJob = new GoogleTranslationJob(this);
			this.translationJob.addTranslationListener(this);
		}
		return this.translationJob;
	}

	private ISelectionService getWorkbenchSelectionService() {
		return (ISelectionService) getSite().getWorkbenchWindow().getService(ISelectionService.class);
	}

	private void handleLanguageChanged() {
		String fromLang = ILanguageConstants.LANGUAGE_CODES[this.fromLanguageSelector.getSelectionIndex()];
		String toLang = ILanguageConstants.LANGUAGE_CODES[this.toLanguageSelector.getSelectionIndex() + 1];

		getPreference().setValue(IGoogleDicPreferenceConstants.SOURCE_LANGUAGE, fromLang);
		getPreference().setValue(IGoogleDicPreferenceConstants.TARGET_LANGUAGE, toLang);
	}

	private void handleSourceTextChanged(String text) {
		getTranslationJob().schedule(250);
	}

	private void handleTextSelection(ITextSelection textSelection) {
		String text = textSelection.getText();

		// bug 6456
		if (!isShowing()) {
			return;
		}

		/**
		 * 5801: AutoReport: - java.lang.NullPointerException
		 * http://118.129.100.135/bugzilla/show_bug.cgi?id=5801
		 */
		if (text == null || text.trim().length() == 0) {
			return;
		}

		text = trim(text);

		getSourceDocument().set(text);

		getTranslationJob().schedule(250);
	}

	private void hookTextSelection() {
		getWorkbenchSelectionService().addSelectionListener(getTextSelectionListener());
	}

	/**
	 * 구글딕뷰가 화면에 표시되고 있는지 여부를 리턴합니다.
	 * 
	 * bug 6456: 구글 딕 뷰가 화면에 보이지 않을 때도, 텍스트 셀렉션에 반응하며 번역을 시도 합니다.
	 * 
	 * @return 구글딕뷰가 화면에 표시되고 있는지 여부.
	 */
	private boolean isShowing() {
		if (this.sourceTextViewer == null) {
			return false;
		}

		StyledText textWidget = this.sourceTextViewer.getTextWidget();
		if (textWidget == null || textWidget.isDisposed()) {
			return false;
		}

		return textWidget.isVisible();
	}

	private void saveRatio() {
		int[] weight = this.textSash.getWeights();
		getPreference().setValue(IGoogleDicPreferenceConstants.SOURCE_RATIO, weight[0]);
		getPreference().setValue(IGoogleDicPreferenceConstants.TRANSLATE_RATIO, weight[1]);
	}

	public void setFocus() {
		getSourceTextViewer().getControl().setFocus();
	}

	public void toggleDirection() {
		int current = SWT.VERTICAL;
		if ((this.textSash.getStyle() & SWT.HORIZONTAL) != 0) {
			current = SWT.HORIZONTAL;
		}

		if (current == SWT.VERTICAL) {
			current = SWT.HORIZONTAL;
		} else {
			current = SWT.VERTICAL;
		}

		getPreference().setValue(IGoogleDicPreferenceConstants.DIRECTION, current);

		saveRatio();
		this.textSash.dispose();
		this.textSash = createTextViewers(this.composite, current);
		this.composite.layout();

	}

	@Override
	public void translationCompleted(final String result) {
		getSite().getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				try {
					getTranslatedDocument().set(result);
				} catch (Exception e) {
					return;
				}
			}
		});

	}

	private String trim(String text) {
		List<SourceTextTrimmerProxy> proxyList = GoogleDicPlugin.getDefault().getTrimmerConfiguration().getTrimmerProxyList();
		for (SourceTextTrimmerProxy eachTrimmer : proxyList) {
			try {
				text = eachTrimmer.trim(text);
			} catch (Exception e) {
				Logger.log(e);
			}
		}
		return text;
	}

	private void unhookTextSelection() {
		getWorkbenchSelectionService().removeSelectionListener(getTextSelectionListener());
	}
}
