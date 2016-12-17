package nightgames.pet.arms;

public enum ArmType {
    GRABBER("Grabber", "a two-pronged grabber"),
    STRIPPER("Stripper", "a hook-like attachment"),
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
