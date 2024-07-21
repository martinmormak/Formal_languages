import fei.tuke.sk.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class LanguageTest {

    @Test
    public void testParser() {
        String input = "3 + 4 * 5 - 6 / 2 ^ 3 ⊕ 2 x x x 3";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(29, result);
    }

    @Test
    public void testParserWithParentheses() {
        String input = "2 * ( 3 + 4 ) - 5 * ( 2 - 1 )";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(9, result);
    }

    @Test
    public void testParserWithUnaryMinus() {
        String input = "- 3 + 4 * 5";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(17, result);
    }

    @Test
    public void testParserWithMultiplicationAndDivision() {
        String input = "- 3 * 24 / 4 * 3";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(-54, result);
    }

    @Test
    public void testParserWithUnaryMinusAndPower() {
        String input = "-2 ^ 3";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(-8, result);

        input = "2 ^ -3";
        lexer = new Lexer(new StringReader(input));
        parser = new Parser(lexer);
        result = parser.statement();

        Assert.assertEquals(0, result);
    }

    @Test
    public void testParserWithExponentiation() {
        String input = "2 ^ 3 ^ 2";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(512, result);
    }


    @Test
    public void testParserWithNestedBrackets() {
        String input = "((3+4)*2+((-3+-2)*(8^(8-3)-4)))/(4*9/(5+1))";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(-40951, result);
    }

    @Test
    public void testParserWithXxxOperator() {
        String input = "2 x x x 3 + 4";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(10, result);
    }

    @Test
    public void testParserWithMultipleOperators() {
        String input = "2 + 3 * 4 - 5 / 2 ^ 2 ⊕ 1 x x x 2";
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        int result = parser.statement();

        Assert.assertEquals(15, result);
    }

    @Test
    public void testParserStatement() {
        Parser parser = new Parser(new Lexer(new StringReader("1+2--3")));
        int result = parser.statement();
        Assert.assertEquals(6, result);

        parser = new Parser(new Lexer(new StringReader("3+4*5-6/2^3⊕2xxx3")));
        result = parser.statement();
        Assert.assertEquals(29, result);

        parser = new Parser(new Lexer(new StringReader("1+2---3")));
        try {
            result = parser.statement();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (ParserCalculatorException e) {
            Assert.assertEquals("Parser error: Wrong char", e.getError());
        }

        parser = new Parser(new Lexer(new StringReader("1+2++2")));
        try {
            result = parser.statement();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (ParserCalculatorException e) {
            Assert.assertEquals("Parser error: Wrong char", e.getError());
        }

        parser = new Parser(new Lexer(new StringReader("(1+2*(3*8-8)-3")));
        try {
            result = parser.statement();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (ParserCalculatorException e) {
            Assert.assertEquals("Parser error: Mising right closer", e.getError());
        }

        parser = new Parser(new Lexer(new StringReader("(1+2)*3+2*(8+3)-8)")));
        try {
            result = parser.statement();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (ParserCalculatorException e) {
            Assert.assertEquals("Parser error: Wrong symbol.", e.getError());
        }
    }

    @Test
    public void testLexerNextToken() {
        Lexer lexer = new Lexer(new StringReader("1+2-3"));
        Assert.assertEquals(Token.NUMBER, lexer.nextToken());
        Assert.assertEquals(1, lexer.getValue());
        Assert.assertEquals(Token.PLUS, lexer.nextToken());
        Assert.assertEquals(Token.NUMBER, lexer.nextToken());
        Assert.assertEquals(2, lexer.getValue());
        Assert.assertEquals(Token.MINUS, lexer.nextToken());
        Assert.assertEquals(Token.NUMBER, lexer.nextToken());
        Assert.assertEquals(3, lexer.getValue());
        Assert.assertEquals(Token.EOF, lexer.nextToken());

        lexer = new Lexer(new StringReader("1xx2"));
        try {
            Assert.assertEquals(Token.NUMBER, lexer.nextToken());
            Assert.assertEquals(1, lexer.getValue());
            lexer.nextToken();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (LexerCalculatorException e) {
            Assert.assertEquals("Lexer error: Multiplication is not valid", e.getError());
        }

        lexer = new Lexer(new StringReader("1xxxx2"));
        try {
            Assert.assertEquals(Token.NUMBER, lexer.nextToken());
            Assert.assertEquals(1, lexer.getValue());
            lexer.nextToken();
            Assert.fail("Expected ParserCalculatorException was not thrown");
        } catch (LexerCalculatorException e) {
            Assert.assertEquals("Lexer error: Multiplication is not valid", e.getError());
        }
    }
}
