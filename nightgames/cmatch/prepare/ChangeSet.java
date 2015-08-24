package nightgames.cmatch.prepare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.cmatch.CustomMatch;
import nightgames.characters.Character;

public class ChangeSet {

	private CustomMatch match;
	private Map<Character, List<PrematchChange>> changeLists;
	private List<PrematchChange> allChanges;
	
	private ChangeSet(){
		changeLists = new HashMap<>();
	}
	
	public ChangeSet(CustomMatch match, List<PrematchChange> changes) {
		this();
		this.match = match;
		match.getCombatants().forEach(c -> changeLists.put(c, new ArrayList<PrematchChange>()));
	}

	public void applyChanges() {
		for (Character c : changeLists.keySet()) {
			List<PrematchChange> changes = changeLists.get(c);
			allChanges.forEach(change -> change.apply(match, c, changes));
		}
	}
	
	public void revertChanges() {
		changeLists.forEach((ch, changes) -> changes.forEach(change -> change.revert(match, ch)));
	}
	
	public static ChangeSet emptyChangeSet() {
		return new ChangeSet();
	}
}
