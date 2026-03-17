package typeinference;

import java.util.List;

public interface PhpDocBlock {
    /** @return all tags matching tagName, empty list if none */
    List<DocTag> getTagsByName(String tagName);
}
