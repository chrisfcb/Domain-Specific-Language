package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.ast.expr.NumberNode;
import no.uio.aeroscript.type.Memory;
import no.uio.aeroscript.type.Point;


public class acMove extends Statement{
    HashMap<Memory, Object> heap;
    NumberNode moveBy;
    Point point;
    
    public acMove(HashMap<Memory, Object> heap, NumberNode n) {
        this.moveBy = n;
        this.heap = heap;
    }

    public acMove(HashMap<Memory, Object> heap, Point point) {
        this.point = point;
        this.heap = heap;
    }

    @Override
    public void execute() {
        System.out.println("acMove, execute()");
        
        if (moveBy != null){
            float result = (float) moveBy.evaluate();
            HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
            Point current_position = (Point) vars.get("current position");

            float new_position = current_position.getX() + result;
            Point new_point = new Point(new_position, current_position.getY());
            
            updateDistanceBy(result);
            calculateCostBy(result);
            vars.put("current position", new_point);
            System.err.println("Move to " + vars.get("current position"));
        } 
        
        else if (point != null){
            HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
            
            updateDistanceAndCostPos(this.point); // need calculated distance to update cost
            vars.put("current position", this.point);
            System.err.println("Move to " + vars.get("current position"));
        }
    }

    private void updateDistanceAndCostPos(Point newPosition){
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        Point oldPosition = (Point) vars.get("current position");
        float oldDistance = (float) vars.get("distance travelled");
        
        float x1 = oldPosition.getX();
        float x2 = newPosition.getX();
        float y1 = oldPosition.getY();
        float y2 = newPosition.getY();

        float deltaX = x2 - x1;
        float deltaY = y2 - y1;                                
        
        float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        float newDistance = oldDistance + distance;

        calculateCostPos(distance); 
        vars.put("distance travelled", newDistance);
    }

    private void updateDistanceBy(float addDistance) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldDistance = (float) vars.get("distance travelled");

        float newDistance = oldDistance + addDistance;

        vars.put("distance travelled", newDistance);
    }

    private void calculateCostPos(float dist) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldBattery = (float) vars.get("battery level");
        
        float cost = dist * 0.7f;
        float newBattery = oldBattery - cost;

        vars.put("battery level", newBattery);
    }

    private void calculateCostBy(float dist) {
        HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
        float oldBattery = (float) vars.get("battery level");
        
        float cost = dist * 0.5f;
        float newBattery = oldBattery - cost;

        vars.put("battery level", newBattery);
    }
}

