package nightgames.creator.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;

public class AttributesVerifier implements VerifierModule {

	private static final List<Attribute> BASE_ATTRS = Arrays.asList(Attribute.Power, Attribute.Seduction,
			Attribute.Cunning);
	private static final List<Attribute> SPECIAL_ATTRS = Arrays.asList(Attribute.Speed, Attribute.Perception);

	@Override
	public List<VerificationResult> verfiy(JsonObject obj) {
		List<VerificationResult> list = new ArrayList<>();

		JsonObject attrs = obj.get("stats").getAsJsonObject().get("base").getAsJsonObject().get("attributes")
				.getAsJsonObject();

		for (Attribute attr : Attribute.values()) {
			if (BASE_ATTRS.contains(attr)) {
				checkSensibleNumber(attrs.get(attr.name()), 3, 60, attr.name(), list);
			} else if (SPECIAL_ATTRS.contains(attr)) {
				checkSensibleNumber(attrs.get(attr.name()), 2, 10, attr.name(), list);
			} else {
				checkSensibleNumber(attrs.get(attr.name()), 0, 60, attr.name(), list);
			}
		}
		
		return list;
	}

}
