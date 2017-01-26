package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Compulsion extends DurationStatus implements Compulsive {

    private final Character compeller;
    
    public Compulsion(Character affected, Character compeller) {
        this(affected, compeller, 3);
    }

    
    public Compulsion(Character affected, Character compeller, int duration) {
        super("Compulsion", affected, duration);
        this.compeller = compeller;
        flag(Stsflag.compelled);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return Global.format("{other:SUBJECT-ACTION:have|has} placed a compulsion on"
                        + " {self:name-possessive} mind!", affected, compeller);
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{other:NAME-POSSESSIVE} compulsion still lies on"
                        + " {self:name-possessive} mind, enforcing {self:possessive} loyalty.",
                        affected, compeller);
    }

    @Override
    public int mod(Attribute a) {
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
        return -20;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Compulsion(newAffected, newOther);
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

    @Override
    public String describe(Combat c, Situation sit) {
        switch (sit) {
            case PREVENT_ESCAPE:
                return Global.format("{self:SUBJECT-ACTION:try|tries} to find a way out of"
                                + " {self:possessive} current predicament, but something inside"
                                + " of {self:direct-object} is stubbornly blocking the attempt."
                                , affected, compeller);
            case PUNISH_PAIN:
                return Global.format("The compulsion in {self:name-possessive}"
                                + "mind reacts to {self:possessive} aggression by sending"
                                + " a powerful shock down {self:possessive} spine.", 
                                affected, compeller);
            case PREVENT_REMOVE_BOMB:
                return Global.format("{other:NAME-POSSESSIVE} compulsion prevents {self:name-do}"
                                + " from removing the device on {self:possessive} chest. How did"
                                + " you end up fighting a techno-demon, again?", affected, compeller);
            case PREVENT_STRUGGLE:
                return Global.format("{self:SUBJECT-ACTION:try|tries} to struggle, but"
                                + " {other:name-possessive} compulsion is having none of it and forces"
                                + " {self:direct-object} to cease {self:possessive} attempts.", affected, compeller);
            case STANCE_FLIP:
                return c.getStance().reverse(c, false).equals(c.getStance()) ?
                                Global.format("{other:SUBJECT-ACTION:tell|tells} {self:name-do}"
                                                + " to be still, and with the compulsion weighing on"
                                                + " {self:possessive} mind there is nothing {self:direct-object}"
                                                + " can do to resist as {other:pronoun-action:put|puts}"
                                                + " {self:direct-object} into a pin.", affected, compeller)
                            :
                                Global.format("Appearantly punishing {self:name-do} for dominating the demon who has"
                                                + " declared {other:reflective} to be {self:possessive} master,"
                                                + " the compulsion {other:pronoun} placed forces {self:possessive}"
                                                + " muscles to go limp."
                                                + " At the same time, {other:subject-action:grab|grabs}"
                                                + " hold of {self:possessive} body and gets {other:reflective}"
                                                + " into a more advantegeous position.", affected, compeller);
            case PREVENT_REVERSAL:
                return Global.format("{self:SUBJECT-ACTION:try|tries} to gain the upper hand"
                                + " over {other:name-do}, but the compulsion disallows it and"
                                + " freezes {self:possessive} body until {self:pronoun-action:change|changes}"
                                + " {self:possessive} mind.", affected, compeller);
            default:
                return "ERROR: Missing compulsion type in Collared";
            
        }
    }
}
