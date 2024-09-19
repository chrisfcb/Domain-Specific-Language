package no.uio.aeroscript.type;
import java.util.Random;

import no.uio.aeroscript.ast.expr.Node;

public class Range extends Node {
    private final float value;
    Random random = new Random();

    public Range(Node min, Node max) {
        float minValue = (float) min.evaluate();
        float maxValue = (float) max.evaluate();
        
        this.value = minValue + random.nextFloat() * (maxValue - minValue);
        //this.value = random.nextInt(minValue, maxValue + 1) + maxValue;
    }

    @Override
    public Object evaluate() {
        return this.value;
    }
}
