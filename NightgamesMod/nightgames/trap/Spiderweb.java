package nightgames.trap;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;

public class Spiderweb implements Trap {
    private Character owner;

    @Override
    public void trigger(Character target) {
        if (target.human()) {
            if (target.mostlyNude()) {
                Global.gui().message(
                                "You feel the tripwire underfoot too late to avoid it. A staggering amount of rope flies up to entangle your limbs and pull you off the ground. "
                                                + "Oh hell. You're completely immobilized and suspended in midair. Surprisingly, it's not that uncomfortable, but if someone finds you before you can get free, "
                                                + "you'll be completely defenseless.");
            } else {
                Global.gui().message(
                                "You feel the tripwire underfoot too late to avoid it. A staggering amount of rope flies up to entangle your limbs and pull you off the ground. "
                                                + "Something snags your clothes and pulls them off of you with unbelievable precision."
                                                + "Oh hell. You're completely immobilized and suspended naked in midair. Surprisingly, it's not that uncomfortable, but if someone finds you before you can get free, "
                                                + "you'll be completely defenseless.");
            }
        } else if (target.location().humanPresent()) {
            Global.gui().message("You hear a snap as " + target.name()
                            + " triggers your spiderweb trap and ends up helplessly suspended in midair like a naked present.");
        }
        target.state = State.webbed;
        target.delay(1);
        target.location().opportunity(target, this);
    }

    @Override
    public boolean decoy() {
        return false;
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.Rope, 4) && owner.has(Item.Spring, 2) && owner.has(Item.Tripwire);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.has(Trait.spider);
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.Tripwire, 1);
        owner.consume(Item.Rope, 4);
        owner.consume(Item.Spring, 2);
        return "With quite a bit of time and effort, you carefully setup a complex series of spring loaded snares. Anyone who gets caught in this will be rendered as helpless "
                        + "as a fly in a web.";
    }

    @Override
    public Character owner() {
        return owner;
    }

    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        if (attacker.human()) {
            Global.gui().message(
                            victim.name() + " is naked and helpless in the giant rope web. You approach slowly, taking in the lovely view of her body. You trail your fingers "
                                            + "down her front, settling between her legs to tease her sensitive pussy lips. She moans and squirms, but is completely unable to do anything in her own defense. "
                                            + "You are going to make her cum, that's just a given. If you weren't such a nice guy, you would leave her in that trap afterward to be everyone else's prey "
                                            + "instead of helping her down. You kiss and lick her neck, turning her on further. Her entrance is wet enough that you can easily work two fingers into her "
                                            + "and begin pumping. You gradually lick your way down her body, lingering at her nipples and bellybutton, until you find yourself eye level with her groin. "
                                            + "You can see her clitoris, swollen with arousal, practically begging to be touched. You trap the sensitive bud between your lips and attack it with your tongue. "
                                            + "The intense stimulation, coupled with your fingers inside her, quickly brings her to orgasm. While she's trying to regain her strength, you untie the ropes "
                                            + "binding her hands and feet and ease her out of the web.");
        } else if (victim.human()) {
            Global.gui().message("You're trying to figure out a way to free yourself, when you see " + attacker.name()
                            + " approach. You groan in resignation. There's no way you're "
                            + "going to get free before she finishes you off. She smiles as she enjoys your vulnerable state. She grabs your dangling penis and puts it in her mouth, licking "
                            + "and sucking it until it's completely hard. Then the teasing starts. She strokes you, rubs you, and licks the head of your dick. She uses every technique to "
                            + "pleasure you, but stops just short of letting you ejaculate. It's maddening. Finally you have to swallow your pride and beg to cum. She pumps you dick in earnest "
                            + "now and fondles your balls. When you cum, you shoot your load onto her face and chest. You hang in the rope web, literally and figuratively drained. "
                            + attacker.name() + " " + "gratiously unties you and helps you down.");
        }
        if (victim.getOutfit().getBottomOfSlot(ClothingSlot.bottom) != null) {
            attacker.gain(victim.getTrophy());
        }
        victim.nudify();
        victim.defeated(attacker);
        victim.getArousal().empty();
        attacker.tempt(20);
        Global.getMatch().score(attacker, victim.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        victim.state = State.ready;
        victim.location().endEncounter();
        victim.location().remove(this);
    }

    @Override
    public String toString() {
        return "Spiderweb";
    }

    @Override
    public void resolve(Character active) {
        if (active != owner) {
            trigger(active);
        }
    }
}
