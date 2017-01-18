package nightgames.characters.body;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Sensitized;

public class GenericCockPart extends GenericBodyPart implements CockPart {
    public static int SIZE_TINY = 3;
    public static int SIZE_SMALL = 4;
    public static int SIZE_LITTLE = 5;
    public static int SIZE_AVERAGE = 6;
    public static int SIZE_LARGE = 7;
    public static int SIZE_BIG = 8;
    public static int SIZE_HUGE = 9;
    public static int SIZE_MASSIVE = 10;

    public static String synonyms[] = {"cock", "dick", "shaft", "phallus"};

    public static GenericCockPart generic = new GenericCockPart();
    private int size;
    private static final Map<Integer, String> SIZE_DESCRIPTIONS = new HashMap<>(); 
    static {
        SIZE_DESCRIPTIONS.put(SIZE_TINY, "tiny ");
        SIZE_DESCRIPTIONS.put(SIZE_SMALL, "small ");
        SIZE_DESCRIPTIONS.put(SIZE_LITTLE, "small ");
        SIZE_DESCRIPTIONS.put(SIZE_AVERAGE, "");
        SIZE_DESCRIPTIONS.put(SIZE_LARGE, "large ");
        SIZE_DESCRIPTIONS.put(SIZE_BIG, "big ");
        SIZE_DESCRIPTIONS.put(SIZE_HUGE, "huge ");
        SIZE_DESCRIPTIONS.put(SIZE_MASSIVE, "massive ");
    }

    public static GenericCockPart generateGeneric() {
        return new GenericCockPart(0, 1.2, 1);
    }

    public GenericCockPart(double hotness, double pleasure, double sensitivity, int size) {
        super("cock", "", hotness, pleasure, sensitivity, false, "cock", "a ");
        this.size = size;
    }

    public GenericCockPart(double hotness, double pleasure, double sensitivity) {
        this(hotness, pleasure, sensitivity, SIZE_AVERAGE);
    }

    public GenericCockPart() {
        this(0, 1.2, 1);
    }

    public GenericCockPart(int size) {
        this(0, 1.2, 1, size);
    }

    @Override
    public double getFemininity(Character c) {
        return SIZE_SMALL - size;
    }

    @Override
    public int mod(Attribute a, int total) { 
        int bonus = super.mod(a, total);
        if (size > SIZE_AVERAGE & a == Attribute.Seduction) {
            bonus += (size - SIZE_AVERAGE) * 2;
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
        pleasureMod += self.has(Trait.sexTraining1) ? .5 : 0;
        pleasureMod += self.has(Trait.sexTraining2) ? .7 : 0;
        pleasureMod += self.has(Trait.sexTraining3) ? .7 : 0;
        return pleasureMod;
    }

    @Override
    protected String modlessDescription(Character c) {
        String syn = Global.pickRandom(synonyms).get();
        return (c.hasPussy() ? "girl-" : "") + syn;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyBonuses(self, opponent, target, damage, c);
        if (self.has(Trait.polecontrol)) {
            String desc = "";
            if (self.has(Trait.polecontrol)) {
                desc += "expert ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "cock control to grind against {other:name-possessive} inner walls, making {other:possessive} knuckles whiten as {other:pronoun} {other:action:moan|moans} uncontrollably.",
                            self, opponent));
            bonus += self.has(Trait.polecontrol) ? 8 : 0;
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
        if (opponent.has(Trait.dickhandler) || opponent.has(Trait.anatomyknowledge)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} cock causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.dickhandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        if (self.has(Trait.druglacedprecum) && !opponent.isPartProtected(target)) {
            opponent.add(c, new Sensitized(opponent, target, .2, 2.0, 20));
            c.write(self, Global.format("{self:NAME-POSSESSIVE} drug-laced precum is affecting {other:direct-object}.",
                            self, opponent));
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return c.getArousal().percent() >= 15 || c.has(Trait.alwaysready);
    }

    public String getFluidsNoMods(Character c) {
        return "semen";
    }

    @Override
    public boolean getDefaultErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
        return 2 + (c.has(Trait.polecontrol)? 2 : 0) + (c.has(Trait.druglacedprecum) ? 1 : 0)+ (c.has(Trait.hypnoticsemen) ? 2 : 0);
    }

    @Override
    public String adjective() {
        return "phallic";
    }

    public BodyPart upgrade() {
        GenericCockPart newPart = (GenericCockPart) instance();
        newPart.size = Global.clamp(newPart.size + 1, SIZE_TINY, SIZE_MASSIVE);
        return newPart;
    }

    public BodyPart downgrade() {
        GenericCockPart newPart = (GenericCockPart) instance();
        newPart.size = Global.clamp(newPart.size - 1, SIZE_TINY, SIZE_MASSIVE);
        return newPart;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public PussyPart getEquivalentPussy() {
        List<PartMod> newMods = getPartMods().stream().map(BodyUtils.EQUIVALENT_MODS::get).filter(mod -> mod != null).distinct().collect(Collectors.toList());
        GenericBodyPart newPart = PussyPart.generateGeneric();
        for (PartMod mod : newMods) {
            newPart = (GenericBodyPart)newPart.applyMod(mod);
        }
        return (PussyPart)newPart;
    }
}
