/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.text.java;

import java.util.StringTokenizer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;

import org.eclipse.jdt.core.IJavaProject;

import org.eclipse.jdt.internal.ui.text.correction.PreviewFeaturesSubProcessor;

public class JavaMultiLineStringAutoIndentStrategy extends JavaStringAutoIndentStrategy {

	public JavaMultiLineStringAutoIndentStrategy(String partitioning, IJavaProject project) {
		super(partitioning, project);
	}

	private void javaMultiLineStringIndentAfterNewLine(IDocument document, DocumentCommand command) throws BadLocationException {

		ITypedRegion partition= TextUtilities.getPartition(document, fPartitioning, command.offset, true);
		int offset= partition.getOffset();
		int length= partition.getLength();

		if (command.offset == offset + length && document.getChar(offset + length - 1) == '\"')
			return;

		String indentation= getLineIndentation(document, command.offset);
		String delimiter= TextUtilities.getDefaultLineDelimiter(document);

		IRegion line= document.getLineInformationOfOffset(offset);
		String fullStr= document.get(line.getOffset(), line.getLength()).trim();
		String fullTextBlockText= document.get(offset, length).trim();
		boolean hasTextBlockEnded= PreviewFeaturesSubProcessor.isPreviewFeatureEnabled(fProject) && fullTextBlockText.endsWith("\"\"\""); //$NON-NLS-1$
		boolean isTextBlock= PreviewFeaturesSubProcessor.isPreviewFeatureEnabled(fProject) && fullStr.endsWith("\"\""); //$NON-NLS-1$
		boolean isLineDelimiter= isLineDelimiter(document, command.text);
		if (isEditorWrapStrings() && isLineDelimiter && isTextBlock) {
			if (isTextBlock) {
				if (hasTextBlockEnded) {
					indentation= getLineIndentation(document, command.offset);
					command.text= command.text + indentation;
				} else {
					indentation= getLineIndentation(document, offset);
					indentation+= getExtraIndentAfterNewLine();
					command.text= command.text + indentation + "\"\"\";"; //$NON-NLS-1$
				}
			} else {
				command.text= command.text + indentation;
			}
		} else if (command.text.length() > 1 && !isLineDelimiter && isEditorEscapeStrings()) {
			command.text= getModifiedText(command.text, indentation, delimiter, isEditorEscapeStringsNonAscii());
		}
	}

	/**
	 * The input string will contain line delimiter.
	 *
	 * @param inputString the given input string
	 * @param indentation the indentation
	 * @param delimiter the line delimiter
	 * @return the display string
	 */
	@Override
	protected String displayString(String inputString, String indentation, String delimiter, boolean escapeNonAscii) {

		int length= inputString.length();
		StringBuilder buffer= new StringBuilder(length);
		StringTokenizer tokenizer= new StringTokenizer(inputString, "\n\r", true); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String token= tokenizer.nextToken();
			if (token.equals("\r")) { //$NON-NLS-1$
				buffer.append('\r');
				if (tokenizer.hasMoreTokens()) {
					token= tokenizer.nextToken();
					if (token.equals("\n")) { //$NON-NLS-1$
						buffer.append('\n');
						continue;
					}
				} else {
					continue;
				}
			} else if (token.equals("\n")) { //$NON-NLS-1$
				buffer.append('\n');
				continue;
			}

			StringBuilder tokenBuffer= new StringBuilder();
			for (int i= 0; i < token.length(); i++) {
				char c= token.charAt(i);
				switch (c) {
					default:
						if (escapeNonAscii && (c < 0x20 || c >= 0x80)) {
							String hex= "0123456789ABCDEF"; //$NON-NLS-1$
							tokenBuffer.append('\\');
							tokenBuffer.append('u');
							tokenBuffer.append(hex.charAt((c >> 12) & 0xF));
							tokenBuffer.append(hex.charAt((c >> 8) & 0xF));
							tokenBuffer.append(hex.charAt((c >> 4) & 0xF));
							tokenBuffer.append(hex.charAt(c & 0xF));
						} else {
							tokenBuffer.append(c);
						}
				}
			}
			buffer.append(tokenBuffer);
		}
		return buffer.toString();
	}

	/*
	 * @see org.eclipse.jface.text.IAutoIndentStrategy#customizeDocumentCommand(IDocument, DocumentCommand)
	 */
	@Override
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		try {
			if (command.text == null)
				return;
			if (isSmartMode()) {
				javaMultiLineStringIndentAfterNewLine(document, command);
			}
		} catch (BadLocationException e) {
		}
	}
}
