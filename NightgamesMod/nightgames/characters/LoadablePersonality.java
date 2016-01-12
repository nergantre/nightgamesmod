package nightgames.characters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class LoadablePersonality extends BasePersonality {

	/**
	 *
	 */
	private static final long serialVersionUID = -5611239045633826129L;
	Scriptable scope;

	public LoadablePersonality(String path) {
		super();

		try {
			Reader reader = new FileReader(new File(path));
			scope = Global.cx.initStandardObjects(null, true);
			Object result = Global.cx.evaluateReader(scope, reader, path, 1, null);
			System.out.println(Context.toString(result));
			Object name = Global.cx.evaluateString(scope, "getName()", path, 1, null);
			character = new NPC(Context.toString(name), 1, this);
			ScriptableObject.putProperty(scope, "character", Context.javaToJS(character, scope));
			ScriptableObject.defineProperty(scope, "Trait", new NativeJavaClass(scope, Trait.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Result", new NativeJavaClass(scope, Result.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Attribute", new NativeJavaClass(scope, Attribute.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Emotion", new NativeJavaClass(scope, Emotion.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Item", new NativeJavaClass(scope, Item.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Clothing", new NativeJavaClass(scope, Clothing.class),
					ScriptableObject.READONLY);
			ScriptableObject.defineProperty(scope, "Flag", new NativeJavaClass(scope, Flag.class),
					ScriptableObject.READONLY);
			character.add(Trait.witch);
		} catch (FileNotFoundException e) {
			System.err.println("Could not load personality: " + path);
		} catch (IOException e) {
			System.err.println("Could not load personality: " + path);
		} catch (org.mozilla.javascript.EvaluatorException e) {
			System.err.println(e.getMessage());
		} catch (org.mozilla.javascript.EcmaError e) {
			System.err.println(e.getMessage());
		}
	}

	public String execute(String s, String scriptName) {
		try {
			Object o = Global.cx.evaluateString(scope, s, scriptName, 1, null);
			return Context.toString(o);
		} catch (org.mozilla.javascript.EvaluatorException e) {
			System.err.println(e.getMessage());
		} catch (EcmaError e) {
			System.err.println(e.getMessage());
		}
		return "";
	}

	@Override
	public void rest(int time) {
		// TODO Auto-generated method stub
	}

	@Override
	public String bbLiner(Combat c) {
		return execute("bbLiner()", "bbliner");
	}

	@Override
	public String nakedLiner(Combat c) {
		return execute("nakedLiner()", "nakedLiner");
	}

	@Override
	public String stunLiner(Combat c) {
		return execute("stunLiner()", "stunLiner");
	}

	@Override
	public String taunt(Combat c) {
		return execute("taunt()", "taunt");
	}

	@Override
	public String temptLiner(Combat c) {
		Character target = c.getOther(character);
		ScriptableObject.putProperty(scope, "target", Context.javaToJS(target, scope));
		return execute("taunt(target)", "taunt");
	}

	@Override
	public String victory(Combat c, Result flag) {
		ScriptableObject.putProperty(scope, "combatVar", Context.javaToJS(c, scope));
		ScriptableObject.putProperty(scope, "flagVar", Context.javaToJS(flag, scope));
		return execute("victory(combatVar, flagVar)", "victory");
	}

	@Override
	public String defeat(Combat c, Result flag) {
		ScriptableObject.putProperty(scope, "combatVar", Context.javaToJS(c, scope));
		ScriptableObject.putProperty(scope, "flagVar", Context.javaToJS(flag, scope));
		return execute("defeat(combatVar, flagVar)", "defeat");
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		ScriptableObject.putProperty(scope, "combatVar", Context.javaToJS(c, scope));
		ScriptableObject.putProperty(scope, "targetVar", Context.javaToJS(target, scope));
		ScriptableObject.putProperty(scope, "assistVar", Context.javaToJS(assist, scope));
		return execute("victory3p(combatVar, targetVar, assistVar)", "victory3p");
	}

	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		ScriptableObject.putProperty(scope, "combatVar", Context.javaToJS(c, scope));
		ScriptableObject.putProperty(scope, "targetVar", Context.javaToJS(target, scope));
		ScriptableObject.putProperty(scope, "assistVar", Context.javaToJS(assist, scope));
		return execute("intervene3p(combatVar, targetVar, assistVar)", "intervene3p");
	}

	@Override
	public String describe(Combat c) {
		return execute("describe()", "describe");
	}

	@Override
	public String draw(Combat c, Result flag) {
		ScriptableObject.putProperty(scope, "combatVar", Context.javaToJS(c, scope));
		ScriptableObject.putProperty(scope, "flagVar", Context.javaToJS(flag, scope));
		return execute("draw(combatVar, flagVar)", "draw");
	}

	@Override
	public boolean fightFlight(Character opponent) {
		ScriptableObject.putProperty(scope, "opponentVar", Context.javaToJS(opponent, scope));
		return Boolean.valueOf(execute("fightFlight(opponentVar)", "fightFlight"));
	}

	@Override
	public boolean attack(Character opponent) {
		ScriptableObject.putProperty(scope, "opponentVar", Context.javaToJS(opponent, scope));
		return Boolean.valueOf(execute("attack(opponentVar)", "fightFlight"));
	}

	@Override
	public String startBattle(Character other) {
		return execute("startBattle()", "startBattle");
	}

	@Override
	public boolean fit() {
		return Boolean.valueOf(execute("fit()", "fit"));
	}

	@Override
	public String night() {
		return execute("night()", "night");
	}

	@Override
	public boolean checkMood(Combat c, Emotion mood, int value) {
		ScriptableObject.putProperty(scope, "moodVar", Context.javaToJS(mood, scope));
		ScriptableObject.putProperty(scope, "valueVar", Context.javaToJS(value, scope));
		return Boolean.valueOf(execute("checkMood(moodVar, valueVar)", "checkMood"));
	}

	@Override
	public String orgasmLiner(Combat c) {
		return "";
	}

	@Override
	public String makeOrgasmLiner(Combat c) {
		return "";
	}
}
