package nightgames.characters.body;

import java.util.HashMap;
import java.util.Map;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

public class AssPart extends GenericBodyPart {
    public static int SIZE_SMALL = 0;
    public static int SIZE_NORMAL = 1;
    public static int SIZE_BIG = 2;
    public static int SIZE_HUGE = 3;
    public static AssPart generic = new AssPart("ass", 0, 1.2, SIZE_NORMAL);
    private int size;
    private static final Map<Integer, String> SIZE_DESCRIPTIONS = new HashMap<>(); 
    static {
        SIZE_DESCRIPTIONS.put(SIZE_SMALL, "small ");
        SIZE_DESCRIPTIONS.put(SIZE_NORMAL, "");
        SIZE_DESCRIPTIONS.put(SIZE_BIG, "large ");
        SIZE_DESCRIPTIONS.put(SIZE_HUGE, "huge ");
    }

    public static AssPart generateGeneric() {
        return new AssPart("ass", 0, 1.2, 1);
    }

    public AssPart(String desc, String longDesc, double hotness, double pleasure, double sensitivity, int size) {
        super(desc, longDesc, hotness, pleasure, sensitivity, false, "ass", "a ");
        this.size = size;
    }

    public AssPart(String desc, double hotness, double pleasure, double sensitivity) {
        this(desc, "", hotness, pleasure, sensitivity, SIZE_NORMAL);
    }

    public AssPart() {
        this("ass", 0, 1.2, 1);
    }

    @Override
    public double getFemininity(Character c) {
        return size - SIZE_NORMAL;
    }

    @Override
    public int mod(Attribute a, int total) { 
        int bonus = super.mod(a, total);
        if (size > SIZE_NORMAL & a == Attribute.Seduction) {
            bonus += (size - SIZE_NORMAL) * 2;
        }
        if (size > SIZE_BIG & a == Attribute.Speed) {
            bonus += (size - SIZE_BIG);
        }
        return bonus;
    }

    @Override
    public String fullDescribe(Character c) {
        return SIZE_DESCRIPTIONS.get(size) + describe(c);
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = super.getPleasure(self, target);
        pleasureMod += self.has(Trait.analTraining1) ? .5 : 0;
        pleasureMod += self.has(Trait.analTraining2) ? .7 : 0;
        pleasureMod += self.has(Trait.analTraining3) ? .7 : 0;
        return pleasureMod;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyBonuses(self, opponent, target, damage, c);
        if (self.has(Trait.oiledass) && c.getStance().anallyPenetratedBy(c, self, opponent)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} naturally oiled asshole swallows {other:name-possessive} cock with ease.",
                            self, opponent));
            bonus += 5;
        }

        if ((self.has(Trait.tight) || self.has(Trait.holecontrol)) && c.getStance().anallyPenetrated(c, self)) {
            String desc = "";
            if (self.has(Trait.tight)) {
                desc += "powerful ";
            }
            if (self.has(Trait.holecontrol)) {
                desc += "well-trained ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "sphincter muscles to milk {other:name-possessive} cock, adding to the pleasure.",
                            self, opponent));
            bonus += self.has(Trait.tight) && self.has(Trait.holecontrol) ? 10 : 5;
            if (self.has(Trait.tight)) {
                opponent.pain(c, self, Math.min(30, self.get(Attribute.Power)));
            }
        }
        if (self.has(Trait.drainingass) && !target.isType("strapon")) {
            if (Global.random(3) == 0) {
                c.write(self, Global.format("{self:name-possessive} ass seems to <i>inhale</i>, drawing"
                                + " great gouts of {other:name-possessive} strength from {other:possessive}"
                                + " body.", self, opponent));
                opponent.drain(c, self, self.getLevel());
                opponent.add(c, new Abuff(opponent, Attribute.Power, -3, 10));
                self.add(c, new Abuff(self, Attribute.Power, 3, 10));
            } else {
                c.write(self, Global.format("The feel of {self:name-possessive} ass around"
                                + " {other:name-possessive} %s drains"
                                + " {other:direct-object} of {other:possessive} energy.", self, opponent, target.describe(opponent)));
                opponent.drain(c, self, self.getLevel()/2);
            }
        }
        return bonus;
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        super.tickHolding(c, self, opponent, otherOrgan);
        if (self.has(Trait.autonomousAss)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} " + fullDescribe(self)
                                            + " churns against {other:name-possessive} cock, "
                                            + "seemingly with a mind of its own. {self:POSSESSIVE} internal muscles feel like a hot fleshy hand inside her asshole, jerking {other:possessive} shaft.",
                            self, opponent));
            opponent.body.pleasure(self, this, otherOrgan, 10, c);
        }
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyReceiveBonuses(self, opponent, target, damage, c);
        if (opponent.has(Trait.asshandler) || opponent.has(Trait.anatomyknowledge)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} ass causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.asshandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        if (self.has(Trait.buttslut)) {
            bonus += 10;
            if (Global.random(4) == 0 && self.is(Stsflag.trance)) {
                c.write(opponent, Global.format(
                                "The foreign object rummaging around inside {self:name-possessive} ass <i><b>just feels so right</b></i>. {self:SUBJECT-ACTION:feel|feels} {self:reflective} slipping into a trance!",
                                                self, opponent));
                self.add(c, new Trance(self, 3, false));
            }
            c.write(opponent, Global.format(
                            "The foreign object rummaging around inside {self:name-possessive} ass feels so <i>right</i>. {self:SUBJECT} can't help moaning in time with the swelling pleasure.",
                                            self, opponent));
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return c.has(Trait.oiledass) || c.is(Stsflag.oiled);
    }

    public String getFluidsNoMods(Character c) {
        if (c.has(Trait.oiledass)) {
            return "oils";
        }
        return "";
    }

    @Override
    public boolean getDefaultErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
            return (c.has(Trait.tight) ? 1 : 0) + (c.has(Trait.holecontrol) ? 1 : 0) + (c.has(Trait.oiledass) ? 1 : 0)
                            + (c.has(Trait.autonomousAss) ? 4 : 0);
    }

    @Override
    public String adjective() {
        return "anal";
    }

    public BodyPart upgrade() {
        AssPart newPart = (AssPart) instance();
        newPart.size = Global.clamp(newPart.size + 1, SIZE_SMALL, SIZE_HUGE);
        return newPart;
    }

    public BodyPart downgrade() {
        AssPart newPart = (AssPart) instance();
        newPart.size = Global.clamp(newPart.size - 1, SIZE_SMALL, SIZE_HUGE);
        return newPart;
    }
}
