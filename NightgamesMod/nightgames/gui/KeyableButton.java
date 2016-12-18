package nightgames.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class KeyableButton extends JPanel {
    private static final long serialVersionUID = -2379908542190189603L;
    private final JButton button;

    public KeyableButton(String text) {
        this.button = new JButton(text);
        this.setLayout(new BorderLayout());
        this.add(button);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    public void call() {
        button.doClick();
    }

    public abstract String getText();

    public void setHotkeyTextTo(String string) {
        button.setText(String.format("%s [%s]", getText(), string));
    }

    public void clearHotkeyText() {
        button.setText(getText());
    }

    public JButton getButton() {
        return button;
    }
}
