package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Behind;
import nightgames.stance.Position;
import nightgames.status.Primed;

public class CheapShot extends Skill {

    public CheapShot(Character self) {
        super("Cheap Shot", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 2;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        Position s = c.getStance();
        return s.mobile(getSelf()) && !s.prone(getSelf()) && !s.prone(target) && !s.behind(getSelf())
                        && getSelf().canAct() && !s.penetrated(c, target) && !s.penetrated(c, getSelf())
                        && Primed.isPrimed(getSelf(), 3);
    }

    @Override
    public String describe(Combat c) {
        return "Stop time long enough to get in an unsportsmanlike attack from behind: 3 charges";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().add(new Primed(getSelf(), -3));
        writeOutput(c, Result.normal, target);
        if (target.human() && Global.random(5) >= 3) {
            c.write(getSelf(), getSelf().bbLiner(c, target));
        }
        c.setStance(new Behind(getSelf(), target));
        target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(8, 20)));
        getSelf().buildMojo(c, 10);

        getSelf().emote(Emotion.confident, 15);
        getSelf().emote(Emotion.dominant, 15);
        target.emote(Emotion.nervous, 10);
        target.emote(Emotion.angry, 20);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CheapShot(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (target.mostlyNude()) {
            if (target.hasBalls()) {
                return String.format(
                                "You freeze time briefly, giving you a chance to circle around %s. When time resumes, %s looks around in "
                                                + "confusion, completely unguarded. You capitalize on your advantage by crouching behind %s and delivering a decisive "
                                                + "uppercut to %s dangling balls.",
                                target.name(), target.pronoun(), target.directObject(), target.possessivePronoun());
            } else {
                return String.format(
                                "You freeze time briefly, giving you a chance to circle around %s. When time resumes, %s looks around in "
                                                + "confusion, completely unguarded. You capitalize on your advantage by crouching behind %s and delivering a swift, but "
                                                + "painful cunt punt.",
                                target.name(), target.pronoun(), target.directObject());
            }
        } else {
            return String.format(
                            "You freeze time briefly, giving you a chance to circle around %s. When time resumes, %s looks around in "
                                            + "confusion, completely unguarded. You capitalize on your advantage by crouching behind %s and delivering a decisive "
                                            + "uppercut to the groin.",
                            target.name(), target.pronoun(), target.directObject());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (target.mostlyNude()) {
            return String.format(
                            "%s suddenly vanishes right in front of %s eyes. That wasn't just fast, %s completely disappeared! Before "
                                            + "%s can react, %s %s hit from behind with a devastating punch to %s unprotected balls.",
                            getSelf().name(), target.nameOrPossessivePronoun(), getSelf().pronoun(),
                            target.subject(), target.pronoun(), target.subjectAction("are", "is"), target.possessivePronoun());
        } else {
            return String.format(
                            "%s suddenly vanishes right in front of %s eyes. That wasn't just fast, %s completely disappeared! %s something "
                                            + "that sounds like 'Za Warudo' before %s suffer a painful groin hit from behind.",
                            getSelf().name(), target.nameOrPossessivePronoun(), getSelf().pronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("hear")), target.pronoun());
        }
    }

}
