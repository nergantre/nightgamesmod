package nightgames.debug;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JTextArea;

public class DebugCommand {
	private Pattern pattern;
	private DebugEffect effect;

	public DebugCommand(String command, DebugEffect effect) {
		pattern = Pattern.compile(command);
		this.effect = effect;
	}

	public boolean checkAndExecute(JTextArea output, String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			effect.execute(output, IntStream.range(0, matcher.groupCount() + 1).mapToObj(i -> matcher.group(i))
					.collect(Collectors.toList()));
			return true;
		}
		return false;
	}
}
