// Generated from Prometheus.g4 by ANTLR 4.7.2
package com.graphene.function.prometheus.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PrometheusParser}.
 */
public interface PrometheusListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PrometheusParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(PrometheusParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrometheusParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(PrometheusParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrometheusParser#duration}.
	 * @param ctx the parse tree
	 */
	void enterDuration(PrometheusParser.DurationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrometheusParser#duration}.
	 * @param ctx the parse tree
	 */
	void exitDuration(PrometheusParser.DurationContext ctx);
}