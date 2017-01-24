package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Compulsive.Situation;

public class Collared extends Status implements Compulsive {

    private int charges;
    private final Character owner;
    
    public Collared(Character affected, Character owner) {
        super("Collared", affected);
        flag(Stsflag.collared);
        flag(Stsflag.compelled);
        charges = 10;
        this.owner = owner;
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
        return -20;
    }

    @Override
    public boolean lingering() {
        return true;
    }
    
    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Collared(newAffected, newOther);
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
                return Global.format("{self:SUBJECT-ACTION:try|tries} to struggle, but"
                                + " the collar is having none of it and shocks {self:direct-object}"
                                + " into submission.", affected, owner);
            case PUNISH_PAIN:
                return Global.format("The training collar around {self:name-possessive}"
                                + "neck reacts to {self:possessive} aggression by sending"
                                + " a powerful shock down {self:possessive} spine.", 
                                affected, owner);
            case PREVENT_REMOVE_BOMB:
                return Global.format("{self:SUBJECT-ACTION:reach|reaches} up to grab the sphere on "
                                + "{self:possessive} chest, but the collar around {self:possessive} neck"
                + " does not appreciate the sentiment and shocks {self:direct-object} to keep your arms down.",
                    affected, owner);
            case PREVENT_STRUGGLE:
                return Global.format("{self:SUBJECT-ACTION:try|tries} to struggle, but"
                                + " the collar is having none of it and shocks {self:direct-object}"
                                + " into submission.", affected, owner);
            case STANCE_FLIP:
                return c.getStance().reverse(c, false).equals(c.getStance()) ?
                                Global.format("Distracted by a shock from the collar around {self:possessive}"
                                + " neck, {self:subject-action:have|has} no chance to resist as"
                                + " {other:subject-action:put|puts} {self:direct-object}"
                                + " in a pin.", affected, owner)
                            :
                                Global.format("Appearantly punishing {self:name-do} for being dominant, the collar"
                                                + " around {self:possessive} neck gives {self:direct-object} a painful"
                                                + " shock. At the same time, {other:subject-action:grab|grabs}"
                                                + " hold of {self:possessive} body and gets {other:reflective}"
                                                + " into a more advantegeous position.", affected, owner);
            case PREVENT_REVERSAL:
                return Global.format("{self:SUBJECT-ACTION:try|tries} to get the"
                            + " upper hand, but the collar adamantly refuses by"
                            + " shocking {self:direct-object}.", affected, owner);
            default:
                return "ERROR: Missing compulsion type in Collared";
            
        }
    }
    
    @Override
    public void doPostCompulsion(Combat c, Situation sit) {
        int cost;
        switch (sit) {
            case PREVENT_ESCAPE:
            case PREVENT_STRUGGLE:
            case STANCE_FLIP:
                cost = 2;
                break;
            case PREVENT_REMOVE_BOMB:
            case PUNISH_PAIN:
                cost = 1;
                break;
            default:
                cost = 0;
        }
        charges = Math.max(0, charges - cost);
    }

}
