package no.uio.aeroscript.ast.stmt;

public abstract class Action extends Statement{
    Action action;

    public Action(Action action) {
        this.action = action;
    }

    @Override
    public void execute() {
        
    }
}
