package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;
import nightgames.modifier.item.BanConsumablesModifier;

public class NoItemsModifier extends BaseModifier {

    public NoItemsModifier() {
        items = new BanConsumablesModifier();
    }

    @Override
    public int bonus() {
        return 50;
    }

    @Override
    public String name() {
        return "noitems";
    }

    @Override
    public String intro() {
        return "<i>\"Tell me " + Global.getPlayer().name()
                        + ", are you the sort of player who spends all his winnings on disposable toys and traps to give yourself the edge? You'd "
                        + "probably be better off saving the money and relying more on your own abilities. If you can go the entire night without using any consumable items, I'll "
                        + "toss you a $" + bonus() + " bonus.\"</i>";
    }

    @Override
    public String acceptance() {
        return "Lilly nods, satisfied with your answer. <i>\"Excellent. Traps and items are so impersonal. Besides, who do you think has to clean them up at the end of the night? "
                        + "You're better off without them.\"</i>";
    }

}
