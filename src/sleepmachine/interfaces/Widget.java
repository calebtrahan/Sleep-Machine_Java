package sleepmachine.interfaces;

public interface Widget {
    boolean enabled = false;

    void statusswitch();
    boolean isValid();
    void disable();
    void enable();
    void resetallvalues();
}
