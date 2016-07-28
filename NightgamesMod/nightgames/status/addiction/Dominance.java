package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
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

    @Override
    protected Optional<Status> withdrawalEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Optional<Status> addictionEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeIncrease() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeDecrease() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeWithdrawal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeCombatIncrease() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeCombatDecrease() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String informantsOverview() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String describeMorning() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AddictionType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String describe(Combat c) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int mod(Attribute a) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int regen(Combat c) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int weakened(int x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int tempted(int x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int evade() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int escape() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int counter() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int value() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        // TODO Auto-generated method stub
        return null;
    }

}
