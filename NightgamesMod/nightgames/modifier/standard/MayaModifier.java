package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;

public class MayaModifier extends BaseModifier {

    // Does nothing

    @Override
    public int bonus() {
        return 0;
    }

    @Override
    public String name() {
        return "maya";
    }

    @Override
    public String intro() {
        return "When you arrive at the student union, you notice the girls have all "
                        + "gathered around Lilly. As you get closer, you notice Maya, the recruiter"
                        + ", standing next to her. She isn't usually present during a match, or at"
                        + " least you haven't seen her, so this must be a special occasion. Lilly"
                        + " gives you a nod of acknowledgement as you approach.<br/><br/><i>\"Is everyone"
                        + " here? Good. We have a rare treat tonight.\"</i> She motions toward the"
                        + " visitor. <i>\"Maya, who you have all met, is going to join in this "
                        + "match as a special guest. She is a veteran of the Games and has probably"
                        + " forgotten more about sexfighting than any of you have ever learned."
                        + " You wouldn't normally have an opportunity to face someone of her "
                        + "caliber, but she has graciously come out here to test you rookies.\""
                        + "</i> She brushes one of her pigtails aside for dramatic effect.<br/><br/>Maya "
                        + "smiles politely and gives a small curtsy. <i>\"I rarely find an "
                        + "opportunity to compete anymore, but I like to indulge every once in"
                        + " awhile.\"</i> Her eyes meet yours and something in her piercing gaze"
                        + " makes you feel like a small prey animal. Despite feeling intimidated,"
                        + " you feel your cock stir in your pants against your will. <i>\"I may "
                        + "be a bit rusty, but I'll try to set a good example for you.\"</i><br/><br/>"
                        + "Lilly takes the lead again. <i>\"If any of you actually manage to make "
                        + "Maya cum, I'll give you multiple points for it. Otherwise you can just"
                        + " consider this a learning opportunity and a chance to experience an "
                        + "orgasm at the hands of a master.\"</i><br/><br/>";
    }

    @Override
    public String acceptance() {
        return "";
    }

    @Override
    public boolean isApplicable() {
        return Global.getPlayer().getRank() > 0 && Global.getDate() % 15 == 0;
    }

}
