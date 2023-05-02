package fei.tuke.sk.stmlang;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class Generator {
    private final StateMachineDefinition stateMachine;
    private final Writer writer;

    public Generator(StateMachineDefinition stateMachine, Writer writer) {
        this.stateMachine = stateMachine;
        this.writer = writer;
    }

    public void generate() throws IOException {
        writer.write("#include \"common.h\"\n");
        if(stateMachine.getStates().size()!=0) {
            for (Map.Entry<String, StateDefinition> set : stateMachine.getStates().entrySet()) {
                writer.write("void state_"+set.getKey()+"();\n");
            }
            for (Map.Entry<String, StateDefinition> set : stateMachine.getStates().entrySet()) {
                writeState(set.getKey(), set.getValue());
            }
        }
        writer.write("void main() {\n\tstate_"+stateMachine.getInitialStateName()+"();\n}");
    }

    private void writeState(String name, StateDefinition state) throws IOException {
        writer.write("void state_"+name+"() {\n");
        if(state.getActions().size()!=0){
            for(String action:state.getActions()){
                for (Map.Entry<String, Character> set : stateMachine.getEvents().entrySet()) {
                    if(set.getKey().equals(action)){
                        writer.write("\tsend_event('"+set.getValue()+"');\n");
                    }
                }
            }
        }
        writer.write("\tchar ev;\n\twhile (ev = read_command()) { \n\t\tswitch (ev) {\n");
        if(state.getTransitions().size()!=0) {
            for (TransitionDefinition transition : state.getTransitions()) {
                for (Map.Entry<String, Character> set : stateMachine.getCommands().entrySet()) {
                    if (set.getKey().equals(transition.commandName())) {
                        writer.write("\t\t\tcase '" + set.getValue() + "': {\n\t\t\t\treturn state_" + transition.targetName() + "();\n\t\t\t}\n");
                    }
                }
            }
        }
        if(stateMachine.getResetCommands().size()!=0) {
            for (String reset : stateMachine.getResetCommands()) {
                for (Map.Entry<String, Character> set : stateMachine.getCommands().entrySet()) {
                    if (set.getKey().equals(reset)) {
                        writer.write("\t\t\tcase '" + set.getValue() + "': {\n\t\t\t\treturn state_" + stateMachine.getInitialStateName() + "();\n\t\t\t}\n");
                    }
                }
            }
        }
        writer.write("\t\t}\n\t}\n");
        writer.write("}\n");
    }
}