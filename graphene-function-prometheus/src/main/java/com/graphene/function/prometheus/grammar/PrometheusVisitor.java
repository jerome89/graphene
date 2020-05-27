// Generated from Prometheus.g4 by ANTLR 4.7.2
package com.graphene.function.prometheus.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PrometheusParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PrometheusVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PrometheusParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(PrometheusParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link PrometheusParser#duration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDuration(PrometheusParser.DurationContext ctx);
}