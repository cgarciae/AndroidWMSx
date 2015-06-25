import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Cristian on 6/5/2015.
 */
public class StateMachine<K,A,B> extends StateBehaviour<K,B> implements Stream<A> {

    public final StateBehaviour<A,B> initialState;
    private StateBehaviour<A,B> state;
    private HashMap<A,StateBehaviour<A,B>> stateMap = new HashMap<>();
    private LinkedList<Action1<A>> onDataListeners = new LinkedList<>();


    public StateMachine (K key, StateBehaviour<A,B> initialState, StateBehaviour<A,B>... states) throws Exception {
        super(key);
        this.initialState = initialState;
        init(initialState, states);
    }

    void init (StateBehaviour<A,B> initialState, StateBehaviour<A,B>... states) throws Exception{
        state = initialState;
        addState(initialState);

        for (StateBehaviour<A,B> stateBehaviour : states) {
            addState (stateBehaviour);
        }
    }

    public StateBehaviour<A,B> getState () {return state;}

    @Override
    public void start() {
        super.start();
        state.start();
    }

    @Override
    public void onEnter(B event, K oldKey) {
        super.onEnter(event, oldKey);
        state.onEnter(event, state.key);
    }

    @Override
    public void onExit(B event, K newKey) {
        super.onExit(event, newKey);
        state.onExit(event, state.key);
    }

    public void addState (StateBehaviour<A,B> newState) throws Exception{

        if (stateMap.containsKey(newState.key)) {
            throw new Exception("key already exists");
        }
        stateMap.put(newState.key, newState);
    }

    public StateBehaviour<A,B> getNewState (A stateKey) throws Exception {
        StateBehaviour<A,B> newState = new StateBehaviour<>(stateKey);
        addState(newState);
        return newState;
    }

    @Override
    public K move(B value) {

        if (! started)
            start();

        //Logica de StateMachine
        handleData(value);
        //Logica de StateBehaviour
        return super.move(value);
    }

    private void handleData (B event) {
        //Pasar datos al estado actual
        A newKey = state.move(event);

        //Parar si el "key" es el mismo
        if (newKey == state.key)
            return;

        //Obtener el nuevo estado
        StateBehaviour<A,B> newState = stateMap.get(newKey);

        //Anunciar salida y entrada en cambio de estado
        state.onExit(event, newKey);
        newState.onEnter(event, state.key);

        //Cambiar estado
        state = newState;

        //Anunciar cambio de estado como stream de "keys"
        for (Action1<A> f : new LinkedList<> (onDataListeners)) {
            f.apply (newKey);
        }
    }

    @Override
    public void OnData(Action1<A> action) {
        onDataListeners.add (action);
    }

    public void restart () {
        started = false;
        state = initialState;
    }
}

interface Action {
    void apply();
}

interface Action1<A> {
    void apply(A value);
}

interface Action2<A,B> {
    void apply(A v1, B v2);
}

interface Action3<A,B,C> {
    void apply(A v1, B v2, C v3);
}

interface Func<A> {
    A apply();
}

interface Func1<A,B> {
    B apply(A value);
}

interface Func2<A,B,C> {
    C apply(A v1, B v2);
}

interface Func3<A,B,C,D> {
    D apply(A v1, B v2, C v3);
}

interface Stream<A> {

    void OnData (Action1<A> action);
}
