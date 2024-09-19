package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.ast.expr.NumberNode;
import no.uio.aeroscript.ast.expr.OperationNode;

public class Interpreter extends AeroScriptBaseVisitor<Object> {
    @Override
    public Node visitExpression(AeroScriptParser.ExpressionContext ctx) {        

        // Based on the operator, create an operation node
        if (ctx.PLUS() != null) {
            return new OperationNode("PLUS", (Node) visit(ctx.expression(0)), (Node) visit(ctx.expression(1)));
        } else if (ctx.MINUS() != null) {
            return new OperationNode("MINUS", (Node) visit(ctx.expression(0)), (Node) visit(ctx.expression(1)));
        } else if (ctx.TIMES() != null) {
            return new OperationNode("TIMES", (Node) visit(ctx.expression(0)), (Node) visit(ctx.expression(1)));
        } else if (ctx.RANDOM() != null) {
            return (Node) visitRange(ctx.range());        
        } else if (ctx.POINT() != null) {
            return (Node) visitPoint(ctx.point());        
        } else if (ctx.NEG() != null) {
            return new OperationNode("NEG", (Node) visit(ctx.expression(0)), new NumberNode(0.0f));
        } else if (ctx.LPAREN() != null) {
            return (Node) visit(ctx.expression(0));        
        } else if (ctx.NUMBER() != null) {
            return new NumberNode(Float.parseFloat(ctx.NUMBER().getText()));
        }
        return new NumberNode(Float.parseFloat(ctx.NUMBER().getText()));
    }

    
    @Override
    public Node visitRange(AeroScriptParser.RangeContext ctx) { 
        return new OperationNode("RANDOM", (Node) visit(ctx.expression(0)), (Node) visit(ctx.expression(1)));             
    }

    @Override 
    public Node visitPoint(AeroScriptParser.PointContext ctx) {
        return new OperationNode("POINT", (Node) visit(ctx.expression(0)), (Node) visit(ctx.expression(1)));             
    }
}
