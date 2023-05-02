package fei.tuke.sk.stmlang;

import java.util.*;

public class StateMachineDefinition {
    private final Map<String, Character> events = new HashMap<>();
    private final Map<String, Character> commands = new HashMap<>();
    private final List<String> resetCommands = new ArrayList<>();
    private final Map<String, StateDefinition> states = new LinkedHashMap<>();
    private String initialStateName = null;

    public void addEvent(String name, char value) {
        events.put(name, value);
    }

    public void addCommand(String name, Character value) {
        commands.put(name, value);
    }

    public void addState(String name, StateDefinition state) {
        states.put(name, state);
    }

    public void addResetCommands(String name) {
        resetCommands.add(name);
    }
    public void setInitialStateName(){
        List<String>names=new LinkedList<>();
        List<String>transactions=new LinkedList<>();
        for(Map.Entry<String, StateDefinition> set:states.entrySet()){
            if(set.getValue().getActions().size()!=0&&set.getValue().getTransitions().size()!=0){
                transactions.add(set.getValue().getTransitions().get(0).targetName());
                names.add(set.getKey());
            }
        }
        if(names.size()!=1) {
            for (String name : names) {
                for (String transaction : transactions) {
                    if (name.equals(transaction)) {
                        this.initialStateName = name;
                    }
                }
            }
        }else {
            this.initialStateName = names.get(0);
        }
    }

    public String getInitialStateName() {
        return initialStateName;
    }

    public Map<String, Character> getEvents() {
        return events;
    }

    public Map<String, Character> getCommands() {
        return commands;
    }

    public List<String> getResetCommands() {
        return resetCommands;
    }

    public Map<String, StateDefinition> getStates() {
        return states;
    }

    public void checkCommands(){
        for(Map.Entry<String, Character> firstMap:commands.entrySet()){
            for (Map.Entry<String, Character> secondMap:commands.entrySet()){
                if(firstMap.getValue()==secondMap.getValue()){
                    if(firstMap.getKey()!=secondMap.getKey()){
                        throw new StateMachineException("Two different commands have same character");
                    }
                }
            }
        }
    }

    public void checkResetCommands(){
        for (String command:resetCommands){
            if(!commands.containsKey(command)){
                throw new StateMachineException("Reset command is not command");
            }
        }
    }

    public void checkStates(){
        for(Map.Entry<String, StateDefinition> map:states.entrySet()){
            for (TransitionDefinition transitionDefinition:map.getValue().getTransitions()){
                boolean isValid=false;
                for (String key:states.keySet()){
                    if(transitionDefinition.targetName().equals(key)){
                        isValid=true;
                    }
                }
                if(isValid==false){
                    throw new StateMachineException("Target is not found "+transitionDefinition.targetName());
                }

            }
        }


        for(Map.Entry<String, StateDefinition> map:states.entrySet()){
            for (TransitionDefinition transitionDefinition:map.getValue().getTransitions()){
                boolean isValid=false;
                for (String key:commands.keySet()){
                    if(transitionDefinition.commandName().equals(key)){
                        isValid=true;
                    }
                }
                if(isValid==false){
                    throw new StateMachineException("Command is not found "+transitionDefinition.commandName());
                }

            }
        }

        for(Map.Entry<String, StateDefinition> map:states.entrySet()){
            for (String action:map.getValue().getActions()){
                boolean isValid=false;
                for (String key:events.keySet()){
                    if(action.equals(key)){
                        isValid=true;
                    }
                }
                if(isValid==false){
                    throw new StateMachineException("Command is not found "+action);
                }

            }
        }
    }
}