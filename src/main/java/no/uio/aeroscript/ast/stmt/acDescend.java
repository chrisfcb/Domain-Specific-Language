package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.ast.expr.Node;
import no.uio.aeroscript.type.Memory;


public class acDescend extends Statement{
    HashMap<Memory, Object> heap;
    Node expression;

    public acDescend(HashMap<Memory, Object> heap, Node node) {
        this.heap = heap;
        this.expression = node;
    }
    
    public acDescend(HashMap<Memory, Object> heap) {
        this.heap = heap;
    }

    @Override
    public void execute() {

        if (this.expression != null) {
            float result = (float) expression.evaluate();
            System.err.println("Descending by " + result + " meters.");
    
            HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
            float oldAltitude = (float) vars.get("altitude");
            
            float newAltitude = oldAltitude - result;
            
            vars.put("altitude", newAltitude);
            updateDistance(result);
            calculateCost(result);
        } else {
            HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
            float oldAltitude = (float) vars.get("altitude");
            updateDistance(oldAltitude);
            calculateCost(oldAltitude);
            vars.put("altitude", 0);
        }
    };

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