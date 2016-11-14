package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.combat.Combat;

public class HumanPet extends Pet {
    public HumanPet(Character owner, NPC prototype, int power, int ac) {
        super(prototype.getName(), owner, Ptype.human, power, ac);
        buildSelfWithPrototype(prototype);
    }

    private void buildSelfWithPrototype(NPC prototype) {
        HumanPetCharacter self;
        try {
            self = new HumanPetCharacter(this, prototype, power);
            setSelf(self);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void buildSelf() {
        
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        
    }

    @Override
    public void caught(Combat c, Character captor) {
        // TODO Auto-generated method stub
        
    }

}
