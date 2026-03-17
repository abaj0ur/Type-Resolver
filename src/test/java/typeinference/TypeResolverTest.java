package typeinference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeResolverTest {

    private TypeResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new TypeResolver();
    }

    // --- standard cases ---

    @Test
    void standardType_returnsNamedType() {
        // /** @var User */ for $user -> User
        var variable = Fakes.variable("$user", Fakes.docBlock(Fakes.tag("User")));
        assertEquals(TypeFactory.createType("User"), resolver.inferTypeFromDoc(variable));
    }

    @Test
    void unionType_returnsUnionType() {
        // /** @var string|int */ for $id -> UnionType(string, int)
        var variable = Fakes.variable("$id", Fakes.docBlock(Fakes.tag("string|int")));
        var expected = TypeFactory.createUnionType(List.of(
                TypeFactory.createType("string"),
                TypeFactory.createType("int")
        ));
        assertEquals(expected, resolver.inferTypeFromDoc(variable));
    }

    @Test
    void unionType_withSpaces_returnsUnionType() {
        // "string | int" with spaces around pipe — should still parse
        var variable = Fakes.variable("$id", Fakes.docBlock(Fakes.tag("string | int")));
        var expected = TypeFactory.createUnionType(List.of(
                TypeFactory.createType("string"),
                TypeFactory.createType("int")
        ));
        assertEquals(expected, resolver.inferTypeFromDoc(variable));
    }

    @Test
    void namedTag_matchingVariable_returnsType() {
        // /** @var Logger $log */ for $log -> Logger
        var variable = Fakes.variable("$log", Fakes.docBlock(Fakes.tag("Logger $log")));
        assertEquals(TypeFactory.createType("Logger"), resolver.inferTypeFromDoc(variable));
    }

    // --- name mismatch ---

    @Test
    void namedTag_differentVariable_returnsMixed() {
        // /** @var Admin $adm */ for $guest -> mixed (tag is for a different variable)
        var variable = Fakes.variable("$guest", Fakes.docBlock(Fakes.tag("Admin $adm")));
        assertEquals(TypeFactory.createType("mixed"), resolver.inferTypeFromDoc(variable));
    }

    // --- multiple tags ---

    @Test
    void multipleTags_picksMatchingVariable() {
        // @var int $id AND @var string $name — inspecting $name -> string
        var variable = Fakes.variable("$name", Fakes.docBlock(
                Fakes.tag("int $id"),
                Fakes.tag("string $name")
        ));
        assertEquals(TypeFactory.createType("string"), resolver.inferTypeFromDoc(variable));
    }

    // --- fallback / missing ---

    @Test
    void noDocBlock_returnsMixed() {
        assertEquals(TypeFactory.createType("mixed"), resolver.inferTypeFromDoc(Fakes.noDoc("$x")));
    }

    @Test
    void docBlockWithNoVarTags_returnsMixed() {
        assertEquals(TypeFactory.createType("mixed"), resolver.inferTypeFromDoc(Fakes.emptyDoc("$x")));
    }

    @Test
    void threePartUnion_returnsUnionType() {
        // /** @var string|int|null */ -> UnionType(string, int, null)
        var variable = Fakes.variable("$val", Fakes.docBlock(Fakes.tag("string|int|null")));
        var expected = TypeFactory.createUnionType(List.of(
                TypeFactory.createType("string"),
                TypeFactory.createType("int"),
                TypeFactory.createType("null")
        ));
        assertEquals(expected, resolver.inferTypeFromDoc(variable));
    }
}
