package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Primed extends Status {

    private int charges;
    
    public Primed(Character affected, int charges) {
        super("Primed", affected);
        this.charges = charges;
        flag(Stsflag.primed);
    }

    public static boolean isPrimed(Character ch, int minCharges) {
        if (!ch.is(Stsflag.primed))
            return false;
        return ((Primed) ch.getStatus(Stsflag.primed)).charges >= minCharges;
    }
    
    @Override
    public void tick(Combat c) {
        if (charges <= 0)
            affected.removelist.add(this);
    }
    
    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        if (isPrimed(affected, 1))
            return "";
        return String.format("%s storing time charges.", affected.subjectAction("are", "is"));
    }

    @Override
    public String describe(Combat c) {
        if(affected.human()){
            return "You have "+charges+" time charges primed.";
        }else{
            return ""; // hide
        }
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }
    

    @Override
    public void replace(Status s) {
        assert s instanceof Primed;
        charges += ((Primed) s).charges;
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
        return charges;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Primed(newAffected, charges);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("charges", charges);
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new Primed(null, obj.get("charges").getAsInt());
    }

    public int getCharges() {
        return charges;
    }

}
