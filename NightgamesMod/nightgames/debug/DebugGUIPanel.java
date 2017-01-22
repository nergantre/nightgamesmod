package nightgames.debug;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nightgames.characters.Character;
import nightgames.characters.Attribute;
import nightgames.characters.Trait;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Item;

@SuppressWarnings("unused")
public class DebugGUIPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 9001844139147018883L;

    private static List<DebugCommand> consoleCommands = new ArrayList<>();

    {
        consoleCommands.add(new DebugCommand("all\\.(.*)", (output, list) -> {
            Global.getParticipants().stream().forEach(participant -> {
                consoleCommands.stream().filter(cc -> cc.checkAndExecute(output, participant.getType() + "." + list.get(1))).findFirst();
            });
        }));
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
        consoleCommands.add(new DebugCommand("(\\w+)\\.move (\\w+)", (output, list) -> {
            try {
                Character target = Global.getCharacterByType(list.get(1));
                target.travel(Global.getMatch().getAreas().stream().filter(area -> area.name.toLowerCase().contains(list.get(2).toLowerCase())).findAny().get());
            } catch (NullPointerException e) {
                output.setText(list.get(1) + " is not a valid charater");
            }
        }));
        consoleCommands.add(new DebugCommand("(\\w+)\\.addTrait (\\w+)", (output, list) -> {
            try {
            	Character target = Global.getCharacterByType(list.get(1));
            	if (list.get(2).equals("all")) {
            		for (Trait t : Trait.values()) {
            			target.add(t);
            		}
            	} else {
	                target.add(Trait.valueOf(list.get(2)));
            	}
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

        consoleCommands.add(new DebugCommand("(\\w+)\\.addAtt (\\w+) ?(\\d+)?", (output, list) -> {
            try {
                Character target = Global.getCharacterByType(list.get(1));
                int amt = 1;
                if (list.size() > 3 && list.get(3) != null) {
                    amt = Integer.valueOf(list.get(3));
                }
                target.modAttributeDontSaveData(Attribute.valueOf(list.get(2)), amt);
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
                StringBuilder sb = new StringBuilder();
                sb.append("Level: " + target.getLevel() + "\n");
                List<Trait> traits = new ArrayList<>(target.getTraits());
                for (int i = 0; i < traits.size(); i++) {
                    sb.append(traits.get(i));
                    if (i % 4 == 2) {
                        sb.append("\n");
                    } else if (i != traits.size() - 1) {
                        sb.append(", ");
                    }
                }
                String attString = Arrays.stream(Attribute.values()).filter(att -> target.get(att) != 0).map(att -> String.format("%s: %d", att, target.get(att))).collect(Collectors.joining("\n"));
                output.setText(String.format("Stamina [%s]\nArousal [%s]\nMojo [%s]\nWillpower [%s]\nAttractiveness: %.01f\n%s\n%s",
                                target.getStamina().toString(), target.getArousal().toString(),
                                target.getMojo().toString(), target.getWillpower().toString(), target.body.getHotness(Global.getPlayer()), attString, sb.toString()));
            } catch (NullPointerException e) {
                output.setText(list.get(1) + " is not a valid charater");
            }
        }));
    }

    private JTextArea out;
    private static List<String> oldCommands = new ArrayList<>();
    private static int index = 0 ;

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
        console.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                index = oldCommands.size();
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && !oldCommands.isEmpty()) {
                    if (index == oldCommands.size() && !console.getText().isEmpty()) {
                        oldCommands.add(console.getText());
                    }
                    if (index > 0) {
                        index -= 1;
                    }
                    console.setText(oldCommands.get(index));
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN && !oldCommands.isEmpty()) {
                    if (index < oldCommands.size() - 1) {
                        index += 1;
                    }
                    console.setText(oldCommands.get(Math.min(oldCommands.size() - 1, index)));
                }
            }
        });
        console.addActionListener(action -> {
            out.setText("");
            String command = console.getText();
            if (!command.isEmpty()) {
                oldCommands.add(command);
            }
            index = oldCommands.size();
            Optional<DebugCommand> opt =
                            consoleCommands.stream().filter(cc -> cc.checkAndExecute(out, command)).findFirst();
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
