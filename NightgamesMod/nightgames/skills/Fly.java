package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.FlyingCarry;
import nightgames.status.Falling;

public class Fly extends Fuck {
    public Fly(Character self) {
        super("Fly", self, 5);
    }

    public Fly(String name, Character self) {
        super(name, self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.body.get("wings").size() > 0 && user.get(Attribute.Power) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && !target.wary() && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && !c.getStance().prone(getSelf()) && c.getStance().facing(getSelf(), target)
                        && getSelf().getStamina().get() >= 15;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 50;
    }

    @Override
    public String describe(Combat c) {
        return "Take off and fuck your opponent's pussy in the air.";
    }

    @Override
    public int accuracy(Combat c) {
        return 65;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);

        Result result = target.roll(getSelf(), c, accuracy(c)) ? Result.normal : Result.miss;
        if (getSelf().human()) {
            c.write(getSelf(), premessage + deal(c, premessage.length(), result, target));
        } else if (c.shouldPrintReceive(target, c)) {
            c.write(getSelf(), premessage + receive(c, premessage.length(), result, getSelf()));
        }
        if (result == Result.normal) {
            getSelf().emote(Emotion.dominant, 50);
            getSelf().emote(Emotion.horny, 30);
            target.emote(Emotion.desperate, 50);
            target.emote(Emotion.nervous, 75);
            int m = 5 + Global.random(5);
            int otherm = m;
            if (getSelf().has(Trait.insertion)) {
                otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), m, c, this);
            getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), otherm, c, this);
            c.setStance(new FlyingCarry(getSelf(), target), getSelf(), getSelf().canMakeOwnDecision());
        } else {
            getSelf().add(c, new Falling(getSelf()));
        }
        return result != Result.miss;
    }

    @Override
    public Skill copy(Character target) {
        return new Fly(target);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int amount, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return (amount == 0 ? "You " : "") + "grab " + target.name() + " tightly and try to take off. However "
                            + target.pronoun()
                            + " has other ideas. She knees your crotch as you approach and sends you sprawling to the ground.";
        } else {
            return (amount == 0 ? "You " : "") + "grab " + target.name() + " tightly and take off, "
                            + (target.hasDick() && getSelf().hasPussy()
                                            ? "inserting her dick into your hungry "
                                                            + getSelf().body.getRandomPussy().describe(getSelf()) + "."
                                            : " holding her helpless in the air and thrusting deep into her wet "
                                                            + target.body.getRandomPussy().describe(getSelf()) + ".");
        }
    }

    @Override
    public String receive(Combat c, int amount, Result modifier, Character target) {
        String subject = amount == 0 ? target.subject() + " " : "";
        if (modifier == Result.miss) {
            return String.format("%slunges for %s with a hungry look in %s eyes. However %s other "
                            + "ideas. %s %s %s as %s approaches and %s %s sprawling to the floor.",
                            subject, target.nameDirectObject(), getSelf().possessivePronoun(),
                            target.subjectAction("have", "has"), target.pronoun(),
                            target.action("trip"), getSelf().nameDirectObject(),
                            getSelf().pronoun(), target.pronoun(), target.action("send"));
        } else {
            return String.format("%sleaps at %s, embracing %s tightly"
                            + ". %s then flaps %s %s hard and before %s it"
                            + " %s twenty feet in the sky held up by %s arms and legs."
                            + " Somehow, %s dick ended up inside of %s in the process and"
                            + " the rhythmic movements of %s flying arouse %s to no end.",
                            subject, target.nameDirectObject(), target.directObject(),
                            getSelf().subject(), getSelf().possessivePronoun(),
                            getSelf().body.getRandomWings().describe(getSelf()),
                            target.pronoun(), target.subjectAction("are", "is"),
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), getSelf().possessivePronoun(),
                            target.directObject());
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
