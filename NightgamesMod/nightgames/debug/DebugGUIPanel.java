package nightgames.debug;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Item;

public class DebugGUIPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 9001844139147018883L;

	private static List<DebugCommand> consoleCommands = new ArrayList<>();

	{
		consoleCommands.add(new DebugCommand("(\\w+)\\.setXP (\\d+)", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				target.setXP(Integer.valueOf(list.get(2)));
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			}
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.setMoney (\\d+)", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				target.setMoney(Integer.valueOf(list.get(2)));
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			}
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.addTrait (\\w+)", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				target.add(Trait.valueOf(list.get(2)));
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			} catch (IllegalArgumentException e) {
				output.setText(list.get(2) + " is not a valid trait");
			}
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.removeTrait (\\w+)", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				target.remove(Trait.valueOf(list.get(2)));
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			} catch (IllegalArgumentException e) {
				output.setText(list.get(2) + " is not a valid trait");
			}
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.addItem (\\w+) ?(\\d+)?", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				int amt = 1;
				if (list.size() > 3 && list.get(3) != null) {
					amt = Integer.valueOf(list.get(3));
				}
				target.gain(Item.valueOf(list.get(2)), amt);
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			} catch (IllegalArgumentException e) {
				output.setText(list.get(2) + " is not a valid item");
			}
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.ding( \\d+)?", (output, list) -> {
			Character target = Global.getCharacterByType(list.get(1));
			if (target == null) {
				output.setText(list.get(1) + " is not a valid charater");
				return;
			}
			int times = 1;
			if (list.size() > 2 && list.get(2) != null) {
				try {
					times = Integer.valueOf(list.get(2).trim());
				} catch (NumberFormatException e) {
				}
			}
			IntStream.range(0, times).forEach(i -> target.ding());

		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.ding( \\d+)?", (output, list) -> {
			Character target = Global.getCharacterByType(list.get(1));
			if (target == null) {
				output.setText(list.get(1) + " is not a valid charater");
				return;
			}
			int times = 1;
			if (list.size() > 2 && list.get(2) != null) {
				try {
					times = Integer.valueOf(list.get(2).trim());
				} catch (NumberFormatException e) {
				}
			}
			IntStream.range(0, times).forEach(i -> target.ding());
		}));
		consoleCommands.add(new DebugCommand("(\\w+)\\.list", (output, list) -> {
			try {
				Character target = Global.getCharacterByType(list.get(1));
				output.setText(String.format("Stamina [%s]\nArousal [%s]\nMojo [%s]\nWillpower [%s]\n",
						target.getStamina().toString(), target.getArousal().toString(), target.getMojo().toString(),
						target.getWillpower().toString()));
			} catch (NullPointerException e) {
				output.setText(list.get(1) + " is not a valid charater");
			}
		}));
	}

	private JTextArea out;

	public DebugGUIPanel() {
		add(new JLabel("Debuggers"));
		DebugFlags flags[] = DebugFlags.values();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel debugOptions = new JPanel(new GridLayout((flags.length + 2) / 3, 3));
		add(debugOptions);
		IntStream.range(0, flags.length).forEach(i -> {
			DebugFlags flag = flags[i];
			JCheckBox box = new JCheckBox(flag.name());
			box.setSelected(Global.debug[i]);
			box.addActionListener(event -> {
				Global.debug[i] = box.isSelected();
			});
			debugOptions.add(box);
		});
		out = new JTextArea();
		out.setOpaque(false);
		out.setRows(20);
		out.setEditable(false);
		out.setText("");
		add(out);

		JTextField console = new JTextField();
		console.addActionListener(action -> {
			out.setText("");
			String command = console.getText();
			Optional<DebugCommand> opt = consoleCommands.stream().filter(cc -> cc.checkAndExecute(out, command))
					.findFirst();
			if (!opt.isPresent()) {
				out.setText("Invalid Command");
			} else if (out.getText().isEmpty()) {
				out.setText("OK");
			}
			console.setText("");
		});
		add(console);
	}
}
