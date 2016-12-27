package nightgames.gui;

import nightgames.global.Global;

public class SaveButton extends RunnableButton {

    /**
     * 
     */
    private static final long serialVersionUID = 5665392145091151054L;

    public SaveButton() {
        super("Save", () -> Global.saveWithDialog());
    }
}
