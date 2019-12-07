package com.graphene.reader.graphite.evaluation;

import com.graphene.reader.exceptions.InvalidFunctionException;
import com.graphene.reader.graphite.PathTarget;
import com.graphene.reader.graphite.Target;
import com.graphene.reader.exceptions.InvalidArgumentException;
import com.graphene.reader.graphite.functions.GrapheneFunction;
import com.graphene.reader.graphite.functions.registry.FunctionRegistry;
import com.graphene.reader.graphite.grammar.GraphiteBaseVisitor;
import com.graphene.reader.graphite.grammar.GraphiteParser;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * @author Andrei Ivanov
 */
public class TargetVisitor extends GraphiteBaseVisitor<Target> {

    private String tenant;
    private Long from;
    private Long to;
    private EvaluationContext context;

    public TargetVisitor(String tenant, Long from, Long to, EvaluationContext context) {
        this.tenant = tenant;
        this.from = from;
        this.to = to;
        this.context = context;
    }

    @Override
    public Target visitExpressionPathExpression(GraphiteParser.ExpressionPathExpressionContext ctx) {
        return new PathTarget(ctx.getText(), context, ctx.pathExpression().getText(), tenant, from, to);
    }

    @Override
    public Target visitExpressionCall(GraphiteParser.ExpressionCallContext ctx) {
        GraphiteParser.CallContext call = ctx.call();
        try {
            GrapheneFunction function = FunctionRegistry.getFunction(context, call.FunctionName().getText(), from, to);
            function.setText(ctx.getText());

            for(GraphiteParser.ArgContext arg : call.args().arg()) {
                if (arg instanceof GraphiteParser.ArgExpressionContext) {
                    function.addArg(visit(arg));
                } else if (arg instanceof GraphiteParser.ArgBooleanContext) {
                    function.addArg(Boolean.parseBoolean(arg.getText()));
                } else if (arg instanceof GraphiteParser.ArgNumberContext) {
                    function.addArg(Double.parseDouble(arg.getText()));
                } else if (arg instanceof GraphiteParser.ArgStringContext) {
                    function.addArg(arg.getText().replaceAll("^[\"\']|[\"\']$", ""));
                }
            }

            function.checkArguments();

            return function;
        } catch (InvalidFunctionException | InvalidArgumentException e) {
            e.printStackTrace();
            throw new ParseCancellationException(e.getMessage(), e);
        }


    }


}
