package nightgames.skills;

import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.status.Stsflag;

public class Strapon extends Skill {

    public Strapon(Character self) {
        super("Strap On", self, 15);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !getSelf().has(Trait.strapped) && c.getStance().mobile(getSelf())
                        && !c.getStance().prone(getSelf())
                        && (getSelf().has(Item.Strapon) || getSelf().has(Item.Strapon2)) && !getSelf().hasDick()
                        && !c.getStance().connected(c) && !c.getStance().isFaceSitting(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Put on the strapon dildo";
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 15;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        List<Clothing> unequipped = getSelf().getOutfit().equip(Clothing.getByID("strapon"));
        if (unequipped.isEmpty()) {
            if (getSelf().human()) {
                c.write(getSelf(), Global.capitalizeFirstLetter(deal(c, 0, Result.normal, target)));
            } else if (!target.is(Stsflag.blinded)) {
                c.write(getSelf(), Global.capitalizeFirstLetter(receive(c, 0, Result.normal, target)));
            } else {
                printBlinded(c);
            }
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), "You take off your " + unequipped.get(0)
                                + " and fasten a strap on dildo onto yourself.");
            } else if (!target.is(Stsflag.blinded)){
                c.write(getSelf(),
                                String.format("%s takes off %s %s and straps on a thick rubber "
                                                + "cock and grins at %s in a way that makes %s feel a bit nervous.",
                                                getSelf().subject(), getSelf().possessivePronoun(),
                                                unequipped.get(0), target.nameDirectObject(),
                                                target.directObject()));
            } else printBlinded(c);
        }
        if (!target.is(Stsflag.blinded)) {
            target.loseMojo(c, 10);
            target.emote(Emotion.nervous, 10);
        }
        getSelf().emote(Emotion.confident, 30);
        getSelf().emote(Emotion.dominant, 40);
        Item lost = getSelf().has(Item.Strapon2) ? Item.Strapon2 : Item.Strapon;
        c.getCombatantData(getSelf()).loseItem(lost);
        getSelf().remove(lost);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Strapon(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You put on a strap on dildo.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s straps on a thick rubber cock and grins in a way that "
                        + "makes %s feel a bit nervous.", getSelf().subject(),
                        target.nameDirectObject());
    }

}
