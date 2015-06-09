import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Cristian on 6/5/2015.
 */
public class StateMachine<A,B> extends StateBehaviour<A,B> implements Stream<A> {

    private StateBehaviour<A,B> state;
    HashMap<A,StateBehaviour<A,B>> stateMap = new HashMap<A,StateBehaviour<A,B>>();
    LinkedList<Action1<A>> onDataListeners = new LinkedList<Action1<A>>();



    public StateMachine (StateBehaviour<A,B>... states) throws Exception {
        super(states[0].key);
        init(states);
    }

    public StateMachine (A key, StateBehaviour<A,B>... states) throws Exception {
        super(key);
        init(states);
    }

    void init (StateBehaviour<A,B>... states) throws Exception{
        state = states[0];

        for (StateBehaviour<A,B> stateBehaviour : states) {
            addState (stateBehaviour);
        }
    }

    public void Start () {
        state.onEnter(state.key);
    }

    public void addState (StateBehaviour<A,B> newState) throws Exception{

        if (stateMap.containsKey(newState.key)) {
            throw new Exception("key already exists");
        }
        stateMap.put(newState.key, newState);
    }

    public StateBehaviour<A,B> getNewState (A stateKey) throws Exception {
        StateBehaviour<A,B> newState = new StateBehaviour<A,B>(stateKey);
        addState(newState);
        return newState;
    }

    @Override
    public A onNewData(B value) {
        //Logica de StateMachine
        handleData(value);
        //Logica de StateBehaviour
        return super.onNewData(value);
    }

    void handleData (B value) {
        //Pasar datos al estado actual
        A newKey = state.onNewData(value);

        //Parar si el "key" es el mismo
        if (newKey == state.key)
            return;

        //Obtener el nuevo estado
        StateBehaviour<A,B> newState = stateMap.get(newKey);

        //Anunciar salida y entrada en cambio de estado
        state.onExit(newKey);
        newState.onEnter(state.key);

        //Cambiar estado
        state = newState;

        //Anunciar cambio de estado como stream de "keys"
        for (Action1<A> f : new LinkedList<Action1<A>> (onDataListeners)) {
            f.apply (newKey);
        }
    }

    @Override
    public A getKey() {
        return key;
    }

    @Override
    public void OnData(Action1<A> action) {
        onDataListeners.add (action);
    }
}

interface Action1<A> {
    void apply(A value);
}

interface Func1<A,B> {
    B apply(A value);
}

interface Func2<A,B,C> {
    C apply(A v1, B v2);
}

interface Stream<A> {

    void OnData (Action1<A> action);
}
