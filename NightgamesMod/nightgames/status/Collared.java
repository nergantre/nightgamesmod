package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Collared extends Status {

    private int charges;
    
    public Collared(Character affected) {
        super("Collared", affected);
        flag(Stsflag.collared);
        charges = 15;
    }

    public void spendCharges(Combat c, int amt) {
        charges -= amt;
    }
    
    public void tick(Combat c) {
        if (charges <= 0) {
            c.write("<b>The collar around " + affected.nameOrPossessivePronoun() 
                            + " neck runs out of power and falls off.</b>");
            affected.removelist.add(this);
        }
    }

    public void recharge() {
        charges += 15;
    }
    
    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return Global.format("{self:SUBJECT} now {self:action:have|has} a metallic collar around"
                        + " {self:possessive} neck!", affected, c.getOpponent(affected));
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT-ACTION:are|is} wearing a training collar, which"
                        + " is hampering {self:possessive} ability to fight.", affected, c.getOpponent(affected));
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
        return 0;
    }

    @Override
    public boolean lingering() {
        return true;
    }
    
    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Collared(newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

}
