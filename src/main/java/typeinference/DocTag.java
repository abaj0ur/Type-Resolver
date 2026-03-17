package typeinference;

public interface DocTag {
    /** @return full tag content, e.g. "User $user" or "string|int" */
    String getValue();
}
