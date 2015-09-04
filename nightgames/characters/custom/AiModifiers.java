package nightgames.characters.custom;

import java.util.Map;

import nightgames.items.Item;
import nightgames.skills.Skill;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public class AiModifiers {

	private Map<Class<? extends Skill>, Double>		attackMods;
	private Map<Stance, Double>		positionMods;
	private Map<Stsflag, Double>	selfStatusMods;
	private Map<Stsflag, Double>	oppStatusMods;

	public AiModifiers() {
	}

	public AiModifiers(Map<Class<? extends Skill>, Double> attackMods,
			Map<Stance, Double> positionMods,
			Map<Stsflag, Double> selfStatusMods,
			Map<Stsflag, Double> oppStatusMods) {
		this.attackMods = attackMods;
		this.positionMods = positionMods;
		this.selfStatusMods = selfStatusMods;
		this.oppStatusMods = oppStatusMods;
	}

	public double modAttack(Class<? extends Skill> clazz) {
		return attackMods.getOrDefault(clazz, 0.0);
	}

	public double modPosition(Stance pos) {
		return positionMods.getOrDefault(pos, 0.0);
	}

	public double modSelfStatus(Stsflag flag) {
		return selfStatusMods.getOrDefault(flag, 0.0);
	}

	public double modOpponentStatus(Stsflag flag) {
		return oppStatusMods.getOrDefault(flag, 0.0);
	}

	public Map<Class<? extends Skill>, Double> getAttackMods() {
		return attackMods;
	}

	public void setAttackMods(Map<Class<? extends Skill>, Double> attackMods) {
		this.attackMods = attackMods;
	}

	public Map<Stance, Double> getPositionMods() {
		return positionMods;
	}

	public void setPositionMods(Map<Stance, Double> positionMods) {
		this.positionMods = positionMods;
	}

	public Map<Stsflag, Double> getSelfStatusMods() {
		return selfStatusMods;
	}

	public void setSelfStatusMods(Map<Stsflag, Double> selfStatusMods) {
		this.selfStatusMods = selfStatusMods;
	}

	public Map<Stsflag, Double> getOppStatusMods() {
		return oppStatusMods;
	}

	public void setOppStatusMods(Map<Stsflag, Double> oppStatusMods) {
		this.oppStatusMods = oppStatusMods;
	}

}
