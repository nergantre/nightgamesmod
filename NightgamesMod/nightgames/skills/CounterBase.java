package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.CounterStatus;
import nightgames.status.Stsflag;

public abstract class CounterBase extends Skill {
    private String description;
    private int duration;

    public CounterBase(String name, Character self, int cooldown, String description) {
        this(name, self, cooldown, description, 0);
    }

    public CounterBase(String name, Character self, int cooldown, String description, int duration) {
        super(name, self, cooldown);
        addTag(SkillTag.counter);
        this.description = description;
        this.duration = duration;
    }

    public String getBlockedString(Combat c, Character target) {
        return Global.format(
                        "{self:SUBJECT-ACTION:block|blocks} {other:name-possessive} attack and {self:action:move|moves} in for a counter. "
                                        + "However, {other:subject-action:were|was} wary of {self:direct-object} and {other:action:jump|jumps} back before {self:subject} can catch {other:direct-object}.",
                        getSelf(), target);
    }

    @Override
    public final boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.setup, target));
        } else if (!target.is(Stsflag.blinded)) {
            c.write(getSelf(), receive(c, 0, Result.setup, target));
        } else {
            printBlinded(c);
        }
        getSelf().add(c, new CounterStatus(getSelf(), this, description, duration));
        return true;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 0;
    }

    public abstract void resolveCounter(Combat c, Character target);

    @Override
    public int speed() {
        return 20;
    }
}
