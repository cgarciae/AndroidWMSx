import groovy.transform.CompileStatic;
import junit.framework.TestCase

/**
 * Created by Cristian on 6/9/2015.
 */
@CompileStatic
public class StateMachineTest extends TestCase {

    StateMachine<State,State, Direction> stateMachine;
    StateBehaviour<State, Direction> stateOne;
    StateBehaviour<State, Direction> stateTwo;

    public void setUp () throws Exception {
        stateOne = new StateBehaviour<State, Direction>(State.One);
        stateTwo = new StateBehaviour<State, Direction>(State.Two);
        stateMachine = new StateMachine<State, State, Direction>(State.Zero, stateOne, [stateTwo] as StateBehaviour[]);

        stateOne.addTransition({Direction n ->
            n > Direction.None ? State.Two : State.One;
        });

        stateTwo.addTransition({Direction n ->
            n < Direction.None ? State.One : State.Two;
        });
    }


    public void tearDown() {

    }


    public void testStart() throws Exception {
        //Resultado Inicial
        State result = State.Zero;

        //Agregar onEnter listener al estado inicial
        stateOne.addOnEnterListener({State value ->
            result = value;
        });

        //Verificar que "result" no haya cambiado
        assertEquals(result != stateOne.key, true);

        //Iniciar la maquina, se debe ejecutar "onEnter"
        stateMachine.start();

        //Verificar result sea la "key" del estado inicial, tal como se definio en la lambda
        assertEquals(result, stateOne.key);
    }

    public void testCambiarEstado() throws Exception {

        stateMachine.move(Direction.None);
        assertEquals(stateMachine.state, stateOne);

        stateMachine.move(Direction.Up);
        assertEquals(stateMachine.state, stateTwo);
    }

    public void testOnExitEnter() throws Exception {
        Boolean exited = false;
        Boolean entered = false;
        stateOne.addOnExitListener({State newState ->
            exited = true;
            assertEquals(newState, State.Two);
        });

        stateTwo.addOnEnterListener({State oldState ->
            entered = true;
            assertEquals(oldState, State.One);
        });

        stateMachine.move(Direction.Up);
        assertEquals(stateMachine.state, stateTwo);
        assertEquals(entered && exited, true);
    }


    public void testOnData () {
        Boolean called = false;
        stateOne.addOnDataListener({Direction dir ->
            called = true;
        });

        stateMachine.move(Direction.Up);
        assertEquals(called, true);
    }

    public void testSubMachine () {

        StateMachine<State, State, Direction> superMachine = new StateMachine<State, State, Direction>(State.Zero, stateMachine);
        StateMachine<State, State, Direction> super2Machine = new StateMachine<State, State, Direction>(State.Zero, superMachine);

        super2Machine.move(Direction.Up);

        assertEquals(stateMachine.state, stateTwo);
    }

    enum Direction {
        Down,
        None,
        Up
    }

    enum State {
        Zero,
        One,
        Two
    }
}