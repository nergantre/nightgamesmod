package nightgames.actions;

import java.io.PrintWriter;
import java.io.StringWriter;

import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.gui.GUI;
import nightgames.items.Item;
import nightgames.status.Detected;
import nightgames.status.Horny;

public class Locate extends Action {
    private static final long serialVersionUID = 1L;
    private static final int MINIMUM_SCRYING_REQUIREMENT = 5;

    public Locate() {
        super("Locate");
    }

    @Override
    public boolean usable(Character self) {
        boolean hasUnderwear = false;
        for (Item i : self.getInventory().keySet()) {
            if (i.toString().contains("Trophy")) {
                hasUnderwear = true;
            }
        }
        return self.has(Trait.locator) && hasUnderwear;
    }

    @Override
    public Movement execute(Character self) {
        GUI gui = Global.gui();
        gui.clearCommand();
        gui.clearText();
        gui.validate();
        gui.message("Thinking back to your 'games' with Reyka, you take out a totem to begin a scrying ritual: ");
        handleEvent(self, "Start");
        return Movement.locating;
    }

    public void handleEvent(Character self, String choice) {
        Character target;
        GUI gui = Global.gui();
        if (choice.equals("Start")) {
            Global.getMatch().combatants.stream().filter(c -> self.getAffection(c) >= MINIMUM_SCRYING_REQUIREMENT)
                            .forEach((character) -> {
                                gui.choose(this, character.getName(), self);
                            });
            gui.choose(this, "Leave", self);
        } else if ((target = Global.getNPC(choice)) != null) {
            Area area = target.location();
            gui.clearText();
            if (area != null) {
                gui.message("Drawing on the dark energies inside the talisman, you attempt to scry for "
                                + target.nameOrPossessivePronoun() + " location. In your mind, an image of the <b><i>"
                                + area.name
                                + "</i></b> appears. It falls apart as quickly as it came to be, but you know where "
                                + target.name()
                                + " currently is. Your small talisman is already burning up in those creepy "
                                + "purple flames, the smoke flowing from your nose straight to your crotch and setting another fire there.");
                target.add(new Detected(target, 10));
            } else {
                gui.message("Drawing on the dark energies inside the talisman, you attempt to scry for "
                                + target.nameOrPossessivePronoun() + " location. "
                                + "However, you draw a blank. Your small talisman is already burning up in those creepy "
                                + "purple flames, the smoke flowing from your nose straight to your crotch and setting another fire there.");
            }
            self.add(new Horny(self, self.getArousal().max() / 10, 10, "Scrying Ritual"));
            self.consume(Item.Talisman, 1);
            gui.clearCommand();
            gui.choose(this, "Leave", self);
        } else if (choice.equals("Leave")) {
            gui.clearText();
            gui.clearCommand();
            Global.getMatch().resume();
        } else {
            StringWriter writer = new StringWriter();
            new UnsupportedOperationException().printStackTrace(new PrintWriter(writer));
            gui.clearText();
            gui.message("If you see this text in game, something went wrong with"
                            + " the locator function. Please take the time to send the information"
                            + " below to The Silver Bard at his wordpress blog or Fenoxo's Forum: " + "\n\nSelf: "
                            + self.name() + "(" + self.human() + ")\n" + "Choice: " + choice + "\nStacktrace:\n"
                            + writer.toString());
            gui.clearCommand();
            gui.choose(this, "Leave", self);
        }
    }

    @Override
    public Movement consider() {
        return Movement.locating;
    }

    @Override
    public boolean freeAction() {
        return true;
    }
}
