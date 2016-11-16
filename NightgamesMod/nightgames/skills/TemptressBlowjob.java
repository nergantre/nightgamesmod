package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.FiredUp;

public class TemptressBlowjob extends Blowjob {

    public TemptressBlowjob(Character user) {
        super("Skillful Blowjob", user);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
    }

    @Override
    public float priorityMod(Combat c) {
        return super.priorityMod(c) + 1.5f;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress);
    }

    @Override
    public String describe(Combat c) {
        return "Use your supreme oral skills on your opponent's dick.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 10 + Global.random(getSelf().get(Attribute.Technique) / 2);

        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }

        if (target.roll(getSelf(), c, accuracy(c))) {
            if (!target.body.getRandomCock().isReady(target)) {
                m -= 7;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandomCock(), m, c, this);
                if (target.body.getRandomCock().isReady(target)) {
                    // Was flaccid, got hard
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                    getSelf().add(c, new FiredUp(getSelf(), target, "mouth"));
                } else {
                    // Was flaccid, still is
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                }
            } else {
                FiredUp status = (FiredUp) getSelf().status.stream().filter(s -> s instanceof FiredUp).findAny()
                                .orElse(null);
                int stack = status == null || !status.getPart().equals("mouth") ? 0 : status.getStack();
                c.write(getSelf(), deal(c, stack, Result.normal, target));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandomCock(),
                                m + m * stack / 2, c, this);
                getSelf().add(c, new FiredUp(getSelf(), target, "mouth"));
            }
        } else {
            c.write(getSelf(), deal(c, 0, Result.miss, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TemptressBlowjob(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        switch (modifier) {
            case miss:
                return String.format("%s towards %s %s, but %s %s hips back.", getSelf().subjectAction("move"),
                                target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                                target.pronoun(), target.action("pull"));
            case weak:
                return String.format(
                                "%s up %s flaccid %s, doing everything %s"
                                                + " can to get it hard, but %s %s back before %s can manage it.",
                                getSelf().subjectAction("gobble"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().pronoun(), target.pronoun(),
                                target.action("pull"), getSelf().pronoun());
            case special:
                return String.format(
                                "%s %s %s into %s mouth and %s on it powerfully. It hardens"
                                                + " swiftly, as if %s pulled the blood right into it.",
                                getSelf().subjectAction("take"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().possessivePronoun(),
                                getSelf().action("suck"), getSelf().pronoun());
            default: // should be Result.normal
                switch (damage) {
                    case 0:
                        return String.format(
                                        "%s to town on %s %s, licking it all over."
                                                        + " Long, slow licks along the shaft and small, swift licks"
                                                        + " around the head cause %s to groan in pleasure.",
                                        getSelf().subjectAction("go", "goes"), target.nameOrPossessivePronoun(),
                                        target.body.getRandomCock().describe(target), target.directObject());
                    case 1:
                        return String.format("%s %s lips around the head of %s hard and wet %s "
                                        + "and %s on it forcefully while swirling %s tongue rapidly"
                                        + " around. At the same time, %s hands are massaging and"
                                        + " caressing every bit of sensitive flesh not covered by" + " %s mouth.",
                                        getSelf().subjectAction("lock"), getSelf().possessivePronoun(),
                                        target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                                        getSelf().action("suck"), getSelf().possessivePronoun(),
                                        getSelf().possessivePronoun(), getSelf().possessivePronoun());
                    default:
                        return String.format("%s bobbing up and down now, hands still working"
                                        + " on any exposed skin while %s %s, %s and even %s all over %s"
                                        + " over-stimulated manhood. %s %s not even trying to hide %s"
                                        + " enjoyment, and %s %s loudly every time %s teeth graze" + " %s shaft.",
                                        getSelf().subjectAction("are", "is"), getSelf().pronoun(),
                                        getSelf().action("lick"), getSelf().action("suck"),
                                        getSelf().action("nibble"), target.possessivePronoun(),
                                        target.nameDirectObject(), target.action("are", "is"),
                                        target.possessivePronoun(), target.pronoun(), target.action("grunt"),
                                        getSelf().possessivePronoun(), target.possessivePronoun());
                }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

}
