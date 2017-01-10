package nightgames.pet.arms;

public enum ArmType {
    GRABBER("Grabber", "a two-pronged grabber"),
    STRIPPER("Stripper", "a hook-like attachment"),
    STABILIZER("Stabilizer", "a wide, flat base"),
    HEAT_RAY("Heat Ray Cannon", "a red, slitted opening"),
    DEFAB_CANNON("Defrabrication Cannon", "a spherical, metallic tip"),
    HEAL_CANNON("Heal Cannon", "a blocky tip"),
    ;
    
    private final String name, desc;
    
    private ArmType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
    
}
