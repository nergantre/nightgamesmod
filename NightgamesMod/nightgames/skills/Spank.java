package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Shamed;
import nightgames.status.Stsflag;

public class Spank extends Skill {

    public Spank(Character self) {
        super("Spank", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getStance().prone(target) && c.getStance().reachBottom(getSelf())
                        && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().has(Trait.disciplinarian)) {
            boolean shamed = Global.random(10) >= 5 || !target.is(Stsflag.shamed) && getSelf().canSpend(5);
            if (shamed) {
                getSelf().spendMojo(c, 5);
            }
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.special, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.special, target));
            }
            if (shamed) {
                target.add(c, new Shamed(target));
                target.emote(Emotion.angry, 10);
                target.emote(Emotion.nervous, 15);
            }
            if (target.has(Trait.achilles)) {
                target.pain(c, 5);
            }
            target.pain(c, Global.random(6 + target.get(Attribute.Perception) / 2) + 3);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            target.pain(c, Global.random(6) + 3);
        }
        target.emote(Emotion.angry, 25);
        target.emote(Emotion.nervous, 15);
        target.loseMojo(c, 10);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 8;
    }

    @Override
    public Skill copy(Character user) {
        return new Spank(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public int accuracy(Combat c) {
        return c.getStance().dom(getSelf()) ? 100 : 65;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to spank " + target.name() + ", but she dodges away.";
        }
        if (modifier == Result.special) {
            return "You bend " + target.name()
                            + " over your knee and spank her, alternating between hitting her soft butt cheek and her sensitive pussy.";
        } else {
            return "You spank " + target.name() + " on her naked butt cheek.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name() + " aims a slap at your ass, but you dodge it.";
        }
        if (modifier == Result.special) {
            return getSelf().name()
                            + " bends you over like a misbehaving child and spanks your ass twice. The third spank aims lower and connects solidly with your ballsack, "
                            + "injuring your manhood along with your pride.";
        } else {
            return getSelf().name() + " lands a stinging slap on your bare ass.";
        }

    }

    @Override
    public String describe(Combat c) {
        return "Slap opponent on the ass. Lowers Mojo.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
