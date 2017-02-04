package nightgames.characters.body;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.SizeMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.Abuff;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class BreastsPart extends GenericBodyPart {
    public static BreastsPart flat = (BreastsPart) new BreastsPart().applyMod(new SizeMod(0));
    public static BreastsPart a = (BreastsPart) new BreastsPart().applyMod(new SizeMod(1));
    public static BreastsPart b = (BreastsPart) new BreastsPart().applyMod(new SizeMod(2));
    public static BreastsPart c = (BreastsPart) new BreastsPart().applyMod(new SizeMod(3));
    public static BreastsPart d = (BreastsPart) new BreastsPart().applyMod(new SizeMod(4));
    public static BreastsPart dd = (BreastsPart) new BreastsPart().applyMod(new SizeMod(4));
    public static BreastsPart f = (BreastsPart) new BreastsPart().applyMod(new SizeMod(5));
    public static BreastsPart g = (BreastsPart) new BreastsPart().applyMod(new SizeMod(6));
    public static BreastsPart h = (BreastsPart) new BreastsPart().applyMod(new SizeMod(7));

    public static BreastsPart generateGeneric() {
        return new BreastsPart();
    }

    public BreastsPart() {
        super("breasts", "", 0.0, 1.0, 1.0, true, "breasts", "");
    }

    @Override
    public boolean isVisible(Character c) {
        return c.crotchAvailable() || getSize() > 0;
    }

    @Override
    public double getFemininity(Character c) {
        return 3 * ((double) getSize()) / maximumSize().getSize();
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        double hotness = super.getHotness(self, opponent);
        
        Clothing top = self.getOutfit().getTopOfSlot(ClothingSlot.top);
        hotness += -.1 + Math.sqrt(getSize()) * .15 * self.getOutfit()
                                                .getExposure(ClothingSlot.top);
        if (!opponent.hasDick()) {
            hotness /= 2;
        }
        if (top == null) {
            hotness += .1;
        }
        return Math.max(0, hotness);
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        return (.25 + getSize() * .35) * super.getPleasure(self, target);
    }

    @Override
    public double getSensitivity(Character self, BodyPart target) {
        return (.75 + getSize() * .2) * super.getSensitivity(self, target);
    }

    public static BreastsPart maximumSize() {
        return h;
    }

    @Override
    public int mod(Attribute a, int total) {
        switch (a) {
            case Speed:
                return -Math.max(getSize() - 3, 0) / 2;
            case Seduction:
                return Math.max(getSize() - 3, 0);
            default:
                return 0;
        }
    }

    public static String synonyms[] = {"breasts", "tits", "boobs"};

    @Override
    public void describeLong(StringBuilder b, Character c) {
        if (c.hasPussy() || getSize() > 0) {
            b.append(Global.capitalizeFirstLetter(fullDescribe(c)));
            b.append(" adorn " + c.nameOrPossessivePronoun() + " chest.");
        }
    }

    protected String modlessDescription(Character c) {
        return Global.pickRandom(synonyms).get();
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyBonuses(self, opponent, target, damage, c);
        bonus += Math.max(5, getSize()) + Global.random(Math.min(0, getSize() - 4));
        return bonus;
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        super.tickHolding(c, self, opponent, otherOrgan);
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyReceiveBonuses(self, opponent, target, damage, c);
        if (self.has(Trait.lactating) && target.isType("mouth")) {
            if (self.has(Trait.magicmilk)) {
                float addictionLevel;
                Addiction addiction;
                opponent.addict(AddictionType.MAGIC_MILK, self, Addiction.LOW_INCREASE);
                addiction = opponent.getAddiction(AddictionType.MAGIC_MILK).get();
                addictionLevel = addiction.getMagnitude();
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
                                    Global.format("{other:NAME} desperately {other:action:suck|sucks} at {self:name-possessive} milky teats as soon as they're in front of {other:direct-object}. "
                                                    + "{other:POSSESSIVE} burning need to imbibe {self:possessive} sweet milk is overpowering all rational thought. "
                                                    + "{self:SUBJECT} smiles at {other:direct-object} and gently cradles {other:possessive} head, rocking {other:direct-object} back and forth while {other:subject} drink. "
                                                    + "The warm milk settles in {other:possessive} belly, slowly setting {other:possessive} body on fire with arousal.",
                                    self, opponent));
                } else {
                    // enslaved
                    c.write(opponent,
                                    Global.format("{other:SUBJECT} slavishly {other:action:wrap} {other:possessive} lips around {self:name-possessive} immaculate teats and start suckling. "
                                                    + "{other:POSSESSIVE} vision darkens around the edges and {other:possessive} world is completely focused on draining {self:possessive} wonderful breasts. "
                                                    + "{self:SUBJECT} smiles at {other:direct-object} and gently cradles {other:possessive} head, rocking {other:direct-object} back and forth while {other:subject} drink. "
                                                    + "The warm milk settles in {other:possessive} belly, slowly setting {other:possessive} body on fire with arousal.",
                                    self, opponent));
    
                }
                if (addiction != null)
                    opponent.temptNoSkill(c, self, this, (int) (15 + addiction.getMagnitude() * 35));
    
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
                opponent.add(c, new Abuff(opponent, Attribute.Power, -Global.random(1, 3), 20));
            }
            if (self.has(Trait.Pacification)) {
                c.write(opponent,
                                Global.format("With every drop of {self:name-possessive} infernal milk {other:subject-action:swallow},"
                                                + " {self:pronoun} seems more and more impossibly beautiful to {other:possessive} eyes."
                                                + " Why would {other:pronoun} want to mar such perfect beauty?",
                                                self, opponent));
                opponent.add(c, new Charmed(opponent, 2));
            }
            if (self.has(Trait.PheromonedMilk) && !opponent.has(Trait.Rut)) {
                c.write(opponent, Global.format("<b>Drinking {self:possessive} breast milk sends {other:direct-object} into a chemically induced rut!</b>",
                                                self, opponent));
                opponent.addTemporaryTrait(Trait.Rut, 10);
            }
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return true;
    }

    public String getFluidsNoMods(Character c) {
        if (c.has(Trait.lactating)) {
            return "milk";
        }
        return "";
    }

    @Override
    public boolean getDefaultErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
        double priority = getPleasure(c, null);
        if (c.has(Trait.temptingtits)) {
            priority += .5;
        }
        if (c.has(Trait.beguilingbreasts)) {
            priority += .5;
        }
        return priority;
    }

    @Override
    public String adjective() {
        return "breast";
    }

    public BodyPart upgrade() {
        return this.applyMod(new SizeMod(SizeMod.clampToValidSize(this, getSize() + 1)));
    }

    public BodyPart downgrade() {
        return this.applyMod(new SizeMod(SizeMod.clampToValidSize(this, getSize() - 1)));
    }
}
