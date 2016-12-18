package nightgames.gui;

import java.awt.Font;

class RunnableButton extends KeyableButton {
    private static final long serialVersionUID = 5435929681634872672L;
    private String text;
    public RunnableButton(String text, Runnable runnable) {
        super(text);
        this.text = text;
        getButton().setFont(new Font("Baskerville Old Face", 0, 18));
        if (text.length() > 20) {
            
        }
        getButton().addActionListener((evt) -> runnable.run());
    }

    @Override
    public String getText() {
        return text;
    }
}