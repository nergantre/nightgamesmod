package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.AnalCowgirl;
import nightgames.status.BodyFetish;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class ReverseAssFuck extends Fuck {
    public ReverseAssFuck(Character self) {
        super("Anal Ride", self, 0);
        addTag(SkillTag.anal);
    }

    @Override
    public float priorityMod(Combat c) {
        return ((getSelf().getMood() == Emotion.dominant ? 1.0f : 0)
                        + (getSelf().has(Trait.autonomousAss) ? 4.0f : 0) 
                        + (getSelf().has(Trait.oiledass) ? 2.0f : 0)
                        + (getSelf().has(Trait.drainingass) ? 3.f : 0)
                        + (getSelf().has(Trait.bewitchingbottom) ? 3.f : 0))
                        * (getSelf().has(Trait.powerfulcheeks) ? 2.f : 1.f);
    }

    @Override
    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomCock();
    }

    @Override
    public BodyPart getSelfOrgan() {
        return getSelf().body.getRandom("ass");
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && c.getStance().mobile(getSelf()) && c.getStance().prone(target)
                        && !c.getStance().mobile(target) && getSelf().canAct() && getTargetOrgan(target).isReady(target)
                        && (getSelfOrgan().isReady(getSelf()) || getSelf().has(Item.Lubricant)
                                        || getSelf().getArousal().percent() > 50 || getSelf().has(Trait.alwaysready));
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);
        if (!getSelf().hasStatus(Stsflag.oiled) && getSelf().getArousal().percent() > 50
                        || getSelf().has(Trait.alwaysready)) {
            String fluids = getSelf().hasDick() ? "copious pre-cum" : "own juices";
            if (premessage.isEmpty()) {
                premessage = "{self:subject-action:lube|lubes}";
            } else {
                premessage += "{self:action:lube|lubes}";
            }
            premessage += " up {self:possessive} ass with {self:possessive} " + fluids + ".";
            getSelf().add(c, new Oiled(getSelf()));
        } else if (!getSelf().hasStatus(Stsflag.oiled) && getSelf().has(Item.Lubricant)) {
            if (premessage.isEmpty()) {
                premessage = "{self:subject-action:lube|lubes}";
            } else {
                premessage += "{self:action:lube|lubes}";
            }
            premessage += " up {self:possessive} ass.";
            getSelf().add(c, new Oiled(getSelf()));
            getSelf().consume(Item.Lubricant, 1);
        }
        c.write(getSelf(), Global.format(premessage, getSelf(), target));

        int m = 5 + Global.random(5);
        writeOutput(c, Result.normal, target);

        int otherm = m;
        if (getSelf().has(Trait.insertion)) {
            otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
        }
        target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), m, c, this);
        getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), otherm, c, this);
        c.setStance(new AnalCowgirl(getSelf(), target), getSelf(), getSelf().canMakeOwnDecision());
        getSelf().emote(Emotion.dominant, 30);
        if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish) || getSelf().has(Trait.bewitchingbottom)) {
            target.add(c, new BodyFetish(target, getSelf(), "ass", .25));
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 15;
    }

    @Override
    public Skill copy(Character user) {
        return new ReverseAssFuck(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You make sure your %s is sufficiently lubricated and you push %s %s into your greedy hole.",
                        getSelfOrgan().describe(getSelf()), target.nameOrPossessivePronoun(),
                        getTargetOrgan(target).describe(target));
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s makes sure %s %s is sufficiently lubricated and pushes %s %s into %s greedy hole.",
                        getSelf().name(), getSelf().possessivePronoun(), getSelfOrgan().describe(getSelf()), 
                        target.nameOrPossessivePronoun(),
                        getTargetOrgan(target).describe(target), getSelf().possessivePronoun());
    }

    @Override
    public String describe(Combat c) {
        return "Fuck your opponent with your ass.";
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
