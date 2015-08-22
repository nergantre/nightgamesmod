package nightgames.characters.body;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.CockBound;
import nightgames.status.Enthralled;
import nightgames.status.FluidAddiction;
import nightgames.status.Horny;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;
import nightgames.status.Winded;

import java.io.PrintWriter;
import java.util.Scanner;

public enum CockPart implements BodyPart {
	tiny("tiny", 3), small("smallish", 4), average("average", 6), big("big", 8), huge("huge", 9), massive("massive",
			10), primal("primal", 9), blessed("blessed", 8), incubus("incubus", 8), bionic("bionic",
					8), enlightened("enlightened", 8);

	public String desc;
	public double size;
	public static String synonyms[] = { "cock", "dick", "shaft", "phallus" };

	CockPart(String desc, double size) {
		this.desc = desc;
		this.size = size;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A ");
		b.append(fullDescribe(c));
		b.append(" hangs between " + c.nameOrPossessivePronoun() + " legs.");
	}

	@Override
	public String describe(Character c) {
		String syn = Global.pickRandom(synonyms);
		return Global.maybeString(desc + " ") + (c.hasPussy() ? "girl-" : "") + syn;
	}

	@Override
	public double priority(Character c) {
		return this.getPleasure(c, null);
	}

	@Override
	public String fullDescribe(Character c) {
		if (this == bionic)
			return "bionic robo-cock";
		String syn = Global.pickRandom(synonyms);
		return desc + " " + (c.hasPussy() ? "girl-" : "") + syn;
	}

	public boolean isType(String type) {
		return type.equalsIgnoreCase("cock");
	}

	@Override
	public String getType() {
		return "cock";
	}

	@Override
	public String toString() {
		return desc;
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		double hotness = Math.log(size / 4 + 1) / Math.log(2) - 1;
		if (this == incubus)
			hotness *= 1.25;
		if (this == bionic)
			hotness *= 0.8;
		if (!opponent.hasPussy()) {
			hotness /= 2;
		}
		return hotness;
	}

	public double getPleasureBase() {
		return Math.log(size + 2.5) / Math.log(2) - 1.8;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = getPleasureBase();
		pleasureMod += self.has(Trait.cockTraining1) ? .5 : 0;
		pleasureMod += self.has(Trait.cockTraining2) ? .7 : 0;
		pleasureMod += self.has(Trait.cockTraining3) ? .7 : 0;

		if (this == primal)
			pleasureMod += .4;
		if (this == incubus)
			pleasureMod += .3;
		if (this == bionic)
			pleasureMod += .3;
		if (this == enlightened)
			pleasureMod += .2;
		return pleasureMod;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		if (isGeneric())
			return Math.log(size / 5 + 1) / Math.log(2);
		if (this == primal)
			return 1.2;
		if (this == blessed)
			return 0.75;
		if (this == incubus)
			return 0.9;
		if (this == bionic)
			return 0.5;
		if (this == enlightened)
			return 0.8;
		return 1;
	}

	public boolean isReady(Character self) {
		return self.has(Trait.alwaysready) || self.getArousal().percent() >= 15 || this == bionic;
	}

	public BodyPart upgrade() {
		CockPart values[] = CockPart.values();
		if (ordinal() < massive.ordinal())
			return values[ordinal() + 1];
		else
			return this;
	}

	public static CockPart maximumSize() {
		CockPart max = tiny;
		for (CockPart c : CockPart.values()) {
			if (c.size > max.size) {
				max = c;
			}
		}
		return max;
	}

	public BodyPart downgrade() {
		if (ordinal() > 0 && ordinal() <= massive.ordinal())
			return CockPart.values()[ordinal() - 1];
		else
			return this;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	@Override
	public BodyPart load(Scanner loader) {
		return CockPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (self.has(Trait.polecontrol)) {
			String desc = "";
			if (self.has(Trait.polecontrol)) {
				desc += "expert ";
			}
			c.write(self,
					Global.format(
							"{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
									+ "cock control to grind against {other:name-possessive} inner walls, making {other:possessive} knuckles whiten as {other:subject-action:moan|moans} uncontrollably.",
							self, opponent));
			bonus += (self.has(Trait.polecontrol)) ? 8 : 0;
		}
		if (this == blessed) {
			String message = "";
			if (target == PussyPart.succubus) {
				message += String.format(
						"The holy energies inside %s %s radiate outward and into %s, causing %s %s to grow much more sensitve.",
						self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
						opponent.possessivePronoun(), target.describe(opponent));
				bonus += damage * 0.5; // +50% damage
			}
			if (Global.random(8) == 0 && !opponent.wary()) {
				message += String.format("Power radiates out from %s %s, seeping into %s and subverting %s will. ",
						self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
						opponent.directObject());
				opponent.add(c, new Enthralled(opponent, self, 3));
			}
			if (self.hasStatus(Stsflag.cockbound)) {
				String binding = ((CockBound) self.getStatus(Stsflag.cockbound)).binding;
				message += String.format(
						"With the merest of thoughts, %s %s out a pulse of energy from %s %s, freeing it from %s %s. ",
						self.subject(), self.human() ? "send" : "sends", self.possessivePronoun(), describe(self),
						opponent.nameOrPossessivePronoun(), binding);
				self.removeStatus(Stsflag.cockbound);
			}
			c.write(self, message);
		} else if (this == incubus) {
			String message = String.format("%s demonic appendage latches onto %s will, trying to draw it into %s.",
					self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(), self.directObject());
			int amtDrained;
			if (target == PussyPart.feral) {
				message += String.format(" %s %s gladly gives it up, eager for more pleasure.",
						opponent.possessivePronoun(), target.describe(opponent));
				amtDrained = 3;
				bonus += 2;
			} else if (target == PussyPart.cybernetic) {
				message += String.format(
						" %s %s does not oblige, instead sending a pulse of electricity through %s %s and up %s spine",
						opponent.nameOrPossessivePronoun(), target.describe(opponent), self.nameOrPossessivePronoun(),
						describe(self), self.possessivePronoun());
				self.pain(c, Global.random(9) + 4);
				amtDrained = 0;
			} else {
				message += String.format("Despite %s best efforts, some of the elusive energy passes into %s.",
						opponent.nameOrPossessivePronoun(), self.nameDirectObject());
				amtDrained = 1;
			}
			if (amtDrained != 0) {
				opponent.loseWillpower(c, amtDrained);
				self.restoreWillpower(c, amtDrained);
			}
			c.write(self, message);
		} else if (this == bionic) {
			String message = "";
			if (Global.random(5) == 0 && target.getType().equals("pussy")) {
				message += String.format(
						"%s %s out inside %s %s, pressing the metallic head of %s %s tightly against %s cervix. "
								+ "Then, a thin tube extends from %s uthera and into %s womb, pumping in a powerful aphrodisiac that soon has %s sensitive and"
								+ " gasping for more.",
						self.subject(), self.human() ? "bottom" : "bottoms", opponent.nameOrPossessivePronoun(),
						target.describe(opponent), self.possessivePronoun(), describe(self),
						opponent.possessivePronoun(), self.possessivePronoun(), opponent.possessivePronoun(),
						opponent.directObject());
				opponent.add(c, new Hypersensitive(opponent));
				// Instantly addict
				opponent.add(c, new FluidAddiction(opponent, self, 2));
				opponent.add(c, new FluidAddiction(opponent, self, 2));
				opponent.add(c, new FluidAddiction(opponent, self, 2));
				bonus -= 3; // Didn't actually move around too much
			} else if (target != PussyPart.fiery) {
				message += String.format(
						"Sensing the flesh around it, %s %s starts spinning rapidly, vastly increasing the friction against the walls of %s %s.",
						self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
						target.describe(opponent));
				bonus += 5;
				if (Global.random(5) == 0) {
					message += String.format(
							" The intense sensations cause %s to forget to breathe for a moment, leaving %s literally breathless.",
							opponent.subject(), opponent.directObject());
					opponent.add(c, new Winded(opponent, 1));
				}
			}
			c.write(self, message);
		} else if (this == enlightened) {
			String message = "";
			if (target == PussyPart.succubus) {
				message += String.format(
						"Almost instinctively, %s %s entire being into %s %s. While this would normally be a good thing,"
								+ " whilst fucking a succubus it is very, very bad indeed.",
						self.subjectAction("focus", "focuses"), self.possessivePronoun(), self.possessivePronoun(),
						describe(self));
				// Actual bad effects are dealt with in PussyPart
			} else {
				message += String.format(
						"Drawing upon %s extensive training, %s, concentrating will into %s %s and enhancing %s abilities",
						self.possessivePronoun(), self.subjectAction("meditate", "meditates"), self.possessivePronoun(),
						self.possessivePronoun(), describe(self), self.possessivePronoun());
				for (int i = 0; i < Math.max(2, (self.get(Attribute.Ki) + 5) / 10); i++) { // +5
																							// for
																							// rounding:
																							// 24->29->20,
																							// 25->30->30
					Attribute attr = new Attribute[] { Attribute.Power, Attribute.Cunning, Attribute.Seduction }[Global
							.random(3)];
					self.add(new Abuff(self, attr, 1, 10));
				}
				self.buildMojo(c, 5);
				self.restoreWillpower(c, 1);
			}
			c.write(self, message);
		}
		return bonus;
	}

	@Override
	public String getFluids(Character c) {
		return this == bionic ? "artificial lubricant" : "pre-cum";
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@Override
	public boolean isNotable() {
		return true;
	}

	@Override
	public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		double bonus = 0;
		if (opponent.has(Trait.dickhandler)) {
			c.write(opponent,
					Global.format(
							"{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} cock causes {self:subject} to shudder uncontrollably.",
							self, opponent));
			bonus += 5;
		}
		return bonus;
	}

	@Override
	public String prefix() {
		return "a ";
	}

	@Override
	public int compare(BodyPart other) {
		if (other instanceof CockPart) {
			return (int) (size - ((CockPart) other).size);
		}
		return 0;
	}

	@Override
	public boolean isVisible(Character c) {
		return c.pantsless();
	}

	@Override
	public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
			Combat c) {
		return 0;
	}

	@Override
	public int mod(Attribute a, int total) {
		switch (a) {
		case Speed:
			return (int) -Math.round(Math.max(size - 6, 0));
		case Seduction:
			return (int) Math.round(Math.max(size - 6, 0));
		default:
			return 0;
		}
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
		if (this == primal) {
			c.write(self,
					String.format("Raw sexual energy flows from %s %s into %s %s, enflaming %s lust",
							self.nameOrPossessivePronoun(), describe(self), opponent.nameOrPossessivePronoun(),
							otherOrgan.describe(opponent), opponent.possessivePronoun()));
			opponent.add(c,
					new Horny(opponent, Global.random(3) + 1, 3, self.nameOrPossessivePronoun() + " primal passion"));
		}
	}

	@Override
	public int counterValue(BodyPart other) {
		if (isGeneric() && other.getType().equals("pussy") && other != PussyPart.normal) {
			// Pussy mods are very, very dangerous. We should stay well clear.
			return -1;
		}
		if (!isGeneric() && other == PussyPart.normal || other.getType().equals("ass")) {
			// Normal pussies and assholes are easy prey for enhanced dicks.
			return 1;
		}
		if (this == primal)
			return other == PussyPart.fiery ? 1 : other == PussyPart.arcane ? -1 : 0;
		if (this == blessed)
			return other == PussyPart.succubus ? 1 : other == PussyPart.feral ? -1 : 0;
		if (this == incubus)
			return other == PussyPart.feral ? 1 : other == PussyPart.cybernetic ? -1 : 0;
		if (this == bionic)
			return other == PussyPart.arcane ? 1 : other == PussyPart.fiery ? -1 : 0;
		if (this == enlightened)
			return other == PussyPart.cybernetic ? 1 : other == PussyPart.succubus ? -1 : 0;
		return 0;
	}

	public boolean isGeneric() {
		return ordinal() <= massive.ordinal();
	}

	@Override
	public void onOrgasm(Combat c, Character self, Character opponent, BodyPart target, boolean selfCame) {
		if (this == incubus && c.getStance().inserted(self)) {
			if (selfCame) {
				if (target == PussyPart.cybernetic) {
					c.write(self,
							String.format(
									"%s demonic seed splashes pointlessly against the walls of %s %s, failing even in %s moment of defeat.",
									self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(),
									target.describe(opponent), self.possessivePronoun()));
				} else {
					int duration = Global.random(3) + 3;
					String message = String.format(
							"The moment %s erupts inside %s, %s mind goes completely blank, leaving %s pliant and ready.",
							self.subject(), opponent.subject(), opponent.possessivePronoun(), opponent.directObject());
					if (target == PussyPart.feral) {
						message += String.format(" %s offers no resistance to the subversive seed.",
								opponent.subject());
						duration += 3;
					}
					opponent.add(c, new Enthralled(opponent, self, duration));
					c.write(self, message);
				}
			} else {
				if (target != PussyPart.cybernetic) {
					c.write(self,
							String.format(
									"Sensing %s moment of passion, %s %s greedliy draws upon the rampant flows of orgasmic energy within %s, transferring the power back into %s.",
									opponent.nameOrPossessivePronoun(), self.nameOrPossessivePronoun(), describe(self),
									opponent.directObject(), self.directObject()));
					int attDamage = target == PussyPart.feral ? 10 : 5;
					int willDamage = target == PussyPart.feral ? 40 : 20;
					opponent.add(c, new Abuff(opponent, Attribute.Power, -attDamage, 20));
					opponent.add(c, new Abuff(opponent, Attribute.Cunning, -attDamage, 20));
					opponent.add(c, new Abuff(opponent, Attribute.Seduction, -attDamage, 20));
					self.add(c, new Abuff(self, Attribute.Power, attDamage, 20));
					self.add(c, new Abuff(self, Attribute.Cunning, attDamage, 20));
					self.add(c, new Abuff(self, Attribute.Seduction, attDamage, 20));
					opponent.loseWillpower(c, willDamage);
					self.restoreWillpower(c, willDamage);

				}
			}
		}
	}
}