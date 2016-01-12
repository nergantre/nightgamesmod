package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Stsflag;

public class OrgasmSeal extends Skill {

	public OrgasmSeal(Character self) {
		super("Orgasm Seal", self, 4);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Arcane) >= 15 || user.get(Attribute.Dark) >= 5;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && !target.is(Stsflag.orgasmseal);
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public String describe(Combat c) {
		return "Prevents your opponent from cumming with a mystical seal";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (getSelf().human()) {
			c.write(getSelf(), deal(c, 0, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(), receive(c, 0, Result.normal, target));
		}
		target.add(c, new nightgames.status.OrgasmSeal(target, 15));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new OrgasmSeal(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You focus your energy onto " + target.nameOrPossessivePronoun()
				+ " abdomen, coalescing it into a blood red mark that prevents her from cumming.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name() + " makes a complicated gesture and envelopes her finger tips in a blood red glow. "
				+ "With a nasty grin, she jams her finger into your " + (target.hasBalls() ? "balls" : "lower abdomen")
				+ ". Strangely it doesn't hurt at all, but when " + getSelf().subject()
				+ " withdraws her finger, she leaves a glowing pentagram on you.";
	}
}
