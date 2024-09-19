package no.uio.aeroscript.ast.expr;
import no.uio.aeroscript.type.Point;
import no.uio.aeroscript.type.Range;


public class OperationNode extends Node {
    private final String operation;
    private final Node left;
    private final Node right;

    public OperationNode(String operation, Node left, Node right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public Object evaluate() {
        Object leftValue = left.evaluate();
        Object rightValue = right.evaluate();
        
        return switch (operation) {
            case "PLUS" -> (Float) leftValue + (Float) rightValue;
            case "MINUS" -> (Float) leftValue - (Float) rightValue;
            case "TIMES" -> (Float) leftValue * (Float) rightValue;
            case "NEG" -> -(Float) leftValue;
            case "RANDOM" -> new Range(left, right).evaluate();
            case "POINT" -> new Point(left, right);
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }

}
