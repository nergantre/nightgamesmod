package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class SlimeOil extends SimpleEnemySkill {
    public SlimeOil(Character self) {
        super("Slime Oil", self, 10);
        addTag(SkillTag.buff);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !target.is(Stsflag.oiled);
    }

    @Override
    public int accuracy(Combat c) {
        return 80;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            c.write(getSelf(), Global.format("{self:SUBJECT} forms into a shape that's vaguely human and clearly female. "
                                        + "Somehow it manages to look cute and innocent while still being an animated blob of slime. "
                                        + "The slime suddenly pounces on {other:name-do} and wraps itself around {other:direct-object}. "
                                        + "It doesn't seem to be attacking {other:direct-object} as much as giving you a hug, "
                                        + "but it leaves {other:direct-object} covered in slimy residue", getSelf(), target));
            target.add(new Oiled(target));
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} launches itself towards {other:name-do}, but {other:SUBJECT-ACTION:sidestep|sidesteps} it handily.",
                            getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SlimeOil(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }
}
