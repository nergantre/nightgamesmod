package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.FootWorship;
import nightgames.skills.Masturbate;
import nightgames.stance.Engulfed;
import nightgames.stance.Kneeling;

public class Parasited extends Status {
    private Character other;
    private double time;
    private int stage;

    public Parasited(Character affected, Character other) {
        super("parasited", affected);
        this.other = other;
        this.stage = 0;
        this.time = 0;
        flag(Stsflag.parasited);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return Global.format(
                        "{other:SUBJECT-ACTION:have|has} planted a part of {other:reflective} in {self:name-possessive} head!\n", affected, other);
    }

    @Override
    public String describe(Combat c) {
        return "You have a part of " + other.nameOrPossessivePronoun() + " inside your head"; 
    }

    @Override
    public float fitnessModifier() {
        return -10;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (time >= 3) {
            if (stage < 3) {
                stage = 3;
                Global.gui().message(c, other,
                                Global.format("Suddenly, you hear a disembodied but familiar voice. \"Testing... testing... Ah good, looks like it has worked.\"",
                                affected, other));
                Global.gui().message(c, affected,
                                Global.format("You... seem to be hearing {other:name-possessive} voice inside your head. That's not good.",
                                affected, other));
                Global.gui().message(c, other,
                                Global.format("{other:NAME} gives you a satisfied smile and her disembodied voice echoes again inside your head, \"{self:NAME}, don't worry... I have connected myself with your brain... We will have so much fun together...\"",
                                affected, other));
                
            }
            switch(Global.random(8)) {
                case 0:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will cum for me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("With absolutely no warning, you feel an incredible orgasm rip through your body.",
                                    affected, other));
                    BodyPart part = Global.pickRandom(c.getStance().partsFor(affected)).orElse(affected.body.getRandomGenital());
                    BodyPart otherPart = Global.pickRandom(c.getStance().partsFor(other)).orElse(other.body.getRandom("skin"));
                    affected.doOrgasm(c, other, part, otherPart);
                    break;
                case 1:
                    Global.gui().message(c, other,
                                    Global.format("\"...Give yourself to me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("With no input from your consciousness, your body mechanically walks up to {self:name-possessive} body and presses itself into her slime. While immobilized by your inability to send signals through your locomotive nerves, your body slowly sinks into her crystal blue body.",
                                    affected, other));
                    c.setStance(new Engulfed(other, affected));
                    break;
                case 2:
                case 3:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will please me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("You feel an immense need to service {self:NAME}!",
                                    affected, other));
                    c.getRandomWorshipSkill(affected, other).orElse(new FootWorship(affected)).resolve(c, other);
                    break;
                case 4:
                case 5:
                    if (!c.getStance().dom(affected) && !c.getStance().prone(affected)) {
                        Global.gui().message(c, other,
                                        Global.format("\"...You will kneel for me...\"",
                                        affected, other));
                        c.setStance(new Kneeling(other, affected));
                        break;
                    }
                case 6:
                case 7:
                default:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will pleasure yourself...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("Your hands involunarily reach into your crotch and start masturbating!",
                                    affected, other));
                    (new Masturbate(affected)).resolve(c, other);
            }
            affected.add(c, new Frenzied(affected, 1000));
        } else if (time >= 2) {
            if (stage < 2) {
                stage = 2;
                if (affected.human() || other.human())
                Global.gui().message(c, affected,
                                Global.format("The parasite inside you starts moving again. After a long journey, it has somehow reached inside your skull. Even though that part of your body should have no nerves, you swear you can feel its cold pseudopods integrating itself with your brain.",
                                affected, other));
            }
            if (affected.human() || other.human())
                Global.gui().message(c, affected,
                                Global.format("{self:NAME-POSSESSIVE} thoughts slow down even further. It's becoming difficult to remember why you are even fighting in the first place.",
                                                affected, other));
            affected.loseWillpower(c, 2);
        } else if (time >= 1) {
            if (stage < 1) {
                stage = 1;
                if (affected.human() || other.human())
                Global.gui().message(c, affected,
                                Global.format("The slimey parasite inside you starts moving again. You can feel it crawling through your head.",
                                                affected, other));
            }
            if (affected.human() || other.human())
            Global.gui().message(c, affected,
                            Global.format("{self:NAME-POSSESSIVE} thoughts slow down. Somehow the parasite is sapping your will to fight.",
                                            affected, other));
            affected.loseWillpower(c, 1);
        } else {
            if (affected.human() || other.human())
            Global.gui().message(c, affected, Global.format("A part of {other:name-possessive} slime is lodged inside your head. It doesn't feel uncomfortable, but you are scared of the implications.",
                            affected, other));
        }

        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
        time += .25;
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
        return -5;
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

    public String toString() {
        return "Parasited";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Parasited(newAffected, newOther);
    }

    @SuppressWarnings("unchecked")
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        return obj;
    }

    public Status loadFromJSON(JSONObject obj) {
        return new Parasited(null, null);
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
