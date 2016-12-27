package nightgames.creator.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

public class Verifier {

	private static final List<VerifierModule> MODULES = Collections.unmodifiableList(Arrays.asList(
			new BasicsVerifier(), new AttributesVerifier(), new BodyVerifier(), new TraitsVerifier(),
			new LinesVerifier(), new RecruitmentVerifier()
			));

	public static List<VerificationResult> verify(JsonObject obj) {
		return MODULES.stream().flatMap(m -> m.verfiy(obj).stream()).collect(Collectors.toList());
	}
}
