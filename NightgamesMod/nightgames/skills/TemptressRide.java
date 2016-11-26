package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.FiredUp;

public class TemptressRide extends Thrust {

    public TemptressRide(Character self) {
        super("Improved Ride", self);
    }

    @Override
    public BodyPart getSelfOrgan(Combat c) {
        if (c.getStance().vaginallyPenetratedBy(c, getSelf(), c.getOpponent(getSelf()))) {
            return getSelf().body.getRandomPussy();
        }
        return null;
    }

    @Override
    public BodyPart getTargetOrgan(Combat c, Character target) {
        if (c.getStance().inserted(target)) {
            return target.body.getRandomInsertable();
        }
        return null;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress) && user.get(Attribute.Technique) >= 11;
    }

    @Override
    public String getLabel(Combat c) {
        return "Skillful Ride";
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (c.getStance().anallyPenetrated(c, getSelf())) {
            return super.resolve(c, target);
        }
        int targetDmg = 10 + Global.random(Math.max(10, getSelf().get(Attribute.Technique)));
        int selfDmg = (int) Math.max(1f, targetDmg / 3f);
        if (getSelf().has(Trait.experienced)) {
            selfDmg *= 0.67;
        }
        FiredUp status = (FiredUp) getSelf().status.stream().filter(s -> s instanceof FiredUp).findAny().orElse(null);
        int stack = status == null || !status.getPart().equals("pussy") ? 0 : status.getStack();

        if (getSelf().human()) {
            c.write(getSelf(), deal(c, stack, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, stack, Result.normal, target));
        }

        target.body.pleasure(getSelf(), getSelf().body.getRandomPussy(), target.body.getRandomCock(),
                        targetDmg + targetDmg * stack / 2, c, this);

        getSelf().body.pleasure(target, target.body.getRandomCock(), getSelf().body.getRandomPussy(), selfDmg, c, this);

        getSelf().add(c, new FiredUp(getSelf(), target, "pussy"));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TemptressRide(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        switch (damage) {
            case 0:
                return String.format(
                                "%s holding just the tip of %s %s inside of %s %s,"
                                                + " rubbing it against the soft, tight entrance. Slowly	moving"
                                                + " %s hips, %s %s driving %s crazy.",
                                getSelf().subjectAction("are", "is"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().possessivePronoun(),
                                getSelf().body.getRandomPussy().describe(getSelf()), getSelf().possessivePronoun(),
                                getSelf().pronoun(), getSelf().action("are", "is"), target.directObject());
            case 1:
                return String.format(
                                "%s down fully onto %s, squeezing %s tightly"
                                                + " with %s %s. The muscles are wound so tight that it's nearly"
                                                + " impossible to move at all, but %s %s down hard and eventually"
                                                + " all of %s %s is lodged firmly inside of %s.",
                                getSelf().subjectAction("slide"), target.subject(), target.directObject(),
                                getSelf().possessivePronoun(), getSelf().body.getRandomPussy().describe(getSelf()),
                                getSelf().pronoun(), getSelf().action("push", "pushes"), target.possessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().directObject());
            default:
                return String.format(
                                "%s up and down %s rock-hard %s while the velvet vise"
                                                + " of %s %s is undulating on %s shaft, sending ripples along it"
                                                + " as if milking it. Overcome with pleasure, %s entire body tenses up and"
                                                + " %s %s %s head back, trying hard not to cum instantly.",
                                getSelf().subjectAction("move"), target.nameOrPossessivePronoun(),
                                target.body.getRandomCock().describe(target), getSelf().possessivePronoun(),
                                getSelf().body.getRandomPussy().describe(getSelf()), target.possessivePronoun(),
                                target.possessivePronoun(), target.pronoun(), target.action("throw"),
                                target.possessivePronoun());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }

}
