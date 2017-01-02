package nightgames.trap;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.RemoteMasturbation;

public class RemoteControl extends Trap {

    public RemoteControl(Character owner) {
        super("Remote Control", owner);
    }

    @Override
    protected void trigger(Character target) {
        if (target.human()) {
            String msg = "You see a small, oblong object laying on the floor,"
                            + " and bend over to pick it up. It's black and shiny, but has no"
                            + " real discernable features. Suddenly a ring of light appears around"
                            + " the thing, and you freeze in place and hear " + owner.nameOrPossessivePronoun()
                            + " voice in your mind." + " <i>\"Is someone there? Who did I catch? Ah, it's you, "
                            + target.name + "! Wonderful!\"</i> Without warning, the hand not holding the weird device"
                            + " flies down towards your crotch and";
            if (!target.outfit.slotOpen(ClothingSlot.bottom)) {
                msg += ", first removing all the clothing covering your nethers,";
                target.outfit.undressOnly(c -> c.getSlots()
                                                .contains(ClothingSlot.bottom));
            }
            String otherHand;
            if (target.hasDick()) {
                msg += " grabs hold of your " + target.body.getRandomCock()
                                                           .describe(target)
                                + ". You try to stop, try to let go of the black thing, but"
                                + " you don't seem to have any control at all. The hand on your"
                                + " cock moves deftly, but not in the way you would when"
                                + " masturbating. It's definitely effective, though. You"
                                + " don't think you're going to last too long doing this.";
                otherHand = "wrapped around your cock, pumping it intently.";
            } else if (target.hasPussy()) {
                msg += " strokes the outside of your " + target.body.getRandomPussy()
                                                                    .describe(target)
                                + ". " + owner.nameOrPossessivePronoun() + " experienced, feather-light"
                                + " touch soon gets you lubricated enough to allow your fingers passage"
                                + " deeper into your folds and the hole in their center. You gasp as "
                                + owner.name + ", by proxy, rubs your clit with 'your'"
                                + " thumb while probing your pussy with delicate thrusts of 'your' fingers.";
                otherHand = "between your thighs, working dilligently on your pussy.";
            } else {
                msg += " finds nothing there. <i>\"Oh, right. I forgot. You're really missing out," + target.name
                                + ", you ought to do something about that. Still, this"
                                + " doesn't mean there is </i>nothing<i> we can do...\"</i>"
                                + " Your errant limb bends back up, and you involuntarily wet a finger."
                                + " It then moves down behind your back, finding your ass. After a few"
                                + " probing touches, it plunges in and starts massaging your insides.";
                otherHand = "buried between your asscheeks, with a finger up your bottom.";
            }
            msg += " \"<i>I'm on my way to you now. Try not to cum before I get there, alright? The Remote Control"
                            + " is not very good at measuring how far along you are. See you soon!</i>\""
                            + " Your mind goes silent again, but your body is still out of your control,"
                            + " one hand holding the 'Remote Control', as it is appearantly called,"
                            + " the other " + otherHand;
            Global.gui().message(msg);
        } else {

        }
        target.addNonCombat(new RemoteMasturbation(target, owner));
        target.location().opportunity(target, this);
        target.location().alarm = true;
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.RemoteControl);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.has(Trait.RemoteControl);
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.RemoteControl, 1);
        return "<b>RemoteControl setup text - should not be displayed.</b>";
    }
    
    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        enc.engage(new Combat(attacker, victim, attacker.location()));
        attacker.location().remove(this);
    }

}
