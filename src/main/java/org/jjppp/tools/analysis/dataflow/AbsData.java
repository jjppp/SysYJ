package org.jjppp.tools.analysis.dataflow;

public interface AbsData<T> {
    T merge(T rhs);
}
