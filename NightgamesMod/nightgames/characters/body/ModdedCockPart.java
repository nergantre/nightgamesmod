package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class ModdedCockPart implements CockPart {
	private BasicCockPart base;
	private CockMod mod;

	public ModdedCockPart(BasicCockPart bodyPart, CockMod mod) {
		setBase(bodyPart);
		this.mod = mod;
	}

	@Override
	public BodyPartMod getMod() {
		return mod;
	}

	@Override
	public boolean isType(String type) {
		return type.equals("cock");
	}

	@Override
	public String getType() {
		return "cock";
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return mod.getHotness(self, opponent, getBase());
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		return mod.getPleasure(self, target, getBase());
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return mod.getSensitivity(target, getBase());
	}

	@Override
	public boolean isReady(Character self) {
		return mod.isReady(self, getBase());
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject save() {
		JSONObject save = new JSONObject();
		save.put("base", getBase().save());
		save.put("mod", mod.save());
		return save;
	}

	@Override
	public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		return mod.applyBonuses(self, opponent, target, damage, c, this);
	}

	@Override
	public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
			Combat c) {
		return mod.applySubBonuses(self, opponent, with, target, damage, c, this);
	}

	@Override
	public String getFluids(Character c) {
		return mod.getFluids(c, getBase());
	}

	@Override
	public boolean isVisible(Character c) {
		return mod.isVisible(c, getBase());
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@Override
	public boolean isNotable() {
		return mod.isNotable(getBase());
	}

	@Override
	public BodyPart upgrade() {
		return new ModdedCockPart((BasicCockPart) getBase().upgrade(), mod);
	}

	@Override
	public int compare(BodyPart other) {
		if (other instanceof ModdedCockPart) {
			ModdedCockPart otherMod = (ModdedCockPart) other;
			if (mod.equals(otherMod.mod)) {
				return getBase().compare(otherMod.getBase());
			} else {
				return mod.name().compareTo(otherMod.mod.name());
			}
		} else {
			return 0;
		}
	}

	@Override
	public BodyPart downgrade() {
		return new ModdedCockPart((BasicCockPart) getBase().downgrade(), mod);
	}

	@Override
	public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
		return mod.applyReceiveBonuses(self, opponent, target, damage, c, this);
	}

	@Override
	public String prefix() {
		return getBase().prefix();
	}

	@Override
	public String fullDescribe(Character c) {
		return mod.fullDescribe(c, getBase());
	}

	@Override
	public double priority(Character c) {
		return mod.priority(c, getBase());
	}

	@Override
	public int mod(Attribute a, int total) {
		return mod.mod(a, total, getBase());
	}

	@Override
	public BodyPart load(JSONObject obj) {
		JSONObject baseObj = (JSONObject) obj.get("base");
		JSONObject modObj = (JSONObject) obj.get("mod");
		BasicCockPart base = (BasicCockPart) getBase().load(baseObj);
		CockMod mod = this.mod.load(modObj);
		return new ModdedCockPart(base, mod);
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
		mod.tickHolding(c, self, opponent, otherOrgan, this);
	}

	@Override
	public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target) {
		mod.onStartPenetration(c, self, opponent, target, this);
	}

	@Override
	public void onEndPenetration(Combat c, Character self, Character opponent, BodyPart target) {
		mod.onEndPenetration(c, self, opponent, target, this);
	}

	@Override
	public int counterValue(BodyPart other) {
		if (mod == CockMod.primal) {
			return other == PussyPart.fiery ? 1 : other == PussyPart.arcane ? -1 : 0;
		}
		if (mod == CockMod.runic) {
			return other == PussyPart.succubus ? 1 : other == PussyPart.feral ? -1 : 0;
		}
		if (mod == CockMod.incubus) {
			return other == PussyPart.feral ? 1 : other == PussyPart.cybernetic ? -1 : 0;
		}
		if (mod == CockMod.bionic) {
			return other == PussyPart.arcane ? 1 : other == PussyPart.fiery ? -1 : 0;
		}
		if (mod == CockMod.enlightened) {
			return other == PussyPart.cybernetic ? 1 : other == PussyPart.succubus ? -1 : 0;
		}
		if (other.isGeneric()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double getSize() {
		return getBase().getSize();
	}

	@Override
	public boolean isGeneric() {
		return false;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A ");
		b.append(fullDescribe(c));
		b.append(" hangs between " + c.nameOrPossessivePronoun() + " legs.");
	}

	@Override
	public String describe(Character c) {
		return mod.describe(c, getBase());
	}

	@Override
	public void onOrgasm(Combat c, Character self, Character opponent, BodyPart target, boolean selfCame) {
		mod.onOrgasm(c, self, opponent, target, selfCame, this);
	}

	public BasicCockPart getBase() {
		return base;
	}

	public void setBase(BasicCockPart base) {
		this.base = base;
	}

	@Override
	public BodyPart applyMod(CockMod mod) {
		return base.applyMod(mod);
	}
}
