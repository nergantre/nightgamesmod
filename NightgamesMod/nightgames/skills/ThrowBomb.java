package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Stance;
import nightgames.status.PheromoneBombed;
import nightgames.status.Stsflag;

public class ThrowBomb extends Skill {


    private static final String DOM_SEX =
                    "{other:SUBJECT}, having pulled a small sphere from somewhere, leans down and places"
                                    + " said sphere on your chest. It gives of a soft beep. You try to roll it"
                                    + " of you by twisting you torso, but the device sticks to you as if it were"
                                    + " glued in place.";
    private static final String SUB_SEX = "{other:SUBJECT} jabs {other:possessive} arm at your chest,"
                    + " almost as if {other:pronoun} were punching you in the sternum. In fact, {other:pronoun}"
                    + " was holding a small, spherical device, which now appears to be stuck to your chest."
                    + " The thing is beeping softly, and by the confident little smirk {other:subject} is giving"
                    + " you from below, it might be best to remove it. Soon.";

    public ThrowBomb(Character self) {
        super("Throw Bomb", self);
    }
    
    @Override
    public String getLabel(Combat c) {
        return c.getStance().en == Stance.neutral ? "Throw Bomb" : "Stick Bomb";
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.bomber);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().has(Item.Battery, 3) && target.outfit.slotOpen(ClothingSlot.top)
                        && !target.is(Stsflag.bombed)
                        && c.getStance().front(getSelf())
                        || (c.getStance().behind(getSelf()) && c.getStance().dom(getSelf()));
    }

    @Override
    public String describe(Combat c) {
        return "Try to stick a Pheromone Bomb to your opponent. (3 Batteries)";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Battery, 3);
        Stance s = c.getStance().en;
        if ((s == Stance.behind && c.getStance()
                                    .dom(getSelf()))
                        || s == Stance.cowgirl || s == Stance.missionary || s == Stance.mount || c.getStance()
                                                                                                  .dom(getSelf())
                        || target.roll(getSelf(), c, 75)) {
            writeOutput(c, Result.normal, target);
            target.add(c, new PheromoneBombed(target));
        } else {
            writeOutput(c, Result.miss, target);
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new ThrowBomb(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return Global.format("You try to get a Pheromone Bomb on {other:subject}, but "
                            + "{other:pronoun} slaps it out of your hand.", getSelf(), target);
        }
        return Global.format(
                        "You take out a Pheromone Bomb, arm it, and stick it to" + " {other:name-possessive} chest.",
                        getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        switch (c.getStance().en) {
            case behind:
            case pin:
            case behindfootjob:
                return Global.format(
                                "{self:SUBJECT-ACTION} reaches around you from behind and holds a spherical device"
                                                + " to your chest. It beeps softly when it makes contact, and"
                                                + " when {self:pronoun} pulls {self:possessive} hand back it sticks to you,"
                                                + " still beeping intermittently.",
                                getSelf(), target);
            case cowgirl:
            case missionary:
            case mount:
                return Global.format(c.getStance()
                                      .dom(getSelf()) ? DOM_SEX : SUB_SEX,
                                getSelf(), target);
            case neutral:
                if (modifier == Result.miss) {
                    return Global.format(
                                    "{self:SUBJECT} throws something at you. It looks somewhat like a baseball at first,"
                                                    + " but as you step to the side and see it fly past you note that it's actually"
                                                    + " a metal sphere; a machine of some sort. It's probably a good thing it sailed past.",
                                    getSelf(), target);
                }
                return Global.format(
                                "With a near-perfect baseball pitch, {self:subject} throws some kind of object at you."
                                                + " It hits you square in the chest, beeping as it impacts. It seems to be a"
                                                + " small device, though you can't tell what it's supposed to do. You do know"
                                                + " that it's sticking to you quite insistently, and you might not want to find"
                                                + " out what else it can do.",
                                getSelf(), target);
            default:
                if (modifier == Result.miss) {
                    return Global.format(
                                    "{self:SUBJECT} brandishes a small spherical device, but you slap it out of"
                                                    + " {self:possessive} hands before {self:pronoun} can do anything with it.",
                                    getSelf(), target);
                }
                return Global.format(
                                "{self:SUBJECT} takes out a small, round device and sticks it onto your chest."
                                                + " Knowing {self:pronoun}, it would probably be best to get it off quickly.",
                                getSelf(), target);
        }
    }

}
