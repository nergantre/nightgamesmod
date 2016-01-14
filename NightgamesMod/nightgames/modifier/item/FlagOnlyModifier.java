package nightgames.modifier.item;

import nightgames.ftc.FTCMatch;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.characters.Character;

public class FlagOnlyModifier extends ItemModifier {

    @Override
    public boolean itemIsBanned(Character c, Item i) {
        return ((FTCMatch) Global.getMatch()).isPrey(c) && i != Item.Flag;
    }

    @Override
    public String toString() {
        return "flag-only";
    }

}
