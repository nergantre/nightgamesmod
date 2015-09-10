package nightgames.skills;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Cowgirl;
import nightgames.stance.FaceSitting;
import nightgames.stance.Mount;
import nightgames.stance.ReverseMount;
import nightgames.status.Bound;
import nightgames.status.Flatfooted;
import nightgames.status.Stsflag;

public class Command extends Skill {

	public Command(Character self) {
		super("Command", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return !user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !getSelf().human()&&getSelf().canRespond()&&target.is(Stsflag.enthralled);
	}

	@Override
	public float priorityMod(Combat c) {
		return 10.0f;
	}

	@Override
	public String describe(Combat c) {
		return "Order your thrall around";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		CockPart otherCock = target.body.getRandomCock();
		PussyPart otherPussy = target.body.getRandomPussy();

		CockPart selfCock = getSelf().body.getRandomCock();
		PussyPart selfPussy = getSelf().body.getRandomPussy();
		boolean otherReady = (otherCock != null && otherCock.isReady(target))
				&& (otherPussy != null && otherPussy.isReady(target));
		boolean selfReady = (selfCock != null && selfCock.isReady(getSelf()))
				&& (selfPussy != null && selfPussy.isReady(getSelf()));
		if (getSelf().bound()) { // Undress self
			c.write(getSelf(),"You feel a compulsion to loosen " + getSelf().nameOrPossessivePronoun()
					+ " bondage. She quickly hops to her feet and grins at you like a predator while rubbing her wrists.");
			getSelf().free();
		} else if (!target.mostlyNude()) { // Undress self
			c.write(getSelf(),receive(c, 0, Result.miss, target));
			new Undress(target).resolve(c, getSelf());
		} else if (!getSelf().crotchAvailable() && !getSelf().getOutfit().slotUnshreddable(ClothingSlot.bottom)) {
			c.write(getSelf(),receive(c, 0, Result.weak, target));
			c.write(getSelf(),"Like a hungry beast, you rip off " + getSelf().name()
					+ "'s " + getSelf().shred(ClothingSlot.bottom) + ".");
		} else if (!getSelf().breastsAvailable() && !getSelf().getOutfit().slotUnshreddable(ClothingSlot.top)) {
			c.write(getSelf(),receive(c, 0, Result.weak, target));
			c.write(getSelf(),"Like a hungry beast, you rip off " + getSelf().name()
					+ "'s " + getSelf().shred(ClothingSlot.top) + ".");
		} else if (!getSelf().crotchAvailable()) {
			(new Undress(getSelf())).resolve(c, target);
		} else if (!otherReady) { // Masturbate
			c.write(getSelf(),receive(c, 0, Result.normal, target));
			new Masturbate(target).resolve(c, getSelf());
		} else if (!selfReady) { // Pleasure me
			c.write(getSelf(),receive(c, 1, Result.critical, target));
			c.setStance(new FaceSitting(getSelf(), target));
			c.write(getSelf(),"<br>");
			List<Skill> possible = new ArrayList<>();
			if (getSelf().hasPussy()) {
				possible.add(new PussyWorship(target));
			} else if (getSelf().hasDick()) {
				possible.add(new CockWorship(target));
			} else {
				possible.add(new Anilingus(target));
			}
		} else if (!c.getStance().penetration(getSelf())
				&& getSelf().hasPussy() && target.hasDick()) { // Fuck me
			c.setStance(new Mount(target, getSelf()));
			c.write(getSelf(),receive(c, 0, Result.special, target));
			new Fuck(target).resolve(c, getSelf());
		} else if (!c.getStance().penetration(getSelf())
				&& target.hasPussy() && getSelf().hasDick()) { // Fuck me
			c.setStance(new Mount(target, getSelf()));
			c.write(getSelf(),receive(c, 0, Result.special, target));
			new ReverseFuck(target).resolve(c, getSelf());
		} else if (c.getStance().penetration(getSelf())) { // I drain you
			if (Global.random(5) >= 4 && getSelf().get(Attribute.Dark) > 0) {
				c.write(getSelf(),receive(c, 0, Result.critical, target));
				c.write(getSelf(),"<br>");
				new Drain(getSelf()).resolve(c, target);
			} else {
				c.write(getSelf(),receive(c, 0, Result.critical, target));
				c.write(getSelf(),"<br>");
				new Piston(getSelf()).resolve(c, target);
			}
		} else { // Confused
			c.write(getSelf(),receive(c, 0, Result.normal, target));
			new Masturbate(target).resolve(c, getSelf());
		}
		return true;
	}

	@Override
	public Skill copy(Character target) {
		return new Command(target);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (modifier == null)
			return getSelf().name()
					+ "'s order confuses you for a moment, snapping her control over you.";
		switch (modifier) {
		case critical:
			switch (damage) {
			case 0:
				return "While commanding you to be still, " + getSelf().name()
						+ " starts bouncing wildly on your dick.";
			case 1:
				return "Her scent overwhelms you and you feel a compulsion to pleasure her.";
			case 2:
				return "You feel an irresistible complusion to lie down on your back";
			default:
				break;
			}
		case miss:
			return "You feel an uncontrollable desire to undress yourself";
		case normal:
			return getSelf().name()
					+ "' eyes tell you to pleasure yourself for her benefit";
		case special:
			return getSelf().name()
					+ "'s voice pulls you in and you cannot resist fucking her";
		case weak:
			return "You are desperate to see more of " + getSelf().name()
					+ "'s body";
		default:
			return null;
		}
	}
}
