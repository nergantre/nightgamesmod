package nightgames.status;


import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Blinded extends Status {

    private String cause;
    private final boolean voluntary;
    
    
    public Blinded(Character affected, String cause, boolean voluntary) {
        super("Blinded", affected);
        this.cause = cause;
        this.voluntary = voluntary;
        flag(Stsflag.blinded);
        flag(Stsflag.debuff);
    }

    public boolean isVoluntary() {
        return voluntary;
    }
    
    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return Global.capitalizeFirstLetter(String.format("%s eyes are now blocked by %s", affected.nameOrPossessivePronoun(), cause));
    }

    @Override
    public String describe(Combat c) {
        return Global.capitalizeFirstLetter(String.format("%s eyesight is blocked by %s.", affected.nameOrPossessivePronoun(), cause));
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
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
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
        return 0;
    }

    @Override
    public int evade() {
        return -20;
    }

    @Override
    public int escape() {
        return -10;
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
        return -20;
    }

    @Override
    public int value() {
        return -3;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Blinded(newAffected, cause, voluntary);
    }

    @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("cause", cause);
        obj.addProperty("voluntary", voluntary);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Blinded(null, obj.get("cause").toString(), obj.get("voluntary").getAsBoolean());
    }

    public String getCause() {
        return cause;
    }

}
