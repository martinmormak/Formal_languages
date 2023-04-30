import fei.tuke.sk.stmlang.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
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
        String input = "resetCommands {\nopenDoor\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));
        StateMachineDefinition definition = parser.stateMachine();

        Assert.assertEquals(1, definition.getResetCommands().size());

        input = "resetComands {\nopenDoor\n}";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "resetCommands {\nopenDoor\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
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
        String input = "state idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n}";
        Parser parser = new Parser(new Lexer(new StringReader(input)));

        StateMachineDefinition definition = parser.stateMachine();

        for (Map.Entry<String, StateDefinition> set : definition.getStates().entrySet()) {
            Assert.assertEquals("idle", set.getKey());
            Assert.assertEquals("unlockDoor", set.getValue().getActions().get(0).toString());
            Assert.assertEquals("lockPanel", set.getValue().getActions().get(1).toString());
            Assert.assertEquals("active", set.getValue().getTransitions().get(0).targetName());
            Assert.assertEquals("closeDoor", set.getValue().getTransitions().get(0).commandName());
        }

        input = "stat idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n}";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Wrong character", e.getError());
        }

        input = "state idle {\nactions {unlockDoor lockPanel}\ncloseDoor => active\n";
        parser = new Parser(new Lexer(new StringReader(input)));
        try {
            definition = parser.stateMachine();
            Assert.fail("Expected StateMachineException was not thrown");
        } catch (StateMachineException e) {
            Assert.assertEquals("State machine error: Lexer this is not letter\uFFFF", e.getError());
        }
    }
}
