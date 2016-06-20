package nightgames.characters.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Character;
import nightgames.requirement.AnalRequirement;
import nightgames.requirement.Requirement;
import nightgames.requirement.DomRequirement;
import nightgames.requirement.InsertedRequirement;
import nightgames.requirement.ReverseRequirement;
import nightgames.requirement.StanceRequirement;
import nightgames.requirement.StatusRequirement;
import nightgames.requirement.SubRequirement;
import nightgames.requirement.WinningRequirement;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

@SuppressWarnings("unchecked")
public enum CommentSituation {
    // Fucking
    VAG_DOM_PITCH_WIN(2, new InsertedRequirement(true), rev(new AnalRequirement(false)), new DomRequirement(),
                    new WinningRequirement()),
    VAG_DOM_PITCH_LOSE(2, new InsertedRequirement(true), rev(new AnalRequirement(false)), new DomRequirement(),
                    rev(new WinningRequirement())),
    VAG_DOM_CATCH_WIN(2, rev(new InsertedRequirement(true)), new AnalRequirement(false), new DomRequirement(),
                    new WinningRequirement()),
    VAG_DOM_CATCH_LOSE(2, rev(new InsertedRequirement(true)), new AnalRequirement(false), new DomRequirement(),
                    rev(new WinningRequirement())),
    VAG_SUB_PITCH_WIN(2, new InsertedRequirement(true), rev(new AnalRequirement(false)), new SubRequirement(),
                    new WinningRequirement()),
    VAG_SUB_PITCH_LOSE(2, new InsertedRequirement(true), rev(new AnalRequirement(false)), new SubRequirement(),
                    rev(new WinningRequirement())),
    VAG_SUB_CATCH_WIN(2, rev(new InsertedRequirement(true)), new AnalRequirement(false), new SubRequirement(),
                    new WinningRequirement()),
    VAG_SUB_CATCH_LOSE(2, rev(new InsertedRequirement(true)), new AnalRequirement(false), new SubRequirement(),
                    rev(new WinningRequirement())),
    ANAL_DOM_PITCH_WIN(2, new InsertedRequirement(true), rev(new AnalRequirement(true)), new DomRequirement(),
                    new WinningRequirement()),
    ANAL_DOM_PITCH_LOSE(2, new InsertedRequirement(true), rev(new AnalRequirement(true)), new DomRequirement(),
                    rev(new WinningRequirement())),
    ANAL_DOM_CATCH_WIN(2, rev(new InsertedRequirement(true)), new AnalRequirement(true), new DomRequirement(),
                    new WinningRequirement()),
    ANAL_DOM_CATCH_LOSE(2, rev(new InsertedRequirement(true)), new AnalRequirement(true), new DomRequirement(),
                    rev(new WinningRequirement())),
    ANAL_SUB_PITCH_WIN(2, new InsertedRequirement(true), rev(new AnalRequirement(true)), new SubRequirement(),
                    new WinningRequirement()),
    ANAL_SUB_PITCH_LOSE(2, new InsertedRequirement(true), rev(new AnalRequirement(true)), new SubRequirement(),
                    rev(new WinningRequirement())),
    ANAL_SUB_CATCH_WIN(2, rev(new InsertedRequirement(true)), new AnalRequirement(true), new SubRequirement(),
                    new WinningRequirement()),
    ANAL_SUB_CATCH_LOSE(2, rev(new InsertedRequirement(true)), new AnalRequirement(true), new SubRequirement(),
                    rev(new WinningRequirement())),

    // Stances
    BEHIND_DOM_WIN(1, new StanceRequirement("behind"), new DomRequirement(), new WinningRequirement()),
    BEHIND_DOM_LOSE(1, new StanceRequirement("behind"), new DomRequirement(), rev(new WinningRequirement())),
    BEHIND_SUB_WIN(1, new StanceRequirement("behind"), new SubRequirement(), new WinningRequirement()),
    BEHIND_SUB_LOSE(1, new StanceRequirement("behind"), new SubRequirement(), rev(new WinningRequirement())),
    SIXTYNINE_DOM_WIN(1, new StanceRequirement("sixnine"), new DomRequirement(), new WinningRequirement()),
    SIXTYNINE_DOM_LOSE(1, new StanceRequirement("sixnine"), new DomRequirement(), rev(new WinningRequirement())),
    SIXTYNINE_SUB_WIN(1, new StanceRequirement("sixnine"), new SubRequirement(), new WinningRequirement()),
    SIXTYNINE_SUB_LOSE(1, new StanceRequirement("sixnine"), new SubRequirement(), rev(new WinningRequirement())),
    MOUNT_DOM_WIN(1, new StanceRequirement("mount"), new DomRequirement(), new WinningRequirement()),
    MOUNT_DOM_LOSE(1, new StanceRequirement("mount"), new DomRequirement(), rev(new WinningRequirement())),
    MOUNT_SUB_WIN(1, new StanceRequirement("mount"), new SubRequirement(), new WinningRequirement()),
    MOUNT_SUB_LOSE(1, new StanceRequirement("mount"), new SubRequirement(), rev(new WinningRequirement())),
    NURSING_DOM_WIN(1, new StanceRequirement("nursing"), new DomRequirement(), new WinningRequirement()),
    NURSING_DOM_LOSE(1, new StanceRequirement("nursing"), new DomRequirement(), rev(new WinningRequirement())),
    FACESIT_DOM_WIN(1, new StanceRequirement("facesitting"), new DomRequirement(), new WinningRequirement()),
    FACESIT_DOM_LOSE(1, new StanceRequirement("facesitting"), new DomRequirement(), rev(new WinningRequirement())),
    PIN_DOM_WIN(1, new StanceRequirement("pin"), new DomRequirement(), new WinningRequirement()),
    PIN_DOM_LOSE(1, new StanceRequirement("pin"), new DomRequirement(), rev(new WinningRequirement())),
    PIN_SUB_WIN(1, new StanceRequirement("pin"), new SubRequirement(), new WinningRequirement()),
    PIN_SUB_LOSE(1, new StanceRequirement("pin"), new SubRequirement(), rev(new WinningRequirement())),
    ENGULFED_DOM(1, new StanceRequirement("engulfed"), new DomRequirement()),

    // Statuses
    SELF_BOUND(0, new StatusRequirement("bound")),
    OTHER_BOUND(0, rev(new StatusRequirement("bound"))),
    OTHER_STUNNED(0, rev(new StatusRequirement("stunned"))),
    OTHER_TRANCE(0, rev(new StatusRequirement("trance"))),
    OTHER_CHARMED(0, rev(new StatusRequirement("charmed"))),
    OTHER_ENTHRALLED(0, rev(new StatusRequirement("enthralled"))),
    SELF_HORNY(0, new StatusRequirement("horny")),
    OTHER_HORNY(0, rev(new StatusRequirement("horny"))),
    SELF_OILED(0, new StatusRequirement("oiled")),
    OTHER_OILED(0, rev(new StatusRequirement("oiled"))),
    SELF_COCKBOUND(0, new StatusRequirement("cockbound")),
    OTHER_COCKBOUND(0, rev(new StatusRequirement("cockbound"))),
    OTHER_PARASITED(0, rev(new StatusRequirement("parasited"))),

    NO_COMMENT(-1);

    private static final Map<String, Map<CommentSituation, String>> DEFAULT_COMMENTS;

    static {
        DEFAULT_COMMENTS = new HashMap<>();
        InputStream is = ResourceLoader.getFileResourceAsStream("data/DefaultComments.json");
        JSONArray root = (JSONArray) JSONValue.parse(new InputStreamReader(is));
        root.forEach(o -> loadDefaultComments((JSONObject) o));
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int priority;
    private final Set<Requirement> reqs;

    private CommentSituation(int priority, Requirement... reqs) {
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
                        .max(Comparator.comparingInt(CommentSituation::getPriority)).get();
    }

    private static Requirement rev(Requirement req) {
        return new ReverseRequirement(Arrays.asList(req));
    }

    public static Map<CommentSituation, String> getDefaultComments(String npcType) {
        return DEFAULT_COMMENTS.getOrDefault(npcType, Collections.emptyMap());
    }

    private static void loadDefaultComments(JSONObject obj) {
        String type = JSONUtils.readString(obj, "character");
        Map<CommentSituation, String> comments = new HashMap<>();
        JSONArray arr = (JSONArray) obj.get("comments");
        arr.forEach(o -> parseComment((JSONObject) o, comments));
        DEFAULT_COMMENTS.put(type, comments);
    }

    static void parseComment(JSONObject obj, Map<CommentSituation, String> target) {
        String type = JSONUtils.readString(obj, "situation");
        CommentSituation sit = valueOf(type);
        String comm = JSONUtils.readString(obj, "comment");
        target.put(sit, comm);
    }
}
