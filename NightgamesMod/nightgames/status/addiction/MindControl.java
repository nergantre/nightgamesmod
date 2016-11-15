package nightgames.status.addiction;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Anal;
import nightgames.stance.AnalCowgirl;
import nightgames.stance.Position;
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
        return Optional.of(new MindControlWithdrawal());
    }

    @Override
    protected Optional<Status> addictionEffects() {
        affected.add(Trait.mindcontrolresistance);
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return cause.name() + " has you completely in " + cause.directObject() + " grasp. Your body moves "
                        + "automatically to obey " + cause.directObject() + " commands, now.";
            case LOW:
                return "You feel a tug on your mind every time " + cause.name() + " speaks, pushing you to do as "
                        + cause.pronoun() + " says.";
            case MED:
                return "You find your body moving to " + cause.name() + "'s words without any input from your mind.";
            case NONE:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return cause.name() + "'s control is weakening, and only " + cause.directObject() + " strongest commands"
                        + " have a noticable effect.";
            case MED:
                return "You feel as if " + cause.name() + "'s words do not bury themselves as deeply into your psyche as before."
                        + " Can you resist " + cause.directObject() + "?";
            case NONE:
                return "At last that invisible string tying you to " + cause.name() + " snaps, and you are back in control"
                        + " of your mind.";
            case HIGH:
            default:
                return ""; // hide
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "<b>You are now constantly fighting your own body to keep from doing " + cause.name() + "'s will.</b>";
            case LOW:
                return "<b>Your body tries to steer you towards " + cause.name() + " all the time, and it's taking"
                        + " serious effort to resist.</b>";
            case MED:
                return "<b>Keeping your body in line and away from " + cause.name() + " is getting really difficult know,"
                        + " and it's a severe strain on your stamina.</b>";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeCombatIncrease() {
        return cause.name() + "'s words weigh increasingly heavily on you, and it's getting harder to resist.";
    }

    @Override
    protected String describeCombatDecrease() {
        return "Doing " + cause.name() + "'s bidding relieves some of the pressure in your mind.";
    }

    @Override
    public String informantsOverview() {
        return "Oh, that is just nasty. You've got to hand it to " + cause.directObject() + ", though, " + cause.pronoun()
                + " got you good. It looks like " + cause.directObject() + " control somehow bypasses your mind and goes"
                + " straight to your motor functions. That's a special kind of mean, because you'll be entirely"
                + " conscious for the whole thing, not turned into some kind of willing slave. There's"
                + " two ways you can go about this: You can do what " + cause.pronoun() + " wants you to do, but on your"
                + " terms, or you can try to defy " + cause.directObject() + " as long as you can and beat "
                + cause.directObject() + " quickly. If you play along, by laying down or whacking off or something, then"
                + " that will obviously be bad for you but it would also mean you stay more or less in control of it all."
                + " If you fight " + cause.directObject() + " control, you'll be able to function normally for a while,"
                + " but you will eventually break. When you do, " + cause.pronoun() + "'ll have total control until you"
                + " recover, which would be far worse. Resisting " + cause.directObject() + " commands will take"
                + " some serious effort, so it would probably leave you quite tired. So my advice is: don't cum inside"
                + " of " + cause.directObject() + " again while " + cause.pronoun() + " can look you in the eyes. It's that simple.";
    }

    @Override
    public String describeMorning() {
        return "Your hand shoots to your hardening dick as soon as you wake up. You have know idea how,"
                + " but you somehow know it's what " + cause.name() + " wants you to do, and your body is responding"
                + " accordingly. You force your hand to your side and awkwardly get dressed. Whenever you're"
                + " not paying attention, it shoots back and rubs your crotch again, though. Perhaps you"
                + " can persuade " + cause.name() + " to go a little easier on you? Then again, maybe not.";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.MIND_CONTROL;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "There " + cause.pronoun() + " is! " + cause.name() + " does not look pleased after you haven't visited "
                    + cause.directObject() + " all day.";
        }
        return "Your breathing accelerates when you see " + cause.name() + "; you know what power " + cause.pronoun()
                + " has over you...";
    }

    @Override
    public String describe(Combat c) {
        switch (getCombatSeverity()) {
            case HIGH:
                return "Every word " + cause.name() + " speaks rings of truth to you, even though " + cause.pronoun() + "'s"
                        + " telling you to submit to " + cause.directObject() + ". Your body trembles, and you will soon"
                        + " be forced to obey.";
            case LOW:
                return cause.name() + " keeps saying things for you to do, and you don't know how"
                                + " long you'll be able to resist " + cause.directObject() + ".";
            case MED:
                return cause.name() + "'s words are starting to have a greater pull on you. You won't hold out much longer.";
            case NONE:
            default:
                return "";

        }
    }

    @Override
    public void tick(Combat c) {
        super.tick(c);
        if (!affected.is(Stsflag.enthralled) && Global.randomdouble() < magnitude / 3) {
            affected.addlist.add(new Enthralled(affected, cause, 3));
            c.write(cause, cause.name() + "'s constant urging overcomes your defences, washing away all of your resistance.");
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

    @Override public Status loadFromJson(JsonObject obj) {
        return new MindControl(Global.getCharacterByType(obj.get("cause").getAsString()),
                        obj.get("magnitude").getAsInt());
    }

    public static class Result {
        private boolean succeeded;
        private String description;

        public Result(Character controller, Position pos) {
            if (Global.getPlayer()
                      .is(Stsflag.blinded)) {
                succeeded = false;
                description = "Since you can't see, you are protected from " + controller.name() + "'s controlling gaze.";
            } else
                switch (pos.en) {
                    case cowgirl:
                        succeeded = true;
                        description = "You turn your head away as you feel your orgasm coming on, wary of " + controller.name() + "'s"
                                        + " hypnotic eyes. " + Global.capitalizeFirstLetter(controller.pronoun()) + "'s"
                                        + " not having it, though. " + controller.pronoun() + " grabs your head"
                                        + " and forces your eyelids open with " + controller.directObject()+ " thumbs. ";
                        break;
                    case anal:
                        if (pos instanceof AnalCowgirl) {
                            succeeded = true;
                            description = "You turn your head away as you feel your orgasm coming on, wary of " + controller.name() + "'s"
                                            + " hypnotic eyes. " + Global.capitalizeFirstLetter(controller.pronoun()) + "'s"
                                            + " not having it, though. " + controller.pronoun() + " grabs your head"
                                            + " and forces your eyelids open with " + controller.directObject() + " thumbs. ";
                            break;
                        } else if (pos instanceof Anal) {
                            succeeded = false;
                            description = "Since you're not facing " + controller.name() + ", " + controller.directObject()
                                            + " hypnotic eyes cannot affect you.";
                            break;
                        }
                        // Fall-through intentional -- AnalProne
                    case mount:
                    case missionary:
                    case flying:
                        succeeded = true;
                        if (pos.dom(controller)) {
                            description = "You turn your head away as you feel your orgasm coming on, wary of " + controller.name() + "'s"
                                            + " hypnotic eyes. " + Global.capitalizeFirstLetter(controller.pronoun()) + "'s"
                                            + " not having it, though. " + controller.pronoun() + " twists your head back"
                                            + " and forces your eyelids open with " + controller.directObject() + " thumbs. ";
                        } else {
                            description = "At the moment of your orgasm, " + controller.name() + " pulls herself up by"
                                            + " your neck and touches " + controller.directObject() + " nose to yours."
                                            + " So close to cumming, you can't bring yourself to look away. ";
                        }
                        break;
                    case pin:
                        if (pos.dom(controller)) {
                            succeeded = true;
                            description = "You turn your head away as you feel your orgasm coming on, wary of " + controller.name() + "'s"
                                            + " hypnotic eyes. " + Global.capitalizeFirstLetter(controller.pronoun()) + "'s"
                                            + " not having it, though. " + controller.pronoun() + " grabs your head"
                                            + " and forces your eyelids open with " + controller.directObject() + " thumbs. ";
                        } else {
                            succeeded = false;
                            description = "With " + controller.name() + " pinned beneath you as " + controller.pronoun()
                                            + " is, it's not hard for you to keep your eyes from meeting hers as you "
                                            + "launch into your orgasm.";
                        }
                        break;
                    case oralpin:
                        if (pos.dom(controller)) {
                            succeeded = true;
                            description = controller.name() + " grabs a fistful of your hair and pulls your head downwards. There is"
                                            + " nothing you can do to evade " + controller.directObject() + " hypnotic"
                                            + " gaze as you erupt into " + controller.directObject() + " sucking mouth. ";
                        } else {
                            // probably extremely rare
                            succeeded = false;
                            description = "You close your eyes to make certain you're not going to be affected by "
                                            + controller.name() + "'s eyes as you cum.";
                        }
                        break;
                    case neutral:
                    case standingover:
                        if (Global.getPlayer()
                                  .canAct()) {
                            succeeded = false;
                            description = "With the freedom of movement you have at the moment, turning your gaze away"
                                            + " from " + controller.name() + "'s hypnotic eyes is quite easy even with "
                                            + "your impending orgasm.";
                        } else {
                            succeeded = true;
                            description = "Immobilized as you are, you can't keep " + controller.name() + " from gazing "
                                            + "deeply into your eyes as your orgasm begins to wash over you.";
                        }
                        break;
                    default:
                        if (pos.facing(Global.getPlayer(), controller)) {
                            succeeded = true;
                            description = controller.name() + " gazes into your eyes as " + controller.pronoun() 
                                            + " pushes you over the edge. ";
                        } else {
                            succeeded = false;
                            description = "Since you're not facing " + controller.name() + ", "
                                            + controller.directObject() + " hypnotic eyes cannot affect you.";
                        }
                }
        }

        public boolean hasSucceeded() {
            return succeeded;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Result [succeeded=" + succeeded + ", description=" + description + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((description == null) ? 0 : description.hashCode());
            result = prime * result + (succeeded ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Result other = (Result) obj;
            if (description == null) {
                if (other.description != null)
                    return false;
            } else if (!description.equals(other.description))
                return false;
            if (succeeded != other.succeeded)
                return false;
            return true;
        }
    }

    public class MindControlWithdrawal extends Status {

        public MindControlWithdrawal() {
            super("Mind Control Withdrawal", Global.getPlayer());
        }

        @Override
        public void tick(Combat c) {
            if (affected.getStamina()
                        .percent() > 5) {
                int amt = getSeverity().ordinal() * (Global.random(6) + 1);
                affected.weaken(c, (int) cause.modifyDamage(DamageType.temptation, affected, amt));
                c.write(affected, "You keep fighting your own body to do as you want, and it's tiring you rapidly.");
            }
        }

        @Override
        public String initialMessage(Combat c, boolean replaced) {
            return ""; // handled by withdrawal message
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
            return new MindControlWithdrawal();
        }

        @Override public JsonObject saveToJson() {
            return null;
        }

        @Override public Status loadFromJson(JsonObject obj) {
            return null;
        }

    }
}
