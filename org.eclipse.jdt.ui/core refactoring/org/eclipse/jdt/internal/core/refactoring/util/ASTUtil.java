/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
package org.eclipse.jdt.internal.core.refactoring.util;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

/**
 * Utility class for Abstract syntax tree handling.
 */
public class ASTUtil {
	
	//no instances
	private ASTUtil(){
	}
	
	/**
	 * Returns the source start of a node independent whether it is a declaration or
	 * not.
	 */
	public static int getSourceStart(AstNode node) {
		if (node instanceof AbstractVariableDeclaration)
			return ((AbstractVariableDeclaration)node).declarationSourceStart;
		if (node instanceof TypeDeclaration)
			return ((TypeDeclaration)node).declarationSourceStart;
		if (node instanceof AbstractMethodDeclaration)
			return ((AbstractMethodDeclaration)node).declarationSourceStart;
			
		return node.sourceStart;
	}
	
	/**
	 * Returns the source end of a node independent whether it is a declaration or
	 * not.
	 */
	public static int getSourceEnd(AstNode node) {
		if (node instanceof AbstractVariableDeclaration)
			return ((AbstractVariableDeclaration)node).declarationSourceEnd;
		if (node instanceof TypeDeclaration)
			return ((TypeDeclaration)node).declarationSourceEnd;
		if (node instanceof AbstractMethodDeclaration)
			return ((AbstractMethodDeclaration)node).declarationSourceEnd;
			
		return node.sourceEnd;
	}
	
	public static int getSimpleLength(AstNode node) {
		return node.sourceEnd - node.sourceStart + 1;
	}
	
	public static char[] getIdentifier(FieldBinding binding) {
		return CharOperation.concat(binding.declaringClass.constantPoolName(), binding.name, '^');
	}	
}