package fei.tuke.sk;

public class Parser {
    private final Lexer lexer;
    private Token symbol;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public int statement() {
        // TODO your code goes here
        removeSpaces();
        int buf = S();
        if(symbol!=Token.NUMBER&&symbol!=Token.EOF){
            throw new ParserCalculatorException("Wrong symbol.");
        }
        removeSpaces();
        if(symbol==Token.EOF){
            return buf;
        }else {
            throw new ParserCalculatorException("Wrong symbol.");
        }
        //return 0; // remove me
    }

    private int S() {
        int buf;
        buf=A();
        Token symbolBuf = null;
        while(symbol==Token.PLUS||symbol==Token.MINUS){
            symbolBuf=symbol;
            removeSpaces();
            if(symbolBuf==Token.PLUS){
                /*if(symbol==Token.PLUS){
                    throw new ParserCalculatorException("Two plus symbols in the row.");
                }*/
                buf=buf+A();
            }else if(symbolBuf==Token.MINUS){
                buf=buf-A();
            }
        }
        return buf;
    }
    private int A() {
        int bufB=1;
        int bufA = 1;
        bufB=B();
        if(symbol==Token.MULTIPLICATION){
            removeSpaces();
            bufA=A();
        }
        return bufA*bufB;
    }
    private int B() {
        int buf;
        buf=C();
        while (symbol==Token.DIVISION){
            removeSpaces();
            buf=buf/C();
        }
        return buf;
    }

    private int C() {
        int buf;
        if(symbol==Token.MINUS){
            buf=-1;
            removeSpaces();
            buf=D()*buf;
        }else {
            buf=D();
        }
        return buf;
    }
    private int D() {
        int bufE;
        int buf = 1;
        bufE=E();
        if(symbol==Token.POWER){
            removeSpaces();
            if(symbol==Token.MINUS){
                buf=C();
            }else {
                buf = D();
            }
        }
        return (int)Math.pow(bufE,buf);
    }
    private int E() {
        int buf=0;
        if(symbol==Token.LEFTPAR){
            removeSpaces();
            buf=S();
            if(symbol!=Token.RIGHTPAR){
                throw new ParserCalculatorException("Mising right closer");
            }
            removeSpaces();
        }else if (symbol==Token.NUMBER){
            buf=lexer.getValue();
            removeSpaces();
        }else {
            throw new ParserCalculatorException("Wrong char");
        }
        return buf;
    }
    private void removeSpaces(){
        do {
            symbol = lexer.nextToken();
        }while (symbol==Token.SPACE);
    }

    // E -> T {"+" | "-" T}
    /*private int E() {
        // TODO: your code goes here
        return 0; // remove me
    }*/

    private void match() {
        // TODO: your code goes here
    }

    private void consume() {
        // TODO: your code goes here
    }
}
