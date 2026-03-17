package typeinference;

import java.util.List;
import java.util.Objects;

public class UnionType extends PhpType {
    private final List<PhpType> types;

    public UnionType(List<PhpType> types) {
        super(types.stream().map(PhpType::getName).reduce((a, b) -> a + "|" + b).orElse("mixed"));
        if (types.isEmpty()) throw new IllegalArgumentException("UnionType requires at least one type");
        this.types = List.copyOf(types);
    }

    public List<PhpType> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnionType other)) return false;
        return Objects.equals(types, other.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(types);
    }
}
