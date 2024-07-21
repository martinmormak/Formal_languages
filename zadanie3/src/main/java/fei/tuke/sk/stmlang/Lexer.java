package fei.tuke.sk.stmlang;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.Reader;
import java.util.Map;

/**
 * Lexical analyzer of the state machine language.
 */
public class Lexer {
    private static final Map<String, TokenType> keywords = Map.of(
            "commands", TokenType.COMMANDS,
            "resetCommands", TokenType.RESET_COMMANDS,
            "events", TokenType.EVENTS,
            "state", TokenType.STATE,
            "actions", TokenType.ACTIONS
    );

    private final Reader input;
    private int current;

    private boolean isFirst=true;

    public Lexer(Reader input) {
        this.input = new BufferedReader(input);
    }

    public Token nextToken() {
        //TODO  your code goes here
        if(this.isFirst==true){
            this.consume();
            this.isFirst=false;
        }

        while (Character.isWhitespace(current)) {
            this.consume();
        }

        if (Character.isLetter(current)) {
            return readNameOrKeyword();
        }

        switch (current){
            case '\0','\n',-1:
                return new Token(TokenType.EOF);
            default:
                throw new StateMachineException("Wrong char: "+ (char)current);
        }
        //return new Token(null);
    }

    private Token readNameOrKeyword() {
        //TODO your code goes here
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(current)) {
            sb.append((char) current);
            this.consume();
        }
        String name = sb.toString();
        while (Character.isWhitespace(current)) {
            this.consume();
        }
        if (keywords.containsKey(name)) {
            sb.setLength(0);
            int closers;
            if(current=='{'){
                closers=1;
            }else {
                closers=-1;
            }
            while(closers!=0) {
                if (Character.isLetter(current)|Character.isWhitespace(current)||current=='{'||current=='\''||current=='\n'||current==13||current=='}'||current=='='||current=='>') {
                    sb.append((char) current);
                    this.consume();
                }else {
                    throw new StateMachineException("Lexer this is not letter"+ (char)current);
                }
                if(current=='{'){
                    if(closers==-1){
                        closers=0;
                    }
                    closers++;
                }else if(current=='}'){
                    closers--;
                }
            }
            sb.append((char) current);
            this.consume();
            this.consume();
            String body = sb.toString();
            return new Token(keywords.get(name), body);
        } else {
            if(name.equals('\n')||name.equals('\0')||name.equals(-1)) {
                return new Token(TokenType.EOF);
            }else {
                throw new StateMachineException("Wrong character");
            }
        }
        //return new Token(null);
    }

    private void consume() {
        //TODO your code goes here
        try {
            current = input.read();
        }catch (EOFException e){
            current='\0';
        }catch (Exception e){
            throw new StateMachineException("Lexer reading error.");
        }
    }
}