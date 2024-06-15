package tr.com.jowl.datatypes;

import java.io.Serializable;

/**
 * @param <P1>
 * @param <P2>
 * @author saebnajim1
 */
public class Pair<P1, P2> implements Serializable {
    private P1 p1;
    private P2 p2;

    public Pair() {
    }

    public Pair(P1 p1, P2 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public P1 getP1() {
        return p1;
    }

    public void setP1(P1 p1) {
        this.p1 = p1;
    }

    public P2 getP2() {
        return p2;
    }

    public void setP2(P2 p2) {
        this.p2 = p2;
    }

}
