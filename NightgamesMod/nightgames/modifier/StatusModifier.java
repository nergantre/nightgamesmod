package nightgames.modifier;

import nightgames.characters.Character;
import nightgames.status.Status;

public class StatusModifier {

	public static final StatusModifier NULL_MODIFIER = new StatusModifier(){
		@Override
		public String toString() {
			return "null-status-modifier";
		}
	};
	
	private final Status status;
	private final boolean playerOnly;
	
	public StatusModifier(Status status, boolean playerOnly) {
		this.status = status;
		this.playerOnly = playerOnly;
	}
	
	public StatusModifier(Status status) {
		this(status, false);
	}
	
	private StatusModifier() {
		status = null;
		playerOnly = true;
	}
	
	public void apply(Character c) {
		if ((!playerOnly || c.human()) && status != null)
			c.add(status.instance(c, null));
	}
	
	public StatusModifier andThen(StatusModifier other) {
		StatusModifier me = this;
		return new StatusModifier() {
			public void apply(Character c) {
				me.apply(c);
				other.apply(c);
			}
		};
	}
	
	public static StatusModifier allOf(StatusModifier...mods) {
		if (mods.length == 0)
			return NULL_MODIFIER;
		StatusModifier result = mods[0];
		for (int i = 1; i < mods.length; i++)
			result = result.andThen(mods[i]);
		return result;
	}
	
	public String toString() {
		return status.name;
	}
}
