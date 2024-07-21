package fei.tuke.sk;

public class ParserCalculatorException extends RuntimeException {
    // TODO: create new CalculatorException
    private String error;

    public ParserCalculatorException(String error){
        this.error=error;
    }

    public String getError() {
        return "Parser error: "+error;
    }
}