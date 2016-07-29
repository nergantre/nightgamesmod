package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

@Deprecated
public class MagicMilkAddiction extends Status {
    public static final String MAGICMILK_DRANK_DAYTIME_FLAG = "magicmilk_drank_daytime";
    public static String MAGICMILK_ADDICTION_FLAG = "magicmilk_addiction";
    protected double magnitude;
    int activated;
    Character target;

    public MagicMilkAddiction(Character affected, Character target, double magnitude) {
        super("Magic Milk Addiction", affected);
        this.target = target;
        this.activated = 0;
        this.magnitude = magnitude;
        flag(Stsflag.magicmilkcraving);
    }

    public MagicMilkAddiction(Character affected, Character target) {
        this(affected, target, 0);
    }
    
    public static int getMagicMilkAddictionLevel(Character character) {
        int addictionLevel = character.getFlag(MAGICMILK_ADDICTION_FLAG);
        if (addictionLevel <= 0) {
            return 0;
        } else if (addictionLevel < 10) {
            return 1;
        } else if (addictionLevel < 20) {
            return 2;
        } else if (addictionLevel < 35) {
            return 3;
        } else if (addictionLevel < 50) {
            return 4;
        } else {
            return 5;
        }
    }

    private double activationThreshold() {
        // level 2 addiction should be 7,
        // level 3 addiction should be 5
        // level 4 addiction should be 3
        // level 5 addiction should be 1
        return 7 - 2 * Math.max(0, getMagicMilkAddictionLevel(affected) - 2);
    }

    @Override
    public String describe(Combat c) {
        if (isActive()) {
            if (affected.human()) {
                return "You feel a desperate need to drink " + target.nameOrPossessivePronoun() + " milk.";
            } else {
                return affected.name() + " licks her lips while watching your breasts.";
            }
        } else {
            return "";
        }
    }

    @Override
    public String getVariant() {
        return "Addiction";
    }

    public boolean isActive() {
        return magnitude > activationThreshold();
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public float fitnessModifier() {
        return -(float)magnitude;
    }

    @Override
    public void tick(Combat c) {
        magnitude += 1;
        if (isActive()) {
            double willpowerLoss = Math.max(0, magnitude - activationThreshold());
            if (willpowerLoss > 0) {
                c.write(affected, Global.format("The desire to drink {other:name-possessive} milk is sapping {self:name-possessive} will to fight.", affected, target));
                affected.loseWillpower(c, (int)Math.round(willpowerLoss));
            }
        }
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof MagicMilkAddiction;
        magnitude = 0;
    }

    @Override
    public String toString() {
        if (isActive()) {
            return "In withdraw";
        }
        return "Addiction sated";
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (replaced) {
            return String.format("%s milk craving is temporarily sated.\n", affected.possessivePronoun());
        }
        return "";
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return (int) Math.max(0, magnitude - activationThreshold());
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return 0;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new MagicMilkAddiction(newAffected, newOther, magnitude);
    }
    
    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("magnitude", magnitude);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new MagicMilkAddiction(null, null, obj.get("magnitude").getAsInt());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
