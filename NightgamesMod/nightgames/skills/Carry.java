package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Standing;
import nightgames.status.Falling;

public class Carry extends Fuck {

    public Carry(String name, Character self) {
        super(name, self, 5);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.pleasureSelf);
        addTag(SkillTag.fucking);
        addTag(SkillTag.positioning);
    }

    public Carry(Character self) {
        super("Carry", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 25 && !user.has(Trait.petite);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && !target.wary() && getTargetOrgan(target).isReady(target) && getSelf().canAct()
                        && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && c.getStance().facing(getSelf(), target) && getSelf().getStamina().get() >= 15;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 40;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().human()) {
                c.write(getSelf(), Global.capitalizeFirstLetter(
                                premessage + deal(c, premessage.length(), Result.normal, target)));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.normal, getSelf()));
            }
            int m = 5 + Global.random(5);
            int otherm = m;
            if (getSelf().has(Trait.insertion)) {
                otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), m, c, this);
            getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), otherm, c, this);
            c.setStance(new Standing(getSelf(), target), getSelf(), getSelf().canMakeOwnDecision());
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), Global
                                .capitalizeFirstLetter(premessage + deal(c, premessage.length(), Result.miss, target)));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.miss, target));
            }
            getSelf().add(c, new Falling(getSelf()));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Carry(user);
    }

    @Override
    public int accuracy(Combat c) {
        return 60;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "you pick up " + target.name() + ", but she flips out of your arms and manages to trip you.";
        } else {
            return "you scoop up " + target.name()
                            + ", lifting her into the air and simultaneously thrusting your dick into her hot depths. She lets out a noise that's "
                            + "equal parts surprise and delight as you bounce her on your pole.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return Global.format(
                            (damage > 0 ? "" : "{self:subject} ")
                                            + "picks {other:subject} up, but {other:pronoun-action:manage|manages} out of"
                                            + " {self:posessive} grip before {self:pronoun} can do anything. Moreover, "
                                            + "{other:pronoun-action:scramble|scrambles} to trip {self:direct-object} "
                                            + "while she's distracted.",
                            getSelf(), target);
        } else {
            return Global.format(
                            (damage > 0 ? "" : "{self:subject} ")
                                            + "scoops {other:subject} up in {self:possessive} powerful arms and simultaneously thrusts"
                                            + " {self:posessive} {self:body-part:cock} into {other:possessive} {other:body-part:pussy}.",
                            getSelf(), target);
        }
    }

    @Override
    public String describe(Combat c) {
        return "Picks up opponent and penetrates her: Mojo 10.";
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
