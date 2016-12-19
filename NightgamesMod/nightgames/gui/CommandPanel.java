package nightgames.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

public class CommandPanel {
    private static final List<Character> POSSIBLE_HOTKEYS = Arrays.asList(
                    'q', 'w', 'e', 'r', 't', 'y',
                    'a', 's', 'd', 'f' , 'g', 'h',
                    'z', 'x', 'c', 'v', 'b', 'n'); 
    private static final Set<String> DEFAULT_CHOICES = new HashSet<>(Arrays.asList("Next", "Leave", "Back"));
    private static final int ROW_LIMIT = 6;

    private JPanel panel;
    private int index;
    private int page;
    private Map<Character, KeyableButton> hotkeyMapping;
    private List<KeyableButton> buttons;
    private JPanel rows[];
    public CommandPanel(int width) {
        panel = new JPanel();
        panel.setBackground(GUIColors.bgDark);
        panel.setPreferredSize(new Dimension(width, 160));
        panel.setMinimumSize(new Dimension(width, 160));
        panel.setBorder(new CompoundBorder());
        hotkeyMapping = new HashMap<>();
        rows = new JPanel[POSSIBLE_HOTKEYS.size() / ROW_LIMIT];
        rows[0] = new JPanel();
        rows[1] = new JPanel();
        rows[2] = new JPanel();
        for (JPanel row : rows) {
            FlowLayout layout = new FlowLayout();
            layout.setVgap(0);
            layout.setHgap(4);
            row.setLayout(layout);
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder());
            row.setPreferredSize(new Dimension(0, 20));
            panel.add(row);
        }
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(2));
        buttons = new ArrayList<>();
        index = 0;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void reset() {
        buttons.clear();
        hotkeyMapping.clear();
        clear();
        refresh();
    }

    private void clear() {
        for (JPanel row : rows) {
            row.removeAll();
        }
        POSSIBLE_HOTKEYS.forEach(hotkeyMapping::remove);
        index = 0;  
    }

    public void refresh() {
        panel.repaint();
        panel.revalidate();
    }

    public void add(KeyableButton button) {
        page = 0;
        buttons.add(button);
        use(button);
    }

    private void use(KeyableButton button) {
        int effectiveIndex = index - page * POSSIBLE_HOTKEYS.size();
        int currentPage = page;
        if (effectiveIndex >= 0 && effectiveIndex < POSSIBLE_HOTKEYS.size()) {
            int rowIndex = Math.min(rows.length - 1, effectiveIndex / ROW_LIMIT);
            JPanel row = rows[rowIndex];
            row.add(button);
            Character hotkey = POSSIBLE_HOTKEYS.get(effectiveIndex);
            register(hotkey, button);
            if (DEFAULT_CHOICES.contains(button.getText()) && !hotkeyMapping.containsKey(' ')) {
                hotkeyMapping.put(' ', button);
            }
        } else if (effectiveIndex == -1) {
            KeyableButton leftPage = new RunnableButton("<<<", () -> setPage(currentPage - 1));
            rows[0].add(leftPage, 0);
            register('~', leftPage);
        } else if (effectiveIndex == POSSIBLE_HOTKEYS.size()){
            KeyableButton rightPage = new RunnableButton(">>>", () -> setPage(currentPage + 1));
            rows[0].add(rightPage);
            register('`', rightPage);
        }
        index += 1;
    }

    public void setPage(int page) {
        this.page = page;
        clear();
        buttons.forEach(this::use);
        refresh();
    }

    public Optional<KeyableButton> getButtonForHotkey(char keyChar) {
        return Optional.ofNullable(hotkeyMapping.get(keyChar));
    }

    public void register(Character hotkey, KeyableButton button) {
        button.setHotkeyTextTo(hotkey.toString().toUpperCase());
        hotkeyMapping.put(hotkey, button);
    }
}
