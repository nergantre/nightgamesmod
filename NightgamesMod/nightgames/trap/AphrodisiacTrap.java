package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Flatfooted;

public class AphrodisiacTrap implements Trap {
    private Character owner;

    @Override
    public void trigger(Character target) {
        if (!target.check(Attribute.Perception, 15 + target.baseDisarm())) {
            if (target.human()) {
                Global.gui().message(
                                "You spot a liquid spray trap in time to avoid setting it off. You carefully manage to disarm the trap and pocket the potion.");
                target.gain(Item.Aphrodisiac);
                target.location().remove(this);
            }
        } else {
            if (target.human()) {
                Global.gui().message(
                                "There's a sudden spray of gas in your face and the room seems to get much hotter. Your dick goes rock-hard and you realize you've been "
                                                + "hit with an aphrodisiac.");
            } else if (target.location().humanPresent()) {
                Global.gui().message(
                                target.name() + " is caught in your trap and sprayed with aphrodisiac. She flushes bright red and presses a hand against her crotch. It seems like "
                                                + "she'll start masturbating even if you don't do anything.");
            }
            target.tempt(40);
            target.location().opportunity(target, this);
        }
    }

    @Override
    public boolean decoy() {
        return false;
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.Aphrodisiac) && owner.has(Item.Tripwire) && owner.has(Item.Sprayer)
                        && !owner.has(Trait.direct);
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.Tripwire, 1);
        owner.consume(Item.Aphrodisiac, 1);
        owner.consume(Item.Sprayer, 1);
        return "You set up a spray trap to coat an unwary opponent in powerful aphrodisiac.";
    }

    @Override
    public Character owner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Aphrodisiac Trap";
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.get(Attribute.Cunning) >= 12 && !owner.has(Trait.direct);
    }

    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        victim.add(new Flatfooted(victim, 1));
        enc.engage(new Combat(attacker, victim, attacker.location()));
        attacker.location().remove(this);
    }

    @Override
    public void resolve(Character active) {
        if (active != owner) {
            trigger(active);
        }
    }
}
