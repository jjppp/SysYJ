package org.jjppp.ir.control;

public final class LabelFactory {
    private static int labelCount = -1;

    public static Label alloc(String name) {
        labelCount += 1;
        return new Label(name, labelCount);
    }
}
