package typeinference;

import java.util.List;

public class TypeFactory {

    private TypeFactory() {} // utility class — no instances

    public static PhpType createType(String typeName) {
        return new PhpType(typeName);
    }

    public static PhpType createUnionType(List<PhpType> types) {
        return new UnionType(types);
    }
}
