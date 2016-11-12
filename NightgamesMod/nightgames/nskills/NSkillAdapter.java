package nightgames.nskills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class NSkillAdapter extends Skill {
    SkillInterface skill;

    public NSkillAdapter(Character user, SkillInterface skill) {
        super(skill.getName(), user);
        this.skill = skill;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return skill.getHighestPriorityUsableResult(c, user, target).isPresent();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return skill.getHighestPriorityUsableResult(c, getSelf(), target).isPresent();
    }

    @Override
    public String describe(Combat c) {
        Optional<SkillResult> maybeResult = skill.getHighestPriorityUsableResult(c, getSelf(), c.getOpponent(getSelf()));
        return maybeResult.isPresent() ? maybeResult.get().getDescription() : "";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return skill.resolve(c, getSelf(), target);
    }

    @Override
    public Skill copy(Character user) {
        return new NSkillAdapter(user, skill);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "";
    }
}
