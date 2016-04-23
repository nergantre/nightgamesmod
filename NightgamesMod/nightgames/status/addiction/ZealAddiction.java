package nightgames.status.addiction;

import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.stance.Behind;
import nightgames.stance.Mount;
import nightgames.stance.Position;
import nightgames.status.CrisisOfFaith;
import nightgames.status.Status;

public class ZealAddiction extends Addiction {

    public ZealAddiction(Character cause, float magnitude) {
        super("Zeal", cause, magnitude);
    }

    public ZealAddiction(Character cause) {
        this(cause, .01f);
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
        return Optional.of(new CrisisOfFaith(Global.getPlayer()));
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    public void tick(Combat c) {
        combatMagnitude += magnitude / 10.0;
        if (Global.randomdouble() < Math.min(.5f, combatMagnitude / 2.0)) {
            boolean behindPossible = cause.hasDick();
            Position pos;
            if (!behindPossible || Global.random(2) == 0) {
                pos = new Mount(cause, affected);
                c.write(cause, String.format(
                                "%s tells you to roll over, and once you have done so %s sets"
                                                + " %s down on your stomach.",
                                cause.name(), cause.pronoun(), cause.reflectivePronoun()));
            } else {
                pos = new Behind(cause, affected);
                c.write(cause, String.format("%s motions for you to get up and then casually walks around you"
                                + ", grabbing you from behind.", cause.name()));
            }
            c.setStance(pos);
        }
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return cause.name()
                                + " demands worship! The holy aura only reinforces your faith and your desire to serve!";
            case LOW:
                return cause.name() + " shines brightly as you cum inside of her. "
                                + "Maybe there is something to this whole divinity spiel?";
            case MED:
                return "You are now convinced " + cause.name()
                                + " is a higher power, and you feel a need to serve her accordingly.";
            case NONE:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "Your faith is shaking, is " + cause.name() + " really so divine?";
            case MED:
                return "Your faith in " + cause.name() + " has wavered a bit, but she's still"
                                + " your goddess! Right?";
            case NONE:
                return "You don't konw what possessed you before, but you no longer see " + cause.name()
                                + " as <i>actually</i> divine.";
            case HIGH:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "Your mind is completely preoccupied by " + cause.name() + ". You didn't worship today!"
                                + " Will she be angry? What will you do if she is? You aren't going to be able"
                                + " to focus on much else tonight.";
            case LOW:
                return "You didn't pay your respects to " + cause.name() + " today... Is that bad? Or isn't it?"
                                + " You are confused, and will have less mojo tonight.";
            case MED:
                return "You are terribly nervous at the thought of having to face " + cause.name()
                                + " tonight after failing to pray to her today. The rampaging thoughts are throwing you"
                                + " off your game.";
            case NONE:
                throw new IllegalStateException("Tried to describe withdrawal for an inactive zeal addiction.");
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeCombatIncrease() {
        return "You feel an increasingly strong desire to do whatever " + cause.name()
                        + " wants. She's a goddess, after all!";
    }

    @Override
    protected String describeCombatDecrease() {
        return "Doing " + cause.name() + "'s bidding clears your mind a bit. Why are you really doing this?"
                        + " One look at her reaffirms her divinity in your mind, though.";
    }

    @Override
    public String describeMorning() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AddictionType getType() {
        return AddictionType.ZEAL;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "You tremble as " + cause.name()
                            + " steps into view. Will she punish you for not being pious enough?"
                            + " Perhaps you should beg forgiveness...";
        }
        return "She's here! The holy glow reveals her presence even before you see her, and you nearly drop to your"
                        + " knees where you are.";
    }

    @Override
    public String describe(Combat c) {
        // TODO Auto-generated method stub
        return null;
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
        return -3;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new ZealAddiction(newAffected);
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return null;
    }

}
