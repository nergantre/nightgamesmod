package nightgames.modifier.standard;

import java.util.function.BiConsumer;

import nightgames.characters.Character;
import nightgames.global.Match;
import nightgames.modifier.BaseModifier;
import nightgames.modifier.StatusModifier;
import nightgames.modifier.action.ActionModifier;
import nightgames.modifier.clothing.ClothingModifier;
import nightgames.modifier.item.ItemModifier;
import nightgames.modifier.skill.SkillModifier;
import nightgames.status.Hypersensitive;

public class VibrationModifier extends BaseModifier {

	public VibrationModifier() {
		this.status = new StatusModifier(new Hypersensitive(null));
	}

	@Override
	public int bonus() {
		return 75;
	}

	@Override
	public String name() {
		return "vibration";
	}

}
