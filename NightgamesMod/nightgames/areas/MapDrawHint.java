package nightgames.areas;

import java.awt.Rectangle;

public class MapDrawHint {
    public MapDrawHint() {
        this(new Rectangle(0, 0, 0, 0), "", false);
    }

    public MapDrawHint(Rectangle rect, String label, boolean vertical) {
        this.rect = rect;
        this.label = label;
        this.vertical = vertical;
    }

    public Rectangle rect;
    public String label;
    public boolean vertical;
}
