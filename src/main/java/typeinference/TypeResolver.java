package typeinference;

import java.util.Arrays;
import java.util.List;

public class TypeResolver {

    private static final PhpType MIXED = TypeFactory.createType("mixed");

    public PhpType inferTypeFromDoc(PhpVariable variable) {
        PhpDocBlock doc = variable.getDocBlock();
        if (doc == null) return MIXED;

        List<DocTag> tags = doc.getTagsByName("var");
        if (tags.isEmpty()) return MIXED;

        DocTag tag = findRelevantTag(tags, variable.getName());
        if (tag == null) return MIXED;

        return buildType(extractTypePart(tag.getValue()));
    }

    // Prefer tag with matching $varName; fall back to unnamed tag (no $ token).
    // Variable name is always the LAST token (e.g. "string | int $id" or "User $user").
    private DocTag findRelevantTag(List<DocTag> tags, String varName) {
        DocTag unnamed = null;
        for (DocTag tag : tags) {
            String[] parts = tag.getValue().trim().split("\\s+");
            String lastToken = parts[parts.length - 1];
            if (lastToken.equals(varName)) return tag;
            if (unnamed == null && !lastToken.startsWith("$")) {
                unnamed = tag;
            }
        }
        return unnamed;
    }

    // Type part = everything except the trailing $varName token (if present).
    // e.g. "User $user" -> "User",  "string | int $id" -> "string | int",  "string|int" -> "string|int"
    private String extractTypePart(String value) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return "mixed"; // BUG-3: guard for empty tag
        String[] parts = trimmed.split("\\s+");
        if (parts[parts.length - 1].startsWith("$")) {
            return String.join(" ", Arrays.copyOf(parts, parts.length - 1));
        }
        return trimmed;
    }

    // "string|int" or "string | int" -> UnionType; plain name -> PhpType
    private PhpType buildType(String typeStr) {
        if (!typeStr.contains("|")) return TypeFactory.createType(typeStr);

        List<PhpType> parts = Arrays.stream(typeStr.split("\\|"))
                .map(String::trim)
                .map(TypeFactory::createType)
                .toList();
        return TypeFactory.createUnionType(parts);
    }
}
