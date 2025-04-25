import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainUI extends JFrame {
    private HuffmanCoding huffman;
    private JTextArea inputText, encodedText, decodedText, codesText, freqText, statsText;
    private JPanel treePanel;
    private HuffmanCoding.Node treeRoot;
    private static final int NODE_SIZE = 40;
    private static final int LEVEL_HEIGHT = 80;
    private static final int HORIZONTAL_SPACING = 60;
    private static final Color NODE_COLOR = Color.WHITE;
    private static final Color EDGE_COLOR = Color.BLACK;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private int maxDepth;
    private int maxWidth;

    public MainUI() {
        huffman = new HuffmanCoding();
        initUI();
    }

    private void initUI() {
        setTitle("Huffman Coding");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(Color.BLACK);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.BLACK);

        JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSplitPane.setResizeWeight(0.5);
        topSplitPane.setDividerSize(5);

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(Color.BLACK);
        JLabel inputLabel = new JLabel("Input Text:");
        inputLabel.setForeground(Color.WHITE);
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputText = new JTextArea(4, 25);
        inputText.setLineWrap(true);
        inputText.setBackground(Color.DARK_GRAY);
        inputText.setForeground(Color.WHITE);
        inputPanel.add(new JScrollPane(inputText), BorderLayout.CENTER);
        topSplitPane.setLeftComponent(inputPanel);

        // Encoded Text Panel
        JPanel encodedPanel = new JPanel(new BorderLayout(5, 5));
        encodedPanel.setBackground(Color.BLACK);
        JLabel encodedLabel = new JLabel("Encoded Text:");
        encodedLabel.setForeground(Color.WHITE);
        encodedPanel.add(encodedLabel, BorderLayout.NORTH);
        encodedText = new JTextArea(4, 25);
        encodedText.setLineWrap(true);
        encodedText.setEditable(false);
        encodedText.setBackground(Color.DARK_GRAY);
        encodedText.setForeground(Color.WHITE);
        encodedPanel.add(new JScrollPane(encodedText), BorderLayout.CENTER);
        topSplitPane.setRightComponent(encodedPanel);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(Color.BLACK);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(Color.BLACK);
        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        buttonPanel.add(clearButton);
        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        // Tree Panel
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (treeRoot != null) {
                    drawTree(g, treeRoot, getWidth() / 2, 50, 0, getWidth() / 4);
                }
            }
        };
        treePanel.setBackground(BACKGROUND_COLOR);
        JScrollPane treeScrollPane = new JScrollPane(treePanel);
        centerPanel.add(treeScrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JSplitPane outputSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        outputSplitPane.setResizeWeight(0.5);
        outputSplitPane.setDividerSize(5);

        JPanel leftOutputPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        leftOutputPanel.setBackground(Color.BLACK);

        JPanel freqPanel = new JPanel(new BorderLayout(5, 5));
        freqPanel.setBackground(Color.BLACK);
        JLabel freqLabel = new JLabel("Frequency Table:");
        freqLabel.setForeground(Color.WHITE);
        freqPanel.add(freqLabel, BorderLayout.NORTH);
        freqText = new JTextArea(4, 25);
        freqText.setLineWrap(true);
        freqText.setEditable(false);
        freqText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        freqText.setBackground(Color.DARK_GRAY);
        freqText.setForeground(Color.WHITE);
        freqPanel.add(new JScrollPane(freqText), BorderLayout.CENTER);
        leftOutputPanel.add(freqPanel);

        JPanel codesPanel = new JPanel(new BorderLayout(5, 5));
        codesPanel.setBackground(Color.BLACK);
        JLabel codeLabel = new JLabel("Dictionary (Huffman Codes):");
        codeLabel.setForeground(Color.WHITE);
        codesPanel.add(codeLabel, BorderLayout.NORTH);
        codesText = new JTextArea(4, 25);
        codesText.setLineWrap(true);
        codesText.setEditable(false);
        codesText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        codesText.setBackground(Color.DARK_GRAY);
        codesText.setForeground(Color.WHITE);
        codesPanel.add(new JScrollPane(codesText), BorderLayout.CENTER);
        leftOutputPanel.add(codesPanel);

        JPanel statsPanel = new JPanel(new BorderLayout(5, 5));
        statsPanel.setBackground(Color.BLACK);
        JLabel statsLabel = new JLabel("Statistics:");
        statsLabel.setForeground(Color.WHITE);
        statsPanel.add(statsLabel, BorderLayout.NORTH);
        statsText = new JTextArea(4, 25);
        statsText.setLineWrap(true);
        statsText.setEditable(false);
        statsText.setBackground(Color.DARK_GRAY);
        statsText.setForeground(Color.WHITE);
        statsPanel.add(new JScrollPane(statsText), BorderLayout.CENTER);
        leftOutputPanel.add(statsPanel);

        JPanel rightOutputPanel = new JPanel(new BorderLayout(5, 5));
        rightOutputPanel.setBackground(Color.BLACK);
        JLabel decodedLabel = new JLabel("Decoded Text:");
        decodedLabel.setForeground(Color.WHITE);
        rightOutputPanel.add(decodedLabel, BorderLayout.NORTH);
        decodedText = new JTextArea(4, 25);
        decodedText.setLineWrap(true);
        decodedText.setEditable(false);
        decodedText.setBackground(Color.DARK_GRAY);
        decodedText.setForeground(Color.WHITE);
        rightOutputPanel.add(new JScrollPane(decodedText), BorderLayout.CENTER);

        outputSplitPane.setLeftComponent(leftOutputPanel);
        outputSplitPane.setRightComponent(rightOutputPanel);

        mainPanel.add(topSplitPane, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(outputSplitPane, BorderLayout.SOUTH);

        encodeButton.addActionListener(e -> encodeText());
        decodeButton.addActionListener(e -> decodeText());
        clearButton.addActionListener(e -> {
            inputText.setText("");
            encodedText.setText("");
            decodedText.setText("");
            freqText.setText("");
            codesText.setText("");
            statsText.setText("");
            setTreeRoot(null);
        });

        add(mainPanel);
    }

    private void setTreeRoot(HuffmanCoding.Node root) {
        this.treeRoot = root;
        calculateTreeDimensions();
        treePanel.setPreferredSize(new Dimension(maxWidth * HORIZONTAL_SPACING, (maxDepth + 1) * LEVEL_HEIGHT));
        treePanel.revalidate();
        treePanel.repaint();
    }

    private void calculateTreeDimensions() {
        maxDepth = getTreeDepth(treeRoot);
        maxWidth = (int) Math.pow(2, maxDepth);
    }

    private int getTreeDepth(HuffmanCoding.Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeDepth(node.left), getTreeDepth(node.right));
    }

    private void drawTree(Graphics g, HuffmanCoding.Node node, int x, int y, int level, int spacing) {
        if (node == null) return;

        g.setColor(NODE_COLOR);
        g.fillOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String label = (node.left == null && node.right == null) ? node.ch + "(" + node.freq + ")" : String.valueOf(node.freq);
        FontMetrics fm = g.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        g.drawString(label, x - labelWidth / 2, y + 5);

        int nextY = y + LEVEL_HEIGHT;
        int offset = spacing / (level + 1);

        if (node.left != null) {
            int leftX = x - spacing + offset;
            g.setColor(EDGE_COLOR);
            drawArrowLine(g, x, y + NODE_SIZE / 2, leftX, nextY - NODE_SIZE / 2);
            g.drawString("0", x - 15, y + 30);
            drawTree(g, node.left, leftX, nextY, level + 1, spacing);
        }

        if (node.right != null) {
            int rightX = x + spacing - offset;
            g.setColor(EDGE_COLOR);
            drawArrowLine(g, x, y + NODE_SIZE / 2, rightX, nextY - NODE_SIZE / 2);
            g.drawString("1", x + 15, y + 30);
            drawTree(g, node.right, rightX, nextY, level + 1, spacing);
        }
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void encodeText() {
        String input = inputText.getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to encode");
            encodedText.setText("");
            freqText.setText("");
            codesText.setText("");
            statsText.setText("");
            decodedText.setText("");
            setTreeRoot(null);
            return;
        }

        String encoded = huffman.encode(input);
        encodedText.setText(encoded);

        StringBuilder freq = new StringBuilder();
        freq.append(String.format("%-10s %-10s%n", "char", "quant"));
        freq.append("-".repeat(20)).append("\n");
        for (Map.Entry<Character, Integer> entry : huffman.getFrequencyMap().entrySet()) {
            freq.append(String.format("%-10s %-10d%n", "'" + entry.getKey() + "'", entry.getValue()));
        }
        freqText.setText(freq.toString());

        StringBuilder codes = new StringBuilder();
        codes.append(String.format("%-10s %-20s%n", "char", "code"));
        codes.append("-".repeat(30)).append("\n");
        for (Map.Entry<Character, String> entry : huffman.getHuffmanCodes().entrySet()) {
            codes.append(String.format("%-10s %-20s%n", "'" + entry.getKey() + "'", entry.getValue()));
        }
        codesText.setText(codes.toString());

        StringBuilder stats = new StringBuilder();
        stats.append("Original Size: ").append(huffman.getOriginalBits()).append(" bits\n");
        stats.append("Compressed Size: ").append(huffman.getCompressedBits()).append(" bits\n");
        stats.append("Compression Ratio: ").append(String.format("%.2f", huffman.getCompressionRatio())).append("% (smaller is better)");
        statsText.setText(stats.toString());

        setTreeRoot(huffman.getRoot());
    }

    private void decodeText() {
        String encoded = encodedText.getText();
        if (encoded.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please encode text first");
            return;
        }
        decodedText.setText(huffman.decode(encoded));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}
