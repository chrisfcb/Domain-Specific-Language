package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.type.Memory;


public class acAscend extends Statement{
    HashMap<Memory, Object> heap;
    Node node;

    public acAscend(HashMap<Memory, Object> heap, Node node) {
        this.node = node;
        this.heap = heap;
    }

    @Override
    public void execute() {
        
        float result = (float) node.evaluate();
        System.err.println("Ascending by " + result + " meters.");

        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldAltitude = (float) vars.get("altitude");
        
        float newAltitude = oldAltitude + result;
        
        vars.put("altitude", newAltitude);

        updateDistance(result);
        calculateCost(result);
    }

    private void updateDistance(float addDistance){
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldDistance = (float) vars.get("distance travelled");

        float newDistance = oldDistance + addDistance;

        vars.put("distance travelled", newDistance);
    }

    private void calculateCost(float dist) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldBattery = (float) vars.get("battery level");
        
        float cost = dist * 0.6f;
        float newBattery = oldBattery - cost;

        vars.put("battery level", newBattery);
    }
}
