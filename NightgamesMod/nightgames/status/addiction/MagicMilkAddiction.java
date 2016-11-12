package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class MagicMilkAddiction extends Addiction {

    private int originalMaxWill;

    public MagicMilkAddiction(Character cause, float magnitude) {
        super("Magic Milk Addiction", cause, magnitude);
        flag(Stsflag.magicmilkcraving);
        flag(Stsflag.tolerance); // immune to regular addiction
    }

    public MagicMilkAddiction(Character cause) {
        this(cause, .01f);
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
        double mod = 1.0 / (double) getSeverity().ordinal();
        originalMaxWill = Global.getPlayer()
                                .getWillpower()
                                .max();
        Global.getPlayer()
              .getWillpower()
              .setTemporaryMax((int) (originalMaxWill * mod));
        return Optional.empty();
    }

    @Override
    public void endNight() {
        super.endNight();
        Global.getPlayer()
              .getWillpower()
              .setTemporaryMax(originalMaxWill);
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "You feel empty without " + cause.name() + "'s milk flowing down your throat. You need more!";
            case LOW:
                return "You feel a strange yearning for more of " + cause.name() + "'s milk.";
            case MED:
                return "You know it's bad for you, but you really want more of that milk.";
            case NONE:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "The desire for " + cause.name() + "'s milk has calmed down somewhat.";
            case MED:
                return "You still know you're addicted to " + cause.name() + "'s milk, but you"
                                + " can control yourself a little better now.";
            case NONE:
                return "Finally, you feel the last remnants of the unnatural thirst for milk leave you.";
            case HIGH:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "<b>You haven't had any of " + cause.name() + "'s milk today, and the thirst threatens"
                                + " to overwhelm you. You won't last long in fights tonight...</b>";
            case LOW:
                return "<b>You feel a little uneasy going without " + cause.name() + "'s milk for a whole day,"
                                + " and it's distracting you to the point where it lowers your willpower!</b>";
            case MED:
                return "<b>The thirst for " + cause.name() + "'s milk burns within you, "
                                + "scorching away such useless things as a strong will.</b>";
            case NONE:
                throw new IllegalStateException("Tried to describe withdrawal for an inactive milk addiction.");
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeCombatIncrease() {
        return "The swaying of " + cause.name() + "'s breasts causes "
                        + "you to remember vividly the taste of " + cause.directObject() + " milk. You know you want more.";
    }

    @Override
    protected String describeCombatDecrease() {
        return "Having drank some of " + cause.name() + "'s sweet nectar, the thirst fades into the background. A part"
                        + " of you is already looking forward to more, though.";
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "The burning thirst wells up at the sight of " + cause.name() + ". It would be so easy to subdue,"
                        + " just a little sip...";
        }
        return "\"Milk\" is the first thing you think of when you see " + cause.name() + ". "
                        + "You won't be able to ignore your urges for long...";
    }

    @Override
    public String describe(Combat c) {
        switch (getCombatSeverity()) {
            case HIGH:
                return "You are desperate for more milk and can't even think of resisting " + cause.directObject() + ".";
            case LOW:
                return "You are distracted by the lingering sweetness of " + cause.name() + "'s milk, "
                                + "and it's sapping your will to resist.";
            case MED:
                return "You thirst for more of " + cause.name() + "'s milk and are struggling to keep your mind in the game.";
            case NONE:
            default:
                return "";

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
        return (int) (x * (combatAtLeast(Severity.MED) ? combatAtLeast(Severity.HIGH) ? 1.5 : 1.25 : 1));
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
        return new MagicMilkAddiction(newOther, magnitude);
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new MagicMilkAddiction(Global.getCharacterByType(obj.get("cause").getAsString()),
                        (float) obj.get("magnitude").getAsInt());
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof MagicMilkAddiction;
        magnitude = 0;
    }

    @Override
    public String getVariant() {
        return "Addiction";
    }

    @Override
    public float fitnessModifier() {
        return -combatMagnitude;
    }

    @Override
    public void tick(Combat c) {
        super.tick(c);
        if (combatAtLeast(Severity.LOW)) {
            int loss;
            switch (getCombatSeverity()) {
                case LOW:
                    loss = Global.random(1, 3);
                    break;
                case MED:
                    loss = Global.random(3, 6);
                    break;
                case HIGH:
                    loss = Global.random(5, 8);
                    break;
                default:
                    throw new IllegalStateException();
            }
            Global.getPlayer()
                  .loseWillpower(c, loss, 0, false, " due to your Milk Addiction");
        }
    }

    @Override
    public AddictionType getType() {
        return AddictionType.MAGIC_MILK;
    }

    @Override
    public String describeMorning() {
        switch (getSeverity()) {
            case HIGH:
                aggravate(MED_INCREASE);
                flagDaytime();
                return "You wake up in the morning with a burning need for " + cause.name() + "'s milk. The thought of resisting the urge doesn't even enter your mind. You quickly whip out your cellphone and dial " + cause.name() + "'s number. "
                                + "Moments later, an amused voice answers. You sob into the phone, begging for " + cause.name() + "'s milk. Luckily, " + cause.name() + " doesn't seem to want to tease you today, and readily agrees to drop by. "
                                + "Fifteen agonizing minutes later, the doorbell rings and you rush to answer. Giving you a quick and dirty kiss at the door way, " + cause.name() + " enters your room and sits down on your bed. "
                                + Global.capitalizeFirstLetter(cause.pronoun()) + " pats " + cause.directObject() + " lap and motions for you to strip and lie down. You quickly comply and lay in " + cause.directObject() + " lap facing the ceiling, giddy for more milk. <br>"
                                + "With a coying grin, " + cause.name() + " strips off " + cause.directObject() + " top and lets " + cause.directObject() + " bountiful breasts bounce free of " + cause.directObject() + " bra. Your eyes immediately zeroes into " + cause.directObject() + " nipples, already dripping with opalescent white fluids. "
                                + cause.name() + " lowers " + cause.directObject() + " breasts into your face, and you happily start drinking " + cause.directObject() + " mindbending milk. Seconds turn into minutes and minutes turn into hours. "
                                + "You don't know how long your were nursing at " + cause.directObject() + " teats, but you seemed to have dozed off in the middle of it. You find yourself on the bed by yourself, with a blanket covering you. "
                                + cause.name() + " has already left, but left a note on the kitchen table, <br><i>Hey hun, unfortunately I have to get to class. I made you some lunch that I put in the fridge, and left you a bottle of milk in case the cravings come back. I'll see you tonight at the games okay? Love you baby.</i><br><br>";
            case LOW:
                return "You wake up in the morning with damp underwear. You realize that you've been dreaming of " + cause.name() + "'s milk the entire night. This can't be healthy... <br>"
                                + "You want to immediately head over to " + cause.name() + "'s and ask for another helping, but quickly realize that will just feed the addiction. "
                                + "However, at this rate, you will be thinking of " + cause.directObject() + " the entire day, and affect your willpower. You will have to make a decision to tough it out or call " + cause.directObject() + " up and ask for more.<br><br>";
            case MED:
                return "When you wake up in the morning, the first thing you think of is " + cause.name() + "'s breasts. And the second. And the third. In fact, you realize that's all you can think of right now. "
                                + "You sigh and attempt to take a cold shower to tear your mind from " + cause.directObject() + " sinfully sweet milk. Unfortunately, it does you little good. You will have to make a choice between toughing it out, or caving and calling " + cause.name() + " for a helping of " + cause.directObject() + " addictive cream.<br><br>";
            case NONE:
            default:
                return "You wake up in the morning with your throat feeling strangely parched. You step into the kitchen and take out a carton of milk to attempt to slake your thirst. "
                                + "Five minutes and a empty carton later, you still don't feel much better. You decide to ignore it and head to class.<br><br>";

        }
    }

    @Override
    public String informantsOverview() {
        return "You let " + cause.getName() + "'s milk get to you? I know those new and improved"
                        + " boobs are great to look at, but couldn't you be a bit more careful?"
                        + " Now I'm not at liberty to tell you how I know this, but you'll have noticed that "
                        + "you're likely to find yourself unwilling to keep fighting if you allow this thing"
                        + " to grow too bad. At first it'll only be when directly confronted with " + cause.directObject() + " directly,"
                        + " but if you don't drink for long enough, well, you won't do very well. That said, it"
                        + " does at least mean you won't be affected by any weaker addictive substances, so you've"
                        + " got that going for you.";
    }

}
