package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import nightgames.characters.body.Body;

public class BodyVerifier implements VerifierModule {

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();
		
		JsonObject bodyObj = obj.get("body").getAsJsonObject();
		Body body = Body.load(bodyObj, null);
		checkSensibleNumber(bodyObj.get("hotness"), 1, 4, "hotness", list);
		
		if (!body.has("cock") && !body.has("pussy")) {
			list.add(note("No genitals were found; this tends not to play very well."));
		}
		
		return list;
	}

}
