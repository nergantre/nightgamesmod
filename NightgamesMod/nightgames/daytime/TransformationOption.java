package nightgames.daytime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import nightgames.characters.Character;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.items.Item;
import nightgames.requirements.Requirement;

public class TransformationOption {
    String option;
    Map<Item, Integer> ingredients;
    List<Requirement> requirements;
    String scene;
    String additionalRequirements;
    CustomEffect effect;
    Function<Character, Integer> moneyCost;

    public TransformationOption() {
        option = "";
        ingredients = new HashMap<Item, Integer>();
        moneyCost = c -> 0;
        scene = "";
        additionalRequirements = "";
        requirements = new ArrayList<>();
        effect = (c, self, other) -> {
            return true;
        };
    }
}
