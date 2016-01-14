package nightgames.pet;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class FGoblin extends Pet {

    public FGoblin(Character owner) {
        super("Fetish Goblin", owner, Ptype.fgoblin, 3, 2);
    }

    @Override
    public String describe() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void act(Combat c, Character target) {
        if (target.human()) {

        } else {

        }
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {

    }

    @Override
    public void caught(Combat c, Character captor) {
        if (owner().human()) {

        } else {

        }
    }

    @Override
    public boolean hasDick() {
        return true;
    }

    @Override
    public boolean hasPussy() {
        return true;
    }
}
