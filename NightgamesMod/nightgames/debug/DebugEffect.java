package nightgames.debug;

import java.util.List;

import javax.swing.JTextArea;

public interface DebugEffect {
	void execute(JTextArea output, List<String> args);
}
