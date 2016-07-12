package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Feral extends Status {

    public Feral(Character affected) {
        super("Feral", affected);
        flag(Stsflag.feral);
        flag(Stsflag.purgable);
    }

    @Override
    public String describe(Combat c) {
        return String.format("%s seems beyond reason in %s feral lust.\n",
                        Global.capitalizeFirstLetter(affected.subject()), affected.possessivePronoun());
    }

    @Override
    public float fitnessModifier() {
        return 4;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s turned feral.\n", affected.subjectAction("have", "has"));
    }

    @Override
    public int mod(Attribute a) {
        switch (a) {
            case Power:
                return 1 + affected.getPure(Attribute.Animism) / 2;
            case Cunning:
                return 3;
            case Seduction:
                return 2;
            case Animism:
                return affected.getPure(Attribute.Animism) / 2;
            case Speed:
                return 2;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int regen(Combat c) {
        if (affected.getArousal().percent() < 40) {
            affected.removelist.add(this);
        }
        int ignoreOrgasmChance = Math.min(5, 2 + affected.get(Attribute.Animism) / 10);
        if (Global.random(ignoreOrgasmChance) != 0) {
            affected.addlist.add(new IgnoreOrgasm(affected, 0));
        }
        return 0;
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
        return new Feral(newAffected);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new Feral(null);
    }
}
