package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class StealClothes extends Skill {

    public StealClothes(Character self) {
        super("Steal Clothes", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return (c.getStance()
                 .reachTop(getSelf()) && !target.outfit.slotEmptyOrMeetsCondition(ClothingSlot.top, this::blocked))
                        || (c.getStance()
                             .reachBottom(getSelf())
                                        && !target.outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom,
                                                        this::blocked));

    }

    private boolean blocked(Clothing c) {
        // Allow crossdressing for now, except for strapons and bras.
        // This may change.
        if ((getSelf().hasDick() && c.getID()
                                     .equals("strapon"))
                        || (!getSelf().hasBreasts() && c.getSlots()
                                                        .contains(ClothingSlot.top)
                                        && c.getLayer() == 0))
            return false;

        return getSelf().outfit.canEquip(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Steal and put on an article of clothing: 10 Mojo.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Clothing stripped;
        boolean top;
        if (!target.outfit.slotEmptyOrMeetsCondition(ClothingSlot.top, this::blocked)) {
            stripped = target.outfit.unequip(target.outfit.getTopOfSlot(ClothingSlot.top));
            getSelf().outfit.equip(stripped);
            top = true;
        } else if (!target.outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom, this::blocked)) {
            stripped = target.outfit.unequip(target.outfit.getTopOfSlot(ClothingSlot.bottom));
            getSelf().outfit.equip(stripped);
            top = false;
        } else {
            c.write("<b>Error: Couldn't strip anything in StealClothes#resolve</b>");
            return false;
        }
        c.write(getSelf(), String.format(
                        "%s %s with %s quick movements, and before %s %s what's" + " going on, %s %s graces %s %s.",
                        getSelf().subjectAction("dazzle"), target.subject(), getSelf().possessivePronoun(),
                        target.pronoun(), target.action("realize"), target.possessivePronoun(),
                        stripped.getName(), getSelf().nameOrPossessivePronoun(), top ? "chest" : "hips"));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StealClothes(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public int speed() {
        return 6;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
