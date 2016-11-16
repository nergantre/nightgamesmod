package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Bound;
import nightgames.status.Falling;

public class DarkTendrils extends Skill {

    public DarkTendrils(Character self) {
        super("Dark Tendrils", self, 4);
        addTag(SkillTag.positioning);
        addTag(SkillTag.knockdown);
        addTag(SkillTag.dark);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && !c.getStance().sub(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && getSelf().canAct();
    }

    @Override
    public String describe(Combat c) {
        return "Summon shadowy tentacles to grab or trip your opponent: 15% Arousal";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().arouse((int) (getSelf().getArousal().max() * .15), c);
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (Global.random(2) == 1) {
                writeOutput(c, Result.normal, target);
                target.add(c, new Bound(target, Math.min(10 + 3 * getSelf().get(Attribute.Dark), 55), "shadows"));
                target.add(c, new Falling(target));
            } else if (getSelf().check(Attribute.Dark, target.knockdownDC() - getSelf().getMojo().get())) {
                writeOutput(c, Result.weak, target);
                target.add(c, new Falling(target));
            } else {
                writeOutput(c, Result.miss, target);
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public nightgames.skills.Skill copy(Character user) {
        return new DarkTendrils(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public int accuracy(Combat c) {
        return 75;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You summon dark tentacles to hold " + target.name() + ", but she twists away.";
        } else if (modifier == Result.weak) {
            return "You summon dark tentacles that take " + target.name() + " feet out from under her.";
        } else {
            return "You summon a mass of shadow tendrils that entangle " + target.name()
                            + " and pin her arms in place.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s makes a gesture and evil looking tentacles pop up around %s. %s %s out of the way as they try to grab %s.",
                            getSelf().subject(), target.subject(), Global.capitalizeFirstLetter(target.pronoun()),
                            target.action("dive"), target.directObject());
        } else if (modifier == Result.weak) {
            return String.format("%s shadow seems to come to life as dark tendrils wrap around %s legs and bring %s to the floor.",
                            target.nameOrPossessivePronoun(), target.possessivePronoun(), target.directObject());
        } else {
            return String.format("%s summons shadowy tentacles which snare %s arms and hold %s in place.", 
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject());
        }
    }

}
