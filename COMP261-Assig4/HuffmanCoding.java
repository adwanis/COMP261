/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */

import java.util.*;

public class HuffmanCoding {
    private Node root;
    private Map<Character, String> charToCodeMap;
    private Map<String, Character> codeToCharMap;
    /**
     * This would be a good place to compute and store the tree.
     */
    public HuffmanCoding(String text) {
        // TODO fill this in.
        
        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
        PriorityQueue<Node> queue = buildPriorityQueue(frequencyMap);
        this.root = buildHuffmanTree(queue);
        this.charToCodeMap = new HashMap<>();
        this.codeToCharMap = new HashMap<>();
        buildCodeMaps(this.root, "");
        
    }
    
    private Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }
        return frequencyMap;
    }
    
    private PriorityQueue<Node> buildPriorityQueue(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }
        return priorityQueue;
    }
    
    private Node buildHuffmanTree(PriorityQueue<Node> priorityQueue) {
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node newNode = new Node('\0', left.frequency + right.frequency, left, right);
            priorityQueue.add(newNode);
        }
        return priorityQueue.poll();
    }
    
    private void buildCodeMaps(Node node, String code) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            charToCodeMap.put(node.ch, code);
            codeToCharMap.put(code, node.ch);
        }
        buildCodeMaps(node.left, code + "0");
        buildCodeMaps(node.right, code + "1");
    }
    
        /**
     * Take an input string, text, and encode it with the stored tree. Should
     * return the encoded text as a binary string, that is, a string containing
     * only 1 and 0.
     */
    public String encode(String text) {
        // TODO fill this in.
        StringBuilder encodedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            encodedText.append(charToCodeMap.get(ch));
        }
        return encodedText.toString();
    }

    /**
     * Take encoded input as a binary string, decode it using the stored tree,
     * and return the decoded text as a text string.
     */
    public String decode(String encoded) {
        // TODO fill this in.
        StringBuilder decodedText = new StringBuilder();
        Node currentNode = root;
        for (char bit : encoded.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;
            if (currentNode.left == null && currentNode.right == null) {
                decodedText.append(currentNode.ch);
                currentNode = root;
            }
        }
        return decodedText.toString();
    }

    /**
     * The getInformation method is here for your convenience, you don't need to
     * fill it in if you don't wan to. It is called on every run and its return
     * value is displayed on-screen. You could use this, for example, to print
     * out the encoding tree.
     */
    public String getInformation() {
        //return charToCodeMap.toString();
        return "";
    }
    
    class Node implements Comparable<Node> {
        char ch;
        int frequency;
        Node left, right;
    
        public Node(char ch, int frequency) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = this.right = null;
        }
    
        public Node(char ch, int frequency, Node left, Node right) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    
        @Override
        public int compareTo(Node node) {
            if (this.frequency != node.frequency) {
                return this.frequency - node.frequency;
            }
            return this.ch - node.ch;
        }
    }
}
