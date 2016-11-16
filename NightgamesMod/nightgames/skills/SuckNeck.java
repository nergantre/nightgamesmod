package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class SuckNeck extends Skill {

    public SuckNeck(Character self) {
        super("Suck Neck", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().kiss(getSelf(), target) && getSelf().canAct();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 7;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().get(Attribute.Dark) >= 1) {
                writeOutput(c, Result.special, target);
                int m = 10;
                target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, m));
            } else {
                writeOutput(c, Result.normal, target);
            }
            int m = 1 + Global.random(8);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("skin"), m, c, this);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Seduction) >= 12;
    }

    @Override
    public Skill copy(Character user) {
        return new SuckNeck(user);
    }

    @Override
    public int speed() {
        return 5;
    }

    @Override
    public int accuracy(Combat c) {
        return c.getStance().dom(getSelf()) ? 100 : 70;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Dark) >= 1) {
            return "Drain energy";
        } else {
            return getName(c);
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You lean in to kiss " + target.name() + "'s neck, but she slips away.";
        } else if (modifier == Result.special) {
            return "You draw close to " + target.name()
                            + " as she's momentarily too captivated to resist. You run your tongue along her neck and bite gently. She shivers and you "
                            + "can feel the energy of her pleasure flow into you, giving you strength.";
        } else {
            return "You lick and suck " + target.name() + "'s neck hard enough to leave a hickey.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s goes after %s neck, but %s %s %s back.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("push", "pushes"),
                            getSelf().possessivePronoun());
        } else if (modifier == Result.special) {
            return String.format("%s presses %s lips against %s neck. %s gives %s a "
                            + "hickey and %s knees start to go weak. It's like %s strength"
                            + " is being sucked out through "
                            + "%s skin.", getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), getSelf().subject(),
                            target.directObject(), target.possessivePronoun(), target.possessivePronoun(),
                            target.possessivePronoun());
        } else {
            return String.format("%s licks and sucks %s neck, biting lightly when %s %s expecting it.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("aren't", "isn't"));
        }
    }

    @Override
    public String describe(Combat c) {
        return "Suck on opponent's neck. Highly variable effectiveness";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FOREPLAY;
    }
}
