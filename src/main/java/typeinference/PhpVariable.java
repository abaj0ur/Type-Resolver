package typeinference;

public interface PhpVariable {
    /** @return null if no doc block */
    PhpDocBlock getDocBlock();

    /** @return variable name including $, e.g. "$user" */
    String getName();
}
