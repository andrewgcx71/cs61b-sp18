import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/** perform insert/search operations in trie data structure .*/
public class Trie {

    public TrieNode root = new TrieNode();

    public void insert(String cleanName, String actualName) {
        TrieNode current = root;
        for(int i = 0; i < cleanName.length(); i++) {
            char ch = cleanName.charAt(i);
            if (!current.children.containsKey(ch)) {
                current.children.put(ch, new TrieNode());
            }
            if (i == cleanName.length() - 1) {
                TrieNode trieNode = current.children.get(ch);
                // custom made trie to pass autograder
                if (!trieNode.words.contains(actualName)) {
                    trieNode.words.add(actualName);
                }
            }
            current = current.children.get(ch);
        }
    }

    // return a list result match to prefix word, if no match, return an empty list.
    public List<String> search(String prefix) {
        TrieNode node = root;
        for(int i = 0; i < prefix.length(); i++) {
            node = node.children.get(prefix.charAt(i));
            if(node == null) {
                return new ArrayList<>();
            }
        }
        return searchRecursive(node);
    }

    //Helper: Given the trieNode match to the last character in the prefix, return a list of matched String.
    private List<String> searchRecursive(TrieNode node) {
        if (node.isLeave()) {
            List<String> words = new ArrayList<>();
            // custom made trie to pass autograder
            for (String word : node.words) {
                words.add(word);
            }
            return words;
        } else {
            List<String> words = new ArrayList<>();
            if (node.isWord()) {
                // custom made trie to pass autograder
                for (String word : node.words) {
                    words.add(word);
                }
            }
            for (char ch : node.children.keySet()) {
                TrieNode child = node.children.get(ch);
                words.addAll(searchRecursive(child));
            }
            return words;
        }
    }
}

