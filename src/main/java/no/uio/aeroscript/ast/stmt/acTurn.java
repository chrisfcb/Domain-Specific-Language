package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.type.Memory;


public class acTurn extends Statement{
    HashMap<Memory, Object> heap;
    Node node;
    String direction;

    public acTurn(HashMap<Memory, Object> heap, String direction, Node node) {
        this.node = node;
        this.heap = heap;
        this.direction = direction;
    }

    @Override
    public void execute() {
        float result = (float) node.evaluate();

        if (direction.equals("RIGHT")) {
            calculateCost(result);
            System.out.println(String.format("Turning right by %s degrees", result));
        } 
        if (direction.equals("LEFT")) {
            calculateCost(result);
            System.out.println(String.format("Turning left by %s degrees", result));
        }
        
    }

    private void calculateCost(float angle) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldBattery = (float) vars.get("battery level");
        
        float cost = angle * 0.3f;
        float newBattery = oldBattery - cost;

        vars.put("battery level", newBattery);
    }
}
