package typeinference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Hand-rolled test doubles — no mocking framework needed. */
class Fakes {

    static PhpVariable variable(String name, PhpDocBlock docBlock) {
        return new PhpVariable() {
            @Override public PhpDocBlock getDocBlock() { return docBlock; }
            @Override public String getName()          { return name; }
        };
    }

    static PhpDocBlock docBlock(DocTag... tags) {
        return tagName -> "var".equals(tagName) ? Arrays.asList(tags) : Collections.emptyList();
    }

    static DocTag tag(String value) {
        return () -> value;
    }

    // Convenience: variable with no doc block at all
    static PhpVariable noDoc(String name) {
        return variable(name, null);
    }

    // Convenience: variable with doc block but zero @var tags
    static PhpVariable emptyDoc(String name) {
        return variable(name, tagName -> Collections.emptyList());
    }
}
