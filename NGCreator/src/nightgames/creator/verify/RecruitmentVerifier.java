package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class RecruitmentVerifier implements VerifierModule {

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();
		
		JsonObject rec = obj.get("recruitment").getAsJsonObject();

		assertOrWarn(rec.get("introduction").getAsString().isEmpty(), "Introduction scene is empty", list);
		assertOrWarn(rec.get("confirm").getAsString().isEmpty(), "Confirmation scene is empty", list);
		
		return list;
	}

}
