package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class BasicsVerifier implements VerifierModule {

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();
		if (assertOrError(obj.has("name"), "No name given", list)) {
			assertOrError(!obj.get("name").getAsString().isEmpty(), "Name is empty", list);
		}
		
		
		JsonObject baseStats = obj.get("stats").getAsJsonObject().get("base").getAsJsonObject();
		JsonObject resources = baseStats.get("resources").getAsJsonObject();
		checkSensibleNumber(baseStats.get("level"), 1, 100, "level", list);
		checkSensibleNumber(resources.get("stamina"), 20, 300, "stamina", list);
		checkSensibleNumber(resources.get("arousal"), 20, 300, "arousal", list);
		checkSensibleNumber(resources.get("mojo"), 20, 300, "mojo", list);
		checkSensibleNumber(resources.get("willpower"), 20, 300, "willpower", list);
		
		
		return list;
	}
	
}
