package nightgames.items.clothing;

public enum ClothingTrait {
    open("Open", "Shows what's beneath"),
    bulky("Bulky", "Speed penalty"),
    skimpy("Skimpy", "Better temptation damage"),
    flexible("Flexible", "Can fuck by pulling it aside"),
    indestructible("Indestructible", "Cannot be destroyed"),
    geeky("Geeky", "Science bonus"),
    armored("Armored", "Protects the delicate bits"),
    mystic("Mystic", "Arcane bonus"),
    martial("Martial", "Ki bonus"),
    broody("Broody", "Dark bonus"),
    kinky("Kinky", "Fetish bonus"),
    shoes("Shoes", "Easy to move in"),
    heels("Heels", "Hard to run in unless you're used to it"),
    highheels("High Heels", "Very hard to run in unless you're used to it"),
    higherheels("Higher Heels", "Impossible to run in unless you're used to it"),
    tentacleUnderwear("Tentacle Underwear", "Wearing tentacle underwear"),
    tentacleSuit("Tentacle Suit", "Wearing a tentacle suit"),
    nursegloves("Nurse's Gloves", "Wearing a pair of rubber nitrile gloves."),
    stylish("Stylish", "Better mojo gain"),
    ninja("Ninja Garb", "Bonus cunning and speed"),
    dexterous("Dextrous", "Bonus cunning"),
    lame("Lame", "Small mojo penalty"),
    harpoonDildo("Harpoon Dildo", "Has a dildo stuck inside"),
    harpoonOnahole("Harpoon Onahole", "Has an onahole stuck on"),
    persistent("Persistent", "Difficult to remove"),
    none("", "");

    private String name;
    private String desc;

    ClothingTrait(String name, String desc) {
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
