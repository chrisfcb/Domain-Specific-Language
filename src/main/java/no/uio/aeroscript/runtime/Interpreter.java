package no.uio.aeroscript.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.ast.expr.NumberNode;
import no.uio.aeroscript.ast.expr.OperationNode;
import no.uio.aeroscript.ast.stmt.Execution;
import no.uio.aeroscript.ast.stmt.Reaction;
import no.uio.aeroscript.ast.stmt.Statement;
import no.uio.aeroscript.ast.stmt.acAscend;
import no.uio.aeroscript.ast.stmt.acDescend;
import no.uio.aeroscript.ast.stmt.acDock;
import no.uio.aeroscript.ast.stmt.acMove;
import no.uio.aeroscript.ast.stmt.acTurn;
import no.uio.aeroscript.type.Memory;
import no.uio.aeroscript.type.Point;
import no.uio.aeroscript.type.Range;

public class Interpreter extends AeroScriptBaseVisitor<Object> {
    private final HashMap<Memory, Object> heap;
    private final Stack<Statement> stack;
    private HashMap<String, Runnable> listeners;

    public Interpreter(HashMap<Memory, Object> heap, Stack<Statement> stack) {
        this.heap = heap;
        this.stack = stack;
        this.listeners = new HashMap<>();
    }

    public Point getPosition() {
        assert heap.get(Memory.VARIABLES) instanceof HashMap;
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        return (Point) vars.get("current position");
    }

    public float getDistanceTravelled() {
        // Implement this method that returns the actual distance travelled from the variables
        assert heap.get(Memory.VARIABLES) instanceof HashMap;
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        return (float) vars.get("distance travelled");
    }

    public float getBatteryLevel() {
        // Implement this method that returns the actual battery level from the variables
        assert heap.get(Memory.VARIABLES) instanceof HashMap;
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        return (float) vars.get("battery level");
    }

    private void checkBattery() {
        // Implement this method that checks the battery level and triggers the reaction if the battery level is low
        return ;
    }

    public HashMap<String, Runnable> getListeners() {
        return this.listeners;
    }

    public Execution getFirstExecution() {
        return (Execution) stack.pop();
    }

    @Override
    public Object visitProgram(AeroScriptParser.ProgramContext ctx) {
        
        // Visit the top-level execution block
        for (AeroScriptParser.ExecutionContext execution : ctx.execution()) {
            stack.push((Statement) visitExecution(execution));
        }
        
        return new Program(heap, stack);
    }

    @Override
    public Object visitExecution(AeroScriptParser.ExecutionContext ctx) {        
        ArrayList<Object> statements = new ArrayList<>();
        for (AeroScriptParser.StatementContext stmt : ctx.statement()) {
            statements.add(visitStatement(stmt));
        }
        
        String executionId = ctx.ID(0).getText();
        Execution execution = new Execution(statements, heap, ctx);
        
        // add execution to the heap EXECUTION_TABLE
        HashMap<String, Execution> execution_table = (HashMap<String, Execution>) heap.get(Memory.EXECUTION_TABLE);
        execution_table.put(executionId, execution);
        
        return execution;
        
    }

    @Override
    public Object visitStatement(AeroScriptParser.StatementContext ctx) {
        
        if (ctx.action() != null) {
            return visitAction(ctx.action()); 
        } 
        else if (ctx.reaction() != null) {
            return visitReaction(ctx.reaction()); 
        }
        else if (ctx.execution() != null) {
            return visitExecution(ctx.execution()); 
        } 

        return null;
    }
    
    @Override
    public Object visitAction(AeroScriptParser.ActionContext ctx) {
        
        if (ctx.acAscend() != null) {
            Node node = (Node) visit(ctx.acAscend().expression());
            return new acAscend(heap, node); 
        }
        else if (ctx.acDescend() != null) {
            if (ctx.acDescend().expression() != null) {
                Node node = (Node) visit(ctx.acDescend().expression());
                return new acDescend(heap, node); 
            } else {
                return new acDescend(heap);
            }
        }
        else if (ctx.acDock() != null) {
            return new acDock(heap);
        }
        else if (ctx.acMove() != null) {
            if (ctx.acMove().POINT() != null) {
                Point point = (Point) visit(ctx.acMove().point());
                return new acMove(heap, point);
            } else {
                NumberNode node = (NumberNode) visit(ctx.acMove().NUMBER());
                return new acMove(heap, node);
            }
        }
        else if (ctx.acTurn() != null) {
            if (ctx.acTurn().RIGHT() != null) {
                Node node = (Node) visit(ctx.acTurn().expression());
                return new acTurn(heap, "RIGHT", node);
            } else if (ctx.acTurn().LEFT() != null) {
                Node node = (Node) visit(ctx.acTurn().expression());
                return new acTurn(heap, "LEFT", node);
            } else {
                Node node = (Node) visit(ctx.acTurn().expression());
                String direction = Math.random() < 0.5 ? "LEFT" : "RIGHT";
                // Selects a random direction if not given
                return new acTurn(heap, direction, node);
            }
        }
        // (‘for‘ expression ‘s‘ | ‘at speed‘ expression)?
        else if(ctx.FOR()!=null){

        }
        else if(ctx.SPEED()!=null){
            
        }
        return null;
    }

    @Override
    public Object visitReaction(AeroScriptParser.ReactionContext ctx) {
        
        String event = "";      
        String id = ctx.ID().getText(); // gets id in "‘on‘ event ‘ → ‘ id" 

        if (ctx.event().MESSAGE()!=null) {
            event = ctx.event().ID().getText(); // gets id in "message [id]"
            HashMap<String, String> messages = (HashMap<String, String>) heap.get(Memory.MESSAGES);
            messages.put(event, id);
        }
        else{
            event = ctx.event().getText(); // either "obstacle" or "low battery"
            HashMap<String, String> reactions = (HashMap<String, String>) heap.get(Memory.REACTIONS);
            reactions.put(event, id);
        }

        return new Reaction(event, id, heap, listeners);
    }


    @Override
    public Object visitPoint(AeroScriptParser.PointContext ctx) {
        Node xNode = (Node) visit(ctx.expression(0));
        Node yNode = (Node) visit(ctx.expression(1));
        float x = Float.parseFloat(xNode.evaluate().toString());
        float y = Float.parseFloat(yNode.evaluate().toString());
        return new Point(x, y);
    }

    @Override
    public Object visitRange(AeroScriptParser.RangeContext ctx) {
        Node startNode = (Node) visit(ctx.expression(0));
        Node endNode = (Node) visit(ctx.expression(1));
        float start = Float.parseFloat(startNode.evaluate().toString());
        float end = Float.parseFloat(endNode.evaluate().toString());
        return new Range(start, end);
    }

    @Override
    public Node visitExpression(AeroScriptParser.ExpressionContext ctx) {
        // Case of binary operation
        if (ctx.PLUS() != null || ctx.MINUS() != null || ctx.TIMES() != null) {
            Node left = (Node) visit(ctx.expression(0));
            Node right = (Node) visit(ctx.expression(1));
            String operator = ctx.PLUS() != null ? "PLUS" :
                    ctx.MINUS() != null ? "MINUS" :
                            "TIMES";
            return new OperationNode(operator, left, right);
        }
        // Case of negation
        else if (ctx.NEG() != null) {
            Node expr = (Node) visit(ctx.expression(0));
            return new OperationNode("NEG", expr, null);
        }
        // Case of random
        else if (ctx.RANDOM() != null) {
            Node start;
            Node end;
            if (ctx.range() != null) {
                AeroScriptParser.RangeContext rangeContext = ctx.range();
                start = (Node) visit(rangeContext.expression(0));
                end = (Node) visit(rangeContext.expression(1));
            } else {
                start = new NumberNode((float) 0);
                end = new NumberNode(100F);
            }
            return new OperationNode("RANDOM", start, end);
        }
        // Case of point
        else if (ctx.POINT() != null) {
            AeroScriptParser.PointContext pointContext = ctx.point();
            Node x = (Node) visit(pointContext.expression(0));
            Node y = (Node) visit(pointContext.expression(1));
            return new OperationNode("POINT", x, y);
        }
        // Case of number
        else if (ctx.NUMBER() != null) {
            return new NumberNode(Float.parseFloat(ctx.NUMBER().getText()));
        }
        // Case of expression in parentheses
        else if (ctx.LPAREN() != null) {
            return (Node) visit(ctx.expression(0));
        }
        return new NumberNode(Float.parseFloat(ctx.NUMBER().getText()));
    }
}
