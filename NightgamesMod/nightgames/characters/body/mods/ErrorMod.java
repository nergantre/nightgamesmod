package nightgames.characters.body.mods;

public class ErrorMod extends PartMod {
    public static final ErrorMod INSTANCE = new ErrorMod();

    public ErrorMod() {
        super("error", 0, 1, 1, -1000);
    }

    @Override
    public String describeAdjective(String partType) {
        return "weirdness (ERROR)";
    }
}