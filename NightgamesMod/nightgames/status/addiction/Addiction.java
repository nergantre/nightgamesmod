package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public abstract class Addiction extends Status {

    public static final float LOW_INCREASE = .03f;
    public static final float MED_INCREASE = .08f;
    public static final float HIGH_INCREASE = .15f;

    public static final float LOW_THRESHOLD = .15f;
    public static final float MED_THRESHOLD = .4f;
    public static final float HIGH_THRESHOLD = .7f;

    protected final transient Character cause;
    protected float magnitude;
    protected float combatMagnitude;

    // should be saved
    private boolean didDaytime;
    private boolean overloading;

    protected boolean inWithdrawal;

    protected Addiction(Character affected, String name, Character cause, float magnitude) {
        super(name, affected);
        flag(Stsflag.permanent);
        this.name = name;
        this.cause = cause;
        this.magnitude = magnitude;
        combatMagnitude = .01f;
        didDaytime = false;
        inWithdrawal = false;
        overloading = false;
    }

    protected Addiction(Character affected, String name, Character cause) {
        this(affected, name, cause, .01f);
    }
    
    public Character getCause() {
        return cause;
    }

    @Override
    public void tick(Combat c) {
        combatMagnitude += magnitude / 14.0;
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

    public final boolean atLeast(Severity threshold) {
        return getSeverity().ordinal() >= threshold.ordinal();
    }

    public final boolean combatAtLeast(Severity threshold) {
        return getCombatSeverity().ordinal() >= threshold.ordinal();
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getType().name());
        obj.addProperty("cause", cause.getType());
        obj.addProperty("magnitude", magnitude);
        obj.addProperty("combat", combatMagnitude);
        obj.addProperty("overloading", overloading);
        obj.addProperty("reenforced", didDaytime);
        return obj;
    }

    protected abstract Optional<Status> withdrawalEffects();

    protected abstract Optional<Status> addictionEffects();

    protected abstract String describeIncrease();

    protected abstract String describeDecrease();

    protected abstract String describeWithdrawal();

    protected abstract String describeCombatIncrease();

    protected abstract String describeCombatDecrease();

    public abstract String informantsOverview();

    public abstract String describeMorning();

    public abstract AddictionType getType();

    public void overload() {
        magnitude = 1.0f;
        overloading = true;
    }

    public Optional<Status> startNight() {
        if (!didDaytime || overloading) {
            if (!overloading) {
                float amount = Global.randomfloat() / 4.f;
                if (Global.isDebugOn(DebugFlags.DEBUG_ADDICTION)) {
                    System.out.println("Alleviating addiction " + this.getType() + " by " + amount);
                }
                alleviate(null, amount);
            }
            if (isActive()) {
                inWithdrawal = true;
                if (affected.human()) {
                    Global.gui().message(describeWithdrawal());
                }
                return withdrawalEffects();
            }
        }
        return Optional.empty();
    }

    public void refreshWithdrawal() {
        if (inWithdrawal) {
            Optional<Status> opt = withdrawalEffects();
            if (opt.isPresent() && !affected.has(opt.get()))
                affected.addNonCombat(opt.get().instance(affected, cause));
        }
    }

    public void endNight() {
        inWithdrawal = false;
        clearDaytime();
        if (overloading) {
            magnitude = 0.f;
            overloading = false;
            Global.gui()
                  .message("<b>The overload treatment seems to have worked, and you are now rid of all traces of"
                                  + " your " + name + ".\n</b>");
            affected.removeStatusImmediately(this);
        }
    }

    public Optional<Status> startCombat(Combat c, Character opp) {
        combatMagnitude = atLeast(Severity.MED) ? .2f : .0f;
        if (opp.equals(cause) && atLeast(Severity.LOW)) {
            flags.forEach(affected::flagStatus);
            return addictionEffects();
        }
        return Optional.empty();
    }

    public void endCombat(Combat c, Character opp) {
        flags.forEach(affected::unflagStatus);
    }

    public boolean isActive() {
        return atLeast(Severity.LOW);
    }

    public boolean shouldRemove() {
        return magnitude <= 0.001f;
    }

    public void aggravate(Combat c, float amt) {
        Severity old = getSeverity();
        magnitude = clamp(magnitude + amt);
        if (getSeverity() != old) {
            Global.writeIfCombat(c, cause, Global.format(describeIncrease(), affected, cause));
        }
    }

    public void alleviate(Combat c, float amt) {
        Severity old = getSeverity();
        magnitude = clamp(magnitude - amt);
        if (getSeverity() != old) {
            Global.writeIfCombat(c, cause, Global.format(describeDecrease(), affected, cause));
        }
    }

    public void aggravateCombat(Combat c, float amt) {
        Severity old = getCombatSeverity();
        combatMagnitude = clamp(combatMagnitude + amt);
        if (getSeverity() != old) {
            Global.writeIfCombat(c, cause, Global.format(describeCombatIncrease(), affected, cause));
        }
    }

    public void alleviateCombat(Combat c, float amt) {
        Severity old = getCombatSeverity();
        combatMagnitude = clamp(combatMagnitude - amt);
        if (getSeverity() != old) {
            Global.writeIfCombat(c, cause, Global.format(describeCombatDecrease(), affected, cause));
        }
    }

    private float clamp(float amt) {
        if (amt < 0.f)
            return 0.f;
        if (amt > 1.f)
            return 1.f;
        return amt;
    }
    
    public final boolean isAddiction() {
        return true;
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

    public static Addiction load(Character self, JsonObject object) {
        Character cause = Global.getNPCByType(object.get("cause").getAsString());
        if (cause == null) {
            return null;
        }
        AddictionType type = AddictionType.valueOf(object.get("type").getAsString());
        float mag = object.get("magnitude").getAsFloat();
        float combat = object.get("combat").getAsFloat();
        boolean overloading = object.get("overloading").getAsBoolean();
        boolean reenforced = object.get("reenforced").getAsBoolean();
        Addiction a = type.build(self, cause, mag);
        a.magnitude = mag;
        a.combatMagnitude = combat;
        a.overloading = overloading;
        a.didDaytime = reenforced;
        return a;
    }

    public boolean isInWithdrawal() {
        return inWithdrawal;
    }

    public boolean wasCausedBy(Character target) {
        return target != null && target.getType().equals(cause.getType());
    }
}
