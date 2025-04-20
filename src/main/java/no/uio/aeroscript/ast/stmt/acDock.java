package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.type.Memory;
import no.uio.aeroscript.type.Point;


public class acDock extends Statement{
    HashMap<Memory, Object> heap;

    public acDock(HashMap<Memory, Object> heap) {
        this.heap = heap;
    }

    @Override
    public void execute() {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        Point initPos = (Point) vars.get("initial position");
        float oldAltitude = (float) vars.get("altitude");

        updateDistancePos(initPos);
        vars.put("current position", initPos);

        updateDistanceAlt(oldAltitude);
        calculateCost(oldAltitude); // Uses just altitude to calculate cost, not distance
        vars.put("altitude", 0);

        System.err.println("Docking, altitude: " + vars.get("altitude") + ", position: " + vars.get("current position"));
    }

    private void updateDistancePos(Point newPosition) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        Point oldPosition = (Point) vars.get("current position");
        
        float x1 = oldPosition.getX();
        float x2 = newPosition.getX();
        float y1 = oldPosition.getY();
        float y2 = newPosition.getY();

        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        
        float newDistance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        vars.put("distance travelled", newDistance);
    }

    private void updateDistanceAlt(float addDistance){
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
