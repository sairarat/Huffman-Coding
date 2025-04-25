import java.util.*;

public class HuffmanCoding {
    private Node root;
    private Map<Character, String> huffmanCodes = new HashMap<>();
    private Map<Character, Integer> frequencyMap = new HashMap<>();
    private int originalBits;
    private int compressedBits;

    public static class Node implements Comparable<Node> {
        public char ch;
        public int freq;
        public Node left, right;

        public Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        public Node(int freq, Node left, Node right) {
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }

    public String encode(String text) {
        if (text == null || text.isEmpty()) {
            originalBits = 0;
            compressedBits = 0;
            frequencyMap.clear();
            huffmanCodes.clear();
            return "";
        }

        frequencyMap.clear();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.offer(new Node(left.freq + right.freq, left, right));
        }

        root = pq.poll();
        huffmanCodes.clear();
        generateCodes(root, "");

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCodes.get(c));
        }

        originalBits = text.length() * 8;
        compressedBits = encoded.length();

        return encoded.toString();
    }

    public String decode(String encoded) {
        if (encoded == null || encoded.isEmpty() || root == null) return "";

        StringBuilder decoded = new StringBuilder();
        Node current = root;

        for (char bit : encoded.toCharArray()) {
            current = bit == '0' ? current.left : current.right;

            if (current.left == null && current.right == null) {
                decoded.append(current.ch);
                current = root;
            }
        }

        return decoded.toString();
    }

    private void generateCodes(Node node, String code) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.ch, code.length() > 0 ? code : "0");
            return;
        }

        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodes;
    }

    public Map<Character, Integer> getFrequencyMap() {
        return frequencyMap;
    }

    public int getOriginalBits() {
        return originalBits;
    }

    public int getCompressedBits() {
        return compressedBits;
    }

    public double getCompressionRatio() {
        if (originalBits == 0) return 0.0;
        return (double) compressedBits / originalBits * 100.0;
    }

    public Node getRoot() {
        return root;
    }
}
