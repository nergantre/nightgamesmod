package nightgames.characters.custom;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.*;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Character;
import nightgames.json.JsonUtils;
import nightgames.requirements.Requirement;
import nightgames.combat.Combat;

import static nightgames.requirements.RequirementShortcuts.*;

@SuppressWarnings("unchecked")
public enum CommentSituation {
    // Fucking
    VAG_DOM_PITCH_WIN(2, inserted(), rev(anal()), dom(), winning()), VAG_DOM_PITCH_LOSE(2, inserted(), rev(not(anal())),
                    dom(), rev(winning())), VAG_DOM_CATCH_WIN(2, rev(inserted()), not(anal()), dom(),
                    winning()), VAG_DOM_CATCH_LOSE(2, rev(inserted()), not(anal()), dom(),
                    rev(winning())), VAG_SUB_PITCH_WIN(2, inserted(), rev(not(anal())), sub(),
                    winning()), VAG_SUB_PITCH_LOSE(2, inserted(), rev(not(anal())), sub(),
                    rev(winning())), VAG_SUB_CATCH_WIN(2, rev(inserted()), not(anal()), sub(),
                    winning()), VAG_SUB_CATCH_LOSE(2, rev(inserted()), not(anal()), sub(),
                    rev(winning())), ANAL_DOM_PITCH_WIN(2, inserted(), rev(anal()), dom(),
                    winning()), ANAL_DOM_PITCH_LOSE(2, inserted(), rev(anal()), dom(),
                    rev(winning())), ANAL_DOM_CATCH_WIN(2, rev(inserted()), anal(), dom(),
                    winning()), ANAL_DOM_CATCH_LOSE(2, rev(inserted()), anal(), dom(),
                    rev(winning())), ANAL_SUB_PITCH_WIN(2, inserted(), rev(anal()), sub(),
                    winning()), ANAL_SUB_PITCH_LOSE(2, inserted(), rev(anal()), sub(),
                    rev(winning())), ANAL_SUB_CATCH_WIN(2, rev(inserted()), anal(), sub(),
                    winning()), ANAL_SUB_CATCH_LOSE(2, rev(inserted()), anal(), sub(), rev(winning())),

    // Stances
    BEHIND_DOM_WIN(1, position("behind"), dom(), winning()), BEHIND_DOM_LOSE(1, position("behind"), dom(),
                    rev(winning())), BEHIND_SUB_WIN(1, position("behind"), sub(), winning()), BEHIND_SUB_LOSE(1,
                    position("behind"), sub(), rev(winning())), SIXTYNINE_DOM_WIN(1, position("sixnine"), dom(),
                    winning()), SIXTYNINE_DOM_LOSE(1, position("sixnine"), dom(), rev(winning())), SIXTYNINE_SUB_WIN(1,
                    position("sixnine"), sub(), winning()), SIXTYNINE_SUB_LOSE(1, position("sixnine"), sub(),
                    rev(winning())), MOUNT_DOM_WIN(1, position("mount"), dom(), winning()), MOUNT_DOM_LOSE(1,
                    position("mount"), dom(), rev(winning())), MOUNT_SUB_WIN(1, position("mount"), sub(),
                    winning()), MOUNT_SUB_LOSE(1, position("mount"), sub(), rev(winning())), NURSING_DOM_WIN(1,
                    position("nursing"), dom(), winning()), NURSING_DOM_LOSE(1, position("nursing"), dom(),
                    rev(winning())), FACESIT_DOM_WIN(1, position("facesitting"), dom(), winning()), FACESIT_DOM_LOSE(1,
                    position("facesitting"), dom(), rev(winning())), PIN_DOM_WIN(1, position("pin"), dom(),
                    winning()), PIN_DOM_LOSE(1, position("pin"), dom(), rev(winning())), PIN_SUB_WIN(1, position("pin"),
                    sub(), winning()), PIN_SUB_LOSE(1, position("pin"), sub(), rev(winning())), ENGULFED_DOM(1,
                    position("engulfed"), dom()),

    // Statuses
    SELF_BOUND(0, status("bound")), OTHER_BOUND(0, rev(status("bound"))), OTHER_STUNNED(0,
                    rev(status("stunned"))), OTHER_TRANCE(0, rev(status("trance"))), OTHER_CHARMED(0,
                    rev(status("charmed"))), OTHER_ENTHRALLED(0, rev(status("enthralled"))), SELF_HORNY(0,
                    status("horny")), OTHER_HORNY(0, rev(status("horny"))), SELF_OILED(0, status("oiled")), OTHER_OILED(
                    0, rev(status("oiled"))), SELF_COCKBOUND(0, status("cockbound")), OTHER_COCKBOUND(0,
                    rev(status("cockbound"))), OTHER_PARASITED(0, rev(status("parasited"))),

    NO_COMMENT(-1);

    private static final Map<String, Map<CommentSituation, String>> DEFAULT_COMMENTS;

    static {
        DEFAULT_COMMENTS = new HashMap<>();
        try (InputStreamReader reader = new InputStreamReader(
                        ResourceLoader.getFileResourceAsStream("data/DefaultComments.json"))) {
            JsonArray root = JsonUtils.rootJson(reader).getAsJsonArray();
            root.forEach(element -> loadDefaultComment(element.getAsJsonObject()));
        } catch (JsonParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDefaultComment(JsonObject object) {
        String type = object.get("character").getAsString();
        Map<CommentSituation, String> comments = new HashMap<>();
        JsonArray arr = object.getAsJsonArray("comments");
        arr.forEach(element -> parseComment(element.getAsJsonObject(), comments));
        DEFAULT_COMMENTS.put(type, comments);
    }

    static void parseComment(JsonObject obj, Map<CommentSituation, String> target) {
        String type = obj.get("situation").getAsString();
        CommentSituation situation = valueOf(type);
        String comm = obj.get("comment").getAsString();
        target.put(situation, comm);
    }

    private final int priority;
    private final Set<Requirement> reqs;

    CommentSituation(int priority, Requirement... reqs) {
        this.priority = priority;
        this.reqs = Collections.unmodifiableSet(new HashSet<>((Arrays.asList(reqs))));
    }

    public boolean isApplicable(Combat c, Character self, Character other) {
        return reqs.stream().allMatch(r -> r.meets(c, self, other));
    }

    public int getPriority() {
        return priority;
    }

    public static Set<CommentSituation> getApplicableComments(Combat c, Character self, Character other) {
        Set<CommentSituation> comments = EnumSet.allOf(CommentSituation.class);
        comments.removeIf(comm -> !comm.isApplicable(c, self, other));
        if (comments.isEmpty())
            return Collections.singleton(NO_COMMENT);
        return comments;
    }

    public static CommentSituation getBestComment(Combat c, Character self, Character other) {
        return getApplicableComments(c, self, other).stream()
                        .max(Comparator.comparingInt(CommentSituation::getPriority)).orElse(NO_COMMENT);
    }

    public static Map<CommentSituation, String> getDefaultComments(String npcType) {
        return DEFAULT_COMMENTS.getOrDefault(npcType, Collections.emptyMap());
    }
}
