package nightgames.skills;

import java.awt.Color;

public enum Tactics {
    damage(TacticGroup.hurt, new Color(150, 0, 0)),
    pleasure(TacticGroup.pleasure, Color.PINK),
    fucking(TacticGroup.pleasure, new Color(255, 100, 200)),
    positioning(TacticGroup.preparation, new Color(0, 100, 0)),
    stripping(TacticGroup.preparation, new Color(0, 100, 0)),
    recovery(TacticGroup.recovery, Color.WHITE),
    calming(TacticGroup.recovery, Color.WHITE),
    debuff(TacticGroup.manipulation, Color.CYAN),
    summoning(TacticGroup.manipulation, Color.YELLOW),
    misc(TacticGroup.misc, new Color(200, 200, 200));

    private final Color color;
    private final TacticGroup group;
    Tactics(TacticGroup group, Color color) {
        this.color = color;
        this.group = group;
    }

    public Color getColor() {
        return color;
    }

    public TacticGroup getGroup() {
        return group;
    }
}
