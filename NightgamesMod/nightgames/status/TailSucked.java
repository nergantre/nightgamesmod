package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.TailSuck;
import nightgames.skills.damage.DamageType;

public class TailSucked extends Status implements InsertedStatus {

    private Character sucker;
    private int power;

    public TailSucked(Character affected, Character sucker, int power) {
        super("Tail Sucked", affected);
        this.sucker = sucker;
        this.power = power;
        requirements.add((c, self, other) -> c != null && self != null && other != null
                        && new TailSuck(other).usable(c, self));
        flag(Stsflag.bound);
        flag(Stsflag.debuff);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s tail is sucking %s energy straight from %s %s.", sucker.nameOrPossessivePronoun(),
                        affected.nameOrPossessivePronoun(), affected.possessiveAdjective(),
                        affected.body.getRandomCock().describe(affected));
    }

    @Override
    public String describe(Combat c) {
        if (!affected.hasDick()) {
            affected.removelist.add(this);
            return "";
        }
        return String.format("%s tail keeps churning around %s " + "%s, sucking in %s vital energies.",
                        sucker.nameOrPossessivePronoun(), affected.nameOrPossessivePronoun(),
                        affected.body.getRandomCock().describe(affected), affected.possessiveAdjective());
    }

    @Override
    public void tick(Combat c) {
        BodyPart cock = affected.body.getRandomCock();
        BodyPart tail = sucker.body.getRandom("tail");
        if (cock == null || tail == null || c == null) {
            affected.removelist.add(this);
            return;
        }

        c.write(sucker, String.format("%s tail sucks powerfully, and %s" + " some of %s strength being drawn in.",
                        sucker.nameOrPossessivePronoun(), affected.subjectAction("feel", "feels"),
                        affected.possessiveAdjective()));

        Attribute toDrain = Global.pickRandom(affected.att.entrySet().stream().filter(e -> e.getValue() != 0)
                        .map(e -> e.getKey()).toArray(Attribute[]::new)).get();
        Abuff.drain(c, sucker, affected, toDrain, power, 20, true);
        affected.drain(c, sucker, (int) sucker.modifyDamage(DamageType.drain, affected, 10));
        affected.drainMojo(c, sucker, 1 + Global.random(power * 3));
    }

    @Override
    public float fitnessModifier() {
        return -4.0f;
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
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return 0;
    }

    @Override
    public int evade() {
        return power * -5;
    }

    @Override
    public int escape() {
        return power * -5;
    }

    @Override
    public int gainmojo(int x) {
        return (int) (x * 0.2);
    }

    @Override
    public int spendmojo(int x) {
        return (int) (x * 1.2);
    }

    @Override
    public int counter() {
        return -10;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new TailSucked(newAffected, newOther, power);
    }

    @Override public JsonObject saveToJson() {
        return null;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return null;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Character getSucker() {
        return sucker;
    }

    @Override
    public BodyPart getHolePart() {
        return sucker.body.getRandom("tail");
    }

    @Override
    public Character getReceiver() {
        return sucker;
    }

    @Override
    public BodyPart getStickPart() {
        return affected.body.getRandomCock();
    }

    @Override
    public Character getPitcher() {
        return affected;
    }
}
