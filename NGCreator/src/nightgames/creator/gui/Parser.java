package nightgames.creator.gui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nightgames.global.Global;
import nightgames.characters.Character;

final class Parser {
	
	private Character self, other;

	Parser(Character self, Character other) {
		this.self = self;
		this.other = other;
	}

	Parser() {
	}
	
	Character getSelf() {
		return self;
	}

	void setSelf(Character self) {
		this.self = self;
	}

	Character getOther() {
		return other;
	}

	void setOther(Character other) {
		this.other = other;
	}
	
	String parse(String raw) {
		StringBuilder sb = new StringBuilder();
		if (self == null) {
			sb.append("<<set characters to parse the scene>>\n\n");
			sb.append(raw);
		} else {
			sb.append(escape(Global.format(raw, self, other)));
		}
		return sb.toString();
	}
	
	static String escape(String base) {
		String res = base.replaceAll("\"", "\\\\\"");
		res = res.replaceAll("\\r?\\n", "<br/>");
		res = res.replaceAll("\\\\", "\\\\");
		res = res.replaceAll("\\t", "\\\\t");
		return res;
	}
}
