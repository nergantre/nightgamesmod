package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Stsflag;

public class Obey extends Skill {

    public Obey(Character self) {
        super("Obey", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().is(Stsflag.enthralled) && !getSelf().is(Stsflag.stunned);
    }

    @Override
    public String describe(Combat c) {
        return "Obey the succubus' every command";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            String controller = target.useFemalePronouns() ? "mistress'" : "master's";
            c.write(getSelf(), "You patiently await your "+controller+" command.");
        } else if (c.shouldPrintReceive(target)) {
            c.write(getSelf(), getSelf().name() + " stares ahead blankly, waiting for "+getSelf().possessivePronoun()+" orders.");
        }
        return true;
    }

    @Override
    public Skill copy(Character paramCharacter) {
        return new Obey(paramCharacter);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int paramInt, Result paramResult, Character paramCharacter) {
        return "";
    }

    @Override
    public String receive(Combat c, int paramInt, Result paramResult, Character paramCharacter) {
        return "";
    }

}
