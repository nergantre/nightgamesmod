package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Shamed;

public class TailPeg extends Skill {

	public TailPeg(Character self) {
		super("Tail Peg", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.body.get("tail").size() > 0 && user.get(Attribute.Dark)>1;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().getArousal().get() >= 30 && getSelf().canAct()
				&& target.pantsless()
				&& c.getStance().en != Stance.standing
				&& c.getStance().en != Stance.standingover
				&& !(c.getStance().en == Stance.anal && c.getStance().dom(getSelf()));
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public String describe() {
		return "Shove your tail up your opponent's ass.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.roll(this, c,
				accuracy() - (c.getStance().penetration(getSelf()) ? 0 : 5))) {
			int strength = Math.min(20, 10 + getSelf().get(Attribute.Dark)/4);
			boolean shamed = false;
			if (Global.random(4) == 2) {
				target.add(c, new Shamed(target));
				shamed = true;
			}
			if (target.human()) {
				if (c.getStance().penetration(getSelf()))
					c.write(getSelf(),receive(c, 0, Result.special, target));
				else if (c.getStance().dom(target))
					c.write(getSelf(),receive(c, 0, Result.critical, target));
				else if (c.getStance().behind(getSelf()))
					c.write(getSelf(),receive(c, 0, Result.strong, target));
				else
					c.write(getSelf(),receive(c, 0, Result.normal, target));
				if (shamed)
					c.write(getSelf(),"The shame of having your ass violated by "
							+ getSelf().name() + " has destroyed your confidence.");
			} else if (getSelf().human()) {
				if (c.getStance().penetration(getSelf()))
					c.write(getSelf(),deal(c, 0, Result.special, target));
				else if (c.getStance().dom(target))
					c.write(getSelf(),deal(c, 0, Result.critical, target));
				else if (c.getStance().behind(getSelf()))
					c.write(getSelf(),deal(c, 0, Result.strong, target));
				else
					c.write(getSelf(),deal(c, 0, Result.normal, target));
				if (shamed)
					c.write(getSelf(),"[npc shamed]");
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("ass"), strength, c);
			target.pain(c, strength / 2);
			target.emote(Emotion.nervous, 10);
			target.emote(Emotion.desperate, 10);
			getSelf().emote(Emotion.confident, 15);
			getSelf().emote(Emotion.dominant, 25);
			if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
				target.add(c, new BodyFetish(target, getSelf(), "tail", .25, 10));
			}
		} else {
			if (target.human())
				c.write(getSelf(),receive(c, 0, Result.miss, target));
			else
				c.write(getSelf(),deal(c, 0, Result.miss, target));
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new TailPeg(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		switch (modifier) {
		case critical:
			return "";
		case miss:
			return "";
		case normal:
			return "";
		case special:
			return "";
		case strong:
			return "";
		default:
			return "<<This should not be displayed, please inform The Silver Bard: TailPeg-deal>>";
		}
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		switch (modifier) {
		case critical:
			return "Smiling down on you, "
					+ getSelf().name()
					+ " spreads your legs and tickles your butt with her tail."
					+ " You notice how the tail itself is slick and wet as it"
					+ " slowly pushes through your anus, spreading your cheeks a part. "
					+ getSelf().name()
					+ " pumps it in and out a for a few times before taking "
					+ "it out again.";
		case miss:
			return getSelf().name()
					+ " tries to peg you with her tail but you manage to push"
					+ " your butt cheeks together in time to keep it out.";
		case normal:
			return getSelf().name()
					+ " suddenly moves very close to you. You expect an attack from the front"
					+ " and try to move back, but end up shoving her tail right up your ass.";
		case special:
			return getSelf().name()
					+ " smirks and wiggles her tail behind her back. You briefly look "
					+ "at it and the see the appendage move behind you. You try to keep it"
					+ " out by clenching your butt together, but a squeeze of "
					+ getSelf().name()
					+ "'s vagina breaks your concentration, so the tail slides up your ass"
					+ " and you almost lose it as your cock and ass are stimulated so thoroughly"
					+ " at the same time.";
		case strong:
			return getSelf().name()
					+ " hugs you from behind and rubs her chest against your back."
					+ " Distracted by that, she managed to push her tail between your"
					+ " ass cheeks and started tickling your prostate with the tip.";
		default:
			return "<<This should not be displayed, please inform The Silver Bard: TailPeg-receive>>";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "ass";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "tail";
	}
}
