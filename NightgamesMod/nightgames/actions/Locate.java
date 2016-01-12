package nightgames.actions;

import java.io.PrintWriter;
import java.io.StringWriter;

import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.gui.GUI;
import nightgames.items.Item;

public class Locate extends Action {
	private static final long serialVersionUID = 1L;

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
		gui.message("Thinking back to your 'games' with Reyka," + " you take out one of your trophies so you can"
				+ " find its previous owner:");
		handleEvent(self, "Start");
		return Movement.locating;
	}

	public void handleEvent(Character self, String choice) {
		Character target;
		GUI gui = Global.gui();
		if (choice.equals("Start")) {
			Global.everyone().forEach((character) -> {
				if (self.has(character.getTrophy())) {
					gui.choose(this, character.getName(), self);
				}
			});
		} else if ((target = Global.getNPC(choice)) != null) {
			Item sought = target.getTrophy();
			if (sought == null) {
				StringWriter writer = new StringWriter();
				new UnsupportedOperationException().printStackTrace(new PrintWriter(writer));
				gui.clearText();
				gui.message("If you see this text ingame, something went wrong with"
						+ " the locator function. Please take the time to send the information"
						+ " below to The Silver Bard at his wordpress blog or Fenoxo's Forum: " + "\n\nSelf: "
						+ self.name() + "(" + self.human() + ")\n" + "Choice: " + choice + "\nStacktrace:\n"
						+ writer.toString());
				gui.clearCommand();
				gui.choose(this, "Leave", self);
			}
			String desc = sought.getName().split("\'s ")[1].toLowerCase();
			if (self.has(sought)) {
				Area area = target.location();
				gui.clearText();
				if (area != null) {
					gui.message("Focusing on the essence contained in the " + desc
							+ ", you attempt to scry for its owner's location. In your mind, an image of the "
							+ area.name + " appears. It falls apart as quickly as it came to be, but you know where "
							+ target.name()
							+ " currently is. Your hard-earned trophy is already burning up in those creepy "
							+ "purple flames, the smoke flowing from your nose straight to your crotch and setting another fire there.");
				} else {
					gui.message("Focusing on the essence contained in the " + desc
							+ ", you attempt to scry for its owner's location. However, you draw a blank. Your hard-earned trophy is already burning up in those creepy "
							+ "purple flames, the smoke flowing from your nose straight to your crotch and setting another fire there.");
				}
				self.tempt(15);
				self.consume(sought, 1);
				gui.clearCommand();
				gui.choose(this, "Leave", self);
			} else {
				gui.clearText();
				gui.message("You need some of " + target.name()
						+ "'s personal belongings to find her. Underwear would work.");
				execute(self);
			}
		} else if (choice.equals("Leave")) {
			gui.clearText();
			Global.getMatch().resume();
		} else {
			StringWriter writer = new StringWriter();
			new UnsupportedOperationException().printStackTrace(new PrintWriter(writer));
			gui.clearText();
			gui.message("If you see this text in game, something went wrong with"
					+ " the locator function. Please take the time to send the information"
					+ " below to The Silver Bard at his wordpress blog or Fenoxo's Forum: " + "\n\nSelf: " + self.name()
					+ "(" + self.human() + ")\n" + "Choice: " + choice + "\nStacktrace:\n" + writer.toString());
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
