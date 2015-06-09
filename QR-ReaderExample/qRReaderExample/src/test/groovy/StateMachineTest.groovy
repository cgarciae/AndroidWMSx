import groovy.transform.CompileStatic;
import junit.framework.TestCase;
import org.junit.After;

/**
 * Created by Cristian on 6/9/2015.
 */
@CompileStatic
public class StateMachineTest extends TestCase {

    StateMachine<String, Integer> stateMachine;
    StateBehaviour<String, Integer> stateOne;
    StateBehaviour<String, Integer> stateTwo;

    public void setUp () throws Exception {
        stateOne = new StateBehaviour<String, Integer>("1");
        stateTwo = new StateBehaviour<String, Integer>("2");
        stateMachine = new StateMachine<String, Integer>("0", [stateOne, stateTwo] as StateBehaviour[]);
    }


    public void testOnNewData() throws Exception {
        String result = "y";
        stateOne.addOnEnterListener({String value ->
            result = value;
        });
        stateMachine.Start();
        assertEquals(result, stateOne.key);
    }

    public void testOnNewData2() throws Exception {
        System.out.println("Test 2");
    }

    @After
    public void tearDown() {

        System.out.println("@After - tearDown");
    }
}