package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Shamed;

public class BreastRay extends Skill {
	public BreastRay(Character self) {
		super("Breast Ray", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Science)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&target.nude()&&getSelf().has(Item.Battery, 2);
	}
	
	@Override
	public float priorityMod(Combat c) {
		return 2.f;
	}

	@Override
	public String describe(Combat c) {
		return "Grow your opponent's boobs to make her more sensitive: 2 Batteries";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Battery, 2);

		boolean permanent = Global.random(20) == 0 && (getSelf().human() || target.human());
		if(getSelf().human()){
			c.write(getSelf(),deal(c,permanent ? 1 : 0, Result.normal, target));
		} else if(target.human()) {		
			c.write(getSelf(),receive(c,permanent ? 1 : 0, Result.normal, target));
		}
		target.add(c, new Hypersensitive(target));
		BreastsPart part = target.body.getBreastsBelow(BreastsPart.f.size);
		if (permanent) {
			if (part != null) {
				target.body.addReplace(part.upgrade(), 1);
			}
		} else {
			if (part != null) {
				target.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
			}
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new BreastRay(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		String message;
		message = "You point your growth ray at "+target.name()+"'s breasts and fire. Her breasts balloon up and the new sensitivity causes her to moan.";
		if (damage > 0)
			message += " You realize the effects are permanent!";
		return message;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String message;
		message = getSelf().name()+" points a device at your chest and giggles as your " + getSelf().body.getRandomBreasts().describe(getSelf())
					+ " starts ballooning up. You flush and cover yourself, but the increased sensitivity distracts you in a delicious way.";
		if (damage > 0)
			message += " You realize the effects are permanent!";
		return message;
	}

}
