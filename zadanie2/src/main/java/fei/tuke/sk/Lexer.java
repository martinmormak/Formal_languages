package fei.tuke.sk;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.Reader;

public class Lexer {
    private int current;
    private int value;
    private final Reader input;
    private boolean isFirst=true;
    public Lexer(Reader reader) {
        this.input = new BufferedReader(reader);
    }

    public Token nextToken() {
        // TODO: your code goes here
        if(this.isFirst==true){
            this.consume();
            this.isFirst=false;
        }
        char currentChar = (char) current;

        switch (current) {
            case '+', '⊕':
                this.consume();
                return Token.PLUS;
            case '-':
                this.consume();
                return Token.MINUS;
            case '*':
                this.consume();
                return Token.MULTIPLICATION;
            case 'x':
                validMul();
                return Token.MULTIPLICATION;
            case '/':
                this.consume();
                return Token.DIVISION;
            case '^':
                this.consume();
                return Token.POWER;
            case '(':
                this.consume();
                return Token.LEFTPAR;
            case ')':
                this.consume();
                return Token.RIGHTPAR;
            case ' ':
                this.consume();
                return Token.SPACE;
            case '0':
                this.consume();
                if(current>='0'&&current<='9'){
                    throw new LexerCalculatorException("Invalid number");
                }
                return Token.NUMBER;
            case '1','2','3','4','5','6','7','8','9':
                countNumber();
                return Token.NUMBER;
            case '\0','\n',-1:
                return Token.EOF;
            default:
                throw new LexerCalculatorException("Wrong char: "+currentChar);
        }
        //return Token.EOF; // remove me
    }

    private void countNumber(){
        StringBuilder stringBuilder = new StringBuilder();

        while(current>='0'&&current<='9'){
            stringBuilder.append((char)current);
            removeSpaces();
        }
        value=Integer.parseInt(stringBuilder.toString());
    }
    private void validMul(){
        StringBuilder correct = new StringBuilder();
        correct.append("xxx");
        StringBuilder stringBuilder = new StringBuilder();

        while(current=='x'){
            stringBuilder.append((char)current);
            removeSpaces();
        }
        if(!stringBuilder.toString().equals("xxx")){
            throw new LexerCalculatorException("Multiplication is not valid");
        }
    }

    private void removeSpaces(){
        do {
            this.consume();
            //current = (char) current;
        }while (current==' ');
    }

    private void consume() {
        // TODO: your code goes here
        try {
            current = input.read();
        }catch (EOFException e){
            current='\0';
        }catch (Exception e){
            throw new LexerCalculatorException("Reading error.");
        }
    }

    public int getValue() {
        return value;
    }
}
