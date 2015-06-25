import java.util.ArrayList;

/**
 * Created by Cristian on 6/5/2015.
 */

public class StateBehaviour<A,B> {

    public final A key;
    private ArrayList<Action2<B,A>> onEnterList = new ArrayList<>();
    private ArrayList<Action2<B,A>> onExitList = new ArrayList<>();
    private ArrayList<Action2<B,A>> onDataList = new ArrayList<> ();
    private ArrayList<Func2<B,A,A>> transitions = new ArrayList<>();
    protected Boolean started = false;


    public StateBehaviour(A key) {
        this.key = key;
    }

    public A getKey() {
        return key;
    }

    /**
     * [move] recibe
     */
    public A move(B value) {

        for (Action2<B,A> f : new ArrayList<>(onDataList)) {
            f.apply (value, key);
        }

        for (Func2<B,A,A> f : new ArrayList<> (transitions)) {
            A a = f.apply(value, key);

            if (a != key)
                return a;
        }

        return key;
    }

    public void onEnter (B event, A oldKey) {
        for (Action2<B,A> f : new ArrayList<> (onEnterList)) {
            f.apply (event, oldKey);
        }
    }

    public void onExit (B event, A newKey) {
        for (Action2<B,A> f : new ArrayList<> (onExitList)) {
            f.apply (event, newKey);
        }
    }

    public StateBehaviour<A,B> addOnEnterListener (Action2<B,A> listener) {
        onEnterList.add(listener);
        return this;
    }

    public StateBehaviour<A,B> removeOnEnterListener (Action2<B,A> listener) {
        onEnterList.remove(listener);
        return this;
    }

    public StateBehaviour<A,B> addOnExitListener (Action2<B,A> reactive) {
        onExitList.add(reactive);
        return this;
    }

    public StateBehaviour<A,B> removeOnExitListener (Action2<B,A> reactive) {
        onEnterList.remove(reactive);
        return this;
    }

    public StateBehaviour<A,B> addTransition (Func2<B,A,A> transition) {
        transitions.add(transition);
        return this;
    }

    public StateBehaviour<A,B> removeTransition (Func2<B,A,A> transition) {
        transitions.remove(transition);
        return this;
    }

    public StateBehaviour<A,B> addOnDataListener (Action2<B,A> action) {
        onDataList.add(action);
        return this;
    }

    public StateBehaviour<A,B> removeOnDataListener (Action2<B,A> action) {
        onDataList.remove(action);
        return this;
    }

    public void start() {

        if (started)
            return;

        onEnter(null, key);
        started = true;
    }
}
