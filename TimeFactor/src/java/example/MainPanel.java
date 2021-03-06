package example;
// -*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
// @homepage@

import java.awt.*;
import java.util.Optional;
import javax.swing.*;

public final class MainPanel extends JPanel {
    private final JSpinner spinner = new JSpinner();
    private MainPanel() {
        super(new BorderLayout());
        Number lv = (Number) UIManager.get("Tree.timeFactor");
        spinner.setModel(new SpinnerNumberModel(lv, 0L, 5000L, 500L));
        UIManager.put("List.timeFactor", 5000L);

        String[] model = {"a", "aa", "b", "bbb", "bbc"};
        JComboBox<String> combo = new JComboBox<>(model);
        combo.setPrototypeDisplayValue("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");

        JPanel p = new JPanel();
        p.add(spinner);
        p.add(combo);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("ComboBox.timeFactor", p);
        tabbedPane.add("List.timeFactor", new JScrollPane(new JList<>(model)));
        tabbedPane.add("Table.timeFactor(JFileChooser)", new JFileChooser());
        tabbedPane.add("Tree.timeFactor", new JScrollPane(new JTree()));

        add(tabbedPane);
        setPreferredSize(new Dimension(320, 240));
    }
    @Override public void updateUI() {
        Long lv = Optional.ofNullable(spinner).map(s -> (Long) s.getModel().getValue()).orElse(1000L);
        UIManager.put("ComboBox.timeFactor", lv);
        UIManager.put("List.timeFactor", lv);
        UIManager.put("Table.timeFactor", lv);
        UIManager.put("Tree.timeFactor", lv);
        super.updateUI();
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
        JMenuBar mb = new JMenuBar();
        mb.add(LookAndFeelUtil.createLookAndFeelMenu());

        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.setJMenuBar(mb);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

final class LookAndFeelUtil {
    private static String lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
    private LookAndFeelUtil() { /* Singleton */ }
    public static JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("LookAndFeel");
        ButtonGroup lafRadioGroup = new ButtonGroup();
        for (UIManager.LookAndFeelInfo lafInfo: UIManager.getInstalledLookAndFeels()) {
            menu.add(createLookAndFeelItem(lafInfo.getName(), lafInfo.getClassName(), lafRadioGroup));
        }
        return menu;
    }
    private static JRadioButtonMenuItem createLookAndFeelItem(String lafName, String lafClassName, ButtonGroup lafRadioGroup) {
        JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(lafName, lafClassName.equals(lookAndFeel));
        lafItem.setActionCommand(lafClassName);
        lafItem.setHideActionText(true);
        lafItem.addActionListener(e -> {
            ButtonModel m = lafRadioGroup.getSelection();
            try {
                setLookAndFeel(m.getActionCommand());
            } catch (ClassNotFoundException | InstantiationException
                   | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
        lafRadioGroup.add(lafItem);
        return lafItem;
    }
    private static void setLookAndFeel(String lookAndFeel) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        String oldLookAndFeel = LookAndFeelUtil.lookAndFeel;
        if (!oldLookAndFeel.equals(lookAndFeel)) {
            UIManager.setLookAndFeel(lookAndFeel);
            LookAndFeelUtil.lookAndFeel = lookAndFeel;
            updateLookAndFeel();
            // firePropertyChange("lookAndFeel", oldLookAndFeel, lookAndFeel);
        }
    }
    private static void updateLookAndFeel() {
        for (Window window: Frame.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
}
