package fei.tuke.sk.stmlang;

public class StateMachineException extends RuntimeException {
    //TODO your code goes here
    private String error;

    public StateMachineException(String error){
        this.error=error;
    }

    public String getError() {
        return "State machine error: "+error;
    }
}