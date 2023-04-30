package fei.tuke.sk.stmlang;

public record Token(TokenType tokenType, String attribute) {

    public Token(TokenType type) {
        this(type, null);
    }
}