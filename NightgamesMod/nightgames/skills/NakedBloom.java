package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;

public class NakedBloom extends Skill {

    public NakedBloom(Character self) {
        super("Naked Bloom", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !target.reallyNude();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public String describe(Combat c) {
        return "Cast a spell to transform your opponent's clothes into flower petals: 20 Mojo";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
            c.write(target, target.nakedLiner(c, target));
        } else if (c.shouldPrintReceive(target, c)) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        target.nudify();
        target.emote(Emotion.nervous, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new NakedBloom(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You chant a short spell and turn " + target.getName()
                        + "'s clothes into a burst of flowers. The cloud of flower petals flutters to the ground, exposing her nude body.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s mumbles a spell and %s suddenly surrounded by an eruption of flower petals. "
                        + "As the petals settle, %s %s %s %s been stripped completely "
                        + "naked.", getSelf().subject(), target.subjectAction("are", "is"),
                        target.pronoun(), target.action("realize"), target.pronoun(),
                        target.action("have", "has"));
    }

}
