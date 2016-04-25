package nightgames.status.addiction;

import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Status;

public abstract class Addiction extends Status {

    public static final float LOW_INCREASE = .03f;
    public static final float MED_INCREASE = .08f;
    public static final float HIGH_INCREASE = .15f;

    public static final float LOW_THRESHOLD = .15f;
    public static final float MED_THRESHOLD = .4f;
    public static final float HIGH_THRESHOLD = .7f;

    protected final String name;
    protected final Character cause;
    protected float magnitude;
    protected float combatMagnitude;

    private boolean didDaytime;
    protected boolean inWithdrawal;

    protected Addiction(String name, Character cause, float magnitude) {
        super(name, Global.getPlayer());
        this.name = name;
        this.cause = cause;
        this.magnitude = magnitude;
        combatMagnitude = .01f;
        didDaytime = false;
    }

    protected Addiction(String name, Character cause) {
        this(name, cause, .01f);
    }

    public final void clearDaytime() {
        didDaytime = false;
    }
    
    public final void flagDaytime() {
        didDaytime = true;
    }
    
    public final float getMagnitude() {
        return magnitude;
    }

    public final Severity getSeverity() {
        if (magnitude < LOW_THRESHOLD) {
            return Severity.NONE;
        } else if (magnitude < MED_THRESHOLD) {
            return Severity.LOW;
        } else if (magnitude < HIGH_THRESHOLD) {
            return Severity.MED;
        } else {
            return Severity.HIGH;
        }
    }

    public final Severity getCombatSeverity() {
        if (combatMagnitude < LOW_THRESHOLD) {
            return Severity.NONE;
        } else if (combatMagnitude < MED_THRESHOLD) {
            return Severity.LOW;
        } else if (combatMagnitude < HIGH_THRESHOLD) {
            return Severity.MED;
        } else {
            return Severity.HIGH;
        }
    }

    protected final boolean atLeast(Severity threshold) {
        return getSeverity().ordinal() >= threshold.ordinal();
    }

    protected final boolean combatAtLeast(Severity threshold) {
        return getCombatSeverity().ordinal() >= threshold.ordinal();
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType().name());
        obj.put("cause", cause.getType());
        obj.put("magnitude", magnitude);
        obj.put("combat", combatMagnitude);
        return obj;
    }
    
    protected abstract Optional<Status> withdrawalEffects();

    protected abstract Optional<Status> addictionEffects();

    protected abstract String describeIncrease();

    protected abstract String describeDecrease();

    protected abstract String describeWithdrawal();

    protected abstract String describeCombatIncrease();

    protected abstract String describeCombatDecrease();

    public abstract String describeMorning();
    
    public abstract AddictionType getType();

    public Optional<Status> startNight() {
        if (!didDaytime) {
            inWithdrawal = true;
            alleviate(Global.randomfloat()/4.f);
            Global.gui().message(describeWithdrawal());
            return withdrawalEffects();
        }
        return Optional.empty();
    }

    public void endNight() {
        inWithdrawal = false;
        clearDaytime();
    }

    public Optional<Status> startCombat(Combat c, Character opp) {
        combatMagnitude = atLeast(Severity.MED) ? .2f : .0f;
        if (opp.equals(cause) && atLeast(Severity.LOW)) {
            return addictionEffects();
        }
        return Optional.empty();
    }

    public void endCombat(Combat c, Character opp) {
        // NOP
    }

    public boolean isActive() {
        return atLeast(Severity.LOW);
    }

    public boolean shouldRemove() {
        return magnitude <= 0.f;
    }
    
    public void aggravate(float amt) {
        Severity old = getSeverity();
        magnitude = clamp(magnitude + amt);
        if (getSeverity() != old) {
            Global.gui()
                  .message(describeIncrease());
        }
    }

    public void alleviate(float amt) {
        Severity old = getSeverity();
        magnitude = clamp(magnitude - amt);
        if (getSeverity() != old) {
            Global.gui()
                  .message(describeDecrease());
        }
    }

    public void aggravateCombat(float amt) {
        Severity old = getCombatSeverity();
        combatMagnitude = clamp(combatMagnitude + amt);
        if (getSeverity() != old) {
            Global.gui()
                  .message(describeCombatIncrease());
        }
    }

    public void alleviateCombat(float amt) {
        Severity old = getCombatSeverity();
        combatMagnitude = clamp(combatMagnitude - amt);
        if (getSeverity() != old) {
            Global.gui()
                  .message(describeCombatDecrease());
        }
    }
    
    private float clamp(float amt) {
        if (amt < 0.f)
            return 0.f;
        if (amt > 1.f)
            return 1.f;
        return amt;
    }

    public enum Severity {
        NONE,
        LOW,
        MED,
        HIGH;
    }

    public void describeInitial() {
        Global.gui().message(describeIncrease());
    }

    public static Addiction load(AddictionType type, Character cause, float mag, float combat) {
        Addiction a = type.build(cause, mag);
        a.magnitude = mag;
        a.combatMagnitude = combat;
        return a;
    }

    public boolean isInWithdrawal() {
        return inWithdrawal;
    }

    public boolean wasCausedBy(Character target) {
        return target.equals(cause);
    }
}
