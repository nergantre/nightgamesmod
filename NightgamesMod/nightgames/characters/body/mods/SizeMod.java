package nightgames.characters.body.mods;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.global.Global;

public class SizeMod extends PartMod {
    public static int COCK_SIZE_TINY = 3;
    public static int COCK_SIZE_SMALL = 4;
    public static int COCK_SIZE_LITTLE = 5;
    public static int COCK_SIZE_AVERAGE = 6;
    public static int COCK_SIZE_LARGE = 7;
    public static int COCK_SIZE_BIG = 8;
    public static int COCK_SIZE_HUGE = 9;
    public static int COCK_SIZE_MASSIVE = 10;
    private static final Map<Integer, String> COCK_SIZE_DESCRIPTIONS = new HashMap<>(); 
    static {
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_TINY, "tiny ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_SMALL, "small ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_LITTLE, "small ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_AVERAGE, "");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_LARGE, "large ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_BIG, "big ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_HUGE, "huge ");
        COCK_SIZE_DESCRIPTIONS.put(COCK_SIZE_MASSIVE, "massive ");
    }
    public static int ASS_SIZE_SMALL = 0;
    public static int ASS_SIZE_NORMAL = 1;
    public static int ASS_SIZE_GIRLISH = 2;
    public static int ASS_SIZE_FLARED = 3;
    public static int ASS_SIZE_LARGE = 4;
    public static int ASS_SIZE_HUGE = 5;
    private static final Map<Integer, String> ASS_SIZE_DESCRIPTIONS = new HashMap<>(); 
    static {
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_SMALL, "small ");
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_NORMAL, "");
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_GIRLISH, "girlish ");
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_FLARED, "flared ");
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_LARGE, "large ");
        ASS_SIZE_DESCRIPTIONS.put(ASS_SIZE_HUGE, "huge ");
    }

    private static final Map<Integer, String> BREAST_SIZE_DESCRIPTIONS = new HashMap<>();
    private static final Map<Integer, String> BREAST_SIZE_CUPS = new HashMap<>();

    static {
        BREAST_SIZE_CUPS.put(0, "");
        BREAST_SIZE_CUPS.put(1, "A Cup");
        BREAST_SIZE_CUPS.put(2, "B Cup");
        BREAST_SIZE_CUPS.put(3, "C Cup");
        BREAST_SIZE_CUPS.put(4, "D Cup");
        BREAST_SIZE_CUPS.put(5, "DD Cup");
        BREAST_SIZE_CUPS.put(6, "E Cup");
        BREAST_SIZE_CUPS.put(7, "F Cup");
        BREAST_SIZE_CUPS.put(8, "G Cup");
        BREAST_SIZE_CUPS.put(9, "H Cup");
        BREAST_SIZE_DESCRIPTIONS.put(0, "flat");
        BREAST_SIZE_DESCRIPTIONS.put(1, "tiny");
        BREAST_SIZE_DESCRIPTIONS.put(2, "smallish");
        BREAST_SIZE_DESCRIPTIONS.put(3, "modest");
        BREAST_SIZE_DESCRIPTIONS.put(4, "round");
        BREAST_SIZE_DESCRIPTIONS.put(5, "large");
        BREAST_SIZE_DESCRIPTIONS.put(6, "huge");
        BREAST_SIZE_DESCRIPTIONS.put(7, "glorious");
        BREAST_SIZE_DESCRIPTIONS.put(8, "massive");
        BREAST_SIZE_DESCRIPTIONS.put(9, "colossal");
    }

    @Override
    public void loadData(JsonElement element) {
        this.size = element.getAsInt();
        this.modType = "size:"+this.size;
    }

    public JsonElement saveData() {
        return new JsonPrimitive(size);
    }

    public static final SizeMod INSTANCE = new SizeMod(0);

    private int size;

    public SizeMod(int size) {
        super("size:" + size, 0, 0, 0, -100000);
        this.size = size;
    }

    public SizeMod() {
        this(0);
    }

    public String adjective(BodyPart part) {
        if (part.getType().equals("cock")) {
            return COCK_SIZE_DESCRIPTIONS.get(size);
        }
        if (part.getType().equals("ass")) {
            return ASS_SIZE_DESCRIPTIONS.get(size);
        }
        if (part.getType().equals("breasts")) {
            return BREAST_SIZE_DESCRIPTIONS.get(size);
        }
        return "";
    }

    public static int clampToValidSize(BodyPart part, int size) {
        return Global.clamp(size, getMinimumSize(part.getType()), getMaximumSize(part.getType()));
    }

    @Override
    public String getVariant() {
        return "sizemod";
    }

    public Optional<String> getDescriptionOverride(Character self, BodyPart part) {
        if (part.isType("breasts")) {
            if ((size > 0 || self.useFemalePronouns())) {
                if (Global.random(2) == 0) {
                    return Optional.of(BREAST_SIZE_CUPS.get(size) + " " + Global.pickRandom(BreastsPart.synonyms).get());                
                }
            } else {
                if (self.get(Attribute.Power) > self.getLevel() + 5) {
                    return Optional.of("muscular pecs");
                }
                return Optional.of("flat chest");
            }
        }
        return Optional.empty();
    }

    public int getSize() {
        return size;
    }

    @Override
    public String describeAdjective(String partType) {
        return "some of its volume";
    }

    public static int getMaximumSize(String type) {
        switch (type) {
            case "breasts":
                return 9;
            case "cock":
                return COCK_SIZE_MASSIVE;
            case "ass":
                return ASS_SIZE_HUGE;
        }
        return 0;
    }

    public static int getMinimumSize(String type) {
        if (type.equals("cock")) {
            return COCK_SIZE_TINY;
        }
        return 0;
    }
}