package nightgames.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nightgames.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for loading Requirements from JSON.
 */
public class JsonRequirementLoader implements RequirementLoader<JsonElement> {
    @Override public Requirement loadRequirement(String key, JsonElement reqData) {
        Requirement req;
        switch (key) {
            case "anal":
                req = loadAnal(reqData);
                break;
            case "and":
                req = loadAnd(reqData);
                break;
            case "attribute":
                req = loadAttribute(reqData);
                break;
            case "bodypart":
                req = loadBodyPart(reqData);
                break;
            case "dom":
                req = loadDom(reqData);
                break;
            case "duration":
                req = loadDuration(reqData);
                break;
            case "inserted":
                req = loadInserted(reqData);
                break;
            case "item":
                req = loadItem(reqData);
                break;
            case "level":
                req = loadLevel(reqData);
                break;
            case "mood":
                req = loadMood(reqData);
                break;
            case "none":
                req = loadNone(reqData);
                break;
            case "not":
                req = loadNot(reqData);
                break;
            case "orgasm":
                req = loadOrgasm(reqData);
                break;
            case "or":
                req = loadOr(reqData);
                break;
            case "prone":
                req = loadProne(reqData);
                break;
            case "random":
                req = loadRandom(reqData);
                break;
            case "result":
                req = loadResult(reqData);
                break;
            case "reverse":
                req = loadReverse(reqData);
                break;
            case "specificbodypart":
                req = loadSpecificBodyPart(reqData);
                break;
            case "stance":
                req = loadStance(reqData);
                break;
            case "status":
                req = loadStatus(reqData);
                break;
            case "sub":
                req = loadSub(reqData);
                break;
            case "trait":
                req = loadTrait(reqData);
                break;
            case "winning":
                req = loadWinning(reqData);
                break;
            default:
                throw new RuntimeException(
                                "Could not load requirement with key \"" + key + "\". Unknown requirement type.");
        }
        return req;
    }

    @Override public AnalRequirement loadAnal(JsonElement reqData) {
        return new AnalRequirement();
    }

    @Override public AndRequirement loadAnd(JsonElement reqData) {
        //return new AndRequirement(loadSubReqs((JSONObject) reqData));
        return null;
    }

    @Override public AttributeRequirement loadAttribute(JsonElement reqData) {
        //String att = reqData.get("att").getAsString();
        //int amount = reqData.get("amount").getAsInt();
        //return new AttributeRequirement(att, amount);
        return null;
    }

    @Override public BodyPartRequirement loadBodyPart(JsonElement reqData) {
        //String type = reqData.get("type").getAsString();
        //return new BodyPartRequirement(type);
        return null;
    }

    @Override public DomRequirement loadDom(JsonElement reqData) {
        return new DomRequirement();
    }

    @Override public DurationRequirement loadDuration(JsonElement reqData) {
        //int duration = reqData.get("duration").getAsInt();
        //return new DurationRequirement(duration);
        return null;
    }

    @Override public InsertedRequirement loadInserted(JsonElement reqData) {
        return new InsertedRequirement();
    }

    @Override public ItemRequirement loadItem(JsonElement reqData) {
        //String item = reqData.get("item").getAsString();
        //int amount = reqData.get("amount").getAsInt();
        //return new ItemRequirement(item, amount);
        return null;
    }

    @Override public LevelRequirement loadLevel(JsonElement reqData) {
        //int level = reqData.get("level").getAsInt();
        return null;
    }

    @Override public MoodRequirement loadMood(JsonElement reqData) {
        return null;
    }

    @Override public NoneRequirement loadNone(JsonElement reqData) {
        return null;
    }

    @Override public NotRequirement loadNot(JsonElement reqData) {
        return null;
    }

    @Override public OrgasmRequirement loadOrgasm(JsonElement reqData) {
        return null;
    }

    @Override public OrRequirement loadOr(JsonElement reqData) {
        return null;
    }

    @Override public ProneRequirement loadProne(JsonElement reqData) {
        return null;
    }

    @Override public RandomRequirement loadRandom(JsonElement reqData) {
        return null;
    }

    @Override public ResultRequirement loadResult(JsonElement reqData) {
        return null;
    }

    @Override public ReverseRequirement loadReverse(JsonElement reqData) {
        return null;
    }

    @Override public SpecificBodyPartRequirement loadSpecificBodyPart(JsonElement reqData) {
        return null;
    }

    @Override public StanceRequirement loadStance(JsonElement reqData) {
        return null;
    }

    @Override public StatusRequirement loadStatus(JsonElement reqData) {
        return null;
    }

    @Override public SubRequirement loadSub(JsonElement reqData) {
        return null;
    }

    @Override public TraitRequirement loadTrait(JsonElement reqData) {
        return null;
    }

    @Override public WinningRequirement loadWinning(JsonElement reqData) {
        return null;
    }

    private List<Requirement> loadSubReqs(JsonObject reqData) {
        return reqData.entrySet().stream().map(e -> loadRequirement(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

}
