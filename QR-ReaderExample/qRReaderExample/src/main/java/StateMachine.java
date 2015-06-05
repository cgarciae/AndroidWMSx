import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cristian on 6/5/2015.
 */
public class StateMachine<A,B> extends StateBehaviour<A,B> implements Stream<A> {

    private StateBehaviour<A,B> state;
    HashMap<A,StateBehaviour<A,B>> stateMap = new HashMap<A,StateBehaviour<A,B>>();

    public StateMachine(StateBehaviour<A,B> initialState, StateBehaviour<A,B>... states) {
        super(initialState.key);
        init(initialState, states);
    }

    public StateMachine(A key, StateBehaviour<A,B> initialState, StateBehaviour<A,B>... states) {
        super(key);
        init(initialState, states);
    }

    void init (StateBehaviour<A,B> initialState, StateBehaviour<A,B>... states ) {
        state = initialState;

        for (StateBehaviour<A,B> stateBehaviour : states) {
            stateMap.put(stateBehaviour.key, stateBehaviour);
        }
    }

    @Override
    public A getKey() {
        return key;
    }

    @Override
    public void receive(B value) {

    }

    @Override
    public void OnData(Reactive<A> listener) {

    }
}

interface Reactive<A> {
    void receive(A value);
}

interface Transition<A,B> {
    A changeWith (B value);
}

interface Stream<A> {

    void OnData (Reactive<A> listener);
}
