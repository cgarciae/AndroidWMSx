import java.util.ArrayList;

/**
 * Created by Cristian on 6/5/2015.
 */

public class StateBehaviour<A,B> {

    public final A key;
    private ArrayList<Action1<A>> onEnterList = new ArrayList<Action1<A>>();
    private ArrayList<Action1<A>> onExitList = new ArrayList<Action1<A>>();
    private ArrayList<Action1<B>> onDataList = new ArrayList<Action1<B>> ();
    private ArrayList<Func1<B,A>> transitions = new ArrayList<Func1<B,A>>();
    protected Boolean started = false;


    public StateBehaviour(A key) {
        this.key = key;
    }

    /**
     * [move] recibe
     */
    public A move(B value) {

        for (Action1<B> f : new ArrayList<Action1<B>>(onDataList)) {
            f.apply (value);
        }

        for (Func1<B,A> f : new ArrayList<Func1<B,A>> (transitions)) {
            A a = f.apply(value);

            if (a != key)
                return a;
        }

        return key;
    }

    public void onEnter (A value) {
        for (Action1<A> f : new ArrayList<Action1<A>> (onEnterList)) {
            f.apply (value);
        }
    }

    public void onExit (A value) {
        for (Action1<A> f : new ArrayList<Action1<A>> (onExitList)) {
            f.apply (value);
        }
    }

    public StateBehaviour<A,B> addOnEnterListener (Action1<A> reactive) {
        onEnterList.add(reactive);
        return this;
    }

    public StateBehaviour<A,B> removeOnEnterListener (Action1<A> reactive) {
        onEnterList.remove(reactive);
        return this;
    }

    public StateBehaviour<A,B> addOnExitListener (Action1<A> reactive) {
        onExitList.add(reactive);
        return this;
    }

    public StateBehaviour<A,B> removeOnExitListener (Action1<A> reactive) {
        onEnterList.remove(reactive);
        return this;
    }

    public StateBehaviour<A,B> addTransition (Func1<B,A> transition) {
        transitions.add(transition);
        return this;
    }

    public StateBehaviour<A,B> removeTransition (Func1<B,A> transition) {
        transitions.remove(transition);
        return this;
    }

    public StateBehaviour<A,B> addOnDataListener (Action1<B> action) {
        onDataList.add(action);
        return this;
    }

    public StateBehaviour<A,B> removeOnDataListener (Action1<B> action) {
        onDataList.remove(action);
        return this;
    }

    public void start() {

        if (started)
            return;

        onEnter(key);
        started = true;
    }
}
