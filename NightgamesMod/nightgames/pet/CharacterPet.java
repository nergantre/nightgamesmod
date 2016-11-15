package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class CharacterPet extends Pet {
    public CharacterPet(Character owner, Character prototype, int power, int ac) {
        this(prototype.getName(), owner, prototype, power, ac);
    }
    public CharacterPet(String name, Character owner, Character prototype, int power, int ac) {
        super(name, owner, Ptype.human, power, ac);
        buildSelfWithPrototype(prototype);
    }

    private void buildSelfWithPrototype(Character prototype) {
        if (prototype instanceof NPC) {
            NPCPetCharacter self;
            try {
                self = new NPCPetCharacter(getName(), this, (NPC)prototype, power);
                setSelf(self);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else if (prototype instanceof Player) {
            PlayerPetCharacter self;
            try {
                self = new PlayerPetCharacter(getName(), this, (Player)prototype, power);
                setSelf(self);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Could not build CharacterPet for " + prototype.toString());
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
        opponent.caught(c, getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        c.write(getSelf(), Global.format("In the heat of battle, {other:subject} manages to catch {self:name-do} and pin {self:direct-object} down. "
                        + "{other:SUBJECT} takes this chance to grind {other:possessive} knee between {self:name-possessive} legs, rubbing out a quick orgasm for {self:direct-object}.", getSelf(), captor));
        getSelf().doOrgasm(c, captor, null, null);
    }
}
