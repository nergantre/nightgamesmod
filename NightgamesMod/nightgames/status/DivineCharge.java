package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class DivineCharge extends Status {
    public double magnitude;

    public DivineCharge(Character affected, double magnitude) {
        super("Divine Energy", affected);
        flag(Stsflag.divinecharge);
        flag(Stsflag.purgable);
        this.magnitude = magnitude;
    }

    private String getPart(Combat c) {
        boolean penetrated = c.getStance()
                              .vaginallyPenetrated(c, affected);
        boolean inserted = c.getStance()
                            .inserted(affected);
        String part = "body";
        if (penetrated && !inserted) {
            part = "pussy";
        }
        if (!penetrated && inserted) {
            part = "cock";
        }
        if (!penetrated && !inserted && affected.has(Trait.zealinspiring)) {
            part = "pussy";
        }
        return part;
    }

    @Override
    public void tick(Combat c) {
        if (!c.getStance().vaginallyPenetrated(c, affected) && !(affected.has(Trait.zealinspiring) && !Global.getPlayer()
                        .getAddiction(AddictionType.ZEAL).map(Addiction::isInWithdrawal).orElse(false))) {
            magnitude = magnitude / 2;
            c.write(affected, "The holy energy seeps out of " + affected.getName() + ".");
            if (magnitude < .05f)
                affected.removelist.add(this);
        }
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (!replaced) {
            return String.format("%s concentrating divine energy in %s %s.\n", affected.subjectAction("are", "is"),
                            affected.possessivePronoun(), getPart(c));
        }
        return "";
    }

    @Override
    public void onApply(Combat c, Character other) {
        affected.usedAttribute(Attribute.Divinity, c, .25);
    };

    @Override
    public String describe(Combat c) {
        return "Concentrated divine energy surges through " + affected.nameOrPossessivePronoun() + " " + getPart(c)
                        + " (" + Global.formatDecimal(magnitude) + ").";
    }

    @Override
    public float fitnessModifier() {
        return (float) (3 * magnitude);
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof DivineCharge;
        DivineCharge other = (DivineCharge) s;
        magnitude = magnitude + other.magnitude;
        // every 10 divinity past 10, you are allowed to add another stack of
        // divine charge.
        // this will get out of hand super quick, but eh, you shouldn't let it
        // get
        // that far.
        double maximum = Math.max(2, Math.pow(2., affected.get(Attribute.Divinity) / 5.0) * .25);
        this.magnitude = Math.min(maximum, this.magnitude);

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
        return 0;
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
        return new DivineCharge(newAffected, magnitude);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("magnitude", magnitude);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new DivineCharge(null, obj.get("magnitude").getAsFloat());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
