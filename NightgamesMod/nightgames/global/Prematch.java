package nightgames.global;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;

import nightgames.characters.Airi;
import nightgames.characters.Player;
import nightgames.gui.SaveButton;
import nightgames.gui.SceneButton;
import nightgames.modifier.Modifier;
import nightgames.modifier.standard.MayaModifier;
import nightgames.modifier.standard.NoModifier;
import nightgames.modifier.standard.UnderwearOnlyModifier;

public class Prematch implements Scene {
    private Modifier type;

    public Prematch(Player player) {
        Global.current = this;
        Global.unflag(Flag.victory);
        ArrayList<JButton> choice = new ArrayList<JButton>();
        String message = "";
        if (player.getLevel() < 5) {
            message += "You arrive at the student union a few minutes before the start of the match. "
                            + "You have enough time to check in and make idle chat with your opponents before "
                            + "you head to your assigned starting point and wait. At exactly 10:00, the match is on.";
            type = new NoModifier();
            choice.add(new SceneButton("Start The Match"));
        } else if (!Global.checkFlag(Flag.metLilly)) {
            message += "You get to the student union a little earlier than usual. Cassie and Jewel are there already and you spend a few minutes talking with them while "
                            + "you wait for the other girls to show up. A few people are still rushing around making preparations, but it's not clear exactly what they're doing. "
                            + "Other than making sure there are enough spare clothes to change into, there shouldn't be too much setup required. Maybe they're responsible for "
                            + "making sure the game area is clear of normal students, but you can't imagine how a couple students could pull that off.<p>A girl who appears to be "
                            + "in charge calls you over. She has straight, red hair, split into two simple pigtails. Her features are unremarkable except for the freckles lightly "
                            + "spotting her face, but you could reasonably describe her as cute. She mentioned her name once. It was a kind of flower... Lilly? Yeah, "
                            + "that sounds right, Lilly Quinn.<p>Lilly gives you a thin smile that's somewhere between polite and friendly. <i>\"You seem to be doing alright for a beginner. Not "
                            + "everyone takes to this sort of competition so naturally. In fact, if you think you can handle a bit of a handicap, I've been authorized to offer "
                            + "a small bonus.\"</i> Well, you're not going to complain about some extra spending money. It's worth at least hearing her out. <i>\"Sometimes our Benefactor "
                            + "offers some extra prize money, but I can't just give it away for free.\"</i> You think you see a touch of malice enter her smile. <i>\"I came up with some "
                            + "additional rules to make the game a little more interesting. If you accept my rule, then the extra money will be added as a bonus to each point you "
                            + "score tonight.\"</i><p>It's an interesting offer, but it begs the question of why she's extending it to you specifically. Lilly smirks and brushes one of "
                            + "her pigtails over her shoulder. <i>\"Don't worry, I'm not giving you preferential treatment. You're very much not my type. On the other hand, I do like "
                            + "watching your opponents win, and by giving you a handicap I make that more likely to happen. I don't intend to unfairly pick on you though. Fortunately, "
                            + "you'll make more money for every fight you do win, "
                            + "so it's better for everyone.\"</i> That's.... You're not entirely sure how to respond to that. <i>\"For the first rule, I'll start with something simple: for "
                            + "tonight's match, you're only allowed to wear your underwear. Even when you come back here for a change of clothes, you'll only get your underwear. If you "
                            + "agree to this, I'll throw an extra $" + new UnderwearOnlyModifier().bonus()
                            + " on top of your normal prize for each point you score. Interested?\"</i>";
            type = new UnderwearOnlyModifier();
            Global.flag(Flag.metLilly);
            choice.add(new SceneButton("Do it"));
            choice.add(new SceneButton("Not interested"));
        } else if (player.getRank() > 0 && Global.getDate() % 30 == 0) {
            message = message + "When you arrive at the student union, you notice the girls have all "
                            + "gathered around Lilly. As you get closer, you notice Maya, the recruiter"
                            + ", standing next to her. She isn't usually present during a match, or at"
                            + " least you haven't seen her, so this must be a special occasion. Lilly"
                            + " gives you a nod of acknowledgement as you approach.<p><i>\"Is everyone"
                            + " here? Good. We have a rare treat tonight.\"</i> She motions toward the"
                            + " visitor. <i>\"Maya, who you have all met, is going to join in this "
                            + "match as a special guest. She is a veteran of the Games and has probably"
                            + " forgotten more about sexfighting than any of you have ever learned."
                            + " You wouldn't normally have an opportunity to face someone of her "
                            + "caliber, but she has graciously come out here to test you rookies.\""
                            + "</i> She brushes one of her pigtails aside for dramatic effect.<p>Maya "
                            + "smiles politely and gives a small curtsy. <i>\"I rarely find an "
                            + "opportunity to compete anymore, but I like to indulge every once in"
                            + " awhile.\"</i> Her eyes meet yours and something in her piercing gaze"
                            + " makes you feel like a small prey animal. Despite feeling intimidated,"
                            + " you feel your cock stir in your pants against your will. <i>\"I may "
                            + "be a bit rusty, but I'll try to set a good example for you.\"</i><p>"
                            + "Lilly takes the lead again. <i>\"If any of you actually manage to make "
                            + "Maya cum, I'll give you multiple points for it. Otherwise you can just"
                            + " consider this a learning opportunity and a chance to experience an "
                            + "orgasm at the hands of a master.\"</i><p>\n\nTODO: FIX CONDITION";

            type = new MayaModifier();
            choice.add(new SceneButton("Start The Match"));
        } else {
            message += "You arrive at the student union with about 10 minutes to spare before the start of the match. You greet each of the girls and make some idle chatter with "
                            + "them before you check in with Lilly to see if she has any custom rules for you.<p>";
            if (player.getRank() > 0 && Global.checkFlag(Flag.AiriEnabled) && !Global.checkFlag(Flag.Airi)) {
                message += "Before you have a chance to ask though, Lilly mentions to you that there is a new competitor. However, when you ask her for details, she only mentions that her "
                                + "name is Airi, and that she's a Biology student, while holding a visible smirk. Your instincts tells you something is wrong, but you decide to ignore it for now.<p>"
                                + "<b>Airi has entered the games.</b><p>";
                Global.newChallenger(new Airi());
                Global.flag(Flag.Airi);
            }
            type = offer(player);
            message += type.intro();
            if (type.name().equals("normal")) {
                choice.add(new SceneButton("Start The Match"));
            } else {
                choice.add(new SceneButton("Do it"));
                choice.add(new SceneButton("Not interested"));
            }
        }

        choice.add(new SaveButton());
        Global.gui().prompt(message, choice);
    }

    private Modifier offer(Player player) {
        if (Global.random(10) > 4) {
            return new NoModifier();
        }
        HashSet<Modifier> modifiers = Global.getModifierPool();
        modifiers.removeIf(mod -> !mod.isApplicable() || mod.name().equals("normal"));
        return Global.pickRandom(modifiers.toArray(new Modifier[] {}));
    }

    @Override
    public void respond(String response) {
        String message = "";
        ArrayList<JButton> choice = new ArrayList<JButton>();
        if (response.startsWith("Start")) {
            Global.dusk(type);
        } else if (response.startsWith("Not")) {
            type = new NoModifier();
            Global.dusk(type);
        } else if (response.startsWith("Do")) {
            message += type.acceptance();
            choice.add(new SceneButton("Start The Match"));
            Global.gui().prompt(message, choice);
        }
    }
}
