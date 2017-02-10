package nightgames.status.addiction;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.body.mods.DemonicMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.Compulsion;
import nightgames.status.Converted;
import nightgames.status.DarkChaos;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Corruption extends Addiction {
    public Corruption(Character affected, Character cause, float magnitude) {
        super(affected, "Corruption", cause, magnitude);
    }

    public Corruption(Character affected, Character cause) {
        this(affected, cause, .01f);
    }

    @Override
    public void tick(Combat c) {
        super.tick(c);
        if (c == null && Global.random(100) < 66) {
            // if you aren't in combat, just apply corrupt 1/3rd of the time.
            return;
        }
        Severity sev = getCombatSeverity();
        int amt = sev.ordinal() * 2;
        if (cause.has(Trait.Subversion) && affected.is(Stsflag.charmed)) {
            amt *= 1.5;
        }
        Map<Attribute, Integer> buffs = new HashMap<>();
        if (noMoreAttrs() || (atLeast(Severity.MED) && Global.random(100) < 5)) {
            if (!atLeast(Severity.MED)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "The corruption is churning within {self:name-do}, but it seems that it's done all it can for now.", affected, cause));
            } else if (!affected.body.has("tail") || affected.body.getRandom("tail") != TailPart.demonic) {
                Global.writeIfCombat(c, affected, Global.format( "<b>The dark taint changes {self:name-do} even further, and a spade-tipped tail bursts out of {self:possessive}"
                                + " lower back!</b>", affected, cause));
                affected.body.temporaryAddOrReplacePartWithType(TailPart.demonic, Global.random(15, 40));
            } else if (!affected.body.has("wings") || affected.body.getRandom("wings") != WingsPart.demonic) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and a set of black bat wings grows from {self:possessive} back!</b>", affected, cause));
                affected.body.temporaryAddOrReplacePartWithType(WingsPart.demonic, Global.random(15, 40));
            } else if (affected.hasPussy() && !affected.body.getRandomPussy().moddedPartCountsAs(affected, DemonicMod.INSTANCE)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and {self:possessive} pussy turns into that of a succubus!</b>", affected, cause));
                affected.body.temporaryAddPartMod("pussy", DemonicMod.INSTANCE, Global.random(15, 40));
            } else if (affected.hasDick() && !affected.body.getRandomCock().moddedPartCountsAs(affected, CockMod.incubus)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and {self:possessive} cock turns into that of an incubus!</b>", affected, cause));
                affected.body.temporaryAddPartMod("cock", CockMod.incubus, Global.random(15, 40));
            } else if (!atLeast(Severity.HIGH)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "The corruption is churning within {self:name-do}, but it seems that it's done all it can for now.", affected, cause));
            } else if (!affected.hasPussy() && cause.hasDick()) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and a succubus's pussy forms between {self:possessive} legs!</b>", affected, cause));
                affected.body.temporaryAddOrReplacePartWithType(PussyPart.generic.applyMod(DemonicMod.INSTANCE), Global.random(15, 40));
            } else if (!affected.hasDick()) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and an incubus's cock forms between {self:possessive} legs!</b>", affected, cause));
                affected.body.temporaryAddOrReplacePartWithType(new CockPart().applyMod(new SizeMod(SizeMod.COCK_SIZE_BIG)).applyMod(CockMod.incubus), Global.random(15, 40));
            } else if (!affected.body.getRandomAss().moddedPartCountsAs(affected, DemonicMod.INSTANCE)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and {self:possessive} asshole darkens with corruption!</b>", affected, cause));
                affected.body.temporaryAddPartMod("ass", DemonicMod.INSTANCE, Global.random(15, 40));
            } else if (!affected.body.getRandom("mouth").moddedPartCountsAs(affected, DemonicMod.INSTANCE)) {
                Global.writeIfCombat(c, affected, Global.format(
                                "<b>The dark taint changes {self:name-do} even further, and {self:possessive} lush lips turns black!</b>", affected, cause));
                affected.body.temporaryAddPartMod("mouth", DemonicMod.INSTANCE, Global.random(15, 40));
            } else {
                Global.writeIfCombat(c, affected, Global.format(
                                "The corruption is churning within {self:name-do}, but it seems that it's done all it can for now.", affected, cause));
            }
        } else {
            for (int i = 0; i < amt; i++) {
                Optional<Attribute> att = getDrainAttr();
                if (!att.isPresent()) {
                    break;
                }
                buffs.compute(att.get(), (a, old) -> old == null ? 1 : old + 1);
            }
            switch (sev) {
                case HIGH:
                    Global.writeIfCombat(c, affected, Global.format( "The corruption is rampaging through {self:name-possessive} soul, rapidly demonizing {self:direct-object}.", affected, cause));
                    break;
                case MED:
                    Global.writeIfCombat(c, affected, Global.format(
                                    "The corruption is rapidly subverting {self:name-possessive} skills, putting them to a darker use...", affected, cause));
                    break;
                case LOW:
                    Global.writeIfCombat(c, affected, Global.format( "The corruption inside of {self:name-do} is slowly changing {self:possessive} mind...", affected, cause));
                    break;
                case NONE:
                    assert buffs.isEmpty();
                default:
            }
            buffs.forEach((att, b) -> affected.add(c, new Converted(affected, Attribute.Dark, att, b, 20)));
        }
        if (c != null && cause.has(Trait.InfernalAllegiance) && !affected.is(Stsflag.compelled) && shouldCompel() && c.getOpponent(affected).equals(cause)) {
            Global.writeIfCombat(c, affected, Global.format( "A wave of obedience radiates out from the dark essence within {self:name-do}, constraining"
                            + " {self:possessive} free will. It will make fighting " 
                            + cause.getName() + " much more difficult...", affected, cause));
            affected.add(c, new Compulsion(affected, cause));
        }
    }

    private boolean shouldCompel() {
        return getMagnitude() * 50 > Global.random(100);
    }

    private boolean noMoreAttrs() {
        return !getDrainAttr().isPresent();
    }

    private static final Set<Attribute> UNDRAINABLE_ATTS = EnumSet.of(Attribute.Dark, Attribute.Speed, Attribute.Perception);
    private boolean attIsDrainable(Attribute att) {
        return !UNDRAINABLE_ATTS.contains(att) && affected.get(att) > Math.max(10, affected.getPure(att) / 10);
    }
    private Optional<Attribute> getDrainAttr() {
        Optional<Abuff> darkBuff = affected.getStatusOfClass(Abuff.class).stream().filter(status -> status.getModdedAttribute() == Attribute.Dark).findAny();
        if (!darkBuff.isPresent() || darkBuff.get().getValue() <  10 + getMagnitude() * 50) {
            return Global.pickRandom(Arrays.stream(Attribute.values()).filter(this::attIsDrainable).toArray(Attribute[]::new));            
        }
        return Optional.empty();
    }

    @Override
    protected Optional<Status> withdrawalEffects() {
       return Optional.of(new DarkChaos(affected));
    }

    @Override
    protected Optional<Status> addictionEffects() {
        return Optional.of(this);
    }

    @Override
    protected String describeIncrease() {
        switch (getSeverity()) {
            case HIGH:
                if (affected.human()) {
                    return cause.getName() + "'s blackness threatens to overwhelm what purity "
                                    + "remains inside of you, and it's a constant presence in {self:possessive} mind.";
                } else {
                    return cause.getName() + "'s dark taint threatens to overwhelm what purity "
                                    + "remains inside of {self:name-do}, and you can almost feel that {self:pronoun} has almost given up fighting it.";
                }
            case LOW:
                if (affected.human()) {
                    return "The blackness " + cause.getName() + " poured into you is still "
                                    + "there, and it feels like it's alive somehow; a churning mass of corruption and depravity.";
                } else {
                    return "The blackness " + cause.getName() + " poured into {self:name-do} is still "
                                    + "there, and you can almost feel it inside {self:direct-object}; a churning mass of corruption and depravity.";
                }
            case MED:
                return "The corruption in {self:possessive} soul spreads further, seeping into {self:possessive} flesh and bones.";
            case NONE:
            default:
                return "";
        }
    }

    @Override
    protected String describeDecrease() {
        switch (getSeverity()) {
            case HIGH:
                if (affected.human()) {
                    return "The corruption in {self:possessive} soul is backing off, but "
                                    + "there is work to be done yet if you are to be entirely free of it. ";
                } else {
                    return "The corruption in {self:possessive} soul visibly recedes a bit, taking away some of {self:possessive} demonic attributes along with it.";
                }
            case MED:
                if (affected.human()) {
                    return "Whatever it was exactly that " + cause.getName() + " created in you "
                                    + "has weakened somewhat and is no longer taking all of your concentration to resist it. ";
                } else {
                    return "Whatever it was exactly that " + cause.getName() + " has tainted {self:name-do} with "
                                    + "has weakened somewhat and {self:possessive} gaze doesn't feel as dangerous as before. ";
                }
            case LOW:
                if (affected.human()) {
                    return "Whatever it was exactly that " + cause.getName() + " created in you "
                                    + "has weakened considerably and is no longer corrupting {self:possessive} every thought. ";
                } else {
                    return "Whatever it was exactly that " + cause.getName() + " has tainted {self:name-do} with "
                                    + "has weakened considerably and some of {self:possessive} old gentleness is showing through. ";
                }
            case NONE:
                return "The last of the infernal corruption is purified "
                + "from {self:possessive} soul, bringing {self:direct-object} back to normal. Well, as normal as {self:subject-action:are} ever going to be, anyway. ";
            default:
                return "";
        }
    }

    @Override
    protected String describeWithdrawal() {
        switch (getSeverity()) {
            case HIGH:
                return "<b>" + cause.getName() + "'s corruption is working hard to punish you "
                                + "for not feeding it today, and it will cause all kinds of trouble tonight.</b>";
            case LOW:
                return "<b>Something is not quite right. The blackness " + cause.getName()
                                + " put in you is stirring, causing all kinds of strange sensations. Perhaps it's hungry?</b>";
            case MED:
                return "<b>The powerful corruption within {self:name-do} is rebelling"
                                + " against not being fed today. Expect the unexpected tonight.</b>";
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
        return "Something is churning inside of you this morning. It feels both wonderful and disgusting"
                        + " at the same time. You think you hear an echo of a whisper as you go about {self:possessive}"
                        + " daily routine, pushing you to evil acts.";
    }

    @Override
    public AddictionType getType() {
        return AddictionType.CORRUPTION;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        if (inWithdrawal) {
            return "The blackness resonates with " + cause.getName() + ", growing even more powerful and troublesome than before.";
        }
        return "The blackness " + cause.getName() + " places in you resonates with " + cause.directObject() + ". You can"
                        + " feel it starting to corrupt " + affected.possessiveAdjective() + " mind and body!";
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "";
        } else {
            switch (getSeverity()) {
                case HIGH:
                    return Global.format("<b>{self:SUBJECT-ACTION:have} been almost completely demonized by " + cause.nameOrPossessivePronoun() + " demonic influence. "
                                    + "{self:POSSESSIVE} bright eyes have been replaced by ruby-like irises that seem to stare into your very soul. You better finish this one fast!</b>", affected, cause);
                case MED:
                    return Global.format("<b>{self:SUBJECT-ACTION:have} been visibly changed by demonic corruption. "
                                    + "Black lines run along {self:possessive} body where it hadn't before and there's a hungry look in {self:possessive} eyes that "
                                    + "disturbs you almost as much as it turns you on.</b>", affected, cause);
                case LOW:
                    return Global.format("<b>{self:SUBJECT-ACTION:look} a bit strange. While you can't quite put your finger on it, something about {self:direct-object} feels a bit off to you. "
                                    + "Probably best not too worry about it too much.</b>", affected, cause);
                case NONE:
                default:
                    return "";
            }
        }
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
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
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
        return new Corruption((Character)newAffected, newOther, magnitude);
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Corruption(Global.noneCharacter(), Global.getCharacterByType(obj.get("cause").getAsString()),
                        (float) obj.get("magnitude").getAsInt());
    }

    @Override
    public String informantsOverview() {
        return "Dude. Not cool. I like " + cause.getName() + " shaking " + cause.directObject() + " evil ass around at night as much"
                        + " as the next guy, but the evil should stay there, you know? Now, the"
                        + " rest of the competitors will not appreciate {self:possessive} new attitude either."
                        + " I don't see them jumping to {self:possessive} defence any time soon. You should also"
                        + " worry about this thing inside of you taking over the uncorrupted parts of"
                        + " your mind. Also, I would imagine that that evil part of you won't appreciate"
                        + " any efforts to get rid of it. Who knows what chaos it might cause? Of course,"
                        + " if it's the Dark skills you're interested in, then it's probably a good thing."
                        + " But you're not like that, are you?";
    }

}
