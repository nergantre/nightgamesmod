package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Enthralled;
import nightgames.status.Shamed;
import nightgames.status.Trance;

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
        double m = (6 + Global.random(4) + getSelf().body.getHotness(target)) / 3
                        * Math.min(2, 1 + target.getExposure());
        double chance = .25;
        if (target.has(Trait.imagination)) {
            m += 4;
            chance += .25;
        } 
        if (getSelf().has(Trait.bitingwords)) {
            m += 4;
            chance += .25;
        } 
        target.tempt(c, getSelf(), (int) Math.round(m));
        if (Global.randomdouble() < chance) {
            target.add(c, new Shamed(target));
        }
        if (c.getStance().dom(getSelf()) && getSelf().has(Trait.bitingwords)) {
            int willpowerLoss = Math.max(target.getWillpower().max() / 50, 3) + Global.random(3);
            target.loseWillpower(c, willpowerLoss, 0, false, " (Biting Words)");
        }
        if (getSelf().has(Trait.commandingvoice) && Global.random(3) == 0) {
            c.write(getSelf(), Global.format("{other:SUBJECT-ACTION:speak|speaks} with such unquestionable"
                            + " authority that {self:subject-action:don't|doesn't} even consider not obeying."
                            , getSelf(), target));
            target.add(new Enthralled(target, getSelf(), 1));
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
        return getSelf().taunt(c, target);
    }

    @Override
    public String describe(Combat c) {
        return "Embarrass your opponent. Lowers Mojo, may inflict Shamed";
    }
}
