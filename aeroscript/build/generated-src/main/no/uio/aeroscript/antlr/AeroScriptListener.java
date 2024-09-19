// Generated from AeroScript.g4 by ANTLR 4.13.1

package no.uio.aeroscript.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AeroScriptParser}.
 */
public interface AeroScriptListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AeroScriptParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(AeroScriptParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link AeroScriptParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(AeroScriptParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link AeroScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(AeroScriptParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AeroScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(AeroScriptParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AeroScriptParser#point}.
	 * @param ctx the parse tree
	 */
	void enterPoint(AeroScriptParser.PointContext ctx);
	/**
	 * Exit a parse tree produced by {@link AeroScriptParser#point}.
	 * @param ctx the parse tree
	 */
	void exitPoint(AeroScriptParser.PointContext ctx);
	/**
	 * Enter a parse tree produced by {@link AeroScriptParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(AeroScriptParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AeroScriptParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(AeroScriptParser.RangeContext ctx);
}