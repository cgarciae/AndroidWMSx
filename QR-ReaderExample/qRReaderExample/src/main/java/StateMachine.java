import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Cristian on 6/5/2015.
 */
public class StateMachine<A,B> extends StateBehaviour<A,B> implements Stream<A> {

    private StateBehaviour<A,B> state;
    HashMap<A,StateBehaviour<A,B>> stateMap = new HashMap<A,StateBehaviour<A,B>>();
    LinkedList<Action1<A>> onDataListeners = new LinkedList<Action1<A>>();

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
        state.onEnter(state.key);

        for (StateBehaviour<A,B> stateBehaviour : states) {
            stateMap.put(stateBehaviour.key, stateBehaviour);
        }
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
