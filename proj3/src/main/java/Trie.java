import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/** perform insert/search operations in trie data structure .*/
public class Trie {

    public TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode current = root;
        for(int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!current.children.containsKey(ch)) {
                current.children.put(ch, new TrieNode());
            }
            if (i == word.length() - 1) {
                TrieNode trieNode = current.children.get(ch);
                trieNode.word = word;
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
        if(node.isLeave()) {
            return new ArrayList<>(Arrays.asList(node.word));
        } else {
            List<String> list = new ArrayList<>();
            if(node.isWord()) {
                list.add(node.word);
            }
            for(char ch: node.children.keySet()) {
                TrieNode child = node.children.get(ch);
                list.addAll(searchRecursive(child));
            }
            return list;
        }
    }
}

