package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.ReverseMount;
import nightgames.stance.SixNine;

public class Blowjob extends Skill {
    public Blowjob(String name, Character self) {
        super(name, self);
    }

    public Blowjob(Character self) {
        this("Blow", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canUse = c.getStance().isBeingFaceSatBy(c, getSelf(), target) && getSelf().canRespond()
                        || getSelf().canAct();
        return target.crotchAvailable() && target.hasDick() && c.getStance().oral(getSelf(), target)
                        && c.getStance().front(getSelf()) && canUse && !c.getStance().inserted(target)
                        || getSelf().canRespond() && isVaginal(c);
    }

    @Override
    public float priorityMod(Combat c) {
        float priority = 0;
        if (c.getStance().penetratedBy(c, getSelf(), c.getOpponent(getSelf()))) {
            priority += 1.0f;
        }
        if (getSelf().has(Trait.silvertongue)) {
            priority += 1;
        }
        if (getSelf().has(Trait.experttongue)) {
            priority += 1;
        }
        return priority;
    }

    public boolean isVaginal(Combat c) {
        return c.getStance().vaginallyPenetratedBy(c, getSelf(), c.getOpponent(getSelf()))
                        && !c.getOpponent(getSelf()).has(Trait.strapped) && getSelf().has(Trait.vaginaltongue);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        if (isVaginal(c)) {
            return 10;
        } else if (c.getStance().isBeingFaceSatBy(c, getSelf(), c.getOpponent(getSelf()))) {
            return 0;
        } else {
            return 5;
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 10 + Global.random(8);
        boolean facesitting = c.getStance().isBeingFaceSatBy(c, getSelf(), target);
        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }
        if (isVaginal(c)) {
            m += 4;
            writeOutput(c, m, Result.intercourse, target);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("pussy"), target.body.getRandom("cock"), m, c, this);
        } else if (facesitting) {
            writeOutput(c, m, Result.reverse, target);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("cock"), m, c, this);
            target.buildMojo(c, 10);
        } else if (!c.getStance().mobile(target) || target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, m, getSelf().has(Trait.silvertongue) ? Result.special : Result.normal, target);
            BodyPart mouth = getSelf().body.getRandom("mouth");
            BodyPart cock = target.body.getRandom("cock");
            target.body.pleasure(getSelf(), mouth, cock, m, c, this);
            if (mouth.isErogenous()) {
                getSelf().body.pleasure(target, cock, mouth, m, c, this);
            }

            if (ReverseMount.class.isInstance(c.getStance())) {
                c.setStance(new SixNine(getSelf(), target));
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 10 && !user.has(Trait.temptress);
    }

    @Override
    public int accuracy(Combat c) {
        return 75;
    }

    @Override
    public Skill copy(Character user) {
        return new Blowjob(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        if (c.getStance().vaginallyPenetrated(c, getSelf()) && getSelf().has(Trait.vaginaltongue)) {
            return Tactics.fucking;
        } else {
            return Tactics.pleasure;
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String m = "";
        if (modifier == Result.miss) {
            m = "You try to take " + target.name() + "'s penis into your mouth, but she manages to pull away.";
        }
        if (target.getArousal().get() < 15) {
            m = "You suck on " + target.name()
                            + " flaccid little penis until it grows into an intimidating large erection.";
        } else if (target.getArousal().percent() >= 90) {
            m = target.name()
                            + "'s girl-cock seems ready to burst, so you suck on it strongly and attack the glans with your tongue fiercely.";
        } else if (modifier == Result.special) {
            m = "You put your skilled tongue to good use tormenting and teasing her unnatural member.";
        } else if (modifier == Result.reverse) {
            m = "With " + target.name() + " sitting over your face, you have no choice but to try to suck her off.";
        } else {
            m = "You feel a bit odd, faced with " + target.name()
                            + "'s rigid cock, but as you lick and suck on it, you discover the taste is quite palatable. Besides, "
                            + "making " + target.name() + " squirm and moan in pleasure is well worth it.";
        }
        if (modifier != Result.miss && getSelf().body.getRandom("mouth").isErogenous()) {
            m += "<br>Unfortunately for you, your sensitive modified mouth pussy sends spasms of pleasure into you too as you mouth fuck "
                            + target.possessivePronoun() + " cock.";
        }
        return m;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String m = "";
        if (modifier == Result.miss) {
            m += String.format("%s tries to suck %s cock, but %s %s %s hips back to avoid %s.",
                            getSelf().name(), target.nameOrPossessivePronoun(), target.pronoun(),
                            target.action("pull"), target.possessivePronoun(), getSelf().directObject());
        } else if (modifier == Result.special) {
            m += String.format("%s soft lips and talented tongue work over %s dick, drawing out"
                            + " dangerously irresistible pleasure with each touch.", 
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun());
        } else if (modifier == Result.intercourse) {
            m += String.format("%s pussy lips suddenly quiver and %s a long sinuous object wrap around %s cock. "
                            + "%s realize she's controlling her vaginal tongue to blow %s with her pussy! "
                            + "Her lower tongue runs up and down %s shaft causing %s to shudder with arousal.",
                            getSelf().nameOrPossessivePronoun(), target.subjectAction("feel"),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.pronoun()), target.directObject(),
                            target.possessivePronoun(), target.directObject());
        } else if (modifier == Result.reverse) {
            m += String.format("Faced with %s dick sitting squarely in front of %s face, %s"
                            + " obediently tongues %s cock in defeat.", target.nameOrPossessivePronoun(),
                            getSelf().nameOrPossessivePronoun(), getSelf().pronoun(), target.possessivePronoun());
        } else if (target.getArousal().get() < 15) {
            m += String.format("%s %s soft penis into %s mouth and sucks on it until it hardens.",
                            getSelf().subjectAction("take"), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun());
        } else if (target.getArousal().percent() >= 90) {
            m += String.format("%s up the precum leaking from %s cock and %s the entire length into %s mouth, sucking relentlessly.",
                            getSelf().subjectAction("lap"), target.nameOrPossessivePronoun(), getSelf().action("take"),
                            getSelf().possessivePronoun());
        } else {
            int r = Global.random(4);
            if (r == 0) {
                m += String.format("%s %s tongue up the length of %s dick, sending a jolt of pleasure up %s spine. "
                                + "%s slowly wraps %s lips around %s dick and sucks.",
                                getSelf().subjectAction("run"), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                                target.possessivePronoun(), Global.capitalizeFirstLetter(getSelf().pronoun()),
                                getSelf().possessivePronoun(), target.nameOrPossessivePronoun());
            } else if (r == 1) {
                m += String.format("%s on the head of %s cock while %s hand strokes the shaft.",
                                getSelf().subjectAction("suck"), target.nameOrPossessivePronoun(), getSelf().possessivePronoun());
            } else if (r == 2) {
                m += String.format("%s %s way down to the base of %s cock and gently sucks on %s balls.",
                                getSelf().subjectAction("lick"), getSelf().possessivePronoun(),
                                target.nameOrPossessivePronoun(), target.possessivePronoun());
            } else {
                m += String.format("%s %s tongue around the glans of %s penis and teases %s urethra.",
                                getSelf().subjectAction("run"), getSelf().possessivePronoun(),
                                target.nameOrPossessivePronoun(), target.possessivePronoun());
            }
        }

        if (modifier != Result.miss && getSelf().body.getRandom("mouth").isErogenous()) {
            m += String.format("<br>Unfortunately for %s, as %s mouth fucks %s cock %s sensitive"
                            + " modifier mouth pussy sends spasms of pleasure into %s as well.", 
                            getSelf().directObject(), getSelf().subject(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), getSelf().reflectivePronoun());
        }
        return m;
    }

    @Override
    public String describe(Combat c) {
        return "Lick and suck your opponent's dick";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
