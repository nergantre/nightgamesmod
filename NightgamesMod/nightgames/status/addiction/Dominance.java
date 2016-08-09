package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Dominance extends Addiction {

    public Dominance(Character cause, float magnitude) {
        super("Dominance", cause, magnitude);
        flags.add(Stsflag.victimComplex);
    }

    public Dominance(Character cause) {
        this(cause, .01f);
    }

    public static boolean mojoIsBlocked(Combat c) {
        if (c == null)
            return false;
        Player player = Global.getPlayer();
        Character opp = c.getOther(player);
        if (!Global.getPlayer().checkAddiction(AddictionType.DOMINANCE, opp))
            return false;
        int sev = player.getAddictionSeverity(AddictionType.DOMINANCE).ordinal();
        int dom = c.getDominanceOfStance(opp);
        
        return sev >= 5 - dom;
    }
    
    @Override
    protected Optional<Status> withdrawalEffects() {
        return Optional.empty();
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "Held down by Jewel, you feel completely powerless to resist.";
            case LOW:
                return "You feel strangely weak in Jewel's powerful hold.";
            case MED:
                return "Something about the way Jewel is holding on to you is causing your strength to seep away.";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "More and more of your strength is returning since escaping from Jewel. ";
            case MED:
                return "You find some of the strange weakness caused by Jewel's powerful hold"
                                + " fleeing your bones. ";
            case NONE:
                return "You have completely recovered from Jewel's hold. ";
            case HIGH:
            default:
                return "";
        }
    }

    @Override
    protected String describeWithdrawal() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    protected String describeCombatIncrease() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    protected String describeCombatDecrease() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String informantsOverview() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String describeMorning() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.DOMINANCE;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return "";
    }

    @Override
    public String describe(Combat c) {
        return "";
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
    public Status instance(Character newAffected, Character newOther) {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

}
