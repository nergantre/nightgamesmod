package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nightgames.creator.gui.SceneStore;

public class LinesVerifier implements VerifierModule {

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();

		JsonObject lines = obj.get("lines").getAsJsonObject();
		SceneStore.REQUIRED_LINES.forEach(s -> {
			if (assertOrError(lines.has(s), "No lines for situation " + s, list)) {
				JsonArray line = lines.get(s).getAsJsonArray();
				if (assertOrError(line.size() > 0, "No lines for situation " + s, list)) {
					for (int i = 0; i < line.size() - 1; i++) {
						if (!assertOrWarn(
								line.get(i).getAsJsonObject().get("requirements").getAsJsonObject().entrySet()
										.size() > 0,
								"Requirement-less scene not in last position for situation " + s, list))
							break;
					}
				}
			}
		});

		return list;
	}

}
