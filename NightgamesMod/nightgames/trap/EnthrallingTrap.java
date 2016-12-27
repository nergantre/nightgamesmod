package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Enthralled;
import nightgames.status.Flatfooted;

public class EnthrallingTrap extends Trap {

    public EnthrallingTrap() {
        this(null);
    }

    public void setStrength(Character user) {
        setStrength(user.get(Attribute.Dark) + user.get(Attribute.Arcane) + user.getLevel() / 2);
    }

    public EnthrallingTrap(Character owner) {
        super("Enthralling Trap", owner);
    }

    @Override
    public void trigger(Character target) {
        if (target.human()) {
            if (target.check(Attribute.Perception, 25 + target.baseDisarm())
                            || !target.eligible(owner) || !owner.eligible(target)) {
                Global.gui().message("As you step across the " + target.location().name
                                + ", you notice a pentagram drawn on the floor,"
                                + " appearing to have been drawn in cum. Wisely," + " you avoid stepping into it.");
            } else {
                target.addNonCombat(new Enthralled(target, owner, 5 + getStrength() / 20));
                target.location().opportunity(target, this);
                Global.gui().message("As you step across the " + target.location().name
                                + ", you are suddenly surrounded by purple flames. Your mind "
                                + "goes blank for a moment, leaving you staring into the distance."
                                + " When you come back to your senses, you shake your head a few"
                                + " times and hope whatever that thing was, it failed at"
                                + " whatever it was supposed to do. The lingering vision of two"
                                + " large red irises staring at you suggest differently, though.");
            }
        } else if (target.check(Attribute.Perception, 25 + target.baseDisarm()) || !target.eligible(owner) || !owner.eligible(target)) {
            if (target.location().humanPresent()) {
                Global.gui().message("You catch a bout of purple fire in your peripheral vision,"
                                + "but once you have turned to look the flames are gone. All that is left"
                                + " to see is " + target.name() + ", standing still and staring blankly ahead."
                                + " It would seem to be very easy to have your way with her now, but"
                                + " who or whatever left that thing there will probably be thinking" + " the same.");
            }
            target.addNonCombat(new Enthralled(target, owner, 5 + getStrength() / 20));
            target.location().opportunity(target, this);
        }
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.semen);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.get(Attribute.Dark) > 5;
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.semen, 1);
        return "You pop open a bottle of cum and use its contents to draw"
                        + " a pentagram on the floor, all the while speaking"
                        + " incantations to cause the first person to step into"
                        + " it to be immediatly enthralled by you.";
    }

    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        victim.addNonCombat(new Flatfooted(victim, 1));
        enc.engage(new Combat(attacker, victim, attacker.location()));
        attacker.location().remove(this);
    }

}
