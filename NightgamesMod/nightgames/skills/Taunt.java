package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Shamed;

public class Taunt extends Skill {

    public Taunt(Character self) {
        super("Taunt", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.mostlyNude() && !c.getStance().sub(getSelf()) && getSelf().canAct() && !getSelf().has(Trait.shy);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 10;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        double m = (6 + Global.random(4) + getSelf().body.getHotness(getSelf(), target)) / 3
                        * Math.min(2, 1 + target.getExposure());
        if (target.has(Trait.imagination)) {
            m += 4;
            target.tempt(c, getSelf(), (int) Math.round(m));
            if (Global.random(4) >= 1) {
                target.add(c, new Shamed(target));
            }
        } else {
            target.tempt(c, getSelf(), (int) Math.round(m));
            if (Global.random(4) >= 2 || getSelf().has(Trait.bitingwords)) {
                target.add(c, new Shamed(target));
            }
        }
        if (c.getStance().dom(getSelf()) && getSelf().has(Trait.bitingwords)) {
            int willpowerLoss = Math.max(target.getWillpower().max() / 50, 3) + Global.random(3);
            target.loseWillpower(c, willpowerLoss, 0, false, " (Biting Words)");
        }
        target.emote(Emotion.angry, 30);
        target.emote(Emotion.nervous, 15);
        getSelf().emote(Emotion.dominant, 20);
        target.loseMojo(c, 5);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 8;
    }

    @Override
    public Skill copy(Character user) {
        return new Taunt(user);
    }

    @Override
    public int speed() {
        return 9;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You tell " + target.name()
                        + " that if she's so eager to be fucked senseless, you're available during off hours.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().taunt(c);
    }

    @Override
    public String describe(Combat c) {
        return "Embarrass your opponent. Lowers Mojo, may inflict Shamed";
    }
}
