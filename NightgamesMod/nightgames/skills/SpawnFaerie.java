package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.pet.FairyFem;
import nightgames.pet.FairyMale;
import nightgames.pet.Ptype;

public class SpawnFaerie extends Skill {
	private Ptype gender;

	public SpawnFaerie(Character self, Ptype gender) {
		super("Summon Faerie", self);
		this.gender = gender;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Arcane) >= 3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && c.getStance().mobile(getSelf())
				&& !c.getStance().prone(getSelf()) && getSelf().pet == null;
	}

	@Override
	public int getMojoCost(Combat c) {
		return 25;
	}

	@Override
	public String describe(Combat c) {
		return "Summon a Faerie familiar to support you: 15 Mojo";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int power = 7;
		int ac = 4;
		if (getSelf().has(Trait.leadership)) {
			power += 5;
		}
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
			if (gender == Ptype.fairyfem) {
				getSelf().pet = new FairyFem(getSelf(), power, ac);
			} else {
				getSelf().pet = new FairyMale(getSelf(), power, ac);
			}
		} else {
			if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.normal, target));
			}
			getSelf().pet = new FairyFem(getSelf(), power, ac);
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new SpawnFaerie(user, gender);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}

	@Override
	public String getLabel(Combat c) {
		if (gender == Ptype.fairyfem) {
			return "Faerie (female)";
		} else {
			return "Faerie (male)";
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (gender == Ptype.fairyfem) {
			return "You start a summoning chant and in your mind, seek out a familiar. A pretty little faerie girl appears in front of you and gives you a friendly wave before "
					+ "landing softly on your shoulder.";
		} else {
			return "You start a summoning chant and in your mind, seek out a familiar. A six inch tall faerie boy winks into existence in response to your call. The faerie "
					+ "hovers in the air on dragonfly wings.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		return getSelf().name()
				+ " casts a spell as she extends her hand. In a flash of magic, a small, naked girl with butterfly wings appears in her palm.";
	}

}
