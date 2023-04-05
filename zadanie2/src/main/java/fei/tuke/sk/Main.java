package fei.tuke.sk;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // TODO: your code goes here
        try {
            /*Scanner scanner = new Scanner(System.in);
            String input=scanner.nextLine();
            input=input+'\0';
            Reader reader=new StringReader(input);*/
            Reader reader=new InputStreamReader(System.in);
            Lexer lexer = new Lexer(reader);
            int result = new Parser(lexer).statement();

            System.out.printf("\nResult %d\n", result);
        } catch (ParserCalculatorException e) {
            System.out.println(e.getError());
        } catch (LexerCalculatorException e) {
            System.out.println(e.getError());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}