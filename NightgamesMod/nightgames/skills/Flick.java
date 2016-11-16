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

public class Flick extends Skill {

    public Flick(Character self) {
        super("Flick", self, 2);
        addTag(SkillTag.mean);
        addTag(SkillTag.hurt);
        addTag(SkillTag.positioning);
        addTag(SkillTag.staminaDamage);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.crotchAvailable() && c.getStance().reachBottom(getSelf()) && getSelf().canAct()
                        && !getSelf().has(Trait.shy);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (target.has(Trait.brassballs)) {
                writeOutput(c, Result.weak, target);
            } else {
                int mojoLost = 25;
                int m = Global.random(8) + 8;
                writeOutput(c, Result.normal, target);
                if (target.has(Trait.achilles)) {
                    m += 2 + Global.random(target.get(Attribute.Perception) / 2);
                    mojoLost = 40;
                }
                target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, m));
                target.loseMojo(c, mojoLost);
                getSelf().emote(Emotion.dominant, 10);
                target.emote(Emotion.angry, 15);
                target.emote(Emotion.nervous, 15);
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 17;
    }

    @Override
    public Skill copy(Character user) {
        return new Flick(user);
    }

    @Override
    public int speed() {
        return 6;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You flick your finger between " + target.name() + "'s legs, but don't hit anything sensitive.";
        } else if (modifier == Result.weak) {
            return "You flick " + target.name() + "'s balls, but " + target.pronoun() + " seems utterly unfazed.";
        } else {
            if (target.hasBalls()) {
                return "You use two fingers to simultaneously flick both of " + target.name()
                                + " dangling balls. She tries to stifle a yelp and jerks her hips away reflexively. "
                                + "You feel a twinge of empathy, but she's done far worse.";
            } else {
                return "You flick your finger sharply across " + target.name()
                                + "'s sensitive clit, causing her to yelp in surprise and pain. She quickly covers her girl parts "
                                + "and glares at you in indignation.";
            }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s flicks at %s balls, but hits only air.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format("%s flicks %s balls, but %s barely %s a thing.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("feel"));
        } else {
            return String.format("%s gives %s a mischievous grin and flicks each of %s balls with %s finger. "
                            + "It startles %s more than anything, but it does hurt and "
                            + "%s seemingly carefree abuse of %s jewels destroys %s confidence.",
                            getSelf().subject(), target.nameDirectObject(), target.possessivePronoun(),
                            getSelf().possessivePronoun(), target.directObject(), getSelf().nameOrPossessivePronoun(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Flick opponent's genitals, which is painful and embarrassing";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
