/** A class for palindrome operations.*/
public class Palindrome {
    /**Given a String, wordToDeque should return a Deque where
     * the characters appear in the same order as in the String.
     * For example, if the word is “persiflage”,
     * then the returned Deque should have ‘p’ at the front,
     * followed by ‘e’, and so forth.*/
    public Deque<Character> wordToDeque(String word) {
        int length = word.length();
        Deque<Character> lst = new ArrayDeque<>();
        for (int i = 0; i < length; i++) {
            lst.addLast(word.charAt(i));
        }
        return lst;
    }

    /**The isPalindrome method should return true if the given word is a palindrome,
     * and false otherwise.
     * A palindrome is defined as a word that is the same whether it is read forwards or backwards.
     * For example “a”, “racecar”, and “noon” are all palindromes. “horse”, “rancor”,
     * and “aaaaab” are not palindromes. Any word of length 1 or 0 is a palindrome.*/
    public boolean isPalindrome(String word) {
        if (word.length() == 0) {
            return true;
        }
        Deque<Character> characters = wordToDeque(word);
        String reverse = "";
        for (int i = word.length() - 1; i >= 0; i--) {
            reverse += characters.get(i);
        }
        return reverse.equals(word);
    }
    /**The method will return true if the word is a palindrome according
     * to the character comparison test provided by the
     * CharacterComparator passed in as argument cc.*/
    public boolean isPalindrome(String word, CharacterComparator cc) {
        int start = 0;
        int end = word.length() - 1;
        while (start < end) {
            if (!cc.equalChars(word.charAt(start), word.charAt(end))) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }


}
