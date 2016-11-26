package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.status.Alluring;
import nightgames.status.Charmed;

public class TemptressStripTease extends StripTease {

    public TemptressStripTease(Character self) {
        super("Skillful Strip Tease", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress) && user.get(Attribute.Technique) >= 8;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return isDance(c) ? 0 : super.getMojoBuilt(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return isDance(c) ? super.getMojoBuilt(c) : super.getMojoCost(c);
    }

    public boolean canStrip(Combat c, Character target) {
        boolean sexydance = c.getStance().enumerate() == Stance.neutral && getSelf().canAct() && getSelf().mostlyNude();
        boolean normalstrip = !getSelf().mostlyNude();
        return getSelf().stripDifficulty(target) == 0 && (sexydance || normalstrip);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return canStrip(c, target) && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && !c.getStance().prone(getSelf());
    }

    @Override
    public String getLabel(Combat c) {
        return isDance(c) ? "Sexy Dance" : super.getLabel(c);
    }

    @Override
    public String describe(Combat c) {
        return isDance(c) ? "Do a slow, titilating dance to charm your opponent."
                        : "Shed your clothes seductively, charming your opponent.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int technique = getSelf().get(Attribute.Technique);
        //assert technique > 0;

        if (isDance(c)) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.weak, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.weak, target));
            }
            target.tempt(c, getSelf(), 10 + Global.random(Math.max(5, technique)));
            if (Global.random(2) == 0) {
                target.add(c, new Charmed(target, Global.random(Math.min(3, technique))));
            }
            getSelf().add(c, new Alluring(getSelf(), 3));
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }

            target.tempt(c, getSelf(), 15 + Global.random(Math.max(10, technique)));
            target.add(c, new Charmed(target, Global.random(Math.min(5, technique))));
            getSelf().add(c, new Alluring(getSelf(), 5));
            getSelf().undress(c);
        }
        target.emote(Emotion.horny, 30);
        getSelf().emote(Emotion.confident, 15);
        getSelf().emote(Emotion.dominant, 15);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TemptressStripTease(user);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (isDance(c)) {
            return String.format("%s backs up a little and starts swinging"
                            + " her hips side to side. Curious as to what's going on, %s"
                            + " %s attacks and watch as she bends and curves, putting"
                            + " on a slow dance that would be very arousing even if she weren't"
                            + " naked. Now, without a stitch of clothing to obscure %s view,"
                            + " the sight stirs %s imagination. %s shocked out of %s"
                            + " reverie when she plants a soft kiss on %s lips, and %s dreamily"
                            + " %s into her eyes as she gets back into a fighting stance.",
                            getSelf().subject(), target.subjectAction("cease"),
                            target.possessivePronoun(), target.possessivePronoun(),
                            target.nameOrPossessivePronoun(), target.subjectAction("are", "is"),
                            target.possessivePronoun(), target.possessivePronoun(), target.pronoun(),
                            target.action("gaze"));
        } else {
            return String.format("%s takes a few steps back and starts "
                            + "moving sinously. She sensually runs her hands over her body, "
                            + "undoing straps and buttons where she encounters them, and starts"
                            + " peeling her clothes off slowly, never breaking eye contact."
                            + " %s can only gawk in amazement as her perfect body is revealed bit"
                            + " by bit, and the thought of doing anything to blemish such"
                            + " perfection seems very unpleasant indeed.", getSelf().subject(),
                            Global.capitalizeFirstLetter(target.subject()));
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (isDance(c)) {
            return "You slowly dance for " + target.name() + ", showing off" + " your naked body.";
        } else {
            return "You seductively perform a short dance, shedding clothes as you do so. " + target.name()
                            + " seems quite taken with it, as " + target.pronoun()
                            + " is practically drooling onto the ground.";
        }
    }

    private boolean isDance(Combat c) {
        return !super.usable(c, c.getOpponent(getSelf())) && usable(c, c.getOpponent(getSelf()));
    }
}
