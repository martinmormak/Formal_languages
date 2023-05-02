package fei.tuke.sk.stmlang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 Grammar:
 * StateMachine  -> { Statement }
 * Statement     -> Commands | ResetCommands | Events | State
 * Commands      -> "commands" "{" { NAME CHAR } "}"
 * Events        -> "events" "{" { NAME CHAR } "}"
 * ResetCommands -> "resetCommands" "{" { NAME } "}"
 * State         -> "state" "{" [Actions] { Transition } "}"
 * Actions       -> "actions" "{" { NAME } "}"
 * Transition    -> NAME "->" NAME
 */

public class Parser {
    private final Lexer lexer;
    private Token symbol;
    private StateMachineDefinition definition;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    int idx=0;

    public StateMachineDefinition stateMachine() {
        definition = new StateMachineDefinition();
        var first = Set.of(
                TokenType.COMMANDS,
                TokenType.EVENTS,
                TokenType.RESET_COMMANDS,
                TokenType.STATE);
        consume();
        while (first.contains(symbol.tokenType())) {
            switch (symbol.tokenType()) {
                case COMMANDS -> commands();
                case EVENTS -> events();
                case RESET_COMMANDS -> resetCommands();
                case STATE -> state();
                default -> match(TokenType.EOF);
            }
        }
        match(TokenType.EOF);
        definition.setInitialStateName();
        definition.checkCommands();
        definition.checkResetCommands();
        definition.checkStates();
        return definition;
    }

    private void commands() {
        //TODO your code goes here
        //System.out.println("Commands "+symbol.attribute());
        int idx=0;
        StringBuilder sb = new StringBuilder();
        boolean isValid=true;
        while(isValid==true) {
            sb.setLength(0);
            while(symbol.attribute().charAt(idx)!='\''){
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    sb.append(symbol.attribute().charAt(idx));
                }
                idx++;
                if(idx>=symbol.attribute().length()){
                    if(symbol.attribute().charAt(idx-1)!='}')
                    {
                        throw new StateMachineException("Parser wrong char");
                    }
                    isValid=false;
                    break;
                }
            }
            if(isValid==true) {
                String name = sb.toString();
                idx++;
                while (Character.isWhitespace(symbol.attribute().charAt(idx))){
                    idx++;
                }
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    Character value = symbol.attribute().charAt(idx);
                    definition.addCommand(name, value);
                }else {
                    throw new StateMachineException("Parser wrong char");
                }
                idx++;
                while (Character.isWhitespace(symbol.attribute().charAt(idx))){
                    idx++;
                }
                if (symbol.attribute().charAt(idx) != '\'') {
                    throw new StateMachineException("Parser wrong char");
                }
                idx++;
            }
        }
        consume();
    }

    private void events() {
        //TODO your code goes here
        //System.out.println("Events "+symbol.attribute());
        idx=0;
        StringBuilder sb = new StringBuilder();
        boolean isValid=true;
        while(isValid==true) {
            sb.setLength(0);
            while(symbol.attribute().charAt(idx)!='\''){
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    sb.append(symbol.attribute().charAt(idx));
                }
                idx++;
                if(idx>=symbol.attribute().length()){
                    if(symbol.attribute().charAt(idx-1)!='}')
                    {
                        throw new StateMachineException("Parser wrong char");
                    }
                    isValid=false;
                    break;
                }
            }
            if(isValid==true) {
                String name = sb.toString();
                idx++;
                while (Character.isWhitespace(symbol.attribute().charAt(idx))){
                    idx++;
                }
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    Character value = symbol.attribute().charAt(idx);
                    definition.addEvent(name, value);
                }else {
                    throw new StateMachineException("Parser wrong char");
                }
                idx++;
                while (Character.isWhitespace(symbol.attribute().charAt(idx))){
                    idx++;
                }
                if (symbol.attribute().charAt(idx) != '\'') {
                    throw new StateMachineException("Parser wrong char");
                }
                idx++;
            }
        }
        consume();
    }

    private void resetCommands() {
        //TODO your code goes here
        //System.out.println("Reset Commands "+symbol.attribute());
        idx=0;
        StringBuilder sb = new StringBuilder();
        boolean isValid=true;
        while(symbol.attribute().charAt(idx)!='\n'){
            idx++;
        }
        idx++;
        while(isValid==true) {
            sb.setLength(0);
            while(symbol.attribute().charAt(idx)!='\n'){
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    sb.append(symbol.attribute().charAt(idx));
                }
                idx++;
                if(idx>=symbol.attribute().length()){
                    if(symbol.attribute().charAt(idx-1)!='}')
                    {
                        throw new StateMachineException("Parser wrong char");
                    }
                    isValid=false;
                    break;
                }
            }
            if(isValid==true) {
                String name = sb.toString();
                idx++;
                definition.addResetCommands(name);
            }
        }
        consume();
    }

    private void state() {
        //TODO your code goes here (actions + transitions)
        //System.out.println("State "+symbol.attribute());
        idx=0;
        StringBuilder sb = new StringBuilder();
        boolean isValid=true;
        boolean isEnd=false;
        while(symbol.attribute().charAt(idx)!='{'){
            if(Character.isLetter(symbol.attribute().charAt(idx))) {
                sb.append(symbol.attribute().charAt(idx));
            }
            if(idx>=symbol.attribute().length()){
                throw new StateMachineException("Parser wrong char");
            }
            idx++;
        }
        String name=sb.toString();
        int oldIdx=idx;
        idx++;
        sb.setLength(0);
        while(symbol.attribute().charAt(idx)!='{'&&symbol.attribute().charAt(idx)!='='){
            if(Character.isLetter(symbol.attribute().charAt(idx))) {
                sb.append(symbol.attribute().charAt(idx));
            }
            if(idx>=symbol.attribute().length()){
                throw new StateMachineException("Parser wrong char");
            }
            idx++;
        }
        idx++;
        StateDefinition stateDefinition=new StateDefinition();
        List<String> actionsList=null;
        if(sb.toString().equals("actions")){
            actionsList=this.actions();
            for(String act:actionsList){
                stateDefinition.addAction(act);
            }
        }
        else {
            idx=oldIdx;
        }
        idx++;
        while(isValid==true) {
            sb.setLength(0);
            while(symbol.attribute().charAt(idx)!='='){
                if(Character.isLetter(symbol.attribute().charAt(idx))) {
                    sb.append(symbol.attribute().charAt(idx));
                }
                idx++;
                if(idx>=symbol.attribute().length()){
                    if(symbol.attribute().charAt(idx-1)!='}')
                    {
                        throw new StateMachineException("Parser wrong char");
                    }
                    isValid=false;
                    isEnd=true;
                    break;
                }
            }
            idx++;
            if(isValid==true) {
                if(symbol.attribute().charAt(idx)!='>')
                {
                    throw new StateMachineException("Parser wrong char");
                }
                idx++;
                String commandName = sb.toString();
                sb.setLength(0);
                while(symbol.attribute().charAt(idx)!='\n'){
                    if(Character.isLetter(symbol.attribute().charAt(idx))) {
                        sb.append(symbol.attribute().charAt(idx));
                    }
                    idx++;
                    if(idx>=symbol.attribute().length()){
                        if(symbol.attribute().charAt(idx-1)!='}')
                        {
                            throw new StateMachineException("Parser wrong char");
                        }
                        isValid=false;
                        break;
                    }
                }
                idx++;

                if(isValid==true) {
                    String targetName = sb.toString();
                    stateDefinition.addTransition(new TransitionDefinition(commandName,targetName));
                }
            }
        }
        if(isEnd==true){
            definition.addState(name,stateDefinition);
        }
        consume();
    }

    private List<String> actions(){
        //TODO your code goes here
        ArrayList<String> actionsList=new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        while(symbol.attribute().charAt(idx)!='}'){
            if(Character.isLetter(symbol.attribute().charAt(idx))) {
                sb.append(symbol.attribute().charAt(idx));
            }
            if(idx>=symbol.attribute().length()){
                throw new StateMachineException("Parser wrong char");
            }
            idx++;
            if(symbol.attribute().charAt(idx)==' '||symbol.attribute().charAt(idx)=='}')
            {
                actionsList.add(sb.toString());
                sb.setLength(0);
            }
        }
        return actionsList;
    }

    private TransitionDefinition transition(int idx) {
        //TODO your code goes here
        return new TransitionDefinition(null, null);
    }

    private void match(TokenType expectedSymbol) {
        //TODO your code goes here
        if(symbol.tokenType()!=expectedSymbol){
            throw new StateMachineException("Parser error");
        }
    }

    private void consume() {
        //TODO your code goes here
        symbol=lexer.nextToken();
    }
}
