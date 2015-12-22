package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.TailSuck;

public class TailSucked extends Status {

	private Character	sucker;
	private int			power;

	public TailSucked(Character affected, Character sucker, int power) {
		super("Tail Sucked", affected);
		this.sucker = sucker;
		this.power = power;
		requirements.add((c, self, other) -> new TailSuck(self).usable(c, other));
		flag(Stsflag.bound);
		flag(Stsflag.tailsucked);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format(
				"%s tail is sucking %s energy straight from %s %s.",
				sucker.nameOrPossessivePronoun(),
				affected.nameOrPossessivePronoun(),
				affected.possessivePronoun(),
				affected.body.getRandomCock().describe(affected));
	}

	@Override
	public String describe(Combat c) {
		return String.format(
				"%s tail keeps churning around %s "
						+ "%s, sucking in %s vital energies.",
				sucker.nameOrPossessivePronoun(),
				affected.nameOrPossessivePronoun(),
				affected.body.getRandomCock().describe(affected),
				affected.possessivePronoun());
	}

	@Override
	public void tick(Combat c) {
		BodyPart cock = affected.body.getRandomCock();
		BodyPart tail = sucker.body.getRandom("tail");
		if (cock == null || tail == null || c == null) {
			affected.removelist.add(this);
			return;
		}
		
		c.write(sucker, String.format("%s tail sucks powerfully, and %s"
				+ " some of %s strength being drawn in.", 
				sucker.nameOrPossessivePronoun(), affected.subjectAction("feel", "feels"),
				affected.possessivePronoun()));
		
		Attribute toDrain = Global.pickRandom(affected.att.entrySet().stream()
				.filter(e -> e.getValue() != 0).map(e -> e.getKey())
				.toArray(Attribute[]::new));
		affected.add(new Abuff(affected, toDrain, -power, 20));
		sucker.add(new Abuff(sucker, toDrain, power, 20));
		affected.drain(c, sucker, 1 + Global.random(power*3));
		affected.drainMojo(c, sucker, 1 + Global.random(power*3));
	}

	@Override
	public float fitnessModifier() {
		return -4.0f;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return 0;
	}

	@Override
	public int evade() {
		return power * -5;
	}

	@Override
	public int escape() {
		return power * -5;
	}

	@Override
	public int gainmojo(int x) {
		return (int) (x * 0.2);
	}

	@Override
	public int spendmojo(int x) {
		return (int) (x * 1.2);
	}

	@Override
	public int counter() {
		return -10;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new TailSucked(newAffected, newOther, power);
	}

	@Override
	public JSONObject saveToJSON() {
		return null;
	}

	@Override
	public Status loadFromJSON(JSONObject obj) {
		return null;
	}

}
