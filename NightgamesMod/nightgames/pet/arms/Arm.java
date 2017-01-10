package nightgames.pet.arms;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;

public abstract class Arm implements Cloneable {
    protected final ArmManager manager;
    private final String name;
    private final ArmType type;
    
    Arm(ArmManager manager, ArmType type) {
        this.manager = manager;
        this.name = type.getName();
        this.type = type;
    }
 
    public String getName() {
        return name;
    }
    
    public ArmType getType() { 
        return type;
    }
    
    abstract String describe();

    abstract List<ArmSkill> getSkills(Combat c, Character owner, Character target);

    abstract int attackOdds(Combat c, Character owner, Character target);

    public Arm instance() {
        try {
            return (Arm) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
