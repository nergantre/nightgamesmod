package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Behind;

public class Substitute extends Skill {

    public Substitute(Character self) {
        super("Substitute", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 21;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getStance()
                 .mobile(getSelf())
                        && c.getStance()
                            .sub(getSelf())
                        && getSelf().canAct();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Use a decoy to slip behind your opponent: 10 Mojo.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), String.format("By the time %s realizes %s's pinning a dummy, you're already behind %s.",
                            target.name(), target.pronoun(), target.directObject()));
        } else {
            c.write(getSelf(),
                            String.format("%s a good hold of %s body, and %s is surprisingly pliable..."
                                            + " %s wrestling a blow-up doll! The real %s is standing behind %s! How- How"
                                            + " did %s make the switch?!", target.subjectAction("take"),
                                            getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                                            target.subjectAction("are", "is"), getSelf().name, target.directObject(),
                                            getSelf().pronoun()));
        }
        getSelf().emote(Emotion.dominant, 10);
        target.emote(Emotion.nervous, 10);
        c.setStance(new Behind(getSelf(), target), getSelf(), true);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Substitute(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
