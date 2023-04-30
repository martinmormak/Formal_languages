package fei.tuke.sk.stmlang;

import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //TODO your code goes here
        try {
            Reader reader = new FileReader("src/main/input.txt");
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);
            StateMachineDefinition stateMachineDefinition=parser.stateMachine();
            Writer writer=new FileWriter("src/files/program.c",false);
            Generator generator=new Generator(stateMachineDefinition,writer);
            generator.generate();
            writer.close();
        }catch (StateMachineException e){
            System.out.println(e.getError());
        }catch (IOException e){
            System.out.println("Problem to write program.");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}