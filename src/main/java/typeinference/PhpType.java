package typeinference;

import java.util.Objects;

public class PhpType {
    private final String name;

    public PhpType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhpType other)) return false;
        if (o instanceof UnionType) return false; // BUG-2: UnionType != plain PhpType, symmetry fix
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
