package no.uio.aeroscript.runtime;

import java.util.HashMap;
import java.util.Stack;

import no.uio.aeroscript.ast.stmt.Statement;
import no.uio.aeroscript.type.Memory;

public class Program {
    // Add the variables that you might be needing
    private final HashMap<Memory, Object> heap;
    private final Stack<Statement> stack;

    public Program(HashMap<Memory, Object> heap, Stack<Statement> stack) {
        this.heap = heap;
        this.stack = stack;
    }

    // Hint: we should keep track of the executions and add them to a stack

    public void run(String id) {
        // Implement the run method

        while(!stack.empty()){
            stack.pop().execute();
        }
    }
}
