package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class BreastWorship extends Skill {
    public BreastWorship(Character self) {
        super("Breast Worship", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.worship);
        addTag(SkillTag.pleasureSelf);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.breastsAvailable() && c.getStance().reachTop(getSelf()) && c.getStance().front(getSelf())
                        && (getSelf().canAct() || c.getStance().enumerate() == Stance.nursing && getSelf().canRespond())
                        && c.getStance().facing(getSelf(), target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result results = target.has(Trait.lactating) ? Result.special : Result.normal;
        int m = 8 + Global.random(6);
        writeOutput(c, results, target);
        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }
        target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("breasts"), m, c, this);
        if (getSelf().hasDick() && (!getSelf().hasPussy() || Global.random(2) == 0)) {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomCock(), m, c, this);
        } else if (getSelf().hasPussy()) {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomPussy(), m,
                            c, this);
        } else {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomHole(), m, c, this);
        }
        if (results == Result.special) {
            getSelf().temptWithSkill(c, target, target.body.getRandomBreasts(), (3 + target.body.getRandomBreasts().getSize()) * 2, this);
            target.buildMojo(c, 10);
        } else {
            target.buildMojo(c, 5);
        }
        return true;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return 150;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        Optional<BodyFetish> fetish = getSelf().body.getFetish("breasts");
        return user.isPetOf(target) || (fetish.isPresent() && fetish.get().magnitude >= .5);
    }

    @Override
    public Skill copy(Character user) {
        return new BreastWorship(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return "You worshipfully circle your tongue around each of " + target.getName()
                            + "'s nipples, and start sucking like a newborn while furiously masturbating.";
        } else {
            return "You worshipfully circle your tongue around each of " + target.getName()
                            + "'s nipples, and start sucking like a newborn while furiously masturbating. "
                            + "Her milk slides smoothly down your throat, and you're left with a warm comfortable feeling.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return getSelf().getName()
                            + " worshipfully licks and sucks "+target.nameOrPossessivePronoun()+
                            " nipples while uncontrollably playing with "+getSelf().reflectivePronoun()+".";
        } else {
            return String.format("%s worshipfully licks and sucks %s nipples while uncontrollably masturbating, drawing forth "
                            + "a gush of breast milk from %s teats. %s drinks deeply of %s milk, gurgling happily "
                            + "as more of the smooth liquid flows down %s throat.",
                            getSelf().getName(), target.nameOrPossessivePronoun(), target.possessiveAdjective(),
                            getSelf().getName(), target.possessiveAdjective(), getSelf().possessiveAdjective());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Worships your opponent's breasts.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
