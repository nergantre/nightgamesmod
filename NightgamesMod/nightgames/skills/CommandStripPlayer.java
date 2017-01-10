package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;

public class CommandStripPlayer extends PlayerCommand {

    public CommandStripPlayer(Character self) {
        super("Force Strip Player", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !getSelf().mostlyNude();
    }

    @Override
    public String describe(Combat c) {
        return "Make your thrall undress you.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().undress(c);
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CommandStripPlayer(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        return "With an elated gleam in her eyes, " + target.getName()
                        + " moves her hands with nigh-inhuman dexterity, stripping all"
                        + " of your clothes in just a second.";
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        return "<<This should not be displayed, please inform The" + " Silver Bard: CommandStripPlayer-receive>>";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
