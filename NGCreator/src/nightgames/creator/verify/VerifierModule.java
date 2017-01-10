package nightgames.creator.verify;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@FunctionalInterface
public interface VerifierModule {

	List<VerificationResult> verfiy(JsonObject obj);

	default boolean assertOrError(boolean condition, String msg, List<VerificationResult> list) {
		if (!condition) {
			list.add(error(msg));
			return false;
		}
		return true;
	}

	default boolean assertOrWarn(boolean condition, String msg, List<VerificationResult> list) {
		if (!condition) {
			list.add(warn(msg));
			return false;
		}
		return true;
	}

	default VerificationResult error(String msg) {
		return new VerificationResult(MessageLevel.ERROR, msg);
	}

	default VerificationResult warn(String msg) {
		return new VerificationResult(MessageLevel.WARNING, msg);
	}

	default VerificationResult note(String msg) {
		return new VerificationResult(MessageLevel.NOTE, msg);
	}

	default boolean assertNumber(JsonElement el, String name, List<VerificationResult> list) {
		return assertOrError(el.isJsonPrimitive() && ((JsonPrimitive) el).isNumber(), name + " is not a number", list);
	}

	default void checkSensibleNumber(JsonElement el, int min, int max, String name, List<VerificationResult> list) {
		if (assertNumber(el, name, list)) {
			int n = el.getAsInt();
			if (assertOrError(n >= 0, name + " is negative", list)) {
				assertOrWarn(n >= min, name + " seems very low", list);
				assertOrWarn(n <= max, name + " seems very high", list);
			}
		}
	}

}
