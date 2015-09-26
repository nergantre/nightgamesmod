package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.BasicCockPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Shamed;

public class ShrinkRay extends Skill {

	public ShrinkRay(Character self) {
		super("Shrink Ray", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Science)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&target.mostlyNude()&&getSelf().has(Item.Battery, 2);
	}
	
	@Override
	public float priorityMod(Combat c) {
		return 2.f;
	}

	@Override
	public String describe(Combat c) {
		return "Shrink your opponent's 'assets' to damage her ego: 2 Batteries";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().consume(Item.Battery, 2);
		boolean permanent = Global.random(20) == 0 && (getSelf().human() || target.human()) && !target.has(Trait.stableform);
		if(getSelf().human()){
			if(target.hasDick()){
				c.write(getSelf(),deal(c,permanent ? 1 : 0, Result.special, target));
			} else {
				c.write(getSelf(),deal(c,permanent ? 1 : 0, Result.normal, target));
			}
		} else if(target.human()) {
			if(target.hasDick()){
				c.write(getSelf(),receive(c,permanent ? 1 : 0, Result.special, target));
			} else {
				c.write(getSelf(),receive(c,permanent ? 1 : 0, Result.normal, target));
			}
		}
		target.add(c, new Shamed(target));
		if (permanent) {
			if (target.hasDick()) {
				CockPart part = target.body.getCockAbove(BasicCockPart.tiny.size);
				if (part != null) {
					target.body.addReplace(part.downgrade(), 1);
				} else {
					target.body.remove(target.body.getRandomCock());
				}
			} else {
				BreastsPart part = target.body.getBreastsAbove(BreastsPart.flat.size);
				if (part != null) {
					target.body.addReplace(part.downgrade(), 1);
				}
			}
		} else {
			if (target.hasDick()) {
				CockPart part = target.body.getCockAbove(BasicCockPart.tiny.size);
				if (part != null) {
					target.body.temporaryAddOrReplacePartWithType(part.downgrade(), part, 10);
				} else {
					target.body.temporaryRemovePart(target.body.getRandom("cock"), 10);
				}
			} else {
				BreastsPart part = target.body.getBreastsAbove(BreastsPart.flat.size);
				if (part != null) {
					target.body.temporaryAddOrReplacePartWithType(part.downgrade(), part, 10);
				}
			}
		}
		target.loseMojo(c, 50);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ShrinkRay(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		String message;
		if(modifier==Result.special){
			message = "You aim your shrink ray at "+target.name()+"'s cock, shrinking her male anatomy. She turns red and glares at you in humiliation.";
		}
		else{
			message = "You point your shrink ray to turn "+target.name()+"'s breasts. She whimpers and covers her chest in shame.";
		}
		if (damage > 0)
			message += " She glares at you angrily when she realizes the effects are permanent!";
		return message;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String message;
		if(modifier==Result.special){
			message = getSelf().name()+" points a device at your groin and giggles as your genitals shrink. You flush in shame and cover yourself.";
		} else {
			message = getSelf().name()+" points a device at your chest and giggles as your " + getSelf().body.getRandomBreasts().describe(getSelf())
					+ " shrink. You flush in shame and cover yourself.";
		}
		if (damage == 0)
			message += " The effect wears off quickly, but the damage to your dignity lasts much longer.";
		else
			message += " You realize the effects are permanent!";
		return message;
	}

}
