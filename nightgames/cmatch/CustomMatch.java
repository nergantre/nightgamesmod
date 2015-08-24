package nightgames.cmatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.cmatch.prepare.ChangeSet;
import nightgames.cmatch.rules.StopRule;
import nightgames.global.Flag;
import nightgames.global.Global;

public abstract class CustomMatch {

	protected String name;
	protected Map<String, Area> map;
	private int roundCount;
	private boolean pause;
	protected int index;

	protected CustomMatch() {
		Global.gui().startMatch();
		this.pause = false;
		this.index = 0;
	}

	public abstract List<Character> getCombatants();

	public abstract Map<Character, Integer> getScores();

	public abstract String preparationScene();
	
	public abstract String endScene();

	protected abstract ChangeSet changeSet();

	protected abstract Collection<StopRule> stopRules();

	public final int getRoundCount() {
		return roundCount;
	}

	public final Map<String, Area> getMap() {
		return map;
	}

	public final void pause() {
		pause = true;
	}

	public final void resume() {
		pause = false;
		round();
	}

	protected final Collection<StopRule> defaultStopRules() {
		return Collections.singleton(StopRule.any(StopRule.timeout(36), StopRule.characterQuit(Global.getPlayer())));
	}
	
	public final void quit() {
		Character human = Global.getPlayer();
		if(human.state==State.combat){
			if(human.location().fight.getCombat()!=null){
				human.location().fight.getCombat().forfeit(human);
			}
			human.location().endEncounter();
		}
		human.travel(new Area("Retirement","",Movement.retire));
		human.state=State.quit;
		resume();
	}

	/*
	 * Flow is as follows: setUp -> preMatch -> [preRound -> [preTurn ->
	 * Character#move() -> postTurn] -> postRound] -> postMatch -> tearDown
	 * Where bracketed sections are repeated.
	 */

	public final void setUp() {
		getCombatants().forEach(c -> {
			Global.gui().message(Global.gainSkills(c));
			Global.learnSkills(c);
		});
		changeSet().applyChanges();
	}

	public final void tearDown() {
		changeSet().revertChanges();
		getCombatants().forEach(Character::finishMatch);
		Global.gui().clearText();
		Global.gui().message(endScene());
		if(Global.checkFlag(Flag.autosave)){
			Global.save(true);
		}
		Global.gui().endMatch();
	}

	protected void preMatch() {

	}

	protected void postMatch() {

	}

	protected void preRound() {

	}

	protected void postRound() {

	}

	protected final void defaultPreTurn(Character ch) {
		ch.upkeep();
	}

	protected final void defaultPostTurn(Character ch) {

	}

	protected void preTurn(Character ch) {
		defaultPreTurn(ch);
	}

	protected void postTurn(Character ch) {
		defaultPostTurn(ch);
	}

	public final void round() {
		preRound();
		while (!pause) {
			Character ch = getCombatants().get(index);
			preTurn(ch);
			ch.move();
			postTurn(ch);
			index = ++index % getCombatants().size();
		}
		postRound();
		roundCount++;
		if (stopRules().stream().anyMatch(s -> s.test(this))) {
			tearDown();
		} else {
			round();
		}
	}
}
