package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.pet.PetCharacter;

public class CommandDismiss extends PlayerCommand {

    public CommandDismiss(Character self) {
        super("Force Dismiss", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !c.getPetsFor(target).isEmpty();
    }

    @Override
    public String describe(Combat c) {
        return "Have your thrall dismiss their pet.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(), deal(c, 0, Result.normal, target));
        c.getPetsFor(target).stream().map(PetCharacter::getSelf).forEach(pet -> c.removePet(pet.getSelf()));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CommandDismiss(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        return "You think you briefly see a pang of regret in " + target.name()
                        + "'s eyes, but she quickly dismisses her summons.";
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        return "<<This should not be displayed, please inform The" + " Silver Bard: CommandDismiss-receive>>";
    }

}
