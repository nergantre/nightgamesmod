package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.ReverseMount;

public class ReverseStraddle extends Skill {
    public ReverseStraddle(Character self) {
        super("Mount(Reverse)", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.petDisallowed);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && c.getStance().mobile(target) && c.getStance().prone(target)
                        && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new ReverseMount(getSelf(), target), getSelf(), true);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ReverseStraddle(user);
    }

    @Override
    public int speed() {
        return 6;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You straddle " + target.getName() + ", facing her feet.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s sits on %s chest, facing %s crotch.",
                        getSelf().subject(), target.nameOrPossessivePronoun(),
                        target.possessiveAdjective());
    }

    @Override
    public String describe(Combat c) {
        return "Straddle facing groin";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
