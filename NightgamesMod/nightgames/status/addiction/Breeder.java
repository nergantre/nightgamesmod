package nightgames.status.addiction;

import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.status.Horny;
import nightgames.status.Status;
import nightgames.status.Stsflag;

/*
 * TODO:
 * Morning descs for all but Magic Milk!
 */

public class Breeder extends Addiction {

    public Breeder(Character cause, float magnitude) {
        super("Breeder", cause, magnitude);
    }

    public Breeder(Character cause) {
        this(cause, .01f);
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
        return Optional.of(new Horny(affected, 5.f * getSeverity().ordinal(), 999, "your animal instincts"));
    }

    @Override
    protected Optional<Status> addictionEffects() {
        if (atLeast(Severity.HIGH))
            flag(Stsflag.feral);
        else
            unflag(Stsflag.feral);
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "All you can think about while fucking Kat is filling up her pussy with your cum. "
                                + "That desire is seriously undermining your staying power.";
            case LOW:
                return "Whatever Kat did to you, you don't seem to be able to last as long as before while fucking her.";
            case MED:
                return "Whenever you fuck Kat you feel an instinctive need to pound her as hard and fast as you can."
                                + " Of course, that does mean you won't last as long.";
            case NONE:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "You still feel a desire to fuck Kat silly, but it's no longer a driving force"
                                + " in your mind.";
            case MED:
                return "The need to fill Kat with your seed shifts to simply fucking her as hard as you can."
                                + " That won't be enough to last very long, but it's better than before.";
            case NONE:
                return "The animalistic instincts Kat imbued in you have fully faded away.";
            case HIGH:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "<b>Finding and seeding a willing pussy is foremost in your mind after not"
                                + " fucking Kat all day, and you are already hard at the prospect of"
                                + " mending that unfortunate situation.</b>";
            case LOW:
                return "<b>Having not fucked Kat all day, you feel a tingle in your balls telling you it's"
                                + " time to do something about that.</b>";
            case MED:
                return "<b>Your instincts are telling you that you haven't fucked enough today, and"
                                + " they are driving up your arousal.</b>";
            case NONE:
                throw new IllegalStateException("Tried to describe withdrawal for an inactive breeder addiction.");
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeCombatIncrease() {
        return "Your unnatural breeding instincts are slowly but steadily bubbling up in your mind.";
    }

    @Override
    protected String describeCombatDecrease() {
        return "You relish at letting loose inside of Kat, subduing the instinctive needs for now.";
    }

    @Override
    public String informantsOverview() {
        return "<i>\"Well... I don't know Kat that well, but don't you </i>always<i> want to fuck her?"
                        + " What's so special?\"</i> You explain how the desire is so much stronger than usual,"
                        + " that you just can't get it out of your mind. <i>\"Mmmm... You must be overly sensitive"
                        + " to her pheromones. It happens every once in a while, just some genetic bad luck. Or"
                        + " some </i>particularly<i> naughty parents, but let's not go there. Either way, it causes"
                        + " this feedback effect, basically driving you into a rut. The good news is that you'll"
                        + " be able to cum more often, the bad news is that you WILL cum more often when fucking Kat."
                        + " As in all the time. Other contenstants won't be as prepared for your ferocity, though,"
                        + " so you may have an advantage there. If you </i>don't<i> go and fuck someone, well, you"
                        + " might just go crazy. Best not take the risk.\"</i>";
    }

    @Override
    public String describeMorning() {
        return "";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.BREEDER;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "Arousal rages through your body at the sight of " + c.getOther(Global.getPlayer()).name() 
                            + ", expecting a well-earned fuck.";
        }
        return "Your instincts howl at the sight of Kat, urging you to fuck her as soon as possible.";
    }

    @Override
    public String describe(Combat c) {
        switch (getCombatSeverity()) {
            case HIGH:
                return "The animal part of your brain cultivated by Kat is screaming for"
                                + " you to sink your cock into her and fill her with your seed.";
            case LOW:
                return "You feel as if you've had plenty of foreplay. Time to move on.";
            case MED:
                return "Kat's little trick has you all geared up to fuck her. You can "
                                + "think of few things you'd like to do more.";
            case NONE:
            default:
                return "";

        }
    }

    @Override
    public int mod(Attribute a) {
        return a == Attribute.Animism ? getSeverity().ordinal() * 2 : 0;
    }

    @Override
    public void tick(Combat c) {
        if (inWithdrawal) {
            affected.arouse(Math.round(magnitude * 5), c, " (Breeding Instincts)");
            affected.emote(Emotion.horny, 20);
        }
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
        return new Breeder(newAffected);
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new Breeder(Global.getCharacterByType(JSONUtils.readString(obj, "cause")),
                        (float) JSONUtils.readInteger(obj, "magnitude"));
    }

}
