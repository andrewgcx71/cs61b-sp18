import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class TrieNode {
    public Set<String> words = new HashSet<>();

    public Map<Character, TrieNode> children = new HashMap<>();

    public boolean isLeave() {
        return children.isEmpty();
    }

    public boolean isWord() {
        return !words.isEmpty();
    }
}
