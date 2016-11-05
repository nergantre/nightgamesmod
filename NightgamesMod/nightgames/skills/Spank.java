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
        return !c.getStance().prone(target) && c.getStance().reachBottom(getSelf()) && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().has(Trait.disciplinarian)) {
            boolean shamed = Global.random(10) >= 5 || !target.is(Stsflag.shamed) && getSelf().canSpend(5);
            if (shamed) {
                getSelf().spendMojo(c, 5);
            }
            writeOutput(c, Result.special, target);
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
            writeOutput(c, Result.normal, target);
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
            return "You try to spank " + target.name() + ", but "+target.pronoun()+" dodges away.";
        }
        if (modifier == Result.special) {
            return "You bend " + target.name()
                            + " over your knee and spank "+target.directObject()+", alternating between hitting "+target.possessivePronoun()+" soft butt cheek and "+target.possessivePronoun()+" sensitive pussy.";
        } else {
            return "You spank " + target.name() + " on "+target.possessivePronoun()+" naked butt cheek.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s aims a slap at %s ass, but %s %s it.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.pronoun(),
                            target.action("dodge"));
        }
        if (modifier == Result.special) {
            String victim = target.hasBalls() ? "balls" : "clit";
            String hood = target.hasBalls() ? "manhood" : "womanhood";
            return String.format("%s bends %s over like a misbehaving child and spanks %s"
                            + " ass twice. The third spank aims lower and connects solidly with %s %s, "
                            + "injuring %s %s along with %s pride.", getSelf().subject(),
                            target.nameDirectObject(), target.possessivePronoun(),
                            target.possessivePronoun(), victim, target.possessivePronoun(),
                            hood, target.possessivePronoun());
        } else {
            return String.format("%s lands a stinging slap on %s bare ass.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
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
