package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;
import nightgames.status.Tied;

public class TortoiseWrap extends Skill {

    public TortoiseWrap(Character self) {
        super("Tortoise Wrap", self);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Fetish) >= 24;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().reachTop(getSelf()) && !c.getStance().reachTop(target)
                        && getSelf().has(Item.Rope) && c.getStance().dom(getSelf()) && !target.is(Stsflag.tied);
    }

    @Override
    public String describe(Combat c) {
        return "User your bondage skills to wrap your opponent to increase her sensitivity";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Rope, 1);
        writeOutput(c, Result.normal, target);
        target.add(c, new Tied(target));
        target.add(c, new Hypersensitive(target));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TortoiseWrap(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You skillfully tie a rope around %s's torso "
                                        + "in a traditional bondage wrap. %s moans softly as the "
                                        + "rope digs into %s supple skin.",
                        target.getName(), nightgames.global.Global.capitalizeFirstLetter(target.pronoun()),
                        target.possessiveAdjective());
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s ties %s up with a complex series of knots. "
                        + "Surprisingly, instead of completely incapacitating %s, "
                        + "%s wraps %s in a way that only "
                        + "slightly hinders %s movement. However, the discomfort of "
                        + "the rope wrapping around %s seems to make %s sense of touch more pronounced.",
                        getSelf().getName(), target.nameDirectObject(), target.directObject(),
                        getSelf().pronoun(), target.directObject(), target.possessiveAdjective(),
                        target.pronoun(), target.possessiveAdjective());
    }

}
