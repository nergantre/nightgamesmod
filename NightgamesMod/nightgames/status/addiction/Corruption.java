package nightgames.status.addiction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.status.Abuff;
import nightgames.status.DarkChaos;
import nightgames.status.Status;

public class Corruption extends Addiction {

    public Corruption(Character cause, float magnitude) {
        super("Corruption", cause, magnitude);
    }

    public Corruption(Character cause) {
        this(cause, .01f);
    }

    @Override
    public void tick(Combat c) {
        aggravateCombat(Addiction.LOW_INCREASE);
        Severity sev = getCombatSeverity();
        int amt = sev.ordinal() * 2;
        List<Abuff> buffs = new ArrayList<>();
        if (noMoreAttrs()) {
            if (sev != Severity.HIGH) {
                c.write(affected,
                                "The corruption is churning within you, but it seems that it's done all it can for now.");
            } else if (!affected.body.has("tail") || affected.body.getRandom("tail") != TailPart.demonic) {
                c.write(affected, "<b>The dark taint changes you even further, and a spade-tipped tail bursts out of your"
                                + " lower back!</b>");
                affected.body.temporaryAddOrReplacePartWithType(TailPart.demonic, 20);
            } else if (!affected.body.has("wings") || affected.body.getRandom("wings") != WingsPart.demonic) {
                c.write(affected,
                                "<b>The dark taint changes you even further, and a set of black bat wings grows from your back!</b>");
                affected.body.temporaryAddOrReplacePartWithType(WingsPart.demonic, 20);
            } else if (affected.hasPussy() && affected.body.getRandomPussy() != PussyPart.succubus) {
                c.write(affected,
                                "<b>The dark taint changes you even further, and your pussy turns into that of a succubus!</b>");
                affected.body.temporaryAddOrReplacePartWithType(PussyPart.succubus, 20);
            } else if (affected.hasDick() && affected.body.getRandomCock()
                                                          .getMod() != CockMod.incubus) {
                c.write(affected,
                                "<b>The dark taint changes you even further, and your cock turns into that of an incubus!</b>");
                CockPart cock = affected.body.getRandomCock();
                BasicCockPart base;
                if (cock instanceof BasicCockPart) {
                    base = (BasicCockPart) cock;
                } else {
                    base = ((ModdedCockPart) cock).getBase();
                }
                affected.body.temporaryAddOrReplacePartWithType(new ModdedCockPart(base, CockMod.incubus), 20);
            } else if (!affected.hasPussy() && cause.hasDick()) {
                c.write(affected,
                                "<b>The dark taint changes you even further, and a succubus's pussy forms between your legs!</b>");
                affected.body.temporaryAddOrReplacePartWithType(PussyPart.succubus, 20);
            } else if (!affected.hasDick()) {
                c.write(affected,
                                "<b>The dark taint changes you even further, and an incubus's cock forms between your legs!</b>");
                affected.body.temporaryAddOrReplacePartWithType(new ModdedCockPart(BasicCockPart.huge, CockMod.incubus),
                                20);
            } else {
                c.write(affected,
                                "The corruption is churning within you, but it seems that it's done all it can for now.");
            }
        } else {
            for (int i = 0; i < amt; i++) {
                Attribute att;
                do {
                    att = getDrainAttr();
                } while (att != null && (att == Attribute.Dark || affected.get(att) < 10));
                if (noMoreAttrs())
                    break;
                buffs.add(new Abuff(affected, att, -1, 20));
                buffs.add(new Abuff(affected, Attribute.Dark, 1, 20));
            }
            switch (sev) {
                case HIGH:
                    c.write(affected, "The corruption is rampaging through your soul, rapidly demonizing you.");
                    break;
                case LOW:
                    c.write(affected, "The corruption inside of you is slowly changing your mind...");
                    break;
                case MED:
                    c.write(affected,
                                    "The corruption is rapidly subverting your skills, putting them to a darker use...");
                    break;
                case NONE:
                    assert buffs.isEmpty();
                default:
            }
            buffs.forEach(b -> affected.addlist.add(b));
        }
    }

    private boolean noMoreAttrs() {
        return getDrainAttr() == null;
    }
    
    private Attribute getDrainAttr() {
        return Global.pickRandom(Arrays.stream(Attribute.values()).filter(a -> a != Attribute.Dark && affected.get(a) >= 10).toArray(Attribute[]::new));
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
       return Optional.of(new DarkChaos());
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                return "Reyka's blackness threatens to overwhelm what purity "
                                + "remains inside of you, and it's a constant presence in your mind.";
            case LOW:
                return "The blackness Reyka poured into you is still "
                                + "there, and it feels like it's alive somehow; a churning mass of corruption and depravity.";
            case MED:
                return "The corruption in your soul spreads further, seeping into your flesh and bones.";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case LOW:
                return "The corruption in your soul is backing off, but "
                                + "there is work to be done yet if you are to be entirely free of it. ";
            case MED:
                return "Whatever it was exactly that Reyka created in you "
                                + "has weakened considerably and is no longer corrupting your every thought. ";
            case NONE:
                return "The last of the infernal corruption is purified "
                                + "from your soul, bringing you back to normal. Well, as normal as you're ever going to be, anyway. ";
            case HIGH:
            default:
                return "";
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "Reyka's corruption is working hard to punish you "
                                + "for not feeding it today, and it will cause all kinds of trouble tonight. ";
            case LOW:
                return "Something is not quite right. The blackness Reyka "
                                + "put in you is stirring, causing all kinds of strange sensations. Perhaps it's hungry? ";
            case MED:
                return "The powerful corruption within you is rebelling"
                                + " against not being fed today. Expect the unexpected tonight.";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeCombatIncrease() {
        return ""; // Combat messages handled in tick
    }

    @Override
    protected String describeCombatDecrease() {
        return ""; // Combat messages handled in tick
    }

    @Override
    public String describeMorning() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.CORRUPTION;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (inWithdrawal) {
            return "The blackness resonates with Reyka, growing even more powerful and troublesome than before.";
        }
        return "The blackness Rayka places in you resonates with her. You can"
                        + " feel it starting to corrupt your mind and body!";
    }

    @Override
    public String describe(Combat c) {
        return ""; //handled in tick
    }

    @Override
    public int mod(Attribute a) {
        return a == Attribute.Dark ? 5 : 0;
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
        return new Corruption(newOther, magnitude);
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new Corruption(Global.getCharacterByType(JSONUtils.readString(obj, "cause")),
                        (float) JSONUtils.readInteger(obj, "magnitude"));
    }

    @Override
    public String informantsOverview() {
        return "Dude. Not cool. I like Reyka shaking her evil ass around at night as much"
                        + " as the next guy, but the evil should stay there, you know? Now, the"
                        + " rest of the competitors will not appreciate your new attitude either."
                        + " I don't see them jumping to your defence any time soon. You should also"
                        + " worry about this thing inside of you taking over the uncorrupted parts of"
                        + " your mind. Also, I would imagine that that evil part of you won't appreciate"
                        + " any efforts to get rid of it. Who knows what chaos it might cause? Of course,"
                        + " if it's the Dark skills you're interested in, then it's probably a good thing."
                        + " But you're not like that, are you?";
    }

}
