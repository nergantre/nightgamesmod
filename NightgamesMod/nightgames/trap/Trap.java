package nightgames.trap;

import nightgames.areas.Deployable;
import nightgames.characters.Character;
import nightgames.combat.IEncounter;

public abstract class Trap implements Deployable {
    
    protected Character owner;
    private final String name;
    
    protected Trap(String name, Character owner) {
        this.name = name;
        this.owner = owner;
    }
    
    protected abstract void trigger(Character target);

    public boolean decoy() {
        return false;
    }

    public abstract boolean recipe(Character owner);

    public abstract boolean requirements(Character owner);

    public abstract String setup(Character owner);

    public boolean resolve(Character active) {
        if (active != owner) {
            trigger(active);
            return true;
        }
        return false;
    }
    
    @Override
    public final Character owner() {
        return owner;
    }

    @Override
    public final String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object obj) {
        return obj != null && name.equals(obj.toString());
    }
    
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        // NOP
    }
}
