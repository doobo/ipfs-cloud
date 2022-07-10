package com.github.doobo.vbo;


import java.util.Objects;

/**
 * 三个元素元组类
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2022-02-11 16:38
 */
public class TupleThree<A, B, C> extends TupleTwo<A, B> {

    private final C third;

    public TupleThree(A one, B two, C third) {
        super(one, two);
        this.third = third;
    }

    public C getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TupleThree)) return false;
        if (!super.equals(o)) return false;
        TupleThree<?, ?, ?> that = (TupleThree<?, ?, ?>) o;
        return Objects.equals(third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), third);
    }
}
