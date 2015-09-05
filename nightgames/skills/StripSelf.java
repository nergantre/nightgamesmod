package nightgames.skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.items.ItemEffect;
import nightgames.items.clothing.Clothing;

public class StripSelf extends Skill {
	public StripSelf(Character self) {
		super("Strip Self", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		boolean hasClothes = subChoices().size() > 0;
		return hasClothes&&getSelf().canAct()&&c.getStance().mobile(getSelf());
	}

	@Override
	public Collection<String> subChoices() {
		return getSelf().getOutfit().getAllStrippable().stream().map(clothing -> clothing.getName()).collect(Collectors.toList());
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Clothing clothing = null;
		if (getSelf().human()) {
			Optional<Clothing> stripped = getSelf().getOutfit().getEquipped().stream().filter(article -> article.getName().equals(choice)).findAny();
			if (stripped.isPresent()) {
				clothing = getSelf().getOutfit().unequip(stripped.get());
				c.getCombatantData(getSelf()).addToClothesPile(clothing);
			}
		} else {
			clothing = getSelf().stripRandom(c);
		}
		if (clothing == null) {
			c.write(getSelf(), "Skill failed...");
		} else {
			c.write(getSelf(), Global.format(String.format("{self:SUBJECT} stripped off %s%s", clothing.pre(), clothing.getName()), getSelf(), target));
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new StripSelf(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String describe(Combat c) {
		return "Strip yourself";
	}

	@Override
	public boolean makesContact() {
		return false;
	}
}
