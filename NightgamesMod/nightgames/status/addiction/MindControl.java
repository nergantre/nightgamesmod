package nightgames.status.addiction;

import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.stance.Anal;
import nightgames.stance.AnalCowgirl;
import nightgames.stance.Position;
import nightgames.stance.Stance;
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
                return "Mara has you completely in her grasp. Your body moves automatically to obey her"
                                + " commands, now.";
            case LOW:
                return "You feel a tug on your mind every time Mara speaks, pushing you to do as she says.";
            case MED:
                return "You find your body moving to Mara's words without any input from your mind.";
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
        switch (getSeverity()) {
            case HIGH:
                return "<b>You are now constantly fighting your own body to keep from doing Mara's will.</b>";
            case LOW:
                return "<b>Your body tries to steer you towards Mara all the time, and it's taking serious effort"
                                + " to resist.</b>";
            case MED:
                return "<b>Keeping your body in line and away from Mara is getting really difficult know, and it's a"
                                + " severe strain on your stamina.</b>";
            case NONE:
            default:
                return "";
        }
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
        return "Oh, that is just nasty. You've got to hand it to her, though, she got you good."
                        + " It looks like her control somehow bypasses your mind and goes straight to"
                        + " your motor functions. That's a special kind of mean, because you'll be entirely"
                        + " conscious for the whole thing, not turned into some kind of willing slave. There's"
                        + " two ways you can go about this: You can do what she wants you to do, but on your"
                        + " terms, or you can try to defy her as long as you can and beat her quickly. If you"
                        + " play along, by laying down or whacking off or something, then that will obviously be"
                        + " bad for you but it would also mean you stay more or less in control of it all. If you"
                        + " fight her control, you'll be able to function normally for a while, but you will eventually"
                        + " break. When you do, she'll have total control until you recover, which would be far worse."
                        + " Resisting her commands will take some serious effort, so it would probably leave you"
                        + " quite tired. So my advice is: don't cum inside of her again while she can look you in the"
                        + " eyes. It's that simple.";
    }

    @Override
    public String describeMorning() {
        return "Your hand shoots to your hardening dick as soon as you wake up. You have know idea how,"
                        + " but you somehow know it's what Mara wants you to do, and your body is responding"
                        + " accordingly. You force your hand to your side and awkwardly get dressed. Whenever you're"
                        + " not paying attention, it shoots back and rubs your crotch again, though. Perhaps you"
                        + " can persuade Mara to go a little easier on you? Then again, maybe not.";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.MIND_CONTROL;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "There she is! Mara does not look pleased after you haven't visited her all day.";
        }
        return "Your breathing accelerates when you see Mara; you know what power she has over you...";
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
                return "Mara's words are starting to have a greater pull on you. You won't hold out" + " much longer.";
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

    public static class Result {
        private boolean succeeded;
        private String description;

        public Result(Character controller, Position pos) {
            if (Global.getPlayer()
                      .is(Stsflag.blinded)) {
                succeeded = false;
                description = "Since you can't see, you are protected from Mara's controlling gaze.";
            } else
                switch (pos.en) {
                    case cowgirl:
                        succeeded = true;
                        description = "You turn your head away as you feel your orgasm coming on, wary of Mara's"
                                        + " hypnotic eyes. She's not having it, though. She grabs you head and forces"
                                        + " and forces your eyelids open with her thumbs. ";
                        break;
                    case anal:
                        if (pos instanceof AnalCowgirl) {
                            succeeded = true;
                            description = "You turn your head away as you feel your orgasm coming on, wary of Mara's"
                                            + " hypnotic eyes. She's not having it, though. She grabs you head and forces"
                                            + " and forces your eyelids open with her thumbs. ";
                            break;
                        } else if (pos instanceof Anal) {
                            succeeded = false;
                            description = "Since you're not facing Mara, her hypnotic eyes cannot affect you.";
                        }
                        // Fall-through intentional -- AnalProne
                    case mount:
                    case missionary:
                    case flying:
                        succeeded = true;
                        if (pos.dom(controller)) {
                            description = "You turn your head away as you feel your orgasm coming on, wary of Mara's"
                                            + " hypnotic eyes. She's not having it, though. She twists you head back"
                                            + " and forces your eyelids open with her thumbs. ";
                        } else {
                            description = "At the moment of your orgasm, Mara pulls herself up by your neck and touches"
                                            + " her nose to yours. So close to cumming, you can't bring yourself to look away. ";
                        }
                        break;
                    case pin:
                        if (pos.dom(controller)) {
                            succeeded = true;
                            description = "You turn your head away as you feel your orgasm coming on, wary of Mara's"
                                            + " hypnotic eyes. She's not having it, though. She grabs you head and forces"
                                            + " and forces your eyelids open with her thumbs. ";
                        } else {
                            succeeded = false;
                            description = "With Mara pinned beneath you as she is, it's not hard for you to keep your"
                                            + " eyes from meeting hers as you launch into your orgasm.";
                        }
                        break;
                    case oralpin:
                        if (pos.dom(controller)) {
                            succeeded = true;
                            description = "Mara grabs a fistful of your hair and pulls your head downwards. There is"
                                            + " nothing you can do to evade her hypnotic gaze as you erupt into her "
                                            + "sucking mouth. ";
                        } else {
                            // probably extremely rare
                            succeeded = false;
                            description = "You close your eyes to make certain you're not going to be affected by Mara's eyes"
                                            + " as you cum.";
                        }
                        break;
                    case neutral:
                    case standingover:
                        if (Global.getPlayer()
                                  .canAct()) {
                            succeeded = false;
                            description = "With the freedom of movement you have at the moment, turning your gaze away"
                                            + " from Mara's hypnotic eyes is quite easy even with your impending orgasm.";
                        } else {
                            succeeded = true;
                            description = "Immobilized as you are, you can't keep Mara from gazing deeply into your eyes"
                                            + " as your orgasm begins to wash over you.";
                        }
                        break;
                    default:
                        if (pos.facing()) {
                            succeeded = true;
                            description = "Mara gazes into your eyes as she pushes you over the edge. ";
                        } else {
                            succeeded = false;
                            description = "Since you're not facing Mara, her hypnotic eyes cannot affect you.";
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
                affected.weaken(c, amt);
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

        @Override
        public JSONObject saveToJSON() {
            return null;
        }

        @Override
        public Status loadFromJSON(JSONObject obj) {
            return null;
        }

    }
}
