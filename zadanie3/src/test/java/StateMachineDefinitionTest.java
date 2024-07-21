import fei.tuke.sk.stmlang.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StateMachineDefinitionTest {
    @Test
    public void testParserCommands(){
        String input = "commands {\nopenDoor          'd'\nopenWindow        'o'\ncloseDoor         'c'\nclosePanel        'p'\ncloseWindow       'w'\nlightOn           's'\nlightOff          't'\nairConditionerOn  'a'\nairConditionerOff 'x'\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));
        StateMachineDefinition definition=parser.stateMachine();

        Assert.assertEquals(9,definition.getCommands().size());

        input = "comands {\nopenDoor          'd'\nopenWindow        'o'\ncloseDoor         'c'\nclosePanel        'p'\ncloseWindow       'w'\nlightOn           's'\nlightOff          't'\nairConditionerOn  'a'\nairConditionerOff 'x'\n}";
        parser = new Parser(new Lexer(new StringReader(input)));

        try {
            definition=parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "commands {\nopenDoor          'o'\nopenWindow        'o'\ncloseDoor         'c'\nclosePanel        'p'\ncloseWindow       'w'\nlightOn           's'\nlightOff          't'\nairConditionerOn  'a'\nairConditionerOff 'x'\n}";
        parser = new Parser(new Lexer(new StringReader(input)));

        try {
            definition=parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Two different commands have same character", e.getError());
        }

        input = "commands {\nopenDoor          'd'\nopenWindow        'o'\ncloseDoor         'c'\nclosePanel        'p'\ncloseWindow       'w'\nlightOn           's'\nlightOff          't'\nairConditionerOn  'a'\nairConditionerOff 'x'\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition=parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
        }
    }
    @Test
    public void testParserResetCommands() {
        String input = "commands {\nopenDoor          'd'\n}\n\nresetCommands {\nopenDoor\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));
        StateMachineDefinition definition = parser.stateMachine();

        Assert.assertEquals(1, definition.getResetCommands().size());

        input = "commands {\nopenDoor          'd'\n}\n\nresetComands {\nopenDoor\n}";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "commands {\nopenDoor          'd'\n}\n\nresetCommands {\nopenDoor\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
        }


        input = "commands {\n}\n\nresetCommands {\nopenDoor\n}";

        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Reset command is not command", e.getError());
        }
    }

    @Test
    public void testParserEvents() {
        String input = "events {\nunlockPanel 'U'\nlockPanel   'L'\nlockDoor    'C'\nunlockDoor  'D'\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));
        StateMachineDefinition definition = parser.stateMachine();

        Assert.assertEquals(4, definition.getEvents().size());

        input = "event {\nunlockPanel 'U'\nlockPanel   'L'\nlockDoor    'C'\nunlockDoor  'D'\n}";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "events {\nunlockPanel 'U'\nlockPanel   'L'\nlockDoor    'C'\nunlockDoor  'D'\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
        }
    }



    @Test
    public void testParserState() {
        String input = "commands {\nopenDoor          'd'\ncloseDoor         'c'\n}\nevents {\n lockPanel   'L'\nunlockDoor  'D'\n}\nstate idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n}\nstate active {\nopenDoor => idle\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));


        StateMachineDefinition definition = parser.stateMachine();

        for (Map.Entry<String, StateDefinition> set : definition.getStates().entrySet()) {
            if(set.getKey().equals("idle")) {
                Assert.assertEquals("idle", set.getKey());
                Assert.assertEquals("unlockDoor", set.getValue().getActions().get(0).toString());
                Assert.assertEquals("lockPanel", set.getValue().getActions().get(1).toString());
                Assert.assertEquals("active", set.getValue().getTransitions().get(0).targetName());
                Assert.assertEquals("closeDoor", set.getValue().getTransitions().get(0).commandName());
            }else if (set.getKey().equals("active")){
                if(set.getKey().equals("idle")) {
                    Assert.assertEquals("idle", set.getKey());
                    Assert.assertEquals("unlockDoor", set.getValue().getActions().get(0).toString());
                    Assert.assertEquals("lockPanel", set.getValue().getActions().get(1).toString());
                    Assert.assertEquals("idle", set.getValue().getTransitions().get(0).targetName());
                    Assert.assertEquals("openDoor", set.getValue().getTransitions().get(0).commandName());
                }
            }
        }

        input = "commands {\nopenDoor          'd'\ncloseDoor         'c'\n}\nevents {\n lockPanel   'L'\nunlockDoor  'D'\n}\nstat idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n}\nstate active {\nopenDoor => idle\n}";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "commands {\nopenDoor          'd'\ncloseDoor         'c'\n}\nevents {\n lockPanel   'L'\nunlockDoor  'D'\n}\nstate idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n}\nstate active {\nopenDoor => idle\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
        }
    }

    @Test
    public void testOutputFile() throws IOException {

        String input = "commands {\n" +
                "    openDoor          'd'\n" +
                "    openWindow        'o'\n" +
                "    closeDoor         'c'\n" +
                "    closePanel        'p'\n" +
                "    closeWindow       'w'\n" +
                "    lightOn           's'\n" +
                "    lightOff          't'\n" +
                "    airConditionerOn  'a'\n" +
                "    airConditionerOff 'x'\n" +
                "}\n" +
                "\n" +
                "resetCommands {\n" +
                "    openDoor\n" +
                "}\n" +
                "\n" +
                "events {\n" +
                "    unlockPanel 'U'\n" +
                "    lockPanel   'L'\n" +
                "    lockDoor    'C'\n" +
                "    unlockDoor  'D'\n" +
                "}\n" +
                "\n" +
                "state idle {\n" +
                "    actions {unlockDoor lockPanel}\n" +
                "    closeDoor => active\n" +
                "}\n" +
                "\n" +
                "state active {\n" +
                "    lightOn => waitingForAirConditionerAndWindow\n" +
                "    airConditionerOn => waitingForLightAndWindow\n" +
                "    openWindow => waitingForAirConditionerAndLight\n" +
                "}\n" +
                "\n" +
                "state waitingForAirConditionerAndWindow {\n" +
                "    airConditionerOn => waitingForLight\n" +
                "    openWindow => waitingForAirConditioner\n" +
                "    lightOff => active\n" +
                "}\n" +
                "\n" +
                "state waitingForLightAndWindow {\n" +
                "    lightOn => waitingForWindow\n" +
                "    openWindow => waitingForLight\n" +
                "    airConditionerOff => active\n" +
                "}\n" +
                "\n" +
                "state waitingForAirConditionerAndLight {\n" +
                "    lightOn => waitingForAirConditioner\n" +
                "    airConditionerOn => waitingForLight\n" +
                "    closeWindow => active\n" +
                "}\n" +
                "\n" +
                "state waitingForAirConditioner {\n" +
                "    airConditionerOn => unlockedPanel\n" +
                "    lightOff => waitingForAirConditionerAndLight\n" +
                "    closeWindow => waitingForAirConditionerAndWindow\n" +
                "}\n" +
                "\n" +
                "state waitingForLight {\n" +
                "    lightOn => unlockedPanel\n" +
                "    airConditionerOff => waitingForAirConditionerAndLight\n" +
                "    closeWindow => waitingForLightAndWindow\n" +
                "}\n" +
                "\n" +
                "state waitingForWindow {\n" +
                "    openWindow => unlockedPanel\n" +
                "    lightOff => waitingForLightAndWindow\n" +
                "    airConditionerOff => waitingForAirConditionerAndWindow\n" +
                "}\n" +
                "\n" +
                "state unlockedPanel {\n" +
                "    actions {unlockPanel lockDoor}\n" +
                "    closePanel => idle\n" +
                "}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));

        StateMachineDefinition stateMachineDefinition=parser.stateMachine();
        Writer writer=new FileWriter("src/test/java/program.c",false);
        Generator generator=new Generator(stateMachineDefinition,writer);
        generator.generate();
        writer.close();

        String expectedOutput = "#include \"common.h\"\n" +
                "void state_idle();\n" +
                "void state_active();\n" +
                "void state_waitingForAirConditionerAndWindow();\n" +
                "void state_waitingForLightAndWindow();\n" +
                "void state_waitingForAirConditionerAndLight();\n" +
                "void state_waitingForAirConditioner();\n" +
                "void state_waitingForLight();\n" +
                "void state_waitingForWindow();\n" +
                "void state_unlockedPanel();\n" +
                "void state_idle() {\n" +
                "\tsend_event('D');\n" +
                "\tsend_event('L');\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'c': {\n" +
                "\t\t\t\treturn state_active();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_active() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 's': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'a': {\n" +
                "\t\t\t\treturn state_waitingForLightAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'o': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForAirConditionerAndWindow() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'a': {\n" +
                "\t\t\t\treturn state_waitingForLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'o': {\n" +
                "\t\t\t\treturn state_waitingForAirConditioner();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 't': {\n" +
                "\t\t\t\treturn state_active();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForLightAndWindow() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 's': {\n" +
                "\t\t\t\treturn state_waitingForWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'o': {\n" +
                "\t\t\t\treturn state_waitingForLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'x': {\n" +
                "\t\t\t\treturn state_active();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForAirConditionerAndLight() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 's': {\n" +
                "\t\t\t\treturn state_waitingForAirConditioner();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'a': {\n" +
                "\t\t\t\treturn state_waitingForLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'w': {\n" +
                "\t\t\t\treturn state_active();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForAirConditioner() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'a': {\n" +
                "\t\t\t\treturn state_unlockedPanel();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 't': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'w': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForLight() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 's': {\n" +
                "\t\t\t\treturn state_unlockedPanel();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'x': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndLight();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'w': {\n" +
                "\t\t\t\treturn state_waitingForLightAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_waitingForWindow() {\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'o': {\n" +
                "\t\t\t\treturn state_unlockedPanel();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 't': {\n" +
                "\t\t\t\treturn state_waitingForLightAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'x': {\n" +
                "\t\t\t\treturn state_waitingForAirConditionerAndWindow();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void state_unlockedPanel() {\n" +
                "\tsend_event('U');\n" +
                "\tsend_event('C');\n" +
                "\tchar ev;\n" +
                "\twhile (ev = read_command()) { \n" +
                "\t\tswitch (ev) {\n" +
                "\t\t\tcase 'p': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t\tcase 'd': {\n" +
                "\t\t\t\treturn state_idle();\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "void main() {\n" +
                "\tstate_idle();\n" +
                "}";
        String actualOutput = new String(Files.readAllBytes(Path.of("src/test/java/program.c")));

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
