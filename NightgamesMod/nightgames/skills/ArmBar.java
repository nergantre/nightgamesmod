package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.status.Abuff;

public class ArmBar extends Skill {

    public ArmBar(Character self) {
        super("Armbar", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().dom(getSelf()) && c.getStance().reachTop(target) && getSelf().canAct()
                        && !getSelf().has(Trait.undisciplined) && !c.getStance().inserted();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(6, 10));
            writeOutput(c, m, Result.normal, target);
            target.pain(c, getSelf(), m);
            target.add(c, new Abuff(target, Attribute.Power, -4, 5));
            target.emote(Emotion.angry, 15);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 20 && !user.has(Trait.undisciplined);
    }

    @Override
    public Skill copy(Character user) {
        return new ArmBar(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You grab at " + target.name() + "'s arm, but "+target.pronoun()+" pulls it free.";
        } else {
            return "You grab " + target.name()
                            + "'s arm at the wrist and pull it to your chest in the traditional judo submission technique.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s %s wrist, but %s %s it out of %s grasp.",
                            getSelf().subjectAction("grab"), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("pry", "pries"), getSelf().possessivePronoun());
        } else {
            return String.format("%s %s arm between %s legs, forcibly overextending %s elbow. "
                            + "The pain almost makes %s tap out, but %s %s to yank %s arm out of %s grip",
                            getSelf().subjectAction("pull"), target.nameOrPossessivePronoun(), 
                            getSelf().possessivePronoun(), target.possessivePronoun(), target.pronoun(),
                            target.pronoun(), target.action("manage"), target.possessivePronoun(),
                            getSelf().possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "A judo submission hold that hyperextends the arm.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
