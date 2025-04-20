package no.uio.aeroscript.ast.stmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.type.Memory;

public class Execution extends Statement {
    // Add all the variables that you need for the exeuction
    ArrayList<Object> statements;
    HashMap<Memory, Object> heap;
    String nextExecutionId;  // Class variable to store the second ID, if present
    String id;
    
    public Execution(ArrayList<Object> statements, HashMap<Memory, Object> heap, AeroScriptParser.ExecutionContext ctx) {
        this.statements = statements;
        this.heap = heap;
        // Check if secondId is present and store it in nextExecutionId
        if (ctx.secondId != null) {
            this.nextExecutionId = ctx.secondId.getText();
            System.out.print("Next execution: " + ctx.secondId.getText() + "found");
        } else {
            this.nextExecutionId = null;  // Set to null if secondId is not present
        }
    }

    public void receiveMessage(String message) {
        // When you receive a message you need to execute its content
        // Hint: for messages you can use listeners to trigger the execution

        System.out.println("\n----Execution, receiveMessage(): ");
        System.out.println("Message " + message + " recieved");

        HashMap<String, Object> messages = (HashMap<String, Object>) heap.get(Memory.MESSAGES);
        HashMap<String, Object> reactions = (HashMap<String, Object>) heap.get(Memory.REACTIONS);
        
        System.out.println("Checking if message: " + message + " in Memory.MESSAGES ");

        if (messages.containsKey(message)) {
            
            System.out.println("message: " + message + " found in Memory.MESSAGES");  
            
            String executionId = (String) messages.get(message); 
            HashMap<String, Execution> execution_table = (HashMap<String, Execution>) heap.get(Memory.EXECUTION_TABLE);
            
            Execution execution = execution_table.get(executionId);
            if (execution != null) {
                System.out.println("Executing execution connected to message: " + message);  
                execution.execute();

                System.out.println("execution connected to message: " + message + " completed. returning...");
                return;
            } else {
                System.out.println("Execution for message " + message + " not found.");
            }
        }
        else if (reactions.containsKey(message)) {
            
            System.out.println("message: " + message + " found in Memory.REACTIONS");  
            
            String executionId = (String) reactions.get(message); 
            HashMap<String, Execution> execution_table = (HashMap<String, Execution>) heap.get(Memory.EXECUTION_TABLE);
            
            Execution execution = execution_table.get(executionId);
            if (execution != null) {
                if (message.equals("low battery")) {
                    HashMap<String, Object> vars = (HashMap<String, Object>) heap.get(Memory.VARIABLES);
                    float battery = (float) vars.get("battery level");
                    
                    if (battery < 20) {
                        System.out.println("battery is "+ (float) vars.get("battery level") +", battery is below 20. returning...");
                        execution.execute();
                        return;
                    }
                    else{
                        System.out.println("battery is "+ (float) vars.get("battery level") +", battery is not below 20. returning...");
                        return;
                    }
                } 
            } else {
                System.out.println("Execution for reaction " + message + " not found.");
            }
        } else {
            System.out.println("No reaction or message associated with: " + message);
        }

        
        System.out.println("message: " + message + " is not in Memory.REACTIONS or Memory.MESSAGES ");
    }

    @Override
    public void execute() {
        // Check if the secondId is present
        if (nextExecutionId != null) {  // Use the stored nextExecutionId
            HashMap<String, Execution> executionTable = (HashMap<String, Execution>) heap.get(Memory.EXECUTION_TABLE);
            Execution execution = executionTable.get(nextExecutionId);
            if (execution != null) {
                statements.add(execution);
            } else {
                System.err.println("Execution with ID " + nextExecutionId + " not found in execution table.");
            }
        } else {
            System.err.println("No second ID found for execution.");
        }

        System.out.println("--Execution, execute(): Statements: " + this.statements);
        
        Stack<Execution> executionStack = new Stack<>();
        executionStack.push(this); 



        System.out.println("--before while loop");
        while (!executionStack.isEmpty()) {
            System.out.println("--Execution, execute(): going through statements");
            Execution currentExecution = executionStack.pop();
            Integer iteration = 0;
            for (Object stmt : currentExecution.statements) {
                iteration ++;
                System.out.println("--Execution, execute(): going through statement" + stmt + " number " + iteration);
                switch (stmt) {
                    case Execution execution -> executionStack.push(execution); // legger på nøstet execution
                    case Statement statement -> statement.execute(); // kjører actions og reactions
                    default -> System.out.println("Execution, execute(): stmt is not Execution or Statement");
                }
            }
        }
        
        System.out.println("--after while loop");
    }
}

