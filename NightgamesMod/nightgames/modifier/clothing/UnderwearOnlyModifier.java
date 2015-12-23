package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.Set;

public class UnderwearOnlyModifier extends ClothingModifier {

	@Override
	public Set<Integer> allowedLayers() {
		return Collections.singleton(0);
	}
	
}
