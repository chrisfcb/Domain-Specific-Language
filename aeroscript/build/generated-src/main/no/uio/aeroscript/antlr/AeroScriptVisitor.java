// Generated from AeroScript.g4 by ANTLR 4.13.1

package no.uio.aeroscript.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AeroScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AeroScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AeroScriptParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(AeroScriptParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link AeroScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(AeroScriptParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AeroScriptParser#point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPoint(AeroScriptParser.PointContext ctx);
	/**
	 * Visit a parse tree produced by {@link AeroScriptParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(AeroScriptParser.RangeContext ctx);
}