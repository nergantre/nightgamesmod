package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Slimed extends DurationStatus {
    private Character origin;
    private int stacks;

    public Slimed(Character affected, Character other) {
        super("parasited", affected, 4);
        this.origin = other;
        this.stacks = 0;
        // don't auto-remove when cleared.
        requirements.clear();
        flag(Stsflag.parasited);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
    	if (replaced) {
            return Global.format("More pieces of {other:name-possessive} slime are getting stuck to {self:name-possessive} body.\n", affected, origin);
    	}
        return Global.format("Pieces of {other:name-possessive} slime are stuck to {self:name-possessive} body!\n", affected, origin);
    }

    @Override
    public String describe(Combat c) {
    	if (stacks < 10) {
    		return Global.format("A few chunks of {other:name-possessive} slimey body is stuck on {self:direct-object}.", affected, origin);
    	} else if (stacks < 33) {
    		return Global.format("Bits and pieces of {other:name-possessive} slime are stuck on {self:name-do}.", affected, origin);
    	} else if (stacks < 66) {
    		return Global.format("It's becoming difficult to move with so much of {other:name-possessive} slime on {self:name-possessive} body.", affected, origin);
    	} else if (stacks < 100) {
    		return Global.format("It's very difficult to move with so much of {other:name-possessive} slime on {self:name-possessive} body.", affected, origin);
    	} else {
    		return Global.format("{self:SUBJECT-ACTION:are|is} covered head to toe with {other:name-possessive} slime, making it impossible to move!", affected, origin);
    	}
    }

    @Override
    public float fitnessModifier() {
        return -stacks;
    }

    @Override
    public int mod(Attribute a) {
    	if (a == Attribute.Speed) {
    		return -stacks / 10;
    	}

        return 0;
    }

    @Override
    public void tick(Combat c) {
    	super.tick(c);
        if (getDuration() <= 0) {
        	stacks = Math.max(0, stacks - 10);
        	if (stacks == 0) {
        		Global.writeFormattedIfCombat(c, "{self:SUBJECT-ACTION:finally shake|finally shakes} off all of {other:name-possessive} slime!", affected, origin);
        		affected.removeStatus(this);
        	} else {
	    		Global.writeFormattedIfCombat(c, "{self:SUBJECT-ACTION:shake|shakes} off some of {other:name-possessive} sticky slime.", affected, origin);
	        	setDuration(4);
        	}
        }
        if (stacks >= 100) {
        	Global.writeFormattedIfCombat(c, "There's so much slime on {self:name-do} that it solidifies into a sheet of hard plastic!.", affected, origin);
        	affected.removeStatus(this);
        	affected.add(c, new Plasticized(affected));
        }
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
        return -stacks;
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        Slimed other = (Slimed) s;
        setDuration(Math.max(other.getDuration(), getDuration()));
        stacks += other.stacks;
    }

    @Override
    public int escape() {
        return -stacks / 3;
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
        return -stacks / 10;
    }

    public String toString() {
        return "Slimed";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Slimed(newAffected, newOther);
    }

    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        obj.addProperty("stacks", stacks);
        return obj;
    }

    public Status loadFromJson(JsonObject obj) {
    	// TODO implement me
        return new Slimed(null, null);
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
