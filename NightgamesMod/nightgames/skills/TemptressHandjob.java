package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.FiredUp;

public class TemptressHandjob extends Handjob {

    public TemptressHandjob(Character self) {
        super("Skillful Handjob", self);
        addTag(SkillTag.usesHands);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress) && user.get(Attribute.Seduction) >= 5;
    }

    @Override
    public String describe(Combat c) {
        return "Rub your opponent's dick with supreme skill.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 7 + Global.random(getSelf().get(Attribute.Technique) / 2);

        if (target.roll(getSelf(), c, accuracy(c))) {
            if (!target.body.getRandomCock().isReady(target)) {
                m -= 7;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandomCock(), m, c, this);
                if (target.body.getRandomCock().isReady(target)) {
                    // Was flaccid, got hard
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                    getSelf().add(c, new FiredUp(getSelf(), target, "hands"));
                } else {
                    // Was flaccid, still is
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                }
            } else {
                FiredUp status = (FiredUp) getSelf().status.stream().filter(s -> s instanceof FiredUp).findAny()
                                .orElse(null);
                int stack = status == null || !status.getPart().equals("hands") ? 0 : status.getStack();
                c.write(getSelf(), deal(c, stack, Result.normal, target));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandomCock(),
                                m + m * stack / 2, c, this);
                getSelf().add(c, new FiredUp(getSelf(), target, "hands"));
            }
        } else {
            c.write(getSelf(), deal(c, 0, Result.miss, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TemptressHandjob(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        switch (modifier) {
            case miss:
                return String.format("%s down to %s groin, but %s pulls %s hips back.",
                                getSelf().subjectAction("reach", "reaches"), target.nameOrPossessivePronoun(),
                                target.pronoun(), target.possessivePronoun());
            case weak:
                return String.format("%s %s limp %s and %s it expertly, but it remains flaccid despite %s best efforts.",
                                getSelf().subjectAction("grab"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().action("fondle"),
                                getSelf().possessivePronoun());
            case special:
                return String.format(
                                "%s %s limp %s and %s it expertly, and it grows fully hard under %s skilled touch.",
                                getSelf().subjectAction("grab"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().action("massage"),
                                getSelf().possessivePronoun());
            default: // should be Result.normal
                // already hard
                switch (damage) {
                    case 0:
                        return String.format(
                                        "%s hold of %s %s and %s %s fingers over it briskly, hitting all the right spots.",
                                        getSelf().subjectAction("take"), target.nameOrPossessivePronoun(),
                                        target.body.getRandomCock().describe(target), getSelf().action("run"),
                                        getSelf().possessivePronoun());
                    case 1:
                        return String.format(
                                        "%s hold on %s %s tightens, and where once there were gentle touches there are now firm jerks.",
                                        getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                                        target.body.getRandomCock().describe(target));
                    default:
                        return String.format(
                                        "%s latched on to %s %s with both hands now, twisting them in a fierce milking movement and eliciting pleasured groans from %s.",
                                        getSelf().subjectAction("have", "has"), target.nameOrPossessivePronoun(),
                                        target.body.getRandomCock().describe(target), target.directObject());
                }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        // use formatted strings in deal
        return deal(c, damage, modifier, target);
    }
    
    @Override
    public Stage getStage() {
        return Stage.FOREPLAY;
    }
}
