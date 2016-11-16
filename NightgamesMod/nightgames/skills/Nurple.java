package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class Nurple extends Skill {

    public Nurple(Character self) {
        super("Twist Nipples", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.mean);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 13;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.breastsAvailable() && c.getStance().reachTop(getSelf()) && getSelf().canAct();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 10;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        double m = Global.random(4, 7);
        DamageType damageType = DamageType.physical;
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().has(Item.ShockGlove) && getSelf().has(Item.Battery, 2)) {
                writeOutput(c, Result.special, target);
                getSelf().consume(Item.Battery, 2);
                damageType = DamageType.gadgets;
                m += Global.random(16, 30);
            } else {
                writeOutput(c, Result.normal, target);
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(damageType, target, m));
            target.loseMojo(c, (int) getSelf().modifyDamage(DamageType.technique, target, 5));
            target.emote(Emotion.angry, 15);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Nurple(user);
    }

    @Override
    public int speed() {
        return 7;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().has(Item.ShockGlove)) {
            return "Shock breasts";
        } else {
            return getName(c);
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You grope at " + target.name() + "'s breasts, but miss.";
        } else if (modifier == Result.special) {
            return "You grab " + target.name() + "'s boob with your shock-gloved hand, painfully shocking her.";
        } else {
            return "You pinch and twist " + target.name() + "'s nipples, causing her to yelp in surprise.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to grab %s nipples, but misses.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        } else if (modifier == Result.special) {
            return String.format("%s touches %s nipple with %s glove and a jolt of electricity hits %s.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), target.directObject());
        } else {
            return String.format("%s twists %s sensitive nipples, giving %s a jolt of pain.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Twist opponent's nipples painfully";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
