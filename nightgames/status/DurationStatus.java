package nightgames.status;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.custom.requirement.DurationRequirement;
import nightgames.combat.Combat;

public abstract class DurationStatus extends Status {
	private DurationRequirement req;
	public DurationStatus(String name, Character affected, int duration) {
		super(name, affected);
		if (affected.has(Trait.PersonalInertia)) {
			duration = Math.round(1.33f * duration);
		}
		req = new DurationRequirement(duration);
		
		requirements.add(req);
	}
	public int getDuration() {
		return req.duration;
	}

	public void setDuration(int duration) {
		req.duration = duration;
	}

	@Override
	public int regen(Combat c) {
		req.tick(1);
		return 0;
	}
	public void tick(int i) {
		req.tick(i);
	}
}
