import java.util.ArrayList;

/**
 * Created by Cristian on 6/5/2015.
 */

public class StateBehaviour<A,B> implements Reactive<B> {

    public final A key;

    ArrayList<Reactive<A>> onEnterList = new ArrayList<Reactive<A>>();
    ArrayList<Reactive<A>> onExitList = new ArrayList<Reactive<A>>();
    ArrayList<Transition<A,B>> transitions = new ArrayList<Transition<A,B>>();


    public StateBehaviour(A key) {
        this.key = key;
    }

    @Override
    public void receive(B value) {

    }

    public A getKey() {
        return key;
    }

    StateBehaviour<A,B> addOnEnterListener (Reactive<A> reactive) {
        onEnterList.add(reactive);
        return this;
    }

    StateBehaviour<A,B> removeOnEnterListener (Reactive<A> reactive) {
        onEnterList.remove(reactive);
        return this;
    }

    StateBehaviour<A,B> addOnExitListener (Reactive<A> reactive) {
        onExitList.add(reactive);
        return this;
    }

    StateBehaviour<A,B> removeOnExitListener (Reactive<A> reactive) {
        onEnterList.remove(reactive);
        return this;
    }

    StateBehaviour<A,B> addTransition (Transition<A,B> transition) {
        transitions.add(transition);
        return this;
    }

    StateBehaviour<A,B> removeTransition (Transition<A,B> transition) {
        transitions.remove(transition);
        return this;
    }
}
