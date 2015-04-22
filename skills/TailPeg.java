package skills;

import global.Global;
import stance.Stance;
import status.Shamed;
import combat.Combat;
import combat.Result;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

public class TailPeg extends Skill {

	public TailPeg(Character self) {
		super("Tail Peg", self);
	}

	@Override
	public boolean requirements() {
		return self.body.get("tail").size() > 0&&self.getPure(Attribute.Dark)>1;
	}

	@Override
	public boolean requirements(Character user) {
		return user.body.get("tail").size() > 0 && user.getPure(Attribute.Dark)>1;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.getArousal().get() >= 30 && self.canAct()
				&& self.canSpend(20) && target.pantsless()
				&& c.getStance().en != Stance.standing
				&& c.getStance().en != Stance.standingover
				&& !(c.getStance().en == Stance.anal && c.getStance().dom(self));
	}

	@Override
	public String describe() {
		return "Shove your tail up your opponent's ass.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (target.roll(this, c,
				accuracy() + this.self.tohit()
						- (c.getStance().penetration(self) ? 0 : 5))) {
			int strength = Math.min(20, 10 + self.getPure(Attribute.Dark)/4);
			boolean shamed = false;
			if (Global.random(4) == 2) {
				target.add(new Shamed(target));
				shamed = true;
			}
			self.spendMojo(c, 20);
			if (target.human()) {
				if (c.getStance().penetration(self))
					c.write(self,receive(c, 0, Result.special, target));
				else if (c.getStance().dom(target))
					c.write(self,receive(c, 0, Result.critical, target));
				else if (c.getStance().behind(self))
					c.write(self,receive(c, 0, Result.strong, target));
				else
					c.write(self,receive(c, 0, Result.normal, target));
				if (shamed)
					c.write(self,"The shame of having your ass violated by "
							+ self.name() + " has destroyed your confidence.");
			} else if (self.human()) {
				if (c.getStance().penetration(self))
					c.write(self,deal(c, 0, Result.special, target));
				else if (c.getStance().dom(target))
					c.write(self,deal(c, 0, Result.critical, target));
				else if (c.getStance().behind(self))
					c.write(self,deal(c, 0, Result.strong, target));
				else
					c.write(self,deal(c, 0, Result.normal, target));
				if (shamed)
					c.write(self,"[npc shamed]");
			}
			target.body.pleasure(self, self.body.getRandom("tail"), target.body.getRandom("ass"), strength, c);
			target.pain(c, strength / 2);
			target.emote(Emotion.nervous, 10);
			target.emote(Emotion.desperate, 10);
			self.emote(Emotion.confident, 15);
			self.emote(Emotion.dominant, 25);
		} else {
			if (target.human())
				c.write(self,receive(c, 0, Result.miss, target));
			else
				c.write(self,deal(c, 0, Result.miss, target));
		}
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
					+ self.name()
					+ " spreads your legs and tickles your butt with her tail."
					+ " You notice how the tail itself was slick and wet as it"
					+ " slowly pushes through your anus, spreading your cheeks a part. "
					+ self.name()
					+ " pumps it in and out a for a few times before taking "
					+ "it out again.";
		case miss:
			return self.name()
					+ " tries to peg you with her tail but you manage to push"
					+ " your butt cheeks together in time to keep it out.";
		case normal:
			return self.name()
					+ " suddenly moves very close to you. You expect an attack from the front"
					+ " and try to move back, but end up shoving her tail right up your ass.";
		case special:
			return self.name()
					+ " smirks and wiggles her tail behind her back. You briefly look "
					+ "at it and the see the appendage move behind you. You try to keep it"
					+ " out by clenching your butt together, but a squeeze of "
					+ self.name()
					+ "'s vagina breaks your concentration, so the tail slides up your ass"
					+ " and you almost lose it as your cock and ass are stimulated so thoroughly"
					+ " at the same time.";
		case strong:
			return self.name()
					+ " hugs you from behind and rubs her chest against your back."
					+ " Distracted by that, she managed to push her tail between your"
					+ " ass cheeks and started tickling your prostrate with the tip.";
		default:
			return "<<This should not be displayed, please inform The Silver Bard: TailPeg-receive>>";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
