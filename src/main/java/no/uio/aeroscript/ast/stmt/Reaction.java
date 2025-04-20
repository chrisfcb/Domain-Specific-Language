package no.uio.aeroscript.ast.stmt;

import java.util.HashMap;

import no.uio.aeroscript.type.Memory;

public class Reaction extends Statement {
    String event, id;

    HashMap<Memory, Object> heap; 
    HashMap<String, Runnable> listeners;
    
    public Reaction(String event, String id,  HashMap<Memory, Object> heap, HashMap<String, Runnable> listeners){
        this.event = event;
        this.id = id;
        this.listeners = listeners;
        this.heap = heap;
        addListener();
    }

    public void addListener(){
        listeners.put(event, () -> {
                
            System.out.println("\nGetting execution with id: "+ id);
            HashMap<String, Object> execution_table = (HashMap<String, Object>) heap.get(Memory.EXECUTION_TABLE);
            
            Execution execution = (Execution) execution_table.get(id);
            
            System.out.println("Got execution: "+ execution);
            System.out.println("Calling recerivemessage with event= " + event);
            System.out.println(" \n");

            execution.receiveMessage(event);
        });
    }
    
    @Override
    public void execute() {
        System.err.println("\n--Reaction, execute(): Reacting to " + event);
        HashMap<String, Object> execution_table = (HashMap<String, Object>) heap.get(Memory.EXECUTION_TABLE);
        
        Execution execution = (Execution) execution_table.get(id);
        execution.receiveMessage(event);
        

        
    }
}
