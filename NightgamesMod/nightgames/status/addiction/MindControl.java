package nightgames.status.addiction;

import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.status.Enthralled;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class MindControl extends Addiction {

    public MindControl(Character cause, float magnitude) {
        super("Mind Control", cause, magnitude);
    }

    public MindControl(Character cause) {
        this(cause, .01f);
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "Mara has you completely in her grasp. Your body moves automatically to obey her"
                                + " commands, now.";
            case LOW:
                return "You feel a tug on your mind every time Mara speaks, pushing you to do as she says.";
            case MED:
                return "Every time Mara makes a demand you feel strong drive to obey, as if she were"
                                + " speaking directly to your mind.";
            case NONE:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "Mara's control is weakening, and only her strongest commands have a noticable effect.";
            case MED:
                return "You feel as if Mara's words do not bury themselves as deeply into your psyche as before."
                                + " Can you resist her?";
            case NONE:
                return "At last that invisible string tying you to Mara snaps, and you are back in control"
                                + " of your mind.";
            case HIGH:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeWithdrawal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String describeCombatIncrease() {
        return "Mara's words weigh increasingly heavily on you, and it's getting harder to resist.";
    }

    @Override
    protected String describeCombatDecrease() {
        return "Doing Mara's bidding relieves some of the pressure in your mind.";
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
        return AddictionType.MIND_CONTROL;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String describe(Combat c) {
        switch (getCombatSeverity()) {
            case HIGH:
                return "Every word Mara speaks rings of truth to you, even though she's telling"
                                + " you to submit to her. Your body trembles, and you will soon be forced to obey.";
            case LOW:
                return "Mara keeps saying things for you to do, and you don't know how"
                                + " long you'll be able to resist her.";
            case MED:
                return "Mara's words are starting to have a greater pull on you. You won't hold out"
                                + " much longer.";
            case NONE:
            default:
                return "";

        }
    }

    @Override
    public void tick(Combat c) {
        if (atLeast(Severity.HIGH)) {
            flag(Stsflag.cynical);
        } else {
            unflag(Stsflag.cynical);
        }
        if (!affected.is(Stsflag.enthralled) && Global.randomdouble() < magnitude/3) {
            affected.add(new Enthralled(affected, cause, 3));
            c.write(cause, "Mara's constant urging overcomes your defences, washing away all of your resistance.");
        }
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
        assert newAffected.human();
        return new MindControl(newOther, magnitude);
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new MindControl(Global.getCharacterByType(JSONUtils.readString(obj, "cause")),
                        (float) JSONUtils.readInteger(obj, "magnitude"));
    }

}
