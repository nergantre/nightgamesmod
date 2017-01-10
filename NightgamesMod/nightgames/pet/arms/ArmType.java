package nightgames.pet.arms;

public enum ArmType {
    GRABBER("Grabber", "a two-pronged grabber"),
    STRIPPER("Stripper", "a hook-like attachment"),
    STABILIZER("Stabilizer", "a wide, flat base"),
    HEAT_RAY("Heat Ray Cannon", "a red, slitted opening"),
    DEFAB_CANNON("Defrabrication Cannon", "a spherical, metallic tip"),
    HEAL_CANNON("Heal Cannon", "a blocky tip"),
    TENTACLE_INJECTOR("Injector", "a needle tip leaking fluid"),
    TENTACLE_CLINGER("Clinger", "suction cups lining the body"),
    TENTACLE_IMPALER("Impaler", "a tapered phallic tip"),
    TENTACLE_SUCKER("Sucker", "a lewd fleshy opening"),
    TENTACLE_SQUIRTER("Squirter", "a nozzle-like opening"),
    TENTACLE_BIRTHER("Birther", "numerous bulbous pods"),
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
