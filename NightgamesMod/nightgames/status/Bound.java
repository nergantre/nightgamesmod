package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.trap.Trap;

public class Bound extends Status {
    protected float toughness;
    protected String binding;
    protected Optional<Trap> trap;

    public Bound(Character affected, float dc, String binding) {
        this(affected, dc, binding, null);
    }
    public Bound(Character affected, float dc, String binding, Trap trap) {
        this("Bound", affected, dc, binding, trap);
    }

    public Bound(String type, Character affected, float dc, String binding, Trap trap) {
        super(type, affected);
        toughness = dc;
        this.binding = binding;
        this.trap = Optional.ofNullable(trap);
        flag(Stsflag.bound);
        flag(Stsflag.debuff);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now bound by %s.\n", affected.subjectAction("are", "is"), binding);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your hands are bound by " + binding + ".";
        } else {
            return affected.possessiveAdjective() + " hands are restrained by " + binding + ".";
        }
    }

    @Override
    public String getVariant() {
        return binding;
    }

    @Override
    public float fitnessModifier() {
        return -(5 + Math.min(20, toughness) / 2);
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
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
        return -15;
    }

    @Override
    public int escape() {
        return -Math.round(toughness);
    }

    @Override
    public void struggle(Character self) {
        int struggleAmount = 2 + (self.getLevel() + self.get(Attribute.Power) + self.get(Attribute.Cunning)) / 15;
        toughness = Math.max(toughness - struggleAmount, 0);
    }

    @Override
    public boolean lingering() {
        return false;
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
        return -10;
    }

    @Override
    public String toString() {
        return "Bound by " + binding;
    }

    @Override
    public int value() {
        return 0;
    }

    public void tick(Combat c) {
        if (c == null && trap.isPresent()) {
            if (affected.human()) {
                Global.gui().message(Global.format("{self:SUBJECT-ACTION:are|is} still trapped by the %s.", affected, Global.noneCharacter(), trap.get().getName().toLowerCase()));
            }
            affected.location().opportunity(affected, trap.get());
        }
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Bound(newAffected, toughness, binding, trap.orElse(null));
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("toughness", toughness);
        obj.addProperty("binding", binding);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Bound(null, obj.get("toughness").getAsFloat(), obj.get("binding").getAsString());
    }
}
