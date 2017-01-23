package nightgames.pet.arms;

public enum ArmType {
    GRABBER("Grabber", "a two-pronged grabber"),
    STRIPPER("Stripper", "a hook-like attachment"),
    STABILIZER("Stabilizer", "a wide, flat base"),
    HEAT_RAY("Heat Ray Cannon", "a red, slitted opening"),
    DEFAB_CANNON("Defrabrication Cannon", "a spherical, metallic tip"),
    HEAL_CANNON("Heal Cannon", "a blocky tip"),
    TOY_ARM("Silicone Arm", "a malleable silicone ball"),
    TENTACLE_INJECTOR("Injector Tentacle", "a needle tip leaking fluid"),
    TENTACLE_CLINGER("Clinger Tentacle", "suction cups lining the body"),
    TENTACLE_IMPALER("Tentacle Impaler", "a tapered phallic tip"),
    TENTACLE_SUCKER("Sucker Tentacle", "a lewd fleshy opening"),
    TENTACLE_SQUIRTER("Tentacle Squirter", "a nozzle-like opening"),
    TENTACLE_BIRTHER("Birther Tentacle", "numerous bulbous pods"),
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
