package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

public class Blinded extends Status {

    private String cause;
    private final boolean voluntary;
    
    
    public Blinded(Character affected, String cause, boolean voluntary) {
        super("Blinded", affected);
        this.cause = cause;
        this.voluntary = voluntary;
        flag(Stsflag.blinded);
    }

    public boolean isVoluntary() {
        return voluntary;
    }
    
    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s eyes are now blocked by %s", affected.nameOrPossessivePronoun(), cause);
    }

    @Override
    public String describe(Combat c) {
        return String.format("%s eyesight is blocked by %s.", affected.nameOrPossessivePronoun(), cause);
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
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
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

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("cause", cause);
        obj.put("voluntary", voluntary);
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new Blinded(null, obj.get("cause").toString(), JSONUtils.readBoolean(obj, "voluntary"));
    }

    public String getCause() {
        return cause;
    }

}
