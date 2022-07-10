package com.github.doobo.vbo;

import java.util.Objects;

/**
 * 元组类
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2022-02-11 16:32
 */
public class TupleTwo<A, B> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final A one;

    private final B two;

    public TupleTwo(A one, B two) {
        this.one = one;
        this.two = two;
    }

    public A getOne() {
        return one;
    }

    public B getTwo() {
        return two;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TupleTwo<?, ?> tupleTwo = (TupleTwo<?, ?>) o;
        return Objects.equals(one, tupleTwo.one) &&
                Objects.equals(two, tupleTwo.two);
    }

    @Override
    public int hashCode() {
        return Objects.hash(one, two);
    }
}
