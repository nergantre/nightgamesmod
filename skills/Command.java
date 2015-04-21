package skills;

import global.Global;
import status.Bound;
import status.Flatfooted;
import status.Stsflag;
import characters.Attribute;
import characters.Character;
import characters.Trait;
import combat.Combat;
import stance.Cowgirl;
import stance.Mount;
import combat.Result;
import stance.ReverseMount;

public class Command extends Skill {

	public Command(Character self) {
		super("Command", self);
	}

	@Override
	public boolean requirements() {
		return !self.human();
	}

	@Override
	public boolean requirements(Character user) {
		return !user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !self.human()&&self.canRespond()&&target.is(Stsflag.enthralled);
	}

	@Override
	public float priorityMod(Combat c) {
		return 10.0f;
	}

	@Override
	public String describe() {
		return "Order your thrall around";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if (self.bound()) { // Undress self
			c.write(self,"You feel a compulsion to loosen " + self.nameOrPossessivePronoun()
					+ " bondage. She quickly hops to her feet and grins at you like a predator while rubbing her wrists.");
			self.free();
		} if (!target.nude()) { // Undress self
			c.write(self,receive(c, 0, Result.miss, target));
			new Undress(target).resolve(c, self);
		} else if (!self.nude()) { // Undress me
			c.write(self,receive(c, 0, Result.weak, target));
			if (self.topless())
				c.write(self,"Like a hungry beast, you rip off " + self.name()
						+ "'s " + self.bottom.pop() + ".");
			else
				c.write(self,"Like a hungry beast, you rip off " + self.name()
						+ "'s " + self.top.pop() + ".");
		} else if (target.getArousal().get() <= 15) { // Masturbate
			c.write(self,receive(c, 0, Result.normal, target));
			new Masturbate(target).resolve(c, self);
		} else if (!c.getStance().penetration(self)
				&& c.getStance().dom(target)
				&& self.hasPussy() && target.hasDick()) { // Fuck me
			c.setStance(new Mount(target, self));
			c.write(self,receive(c, 0, Result.special, target));
			new Fuck(target).resolve(c, self);
		} else if (!c.getStance().penetration(self)
				&& c.getStance().dom(target)
				&& target.hasPussy() && self.hasDick()) { // Fuck me
			c.setStance(new Mount(target, self));
			c.write(self,receive(c, 0, Result.special, target));
			new ReverseFuck(target).resolve(c, self);
		} else if (c.getStance().penetration(self)) { // I drain you
			if (Global.random(5) >= 4 && self.get(Attribute.Dark) > 0) {
				c.write(self,receive(c, 0, Result.critical, target));
				c.write(self,"<br>");
				new Drain(self).resolve(c, target);
			} else {
				c.write(self,receive(c, 0, Result.critical, target));
				c.write(self,"<br>");
				new Piston(self).resolve(c, target);
			}
		} else if (!c.getStance().penetration(self) && self.getArousal().get() <= 15) { // Pleasure me
			c.write(self,receive(c, 1, Result.critical, target));
			c.setStance(new ReverseMount(target, self));
			c.write(self,"<br>");
			if (self.hasPussy()) {
				new Cunnilingus(target).resolve(c, self);
			} else if (self.hasDick()) {
				new Blowjob(target).resolve(c, self);
			} else {
				new LickNipples(target).resolve(c, self);
			}
		} else { // Confused
			c.write(self,receive(c, 0, null, target));
			target.removelist.add(target.getStatus(Stsflag.enthralled));
			target.add(new Flatfooted(target, 1));
		}
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
			return self.name()
					+ "'s order confuses you for a moment, snapping her control over you.";
		switch (modifier) {
		case critical:
			switch (damage) {
			case 0:
				return "While commanding you to be still, " + self.name()
						+ " starts bouncing wildly on your dick.";
			case 1:
				return "Her scent overwhelms you and you feel a compulsion to pleasure her.";
			case 2:
				return "You feel an irresistible comuplsion to lay down on your back";
			default:
				break;
			}
		case miss:
			return "You feel an uncontrollable desire to undress yourself";
		case normal:
			return self.name()
					+ "' eyes tell you to pleasure yourself for her benefit";
		case special:
			return self.name()
					+ "'s voice pulls you in and you cannot resist fucking her";
		case weak:
			return "You are desperate to see more of " + self.name()
					+ "'s body";
		default:
			return null;
		}
	}
}
