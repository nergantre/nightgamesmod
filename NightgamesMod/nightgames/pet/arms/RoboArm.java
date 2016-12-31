package nightgames.pet.arms;

import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.skills.ArmSkill;

public abstract class RoboArm {

    protected final RoboArmManager manager;
    protected final Character owner;
    private final String name;
    private final ArmType type;
    
    RoboArm(RoboArmManager manager, Character owner, ArmType type) {
        super();
        this.manager = manager;
        this.owner = owner;
        this.name = type.getName();
        this.type = type;
    }
 
    public String getName() {
        return name;
    }
    
    public Character getOwner() {
        return owner;
    }
    
    public ArmType getType() { 
        return type;
    }
    
    public String describe() {
        return "A long, segmented metal arm with " + type.getDesc() + " at its tip.";
    }
    
    abstract List<ArmSkill> getSkills(Combat c, Character owner, Character target);
    
    int attackOdds(Combat c, Character owner, Character target) {
        return (int) Math.min(40, owner.get(Attribute.Science) * .67);
    }
}
