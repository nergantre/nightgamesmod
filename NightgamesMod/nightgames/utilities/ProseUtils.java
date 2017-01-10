package nightgames.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProseUtils {
    private static Map<String, String> FIRST_PERSON_TO_THIRD_PERSON = new HashMap<>();
    static {
        FIRST_PERSON_TO_THIRD_PERSON.put("can", "can");
        FIRST_PERSON_TO_THIRD_PERSON.put("could", "could");
        FIRST_PERSON_TO_THIRD_PERSON.put("may", "may");
        FIRST_PERSON_TO_THIRD_PERSON.put("might", "might");
        FIRST_PERSON_TO_THIRD_PERSON.put("shall", "shall");
        FIRST_PERSON_TO_THIRD_PERSON.put("should", "should");
        FIRST_PERSON_TO_THIRD_PERSON.put("will", "will");
        FIRST_PERSON_TO_THIRD_PERSON.put("would", "would");
        FIRST_PERSON_TO_THIRD_PERSON.put("must", "must");
        FIRST_PERSON_TO_THIRD_PERSON.put("are", "is");
        FIRST_PERSON_TO_THIRD_PERSON.put("have", "has");
    }
    private static List<String> ES_VERB_ENDINGS = Arrays.asList("ch", "sh", "s", "x", "o");
    private static String CONSONANTS = "bcdfghjklmnpqrstvwxyz";
    public static String getThirdPersonFromFirstPerson(String verb) {
        verb = verb.toLowerCase();
        if (FIRST_PERSON_TO_THIRD_PERSON.containsKey(verb)) {
            return FIRST_PERSON_TO_THIRD_PERSON.get(verb);
        }
        if (ES_VERB_ENDINGS.stream().anyMatch(verb::endsWith)) {
            return verb + "es";
        }
        String lastTwoLetters = verb.length() < 2 ? "xx" : verb.substring(verb.length() - 2, verb.length());
        if ('y' == lastTwoLetters.charAt(1) && CONSONANTS.indexOf(lastTwoLetters.charAt(0)) >= 0) {
            return verb.substring(0, verb.length() - 1) + "ies";
        }
        return verb + "s";
    }
}