package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;

public class NoModifier extends BaseModifier {

    @Override
    public int bonus() {
        return 0;
    }

    @Override
    public String name() {
        return "normal";
    }

    @Override
    public String intro() {
        return "<i>\"Sorry " + Global.getPlayer().getTrueName()
                        + ", there's no bonus money available tonight. Our Benefactor doesn't always give us the extra budget.\"<i/> She shrugs casually and "
                        + "brushes her pigtail over her shoulder. <i>\"There's nothing wrong with having a normal match. You don't want to get so caught up in gimmicks that you forget "
                        + "your fundamentals.\"</i> You give her a fairly neutral shrug and spend a few more minutes chatting with her before the match starts. She's surprisingly easy to "
                        + "talk to. You eventually head to your start point and arrive just before 10:00.";
    }

    @Override
    public String acceptance() {
        return "";
    }

}
