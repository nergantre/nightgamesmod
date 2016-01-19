package nightgames.nskillls;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.custom.requirement.AttributeRequirement;
import nightgames.status.Stsflag;

public class SkillPoolBuilder {
    List<SkillInterface> skills;

    public SkillPoolBuilder() {
        skills = new ArrayList<>();
    }

    public void buildSkillPool() {
        GenericSkill shove = new GenericSkill("Shove");
        shove.addRequirement(new AttributeRequirement(Attribute.Power, 5));
        shove.addUsableRequirement((c, user, target) -> !target.is(Stsflag.cockbound) && !c.getStance().dom(user)
                        && !c.getStance().prone(target) && c.getStance().reachTop(user) && user.canAct()
                        && !c.getStance().havingSex());
        SkillResult result = new SkillResult((c, user, target) -> {
            return true;
        });
    }
}
