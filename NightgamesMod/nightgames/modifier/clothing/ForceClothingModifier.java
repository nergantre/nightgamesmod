package nightgames.modifier.clothing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ForceClothingModifier extends ClothingModifier {

	private final Set<String> ids;
	
	public ForceClothingModifier(String... ids) {
		this.ids = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ids)));
	}
	
	@Override
	public Set<String> forcedItems() {
		return ids;
	}
}
