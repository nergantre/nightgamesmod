package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.ReverseMount;
import nightgames.stance.SixNine;
import nightgames.stance.Stance;
import nightgames.status.Enthralled;

public class Cunnilingus extends Skill {

    public Cunnilingus(Character self) {
        super("Lick Pussy", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canUse = c.getStance().enumerate() == Stance.facesitting && getSelf().canRespond()
                        || getSelf().canAct();
        return target.crotchAvailable() && target.hasPussy() && c.getStance().oral(getSelf()) && canUse
                        && !c.getStance().vaginallyPenetrated(target);
    }

    @Override
    public float priorityMod(Combat c) {
        return getSelf().has(Trait.silvertongue) ? 1 : 0;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        if (c.getStance().enumerate() == Stance.facesitting) {
            return 0;
        } else {
            return 5;
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result results = Result.normal;
        boolean facesitting = c.getStance().enumerate() == Stance.facesitting;
        int m = 10 + Global.random(8);
        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }
        int i = 0;
        if (!facesitting && c.getStance().mobile(target) && !target.roll(this, c, accuracy(c))) {
            results = Result.miss;
        } else {
            if (target.has(Trait.entrallingjuices) && Global.random(4) == 0 && !target.wary()) {
                i = -2;
            } else if (target.has(Trait.lacedjuices)) {
                i = -1;
                getSelf().tempt(c, target, 5);
            }
            if (facesitting) {
                results = Result.reverse;
            }
        }
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, i, results, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, i, results, target));
        }
        if (i == -2) {
            getSelf().add(c, new Enthralled(getSelf(), target, 3));
        }
        if (results != Result.miss) {
            if (results == Result.reverse) {
                target.buildMojo(c, 10);
            }
            if (ReverseMount.class.isInstance(c.getStance())) {
                c.setStance(new SixNine(getSelf(), target));
            }
            target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("pussy"), m, c, this);
        }
        return results != Result.miss;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 10;
    }

    @Override
    public Skill copy(Character user) {
        return new Cunnilingus(user);
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
            return "You try to eat out " + target.name() + ", but she pushes your head away.";
        }
        if (target.getArousal().get() < 10) {
            return "You run your tongue over " + target.name() + "'s dry vulva, lubricating it with your saliva.";
        }
        if (modifier == Result.special) {
            return "Your skilled tongue explores " + target.name()
                            + "'s pussy, finding and pleasuring her more sensitive areas. You frequently tease her clitoris until she "
                            + "can't suppress her pleasured moans."
                            + (damage == -1 ? " Under your skilled ministrations, her juices flow freely, and they unmistakably"
                                            + " have their effect on you."
                                            : "")
                            + (damage == -2 ? " You feel a strange pull on you mind,"
                                            + " somehow she has managed to enthrall you with her juices." : "");
        }
        if (modifier == Result.reverse) {
            return "Your resign yourself to lapping at " + target.nameOrPossessivePronoun()
                            + " pussy, as she dominates your face with her ass."
                            + (damage == -1 ? " Under your skilled ministrations, her juices flow freely, and they unmistakably"
                                            + " have their effect on you."
                                            : "")
                            + (damage == -2 ? " You feel a strange pull on you mind,"
                                            + " somehow she has managed to enthrall you with her juices." : "");
        }
        if (target.getArousal().percent() > 80) {
            return "You relentlessly lick and suck the lips of " + target.name()
                            + "'s pussy as she squirms in pleasure. You let up just for a second before kissing her"
                            + " swollen clit, eliciting a cute gasp."
                            + (damage == -1 ? " The highly aroused succubus' vulva is dripping with her "
                                            + "aphrodisiac juices and you consume generous amounts of them."
                                            : "")
                            + (damage == -2 ? " You feel a strange pull on you mind,"
                                            + " somehow she has managed to enthrall you with her juices." : "");
        }
        int r = Global.random(3);
        if (r == 0) {
            return "You gently lick " + target.name() + "'s pussy and sensitive clit."
                            + (damage == -1 ? " As you drink down her juices, they seem to flow "
                                            + "straight down to your crotch, lighting fires when they arrive."
                                            : "")
                            + (damage == -2 ? " You feel a strange pull on you mind,"
                                            + " somehow she has managed to enthrall you with her juices." : "");
        }
        if (r == 1) {
            return "You thrust your tongue into " + target.name() + "'s hot vagina and lick the walls of her pussy."
                            + (damage == -1 ? " Your tongue tingles with her juices, clouding your mind with lust."
                                            : "")
                            + (damage == -2 ? " You feel a strange pull on you mind,"
                                            + " somehow she has managed to enthrall you with her juices." : "");
        }
        return "You locate and capture " + target.name() + "'s clit between your lips and attack it with your tongue."
                        + (damage == -1 ? " Her juices taste wonderful and you cannot help but desire more." : "")
                        + (damage == -2 ? " You feel a strange pull on you mind,"
                                        + " somehow she has managed to enthrall you with her juices." : "");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        if (modifier == Result.miss) {
            return getSelf().name()
                            + " tries to tease your cunt with her mouth, but you push her face away from your box.";
        } else if (modifier == Result.special) {
            return getSelf().nameOrPossessivePronoun()
                            + " skilled tongue explores your pussy, finding and pleasuring your more sensitive areas. "
                            + "She repeatedly attacks your clitoris until you can't suppress your pleasured moans."
                            + (damage == -1 ? " Your aphrodisiac juices manage to arouse her as much as she aroused you."
                                            : "")
                            + (damage == -2 ? " Your tainted juices quickly reduce her into a willing thrall." : "");
        } else if (modifier == Result.reverse) {
            return getSelf().name() + " obediently laps at your pussy as you sit on her face."
                            + (damage == -1 ? " Your aphrodisiac juices manage to arouse her as much as she aroused you."
                                            : "")
                            + (damage == -2 ? " Your tainted juices quickly reduce her into a willing thrall." : "");
        }
        return getSelf().name() + " locates and captures your clit between her lips and attacks it with her tongue."
                        + (damage == -1 ? " Your aphrodisiac juices manage to arouse her as much as she aroused you."
                                        : "")
                        + (damage == -2 ? " Your tainted juices quickly reduce her into a willing thrall." : "");
    }

    @Override
    public String describe(Combat c) {
        return "Perfom cunnilingus on opponent";
    }
}
