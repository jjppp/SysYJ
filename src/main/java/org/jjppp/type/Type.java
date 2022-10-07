package org.jjppp.type;

import org.jjppp.runtime.BaseVal;

public interface Type {
    boolean isConst();

    int size();

    BaseVal defVal();
}
