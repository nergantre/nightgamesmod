package nightgames.characters.body;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public enum BreastsPart implements BodyPart {
    flat("flat", "", 0),
    a("A Cup", "tiny", 1),
    b("B Cup", "smallish", 2),
    c("C Cup", "modest", 3),
    d("D Cup", "round", 4),
    dd("DD Cup", "large", 5),
    e("E Cup", "huge", 6),
    f("F Cup", "glorious", 7),
    g("F Cup", "massive", 8),
    h("F Cup", "colossal", 9);

    public String desc;
    public String name;
    public int size;

    BreastsPart(String name, String desc, int size) {
        this.desc = desc;
        this.name = name;
        this.size = size;
    }

    public static String synonyms[] = {"breasts", "tits", "boobs",};

    @Override
    public void describeLong(StringBuilder b, Character c) {
        if (c.hasPussy() || size > 0) {
            b.append(Global.capitalizeFirstLetter(describe(c, true)));
            b.append(" adorn " + c.nameOrPossessivePronoun() + " chest.");
        }
    }

    @Override
    public String canonicalDescription() {
        return name;
    }

    @Override
    public double priority(Character c) {
        return getPleasure(c, null);
    }

    public String describe(Character c, boolean forceAdjective) {
        if (c.hasPussy() || size > 0) {
            if (forceAdjective) {
                boolean first = Global.random(2) == 0;
                boolean second = first ? Global.random(2) == 0 : true;
                return (first ? desc + ' ' : "") + (second ? name + ' ' : "")
                                + synonyms[Global.random(synonyms.length)];
            } else {
                return Global.maybeString(desc + ' ') + Global.maybeString(name + ' ')
                                + synonyms[Global.random(synonyms.length)];
            }
        } else {
            if (c.get(Attribute.Power) > 25) {
                return "muscular pecs";
            }
            return "flat chest";
        }
    }

    @Override
    public String describe(Character c) {
        return describe(c, true);
    }

    @Override
    public String fullDescribe(Character c) {
        return describe(c, true);
    }

    @Override
    public boolean isType(String type) {
        return type.equalsIgnoreCase("breasts");
    }

    @Override
    public String getType() {
        return "breasts";
    }

    @Override
    public String toString() {
        return desc + ' ' + name;
    }

    @Override
    public boolean isReady(Character self) {
        return true;
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        double hotness = -.25 + size * .3 * self.getOutfit()
                                                .getExposure(ClothingSlot.top);
        if (!opponent.hasDick()) {
            hotness /= 2;
        }
        return Math.max(0, hotness);
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        return .25 + size * .35;
    }

    @Override
    public double getSensitivity(BodyPart target) {
        return .75 + size * .2;
    }

    @Override
    public BodyPart upgrade() {
        BreastsPart values[] = BreastsPart.values();
        if (ordinal() < values.length - 1) {
            return values[ordinal() + 1];
        } else {
            return this;
        }
    }

    @Override
    public double getFemininity(Character self) {
        return 3 * ((double) size) / maximumSize().size;
    }

    public static BreastsPart maximumSize() {
        BreastsPart max = flat;
        for (BreastsPart b : BreastsPart.values()) {
            if (b.size > max.size) {
                max = b;
            }
        }
        return max;
    }

    @Override
    public BodyPart downgrade() {
        if (ordinal() > 0) {
            return BreastsPart.values()[ordinal() - 1];
        } else {
            return this;
        }
    }

     @Override public JsonObject save() {
        JsonObject obj = new JsonObject();
        obj.addProperty("enum", name());
        return obj;
    }

    @Override public BodyPart load(JsonObject obj) {
        return BreastsPart.valueOf(obj.get("enum").getAsString());
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        return Math.max(5, size) + Global.random(Math.min(0, size - 4));
    }

    @Override
    public String getFluids(Character c) {
        return c.has(Trait.lactating) ? "milk" : "";
    }

    @Override
    public double getFluidAddictiveness(Character c) {
        if (c.has(Trait.lactating) && c.has(Trait.addictivefluids)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isErogenous() {
        return size > BreastsPart.flat.size;
    }

    @Override
    public boolean isNotable() {
        return true;
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        if (self.has(Trait.lactating) && target.isType("mouth")) {
            if (self.has(Trait.magicmilk)) {
                float addictionLevel;
                Addiction addiction;
                if (opponent.human()) {
                    Global.getPlayer().addict(AddictionType.MAGIC_MILK, self, Addiction.LOW_INCREASE);
                    addiction = Global.getPlayer().getAddiction(AddictionType.MAGIC_MILK).get();
                    addictionLevel = addiction.getMagnitude();
                } else {
                    addictionLevel = 0;
                    addiction = null;
                }
                if (addictionLevel < Addiction.LOW_THRESHOLD) {
                    // not addicted
                    c.write(opponent,
                                    Global.format("{self:NAME-POSSESSIVE} milk makes the blood surge from {other:name-possessive} head into {other:possessive} crotch, leaving {other:direct-object} light-headed and horny",
                                                    self, opponent));
                } else if (addictionLevel < .3f) {
                    // starting addiction
                    c.write(opponent,
                                    Global.format("{self:NAME-POSSESSIVE} milk seems sweeter than usual. While {other:subject} know from experience that {self:possessive} saccharine cream is a powerful aphrodisiac, {other:pronoun} can't but help drinking down more.",
                                                    self, opponent));
                } else if (addictionLevel < .45f) {
                    // addicted
                    c.write(opponent,
                                    Global.format("As Cassie's milk dribbles down her breasts, you awake to a powerful need for her cream. Ignoring the potential aphrodisiac effectes, you quickly capture her nipples in your lips and relieve your parched throat with her delicious milk.",
                                                    self, opponent));
                } else if (addictionLevel < Addiction.HIGH_THRESHOLD) {
                    // dependent
                    c.write(opponent,
                                    Global.format("{other:NAME} desperately {other:action:suck|sucks} at {self:name-possessive} milky teats as soon as they're available. {other:POSSESSIVE} burning need to imbibe {self:possessive} sweet milk is overpowering any other thoughts. "
                                                    + "{self:SUBJECT} smiles at {other:direct-object} and gently cradles {other:possessive} head, rocking {other:direct-object} back and forth while {other:subject} drink. "
                                                    + "The warm milk settles in {other:possessive} belly, slowly setting {other:possessive} body on fire with arousal.",
                                    self, opponent));
                } else {
                    // enslaved
                    c.write(opponent,
                                    Global.format("{other:SUBJECT} slavishly wrap {other:possessive} lips around {self:name-possessive} immaculate teats and start suckling. "
                                                    + "{other:POSSESSIVE} vision darkens around the edges and {other:possessive} world is completely focused on draining {self:possessive} wonderful breasts. "
                                                    + "{self:SUBJECT} smiles at {other:direct-object} and gently cradles {other:possessive} head, rocking {other:direct-object} back and forth while {other:subject} drink. "
                                                    + "The warm milk settles in {other:possessive} belly, slowly setting {other:possessive} body on fire with arousal.",
                                    self, opponent));
    
                }
                if (addiction != null)
                    opponent.tempt(c, self, this, (int) (15 + addiction.getMagnitude() * 35));
    
                if (opponent.is(Stsflag.magicmilkcraving)) {
                    // temporarily relieve craving
                    addiction.alleviateCombat(Addiction.LOW_INCREASE);
    
                }
                if (c.getCombatantData(opponent) != null) {
                    int timesDrank = c.getCombatantData(opponent)
                                      .getIntegerFlag("drank_magicmilk")
                                    + 1;
                    c.getCombatantData(opponent)
                     .setIntegerFlag("drank_magicmilk", timesDrank);
                }
            }
            if (self.has(Trait.sedativecream)) {
                c.write(opponent,
                                Global.format("The power seems to leave {other:name-possessive} body as {other:pronoun-action:sip|sips} {self:possessive} cloying cream.",
                                                self, opponent));
                opponent.weaken(c, opponent.getStamina().max() / 10);
                opponent.add(new Abuff(opponent, Attribute.Power, -Global.random(1, 3), 20));
            }
        }
        return 0;
    }

    @Override
    public String prefix() {
        return "";
    }

    @Override
    public int compare(BodyPart other) {
        if (other instanceof BreastsPart) {
            return size - ((BreastsPart) other).size;
        }
        return 0;
    }

    @Override
    public boolean isVisible(Character c) {
        return true;
    }

    @Override
    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c) {
        return 0;
    }

    @Override
    public int mod(Attribute a, int total) {
        switch (a) {
            case Speed:
                return -Math.max(size - 3, 0);
            case Seduction:
                return Math.max(size - 3, 0);
            default:
                return 0;
        }
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

    }

    @Override
    public int counterValue(BodyPart otherPart, Character self, Character other) {
        return 0;
    }

    @Override
    public BodyPartMod getMod(Character self) {
        return BodyPartMod.noMod;
    }
}
