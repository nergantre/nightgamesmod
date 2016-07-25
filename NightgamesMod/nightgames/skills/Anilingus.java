package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.AssPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Stsflag;

public class Anilingus extends Skill {
    private static final String worshipString = "Ass Worship";

    public Anilingus(Character self) {
        super("Lick Ass", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().has(Trait.shameless) || getSelf().get(Attribute.Seduction) >= 30;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canUse = c.getStance().enumerate() == Stance.facesitting && getSelf().canRespond()
                        || getSelf().canAct();
        return target.crotchAvailable() && target.body.has("ass") && c.getStance().oral(getSelf()) && canUse
                        && !c.getStance().anallyPenetrated(target);
    }

    @Override
    public float priorityMod(Combat c) {
        return getSelf().has(Trait.silvertongue) ? 1 : 0;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        AssPart targetAss = (AssPart) target.body.getRandom("ass");
        Result result = Result.normal;
        int m = 10;
        int n = 0;
        int selfm = 0;
        if (getLabel(c).equals(worshipString)) {
            result = Result.sub;
            m += 4 + Global.random(6);
            n = 20;
            selfm = 20;
        } else if (c.getStance().enumerate() == Stance.facesitting) {
            result = Result.reverse;
            m += Global.random(6);
            n = 10;
        } else if (!c.getStance().mobile(target) || target.roll(this, c, accuracy(c))) {
            m += Global.random(6);
            if (getSelf().has(Trait.silvertongue)) {
                m += 4;
                result = Result.special;
            }
        } else {
            m = 0;
            n = 0;
            result = Result.miss;
        }
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, m, result, target));
        } else {
            c.write(getSelf(), receive(c, m, result, target));
        }
        if (m > 0) {
            target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), targetAss, m, c, this);
        }
        if (n > 0) {
            target.buildMojo(c, n);
        }
        if (selfm > 0) {
            getSelf().tempt(c, target, target.body.getRandom("ass"), selfm);
        }
        return result != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new Anilingus(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public int accuracy(Combat c) {
        return 75;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to lick " + target.name() + "'s rosebud, but she pushes your head away.";
        } else if (modifier == Result.special) {
            return "You gently rim " + target.name() + "'s asshole with your tongue, sending shivers through her body.";
        } else if (modifier == Result.reverse) {
            return "With " + target.nameOrPossessivePronoun()
                            + " ass pressing into your face, you helplessly give in and take an experimental lick at her pucker.";
        } else if (modifier == Result.sub) {
            return "With a terrible need coursing through you, you lower your face between "
                            + target.nameOrPossessivePronoun()
                            + " rear cheeks and plunge your tongue repeatedly in and out of her "
                            + target.body.getRandom("ass").describe(target) + ". "
                            + "You dimly realize that this is probably arousing you as much as " + target.getName()
                            + ", but worshipping her sublime derriere seems much higher on your priorities than winning.";
        }
        return "You thrust your tongue into " + target.name() + "'s ass and lick it, making her yelp in surprise.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name() + " closes in on your behind, but you manage to push her head away.";
        } else if (modifier == Result.special) {
            return getSelf().name() + " gently rims your asshole with her tongue, sending shivers through your body.";
        } else if (modifier == Result.reverse) {
            return "With your ass pressing into " + getSelf().nameOrPossessivePronoun()
                            + " face, she helplessly gives in and starts licking your ass.";
        } else if (modifier == Result.sub) {
            return "As if entranced, " + getSelf().subject()
                            + " buries her face inside your ass cheeks, licking your crack, and worshipping your anus.";
        }
        return getSelf().name() + " licks your tight asshole, both surprising and arousing you.";
    }

    @Override
    public String describe(Combat c) {
        return "Perform anilingus on opponent";
    }

    @Override
    public String getLabel(Combat c) {
        Optional<BodyFetish> fetish = getSelf().body.getFetish("ass");
        boolean worship = c.getOther(getSelf()).has(Trait.objectOfWorship);
        boolean enthralled = getSelf().is(Stsflag.enthralled);
        return fetish.isPresent() || worship || enthralled ? worshipString : "Lick Ass";
    }
}
