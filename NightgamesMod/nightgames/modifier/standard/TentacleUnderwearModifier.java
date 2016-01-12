package nightgames.modifier.standard;

import java.util.function.BiConsumer;

import nightgames.characters.Character;
import nightgames.global.Match;
import nightgames.modifier.BaseModifier;
import nightgames.modifier.StatusModifier;
import nightgames.modifier.action.ActionModifier;
import nightgames.modifier.clothing.ClothingModifier;
import nightgames.modifier.clothing.ForceClothingModifier;
import nightgames.modifier.item.ItemModifier;
import nightgames.modifier.skill.SkillModifier;

public class TentacleUnderwearModifier extends BaseModifier {

	public TentacleUnderwearModifier() {
		this.clothing = new ForceClothingModifier("tentaclebottom");
	}

	@Override
	public int bonus() {
		return 75;
	}

	@Override
	public String name() {
		return "tentacle-underwear";
	}

	@Override
	public String intro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String acceptance() {
		// TODO Auto-generated method stub
		return null;
	}

}
