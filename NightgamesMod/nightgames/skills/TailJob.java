package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;

public class TailJob extends Skill {

    public TailJob(Character self) {
        super("Tailjob", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        boolean enough = getSelf().get(Attribute.Seduction) >= 20 || getSelf().get(Attribute.Animism) >= 1;
        return enough && user.body.get("tail").size() > 0;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && target.crotchAvailable() && c.getStance().mobile(getSelf())
                        && !c.getStance().mobile(target) && !c.getStance().inserted(target);
    }

    @Override
    public String describe(Combat c) {
        return "Use your tail to tease your opponent";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        int m = (5 + Global.random(10)) + Math.min(getSelf().getArousal().getReal() / 20, getSelf().get(Attribute.Animism));
        String receiver;
        if (target.hasDick()) {
            receiver = "cock";
        } else {
            receiver = "pussy";
        }
        if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "tail", .25));
        }
        target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom(receiver), m, c);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TailJob(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (target.hasDick()) {
            return "You skillfully use your flexible " + getSelf().body.getRandom("tail").describe(getSelf())
                            + " to stroke and tease " + target.name() + "'s sensitive girl-cock.";
        } else {
            return "You skillfully use your flexible " + getSelf().body.getRandom("tail").describe(getSelf())
                            + " to stroke and tease " + target.name() + "'s sensitive girl parts.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (target.hasDick()) {
            return getSelf().name() + " teases your sensitive dick and balls with her "
                            + getSelf().body.getRandom("tail").describe(getSelf())
                            + ". It wraps completely around your shaft and strokes firmly.";
        } else {
            return getSelf().name() + " teases your sensitive pussy with her "
                            + getSelf().body.getRandom("tail").describe(getSelf())
                            + ". It runs along your nether lips and leaves you gasping.";
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
