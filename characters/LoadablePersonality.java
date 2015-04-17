package characters;

import items.Clothing;
import items.Item;

import java.io.*;

import global.Flag;
import global.Global;
import combat.Combat;
import combat.Result;

import org.mozilla.javascript.*;

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
            ScriptableObject.defineProperty(scope, "Trait", new NativeJavaClass
            		(scope, Trait.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Result", new NativeJavaClass
            		(scope, Result.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Attribute", new NativeJavaClass
            		(scope, Attribute.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Emotion", new NativeJavaClass
            		(scope, Emotion.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Item", new NativeJavaClass
            		(scope, Item.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Clothing", new NativeJavaClass
            		(scope, Clothing.class), ScriptableObject.READONLY);
            ScriptableObject.defineProperty(scope, "Flag", new NativeJavaClass
            		(scope, Flag.class), ScriptableObject.READONLY);
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
	public void rest() {
		// TODO Auto-generated method stub
	}

	@Override
	public String bbLiner() {
		return execute("bbLiner()", "bbliner");
	}

	@Override
	public String nakedLiner() {
		return execute("nakedLiner()", "nakedLiner");
	}

	@Override
	public String stunLiner() {
		return execute("stunLiner()", "stunLiner");
	}

	@Override
	public String winningLiner() {
		return execute("winningLiner()", "winningLiner");
	}

	@Override
	public String taunt() {
		return execute("taunt()", "taunt");
	}

	@Override
	public String temptLiner(Character target) {
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
	public String describe() {
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
	public String startBattle() {
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
	public void advance() {
		execute("advance()", "advance");
	}

	@Override
	public boolean checkMood(Emotion mood, int value) {
        ScriptableObject.putProperty(scope, "moodVar", Context.javaToJS(mood, scope));
        ScriptableObject.putProperty(scope, "valueVar", Context.javaToJS(value, scope));
		return Boolean.valueOf(execute("checkMood(moodVar, valueVar)", "checkMood"));
	}
}
