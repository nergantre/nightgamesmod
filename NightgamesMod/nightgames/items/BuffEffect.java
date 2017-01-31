package nightgames.items;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.status.Status;

public class BuffEffect extends ItemEffect {
    private Status applied;

    public BuffEffect(String verb, String otherverb, Status status) {
        super(verb, otherverb, true, true);
        applied = status;
    }

    @Override
    public boolean use(Combat c, Character user, Character opponent, Item item) {
        user.add(c, applied.instance(user, opponent));
        return true;
    }
}