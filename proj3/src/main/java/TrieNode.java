import java.util.Map;
import java.util.HashMap;

public class TrieNode {
    public String word;

    // the current letter's children
    public Map<Character, TrieNode> children = new HashMap<>();

    public boolean isLeave() {
        return children.isEmpty();
    }

    public boolean isWord() {
        return word != null;
    }
}
