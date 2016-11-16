package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.AssPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.BodyFetish;
import nightgames.status.Stsflag;

public class Anilingus extends Skill {
    private static final String worshipString = "Ass Worship";

    public Anilingus(Character self) {
        super("Lick Ass", self);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().has(Trait.shameless) || getSelf().get(Attribute.Seduction) >= 30;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canUse = c.getStance().isBeingFaceSatBy(c, getSelf(), target) && getSelf().canRespond()
                        || getSelf().canAct();
        return target.crotchAvailable() && target.body.has("ass") && c.getStance().oral(getSelf(), target) && canUse
                        && !c.getStance().anallyPenetrated(c, target);
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
        } else if (c.getStance().isBeingFaceSatBy(c, getSelf(), target)) {
            result = Result.reverse;
            m += Global.random(6);
            n = 10;
        } else if (!c.getStance().mobile(target) || target.roll(getSelf(), c, accuracy(c))) {
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
        writeOutput(c, m, result, target);
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
            return "You try to lick " + target.name() + "'s rosebud, but "+target.pronoun()+" pushes your head away.";
        } else if (modifier == Result.special) {
            return "You gently rim " + target.name() + "'s asshole with your tongue, sending shivers through "+target.possessivePronoun()+" body.";
        } else if (modifier == Result.reverse) {
            return "With " + target.nameOrPossessivePronoun()
                            + " ass pressing into your face, you helplessly give in and take an experimental lick at "+target.possessivePronoun()+" pucker.";
        } else if (modifier == Result.sub) {
            return "With a terrible need coursing through you, you lower your face between "
                            + target.nameOrPossessivePronoun()
                            + " rear cheeks and plunge your tongue repeatedly in and out of "+target.possessivePronoun()+" "
                            + target.body.getRandom("ass").describe(target) + ". "
                            + "You dimly realize that this is probably arousing you as much as " + target.getName()
                            + ", but worshipping "+target.possessivePronoun()+" sublime derriere seems much higher on your priorities than winning.";
        }
        return "You thrust your tongue into " + target.name() + "'s ass and lick it, making "+target.directObject()+" yelp in surprise.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s closes in on %s behind, but %s %s to push %s head away.", getSelf().name(), 
                            target.nameOrPossessivePronoun(), getSelf().pronoun(), 
                            target.action("manage"), target.possessivePronoun());
        } else if (modifier == Result.special) {
            return String.format("%s gently rims %s asshole with %s tongue, sending shivers through %s body.",
                            getSelf().name(), target.nameOrPossessivePronoun(), getSelf().possessivePronoun(),
                            target.possessivePronoun());
        } else if (modifier == Result.reverse) {
            return String.format("With %s ass pressing into %s face, %s helplessly gives in and starts licking %s ass.",
                            target.nameOrPossessivePronoun(), getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                            target.possessivePronoun());
        } else if (modifier == Result.sub) {
            return String.format("As if entranced, %s buries %s face inside %s asscheeks, licking %s crack and worshipping %s anus.",
                            getSelf().subject(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(), target.possessivePronoun(), target.possessivePronoun());
        }
        return String.format("%s licks %s tight asshole, both surprising and arousing %s.",
                        getSelf().name(), target.nameOrPossessivePronoun(), target.pronoun());
    }

    @Override
    public String describe(Combat c) {
        return "Perform anilingus on opponent";
    }

    @Override
    public String getLabel(Combat c) {
        Optional<BodyFetish> fetish = getSelf().body.getFetish("ass");
        boolean worship = c.getOpponent(getSelf()).has(Trait.objectOfWorship);
        boolean enthralled = getSelf().is(Stsflag.enthralled);
        return fetish.isPresent() || worship || enthralled ? worshipString : "Lick Ass";
    }
}
