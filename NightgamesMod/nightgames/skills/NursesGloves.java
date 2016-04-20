package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingTrait;

public class NursesGloves extends Skill {
    public NursesGloves(Character self) {
        super("Nurse's Gloves", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Medicine) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !getSelf().has(ClothingTrait.nursegloves)
                        && getSelf().has(Item.MedicalSupplies, 1);
    }

    @Override
    public String describe(Combat c) {
        return "Puts on a pair of plastic medical examiner's gloves";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        getSelf().getOutfit().equip(Clothing.getByID("nursesgloves"));
        getSelf().consume(Item.MedicalSupplies, 1);

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new NursesGloves(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String message;
        message = "You grab a pair of rubber gloves, pulling them on with a satisfying snap.";
        return message;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String message;
        message = "With a lecherous grin on her face, " + getSelf().subject()
                        + " snaps on a pair of rubber gloves similar to those you would see at the doctor's.";
        return message;
    }

}
