package no.uio.aeroscript.ast.expr;

public class NumberNode extends Node {
    private final Float value;

    public NumberNode(Float value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return value;
    }
}

