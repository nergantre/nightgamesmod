package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;

public class Purr extends Skill {

    public Purr(Character self) {
        super("Purr", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Animism) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !(target.is(Stsflag.wary) || target.is(Stsflag.charmed)) && getSelf().canAct()
                        && c.getStance().mobile(getSelf()) && getSelf().getArousal().percent() >= 20;
    }

    @Override
    public String describe(Combat c) {
        return "Purr cutely to try to charm your opponent";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (Global.random(target.getLevel()) <= getSelf().get(Attribute.Animism) * getSelf().getArousal().percent()
                        / 100 && !target.wary()) {
            int damage = getSelf().getArousal().getReal() / 10;
            if (damage < 10) {
                damage = 0;
            }
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, damage, Result.normal, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, damage, Result.normal, target));
            }
            target.tempt(c, getSelf(), damage);
            target.add(c, new Charmed(target));
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Purr(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You let out a soft purr and give " + target.name()
                            + " your best puppy dog eyes. She smiles, but then aims a quick punch at your groin, which you barely avoid. "
                            + "Maybe you shouldn't have mixed your animal metaphors.";
        } else {
            String message = "You give " + target.name()
                            + " an affectionate purr and your most disarming smile. Her battle aura melts away and she pats your head, completely taken with your "
                            + "endearing behavior.";
            if (damage > 0) {
                message += "\nSome of your apparent arousal seems to have affected her, her breath seems shallower than before.";
            }
            return message;
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name()
                            + " slumps submissively and purrs. It's cute, but she's not going to get the better of you.";
        } else {
            String message = getSelf().name()
                            + " purrs cutely, and looks up at you with sad eyes. Oh God, she's so adorable! It'd be mean to beat her too quickly. Maybe you should let her get some "
                            + "attacks in while you enjoy watching her earnest efforts.";
            if (damage > 0) {
                message += "\nYou're not sure if this was intentional, but her flushed face and ragged breathing makes the act a lot more erotic than you would expect. You try to contain your need to fuck the little kitty in heat.";
            }
            return message;
        }
    }
}
