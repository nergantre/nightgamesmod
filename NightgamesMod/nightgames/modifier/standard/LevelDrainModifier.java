package nightgames.modifier.standard;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.modifier.BaseModifier;

public class LevelDrainModifier extends BaseModifier {
    public LevelDrainModifier() {
        custom = (c, m) -> {
            if (!c.human()) {
                if (c.getLevel() < Global.getPlayer().getLevel() + 5) {
                    c.addTemporaryTrait(Trait.ExpertLevelDrainer, 999);
                } else {
                    c.removeTemporarilyAddedTrait(Trait.ExpertLevelDrainer);
                }
                c.addTemporaryTrait(Trait.leveldrainer, 999);
                c.addTemporaryTrait(Trait.tight, 999);
                c.addTemporaryTrait(Trait.stronghold, 999);
                c.addTemporaryTrait(Trait.holecontrol, 999);
                c.addTemporaryTrait(Trait.polecontrol, 999);
                c.addTemporaryTrait(Trait.Unsatisfied, 999);
            }
        };
    }

    @Override
    public int bonus() {
        return 500;
    }

    @Override
    public String name() {
        return "leveldrainer";
    }

    @Override
    public String intro() {
        return "<i>\"" + Global.getPlayer().getName() + ", don't you think you are getting a bit too strong? "
             + "The girls aren't really winning many matches any more! The benefactor doesn't like that, oh no he doesn't. Technically, I'm supposed to put you "
             + "in a different rotation with more difficult opponents, but the girls seems vehemently against it. Isn't it nice to be loved? "
             + "But since we can't really let this continue the way it's going, I have a proposal for you.\"</i> Lilly rubs her hands together mischeviously and takes out "
             + "a small bottle with half a swallow of a dark liquid inside."
             + "<br/>"
             + "<i>\"You know those succubus drafts that the black market has been dealing in lately? The ones that seems to make girls suck out way more than just cum when you're fucking? "
             + "Well this is the advanced version of the stuff, one swallow and they'll be draining your strength right out of you. Permanently. "
             + "Plus, as a side effect, it makes them unable to get off without a good hard pounding. Think you can handle it champ? "
             + "I'll even throw in some interesting stuff as a bonus if you win some matches.\"</i>";
    }

    @Override
    public String acceptance() {
        return "Lilly nods approvingly as you accept. <i>\"Good! I'm glad I don't have to pull you out of these matches. Word of advice for tonight, you better get good at pulling out...\"</i>";
    }

    @Override
    public boolean isApplicable() {
        int playerLevel = Global.getPlayer().getLevel();
        double averageLevel = Global.getParticipants().stream().filter(p -> !p.human()).filter(p -> !Global.checkCharacterDisabledFlag(p)).mapToInt(Character::getLevel).average().orElse(0);
        return playerLevel > averageLevel + 5
                        && (Global.checkFlag(Flag.darkness) || Global.getPlayer().getRank() >= 2)
                        && Global.getParticipants().stream().noneMatch(p -> p.has(Trait.leveldrainer));
    }

    List<Item> EXTRA_LOOT = Arrays.asList(Item.BioGel, Item.RawAether, Item.LubricatingOils, Item.FeralMusk, Item.HolyWater, Item.ExtremeAphrodisiac, Item.nectar);
    @Override
    public void extraWinnings(Character player, int score) {
        if (score > 0) {
            Global.gui().message("Additionally, you get a few bottles from Lilly as extras prizes.");
            for (int i = 0; i < score; i++) {
                player.gain(Global.pickRandom(EXTRA_LOOT).get());
            }
        }
    }
}
