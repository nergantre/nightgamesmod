package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TraitsVerifier implements VerifierModule {

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();
		JsonObject stats = obj.get("stats").getAsJsonObject();
		JsonArray base = stats.get("base").getAsJsonObject().get("traits").getAsJsonArray();
		JsonArray growth = stats.get("growth").getAsJsonObject().get("traits").getAsJsonArray();

		assertOrWarn(base.size() > 0, "No starting traits were selected; are you sure about that?", list);
		assertOrWarn(growth.size() > 0, "No growth traits were selected; are you sure about that?", list);

		int startLevel = stats.get("base").getAsJsonObject().get("level").getAsInt();

		for (JsonElement el : growth) {
			int level = el.getAsJsonObject().get("level").getAsInt();
			if (!assertOrWarn(level >= startLevel,
					"Check the growth traits: at least one has a level below the starting level.", list))
				break;
			checkSensibleNumber(el.getAsJsonObject().get("level"), 1, 100,
					"Growth level " + level + " trait (" + el.getAsJsonObject().get("trait").getAsString() + ")", list);
		}
		
		JsonObject rs = stats.get("growth").getAsJsonObject().get("resources").getAsJsonObject();

		checkSensibleNumber(rs.get("stamina"), 0, 4, "stamina growth", list);
		checkSensibleNumber(rs.get("bonusStamina"), 0, 4, "stamina bonus growth", list);
		checkSensibleNumber(rs.get("arousal"), 0, 4, "arousal growth", list);
		checkSensibleNumber(rs.get("bonusArousal"), 0, 4, "arousal bonus growth", list);
		checkSensibleNumber(rs.get("mojo"), 0, 4, "mojo growth", list);
		checkSensibleNumber(rs.get("bonusMojo"), 0, 4, "mojo bonus growth", list);
		checkSensibleNumber(rs.get("willpower"), 0, 4, "willpower growth", list);
		checkSensibleNumber(rs.get("bonusWillpower"), 0, 4, "willpower bonus growth", list);

		JsonArray attrPoints = rs.get("points").getAsJsonArray();
		assertOrWarn(attrPoints.size() > 0, "No attribute points will be given on level up", list);
		checkSensibleNumber(rs.get("bonusPoints"), 0, 4, "bonus attribute points", list);
		
		return list;
	}

}
