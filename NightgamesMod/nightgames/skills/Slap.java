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
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;

public class Slap extends Skill {

    public Slap(Character self) {
        super("Slap", self);
        addTag(SkillTag.mean);
        addTag(SkillTag.hurt);
        addTag(SkillTag.positioning);
        addTag(SkillTag.staminaDamage);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().reachTop(getSelf()) && getSelf().canAct() && c.getStance().front(getSelf());
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return getSelf().has(Trait.pimphand) ? 15 : 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (isSlime()) {
                writeOutput(c, Result.critical, target);
                target.pain(c, getSelf(), Global.random(10) + getSelf().get(Attribute.Slime) + getSelf().get(Attribute.Power) / 2);
                if (c.getStance().en == Stance.neutral && Global.random(5) == 0) {
                    c.setStance(new StandingOver(getSelf(), target));
                    c.write(getSelf(),
                                    Global.format("{self:SUBJECT-ACTION:slap|slaps} {other:direct-object} hard"
                                                    + " enough to throw {other:pronoun} to the ground.", getSelf(),
                                    target));
                }
                target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(10, 20)));
                target.emote(Emotion.nervous, 40);
                target.emote(Emotion.angry, 30);
            } else if (getSelf().get(Attribute.Animism) >= 8) {
                writeOutput(c, Result.special, target);
                if (getSelf().has(Trait.pimphand)) {
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(35, 50) * (25 + getSelf().getArousal().percent()) / 100));
                    target.emote(Emotion.nervous, 40);
                    target.emote(Emotion.angry, 30);
                } else {
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(25, 45) * (25 + getSelf().getArousal().percent()) / 100));
                    target.emote(Emotion.nervous, 25);
                    target.emote(Emotion.angry, 30);
                }
            } else {
                writeOutput(c, Result.normal, target);
                if (getSelf().has(Trait.pimphand)) {
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(7, 15)));
                    target.emote(Emotion.nervous, 20);
                    target.emote(Emotion.angry, 30);
                } else {
                    target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(5, 10)));
                    target.emote(Emotion.nervous, 10);
                    target.emote(Emotion.angry, 30);
                }
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 5;
    }

    @Override
    public Skill copy(Character user) {
        return new Slap(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }
    
    private boolean isSlime() {
        return getSelf().get(Attribute.Slime) > 4;
    }

    @Override
    public String getLabel(Combat c) {
        if (isSlime()) {
            return "Clobber";
        } else if (getSelf().get(Attribute.Animism) >= 8) {
            return "Tiger Claw";
        } else {
            return "Slap";
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return target.name() + " avoids your slap.";
        } else if (modifier == Result.special) {
            return "You channel your bestial power and strike " + target.name() + " with a solid open hand strike.";
        } else if (modifier == Result.critical) {
            return "You let more of your slime flow to your hand, tripling it in size. Then, you lash out and slam "
                            + target.name() + " in the face.";
        } else {
            return "You slap " + target.name()
                            + "'s cheek; not hard enough to really hurt her, but enough to break her concentration.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to slap %s but %s %s %s wrist.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("catch", "catches"),
                            getSelf().possessivePronoun());
        } else if (modifier == Result.special) {
            return String.format("%s palm hits %s in a savage strike that makes %s head ring.",
                            getSelf().nameOrPossessivePronoun(), target.nameDirectObject(),
                            target.possessivePronoun());
        } else if (modifier == Result.critical) {
            return String.format("%s hand grows significantly, and then %s swings it powerfully into %s face.",
                            getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                            target.nameOrPossessivePronoun());
        } else {
            return String.format("%s slaps %s across the face, leaving a stinging heat on %s cheek.",
                            getSelf().subject(), target.nameDirectObject(), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Slap opponent across the face";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
