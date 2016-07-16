package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Kneeling;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class FootWorship extends Skill {
    public FootWorship(Character self) {
        super("Foot Worship", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        Optional<BodyFetish> fetish = getSelf().body.getFetish("feet");
        return fetish.isPresent() && fetish.get().magnitude >= .5;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.body.has("feet") && c.getStance().reachBottom(getSelf()) && getSelf().canAct()
                        && !c.getStance().behind(getSelf()) && !c.getStance().behind(target)
                        && target.outfit.hasNoShoes();
    }

    @Override
    public int accuracy(Combat c) {
        return 150;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result result = Result.normal;
        int m = 0;
        int n = 0;
        m = 8 + Global.random(6);
        n = 20;
        BodyPart mouth = getSelf().body.getRandom("mouth");
        BodyPart feet = target.body.getRandom("feet");
        if (getSelf().human()) {
            c.write(getSelf(), Global.format(deal(c, 0, Result.normal, target), getSelf(), target));
        } else {
            c.write(getSelf(), Global.format(receive(c, 0, Result.normal, target), getSelf(), target));
        }
        if (m > 0) {
            target.body.pleasure(getSelf(), mouth, feet, m, c);
            if (mouth.isErogenous()) {
                getSelf().body.pleasure(getSelf(), feet, mouth, m, c);
            }
        }
        if (n > 0) {
            target.buildMojo(c, n);
        }
        if (c.getStance().en == Stance.neutral) {
            c.setStance(new Kneeling(target, getSelf()));
        }
        c.getCombatantData(getSelf()).toggleFlagOn("footworshipped", true);
        return result != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new FootWorship(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (!c.getCombatantData(getSelf()).getBooleanFlag("footworshipped")) {
            return "You throw yourself at " + target.nameOrPossessivePronoun()
                            + " dainty feet and start sucking on her toes. " + target.subject()
                            + " seems surprised at first, "
                            + "but then grins and shoves her toes further in to your mouth, eliciting a moan from you.";
        } else {
            return "You can't seem to bring yourself to stop worshipping her feet as your tongue makes its way down to {other:name-possessive} soles. {other:SUBJECT} presses her feet against your face and you feel more addicted to her feet.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (!c.getCombatantData(getSelf()).getBooleanFlag("footworshipped")) {
            return getSelf().name() + " throws herself at your feet. She worshipfully grasps your feet "
                            + "and start licking between your toes, all while her face displays a mask of ecstasy.";
        }
        return "{self:SUBJECT} can't seem to get enough of your feet as {self:pronoun} continues to lick along the bottom of your soles, {self:possessive} face further lost in {self:possessive} servitude as {self:pronoun} is careful not to miss a spot.";
    }

    @Override
    public String describe(Combat c) {
        return "Worship opponent's feet: builds mojo for opponent";
    }
}
