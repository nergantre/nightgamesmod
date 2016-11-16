package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;

public class LickNipples extends Skill {

    public LickNipples(Character self) {
        super("Lick Nipples", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.breastsAvailable() && c.getStance().reachTop(getSelf()) && c.getStance().front(getSelf())
                        && getSelf().canAct() && c.getStance().facing(getSelf(), target) && c.getStance().en != Stance.neutral;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 7;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 3 + Global.random(6);
        if (target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.normal, target);
            if (getSelf().has(Trait.silvertongue)) {
                m += 4;
            }
            target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("breasts"), m, c, this);

        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 14;
    }

    @Override
    public Skill copy(Character user) {
        return new LickNipples(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You go after " + target.name() + "'s nipples, but she pushes you away.";
        } else {
            return "You slowly circle your tongue around each of " + target.name()
                            + "'s nipples, making her moan and squirm in pleasure.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to suck on %s chest, but %s %s %s.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("avoid"), getSelf().directObject());
        } else {
            return String.format("%s licks and sucks %s nipples, sending a surge of excitement straight to %s groin.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Suck your opponent's nipples";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
