/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *     Microsoft Corporation - moved template related code to jdt.core.manipulation - https://bugs.eclipse.org/549989
 *******************************************************************************/
package org.eclipse.jdt.internal.corext.template.java;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;

import org.eclipse.jdt.internal.ui.text.template.contentassist.MultiVariableGuess;


/**
 * A compilation unit context.
 */
public abstract class CompilationUnitContext extends DocumentTemplateContext implements ICompilationUnitContext {

	/** A global state for proposals that change if a master proposal changes. */
	protected MultiVariableGuess fMultiVariableGuess;
	/** */
	protected CompilationUnitContextCore fCompilationUnitContextCore;

	/**
	 * Creates a compilation unit context.
	 *
	 * @param type   the context type
	 * @param document the document
	 * @param completionOffset the completion position within the document
	 * @param completionLength the completion length within the document
	 * @param compilationUnit the compilation unit (may be <code>null</code>)
	 */
	protected CompilationUnitContext(TemplateContextType type, IDocument document, int completionOffset, int completionLength, ICompilationUnit compilationUnit) {
		super(type, document, completionOffset, completionLength);
		this.fCompilationUnitContextCore = new CompilationUnitContextCore(type, document, completionOffset, completionLength, compilationUnit);
	}

	/**
	 * Creates a compilation unit context.
	 *
	 * @param type   the context type
	 * @param document the document
	 * @param completionPosition the position defining the completion offset and length
	 * @param compilationUnit the compilation unit (may be <code>null</code>)
	 * @since 3.2
	 */
	protected CompilationUnitContext(TemplateContextType type, IDocument document, Position completionPosition, ICompilationUnit compilationUnit) {
		super(type, document, completionPosition);
		this.fCompilationUnitContextCore = new CompilationUnitContextCore(type, document, completionPosition, compilationUnit);
	}

	/**
	 * Returns the compilation unit if one is associated with this context,
	 * <code>null</code> otherwise.
	 *
	 * @return the compilation unit of this context or <code>null</code>
	 */
	@Override
	public final ICompilationUnit getCompilationUnit() {
		return this.fCompilationUnitContextCore.getCompilationUnit();
	}

	/**
	 * Returns the enclosing element of a particular element type,
	 * <code>null</code> if no enclosing element of that type exists.
	 *
	 * @param elementType the element type
	 * @return the enclosing element of the given type or <code>null</code>
	 */
	@Override
	public IJavaElement findEnclosingElement(int elementType) {
		return this.fCompilationUnitContextCore.findEnclosingElement(elementType);
	}

	/**
	 * Sets whether evaluation is forced or not.
	 *
	 * @param evaluate <code>true</code> in order to force evaluation,
	 *            <code>false</code> otherwise
	 */
	public void setForceEvaluation(boolean evaluate) {
		this.fCompilationUnitContextCore.setForceEvaluation(evaluate);
	}

	/**
	 * Gets whether evaluation is forced or not.
	 * @return whether evaluation is forced or not.
	 */
	public boolean isForceEvaluation() {
		return this.fCompilationUnitContextCore.isForceEvaluation();
	}

	/**
	 * Gets if the context has a managed position.
	 * @return if the context has a managed position.
	 */
	public boolean isManaged() {
		return this.fCompilationUnitContextCore.isManaged();
	}

	/**
	 * Returns the multi-variable guess.
	 *
	 * @return the multi-variable guess
	 */
	public MultiVariableGuess getMultiVariableGuess() {
		return fMultiVariableGuess;
	}

	/**
	 * @param multiVariableGuess The multiVariableGuess to set.
	 */
	void setMultiVariableGuess(MultiVariableGuess multiVariableGuess) {
		fMultiVariableGuess= multiVariableGuess;
	}

	protected IJavaProject getJavaProject() {
		return this.fCompilationUnitContextCore.getJavaProject();
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.fCompilationUnitContextCore.getAdapter(adapter);
	}
}
