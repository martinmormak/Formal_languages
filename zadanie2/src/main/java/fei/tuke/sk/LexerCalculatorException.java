package fei.tuke.sk;

public class LexerCalculatorException extends RuntimeException {
    // TODO: create new CalculatorException
    private String error;

    public LexerCalculatorException(String error){
        this.error=error;
    }

    public String getError() {
        return "Lexer error: "+error;
    }
}