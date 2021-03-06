package example;
// -*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
// @homepage@

import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

public final class MainPanel extends JPanel {
    private MainPanel() {
        super(new BorderLayout());
        int maskRange = 2;
        Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);
        JTextArea textArea = new JTextArea();
        textArea.setText("aaaaaaaasdfasdfasdfasdf\nasdfasdfasdfasdfasdfasdf\n1234567890\naaaaaaaaaaaaaaaaaasdfasd");
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new NonEditableLineDocumentFilter(maskRange));
        try {
            Highlighter hilite = textArea.getHighlighter();
            Document doc = textArea.getDocument();
            Element root = doc.getDefaultRootElement();
            for (int i = 0; i < maskRange; i++) { // root.getElementCount(); i++) {
                Element elem = root.getElement(i);
                hilite.addHighlight(elem.getStartOffset(), elem.getEndOffset() - 1, highlightPainter);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        add(new JScrollPane(textArea));
        setPreferredSize(new Dimension(320, 240));
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGui();
            }
        });
    }
    public static void createAndShowGui() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class NonEditableLineDocumentFilter extends DocumentFilter {
    private final int maskRange;
    protected NonEditableLineDocumentFilter(int maskRange) {
        super();
        this.maskRange = maskRange;
    }
    @Override public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        if (Objects.nonNull(text)) {
            replace(fb, offset, 0, text, attr);
        }
    }
    @Override public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
        replace(fb, offset, length, "", null);
    }
    @Override public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        if (doc.getDefaultRootElement().getElementIndex(offset) >= maskRange) {
            fb.replace(offset, length, text, attrs);
        }
    }
}
