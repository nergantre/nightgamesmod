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
	
	void parse(String raw, TextFlow tf) {
		tf.getChildren().clear();
		if (self == null) {
			tf.getChildren().add(new Text("<<set characters to parse the scene>>\n\n"));
			tf.getChildren().add(new Text(raw));
		} else {
			tf.getChildren().add(new Text(escape(Global.format(raw, self, other))));
		}
	}
	
	public static String escape(String base) {
		String res = base.replaceAll("\"", "\\\\\"");
		res = res.replaceAll("\\r?\\n", "<br/>");
		res = res.replaceAll("\\\\", "\\\\");
		res = res.replaceAll("\\t", "\\\\t");
		return res;
	}
}
