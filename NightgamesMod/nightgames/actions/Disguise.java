package nightgames.actions;

import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.status.Stsflag;
import nightgames.utilities.DisguiseHelper;

public class Disguise extends Action {
    private static final long serialVersionUID = 2089054062272510717L;

    public Disguise() {
        super("Disguise");
    }

    @Override
    public boolean usable(Character user) {
        return !user.bound() && user.has(Trait.Imposter) && !user.is(Stsflag.disguised) && getRandomNPC(user) != null;
    }

    private NPC getRandomNPC(Character user) {
        NPC target = (NPC) Global.pickRandom(Global.getParticipants()
                        .stream().filter(other -> !other.human() 
                                        && user != other 
                                        && !other.has(Trait.cursed)
                                        && !Global.checkCharacterDisabledFlag(other))
                        .collect(Collectors.toList())).orElse(null);
        return target;
    }

    @Override
    public Movement execute(Character user) {
        NPC target = getRandomNPC(user);
        if (target != null) {
            DisguiseHelper.disguiseCharacter(user, target);
        }
        return Movement.disguise;
    }

    @Override
    public Movement consider() {
        return Movement.disguise;
    }

}
