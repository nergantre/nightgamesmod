package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;

public class Suckle extends Skill {
    public Suckle(Character self) {
        super("Suckle", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.breastsAvailable() && c.getStance().reachTop(getSelf()) && c.getStance().front(getSelf())
                        && (getSelf().canAct() || c.getStance().enumerate() == Stance.nursing && getSelf().canRespond())
                        && c.getStance().facing(getSelf(), target) && c.getStance().en != Stance.neutral;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return resolve(c, target, false);
    }

    public boolean resolve(Combat c, Character target, boolean silent) {
        Result results = target.has(Trait.lactating) ? Result.special : Result.normal;
        int m = (getSelf().get(Attribute.Seduction) > 10 ? 8 : 4) + Global.random(6);
        if (!silent) writeOutput(c, Result.normal, target);
        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }

        target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("breasts"), m, c, this);
        if (results == Result.special) {
            target.buildMojo(c, 10);
        } else {
            target.buildMojo(c, 5);
        }
        return true;
    }
    
    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 6;
    }

    @Override
    public Skill copy(Character user) {
        return new Suckle(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return "You slowly circle your tongue around each of " + target.getName()
                            + "'s nipples, and start sucking like a newborn.";
        } else {
            return "You slowly circle your tongue around each of " + target.getName()
                            + "'s nipples, and start sucking like a newborn. "
                            + "Her milk slides smoothly down your throat, and you're left with a warm comfortable feeling.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return String.format("%s licks and sucks %s nipples, sending a "
                            + "surge of excitement straight to %s groin.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.possessiveAdjective());
        } else {
            return String.format("%s licks and sucks %s nipples, drawing forth "
                            + "a gush of breast milk from %s teats. "
                            + "%s drinks deeply of %s milk, gurgling happily as more of the"
                            + " smooth liquid flows down %s throat.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.possessiveAdjective(),
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            getSelf().possessiveAdjective());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Suck your opponent's nipples. Builds mojo for the opponent.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
