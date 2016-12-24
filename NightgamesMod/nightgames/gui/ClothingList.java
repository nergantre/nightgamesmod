package nightgames.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import nightgames.items.clothing.Clothing;

public class ClothingList extends JList<Clothing> {
    /**
     *
     */
    private static final long serialVersionUID = -4137559825944381962L;

    public ClothingList(DefaultListModel<Clothing> model) {
        super(model);
    }

    @Override
    public String getToolTipText(java.awt.event.MouseEvent event) {
        int location = locationToIndex(event.getPoint());
        Clothing article = getModel().getElementAt(location);
        return article.getDesc();
    }
}
