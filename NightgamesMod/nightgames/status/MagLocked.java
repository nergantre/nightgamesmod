package nightgames.status;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.Item;

public class MagLocked extends Status {

    private int count;

    public MagLocked(Character affected) {
        super("MagLocked", affected);
        flag(Stsflag.maglocked);
        count = 1;
    }

    @Override
    public void replace(Status newStatus) {
        if (count < 3)
            count++;
    }

    public void addLock() {
        if (count < 3) count++;
    }
    
    public int getCount() {
        return count;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return "";
    }

    @Override
    public String describe(Combat c) {
        if (count == 1) {
            return Global.format(
                            "A single inactive MagLock hangs around one of {self:name-possessive}"
                                            + " wrists. It's harmless for now, but any more would be dangerous.",
                            affected, c.getOpponent(affected));
        } else if (count == 2) {
            return Global.format(
                            "{other:NAME-POSSESSIVE} two MagLocks, placed around {self:name-possessive}"
                                            + " wrists, have locked together behind {self:possessive} back and"
                                            + " are restraining {self:possessive} movement.",
                            affected, c.getOpponent(affected));
        } else {
            return Global.format(
                            "Hogtied by {other:name-possessive} MagLocks,"
                                            + "{self:subject-action:are|is} completely immobilized.",
                            affected, c.getOpponent(affected));
        }
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Speed) {
            return count == 3 ? 99 : count == 2 ? 3 : 0;
        }
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (count > 1) {
            flag(Stsflag.bound);
            c.getOpponent(affected).consume(Item.Battery, count - 1);
            if (count == 3) flag(Stsflag.hogtied);
        }
        if (!c.getOpponent(affected).has(Item.Battery)) {
            c.write(Global.format(
                            "<b>{other:NAME-POSSESSIVE} MagLocks have run out of power and "
                                            + "fall harmlessly off of {self:subject} and onto the ground.",
                            affected, c.getOpponent(affected)));
            affected.removelist.add(this);
        }
    }

    @Override
    public Set<Stsflag> flags() {
        Set<Stsflag> flags = new HashSet<>(super.flags().size() + 1);
        flags.add(Stsflag.stunned);
        return flags;
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
        return count == 1 ? -1 : count == 2 ? -4 : -10;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new MagLocked(newAffected);
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
