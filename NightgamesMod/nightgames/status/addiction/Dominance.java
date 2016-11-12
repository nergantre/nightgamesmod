package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Masochistic;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Dominance extends Addiction {

    private int originalWill;

    public Dominance(Character cause, float magnitude) {
        super("Dominance", cause, magnitude);
        flags.add(Stsflag.victimComplex);
        originalWill = -1;
    }

    public Dominance(Character cause) {
        this(cause, .01f);
    }

    public static boolean mojoIsBlocked(Combat c) {
        if (c == null)
            return false;
        Player player = Global.getPlayer();
        Character opp = c.getOpponent(player);
        if (!Global.getPlayer()
                   .checkAddiction(AddictionType.DOMINANCE, opp))
            return false;
        int sev = player.getAddictionSeverity(AddictionType.DOMINANCE)
                        .ordinal();
        int dom = c.getDominanceOfStance(opp);

        return sev >= 5 - dom;
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
        if (originalWill < 0) {
            double mod = Math.min(1.0, 1.0 / (double) getSeverity().ordinal() + .4);
            originalWill = Global.getPlayer()
                                    .getWillpower()
                                    .max();
            Global.getPlayer()
                  .getWillpower()
                  .setTemporaryMax((int) (originalWill * mod));
        }
        return Optional.of(new Masochistic(affected));
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    public void endNight() {
        super.endNight();

        Global.getPlayer()
              .getWillpower()
              .setTemporaryMax(originalWill);
        originalWill = -1;
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "Held down by " + cause.name() + ", you feel completely powerless to resist.";
            case LOW:
                return "You feel strangely weak in " + cause.name() + "'s powerful hold.";
            case MED:
                return "Something about the way " + cause.name() + " is holding on to you is causing your strength to seep away.";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "More and more of your strength is returning since escaping from " + cause.name() + ". ";
            case MED:
                return "You find some of the strange weakness caused by " + cause.name() + "'s powerful hold"
                                + " fleeing your bones. ";
            case NONE:
                return "You have completely recovered from " + cause.name() + "'s hold. ";
            case HIGH:
            default:
                return "";
        }
    }

    @Override
    protected String describeWithdrawal() {
        return "Your body longs for the exquisite pain and submission " + cause.name() + " can bring you,"
                        + " reducing your stamina and causing masochisitc tendencies.";
    }

    @Override
    protected String describeCombatIncrease() {
        return "Being hurt so well just makes you want to submit even more.";
    }

    @Override
    protected String describeCombatDecrease() {
        return "Some of the submissiveness clears from your mind, allowing you to focus" + " more on the fight.";
    }

    @Override
    public String informantsOverview() {
        return "<i>\"Is that all? With all the weird shit going on around here, you're worried about a submissive"
                        + " streak? Well, sure, I can see how it would be a problem. Being held down does not"
                        + " help your chances in a fight, and if you actually enjoy it you are not at all"
                        + " likely to win. Basically, if " + cause.pronoun() + " gets you down and tied up or something, you're going"
                        + " to lose, because you subconciously don't actually want to win.\"</i> That does sound"
                        + " pretty bad... Any upsides? <i>\"Well, I suppose that being on the receiving end of such"
                        + " a powerful dominance, the stuff other people do won't make as much of an impression."
                        + " Personally, I wouldn't go for it, but if you like getting hurt and humiliated, go right"
                        + " ahead.\"";
    }

    @Override
    public String describeMorning() {
        return "";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.DOMINANCE;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return cause.name() + " is looking meaner than ever after you neglected to visit today. Equal"
                            + " parts of fear and desire well up inside of you at the thought of what "
                            + cause.pronoun() + " might do to you.";
        }
        return "You are conflicted at the sight of " + cause.name() + ". One part of you still remembers"
                        + " the pain and humiliation " + cause.pronoun() + " can cause and"
                        + " is terrified because of it, the other part is getting excited"
                        + " for the very same reason.";
    }

    @Override
    public String describe(Combat c) {
        return "";
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
        return new Dominance(newAffected, magnitude);
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new Dominance(Global.getCharacterByType(obj.get("cause")
                                                          .getAsString()),
                        (float) obj.get("magnitude")
                                   .getAsInt());
    }

}
