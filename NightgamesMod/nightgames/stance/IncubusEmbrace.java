package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.skills.FondleBreasts;
import nightgames.skills.Skill;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class IncubusEmbrace extends MaledomSexStance {

    private final Supplier<Status> statusBuilder;
    private final Stsflag flag;
    
    public IncubusEmbrace(Character top, Character bottom) {
        this(top, bottom, null, null);
    }

    public IncubusEmbrace(Character top, Character bottom, Supplier<Status> status, Stsflag flag) {
        super(top, bottom, Stance.incubusembrace);
        this.statusBuilder = status;
        this.flag = flag;
    }
    
    @Override
    public String describe(Combat c) {
        return null;
    }

    @Override
    public void checkOngoing(Combat c) {
        super.checkOngoing(c);
        if (c.getStance() != this) return;
        
        if (flag != null && statusBuilder != null && !bottom.is(flag)) {
            bottom.add(c, statusBuilder.get());
        } else if (flag == null && bottom.hasBreasts()) {
            FondleBreasts fb = new FondleBreasts(top);
            if (Skill.skillIsUsable(c, fb, bottom)) {
                fb.resolve(c, bottom);
            }
        }
    }
    
    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomAss()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }
    
    @Override
    public int dominance() {
        return 4;
    }
    
    @Override
    public boolean mobile(Character c) {
        return c == top;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c == top;
    }

    @Override
    public boolean dom(Character c) {
        return c == top;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public String image() {
        return top.hasBreasts() ? "incubus_embrace.jpg" : "";
    }

}
