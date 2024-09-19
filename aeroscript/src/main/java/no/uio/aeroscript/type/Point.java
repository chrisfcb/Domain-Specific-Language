package no.uio.aeroscript.type;
import no.uio.aeroscript.ast.expr.Node;

public class Point {
    private final float x;
    private final float y;

    public Point(Node n1, Node n2) {
        this.x = (Float) n1.evaluate();
        this.y = (Float) n2.evaluate();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
