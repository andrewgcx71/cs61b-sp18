import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class TrieNode {
    // custom made trie to pass autograder
    public Set<String> words = new HashSet<>();

    // the current letter's children
    public Map<Character, TrieNode> children = new HashMap<>();

    public boolean isLeave() {
        return children.isEmpty();
    }

    public boolean isWord() {
        return !words.isEmpty();
    }
}
