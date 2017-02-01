package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Disguised extends Status {
    private NPC disguisedTarget;

    public Disguised(Character affected, NPC disguiseTarget) {
        super("Disguised", affected);
        if (disguiseTarget == affected) {
            throw new RuntimeException("Tried to disguise as oneself!");
        }
        this.disguisedTarget = disguiseTarget;
        this.flag(Stsflag.disguised);
        this.flag(Stsflag.purgable);
    }

    @Override
    public boolean lingering() {
        return true;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return "";
    }

    @Override
    public String describe(Combat c) {
    	return "";
    }

    @Override
    public int mod(Attribute a) {
        int mod = disguisedTarget.get(a) - affected.getPure(a);
        if (affected.has(Trait.Masquerade)) {
            mod = mod * 3 / 2;
        }
        return mod;
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
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
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
        return new Disguised(newAffected, disguisedTarget);
    }

     @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("disguisedTarget", disguisedTarget.getTrueName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Disguised(null, Global.getNPC(obj.get("disguisedTarget").getAsString()));
    }

    public NPC getTarget() {
        return disguisedTarget;
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
