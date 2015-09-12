package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingTrait;
import nightgames.stance.Stance;
import nightgames.status.Alluring;
import nightgames.status.Charmed;

public class TemptressStripTease extends StripTease {

	public TemptressStripTease(Character self) {
		super(self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.has(Trait.temptress) && user.get(Attribute.Technique) >= 8;
	}

	@Override
	public String getName() {
		return "Skillful Strip Tease";
	}
	
	@Override
	public boolean usable(Combat c, Character target) {
		return super.usable(c, target)
				|| (c.getStance().enumerate() == Stance.neutral
						&& getSelf().canAct() && getSelf().mostlyNude());
	}

	@Override
	public String getLabel(Combat c) {
		return isDance(c) ? "Sexy Dance" : super.getLabel(c);
	}

	@Override
	public String describe(Combat c) {
		return isDance(c)
				? "Do a slow, titilating dance to charm your opponent."
				: "Shed your clothes seductively, charming your opponent.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int technique = getSelf().get(Attribute.Technique);
		assert technique > 0;

		if (isDance(c)) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.weak, target));
			} else {
				c.write(getSelf(), receive(c, 0, Result.weak, target));
			}
			target.tempt(c, getSelf(),
					10 + Global.random(Math.max(5, technique)));
			target.add(
					new Charmed(target, Global.random(Math.min(3, technique))));
			getSelf().add(c, new Alluring(getSelf(), 3));
		} else {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.normal, target));
			} else {
				c.write(getSelf(), receive(c, 0, Result.normal, target));
			}

			target.tempt(c, getSelf(),
					15 + Global.random(Math.max(10, technique)));
			target.add(
					new Charmed(target, Global.random(Math.min(5, technique))));
			getSelf().add(c, new Alluring(getSelf(), 5));
			getSelf().undress(c);
		}
		target.emote(Emotion.horny, 30);
		getSelf().emote(Emotion.confident, 15);
		getSelf().emote(Emotion.dominant, 15);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new TemptressStripTease(user);
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (isDance(c)) {
			return getSelf().name() + " backs up a little and starts swinging"
					+ " her hips side to side. Curious as to what's going on, you"
					+ " cease your attacks and watch as she bends and curves, putting"
					+ " on a slow dance that would be very arousing even if she weren't"
					+ " naked. Now, without a stitch of clothing to obscure your view,"
					+ " the sight stirs your imagination. You are shocked out of your"
					+ " reverie when she plants a soft kiss on your lips, and you dreamily"
					+ " gaze into her eyes as she gets back into a fighting stance.";
		} else {
			return getSelf().name() + " takes a few steps back and starts "
					+ "moving sinously. She sensually runs her hands over her body, "
					+ "undoing straps and buttons where she encounters them, and starts"
					+ " peeling her clothes of slowly, never breaking eye contact."
					+ " You can only gawk in amazement as her perfect body is revealed bit"
					+ " by bit, and the thought of doing anything to blemish such"
					+ " perfection seems very unpleasant indeed.";
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (isDance(c)) {
			return "You slowly dance for " + target.name() + ", showing off"
					+ " your naked body.";
		} else {
			return "You seductively perform a short dance, shedding clothes as you do so. "
					+ target.name() + " seems quite taken with it, as "
					+ target.pronoun()
					+ " is practically drooling onto the ground.";
		}
	}

	private boolean isDance(Combat c) {
		return !super.usable(c, c.getOther(getSelf()))
				&& this.usable(c, c.getOther(getSelf()));
	}
}
