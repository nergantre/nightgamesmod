package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.addiction.AddictionType;

public class CrisisOfFaith extends Status {

    public CrisisOfFaith(Character affected) {
        super("Crisis of Faith", affected);
        assert affected.human();
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return ""; // explanation given in withdrawal message
    }

    @Override
    public boolean lingering() {
        return true;
    }
    
    @Override
    public String describe(Combat c) {
        return "You are deeply disturbed by the doubt in your heart, limiting mojo gain.";
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
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return (int) ((double) x / Global.getPlayer().getAddiction(AddictionType.ZEAL).getMagnitude());
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
        return -5;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new CrisisOfFaith(newAffected);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new CrisisOfFaith(Global.getPlayer());
    }

}
