package nightgames.requirements;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Class for loading Requirements from JSON.
 */
public class JsonRequirementLoader implements RequirementLoader<JsonElement> {
    public List<Requirement> loadRequirements(JsonObject requirements) {
        return requirements.entrySet().stream().map(e -> loadRequirement(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

    @Override public Requirement loadRequirement(String key, JsonElement reqData) {
        switch (key) {
            case "anal":
                return loadAnal(reqData);
            case "and":
                return loadAnd(reqData);
            case "attribute":
                return loadAttribute(reqData);
            case "bodypart":
                return loadBodyPart(reqData);
            case "dom":
                return loadDom(reqData);
            case "duration":
                return loadDuration(reqData);
            case "inserted":
                return loadInserted(reqData);
            case "item":
                return loadItem(reqData);
            case "level":
                return loadLevel(reqData);
            case "mood":
                return loadMood(reqData);
            case "none":
                return loadNone(reqData);
            case "not":
                return loadNot(reqData);
            case "orgasms":
                return loadOrgasm(reqData);
            case "or":
                return loadOr(reqData);
            case "prone":
                return loadProne(reqData);
            case "random":
                return loadRandom(reqData);
            case "result":
                return loadResult(reqData);
            case "reverse":
                return loadReverse(reqData);
            case "specificbodypart":
                return loadSpecificBodyPart(reqData);
            case "position":
                return loadPosition(reqData);
            case "status":
                return loadStatus(reqData);
            case "sub":
                return loadSub(reqData);
            case "trait":
                return loadTrait(reqData);
            case "winning":
                return loadWinning(reqData);
            default:
                throw new IllegalArgumentException(
                                "Could not load requirements with key \"" + key + "\". Unknown requirements type.");
        }
    }

    @Override public AnalRequirement loadAnal(JsonElement reqData) {
        return new AnalRequirement();
    }

    @Override public AndRequirement loadAnd(JsonElement reqData) {
        return new AndRequirement(loadSubReqs(reqData.getAsJsonObject()));
    }

    @Override public AttributeRequirement loadAttribute(JsonElement reqData) {
        String att = reqData.getAsJsonObject().get("att").getAsString();
        int amount = reqData.getAsJsonObject().get("amount").getAsInt();
        return new AttributeRequirement(att, amount);
    }

    @Override public BodyPartRequirement loadBodyPart(JsonElement reqData) {
        return new BodyPartRequirement(reqData.getAsString());
    }

    @Override public DomRequirement loadDom(JsonElement reqData) {
        return new DomRequirement();
    }

    @Override public DurationRequirement loadDuration(JsonElement reqData) {
        return new DurationRequirement(reqData.getAsInt());
    }

    @Override public InsertedRequirement loadInserted(JsonElement reqData) {
        return new InsertedRequirement();
    }

    @Override public ItemRequirement loadItem(JsonElement reqData) {
        String item = reqData.getAsJsonObject().get("item").getAsString();
        int amount = reqData.getAsJsonObject().get("amount").getAsInt();
        return new ItemRequirement(item, amount);
    }

    @Override public LevelRequirement loadLevel(JsonElement reqData) {
        return new LevelRequirement(reqData.getAsInt());
    }

    @Override public MoodRequirement loadMood(JsonElement reqData) {
        return new MoodRequirement(reqData.getAsString());
    }

    @Override public NoneRequirement loadNone(JsonElement reqData) {
        return new NoneRequirement();
    }

    @Override public NotRequirement loadNot(JsonElement reqData) {
        return new NotRequirement(loadSubReqs(reqData.getAsJsonObject()).get(0));
    }

    @Override public OrgasmRequirement loadOrgasm(JsonElement reqData) {
        return new OrgasmRequirement(reqData.getAsInt());
    }

    @Override public OrRequirement loadOr(JsonElement reqData) {
        return new OrRequirement(loadSubReqs(reqData.getAsJsonObject()));
    }

    @Override public ProneRequirement loadProne(JsonElement reqData) {
        return new ProneRequirement();
    }

    @Override public RandomRequirement loadRandom(JsonElement reqData) {
        return new RandomRequirement(reqData.getAsFloat());
    }

    @Override public ResultRequirement loadResult(JsonElement reqData) {
        return new ResultRequirement(reqData.getAsString());
    }

    @Override public ReverseRequirement loadReverse(JsonElement reqData) {
        return new ReverseRequirement(loadSubReqs(reqData.getAsJsonObject()).get(0));
    }

    // TODO: figure out how to represent this in json
    @Override public SpecificBodyPartRequirement loadSpecificBodyPart(JsonElement reqData) {
        throw new RuntimeException("Loading SpecificBodyPartRequirements from JSON is not implemented.");
    }

    @Override public PositionRequirement loadPosition(JsonElement reqData) {
        return new PositionRequirement(reqData.getAsString());
    }

    @Override public StatusRequirement loadStatus(JsonElement reqData) {
        return new StatusRequirement(reqData.getAsString());
    }

    @Override public SubRequirement loadSub(JsonElement reqData) {
        return new SubRequirement();
    }

    @Override public TraitRequirement loadTrait(JsonElement reqData) {
        return new TraitRequirement(reqData.getAsString());
    }

    @Override public WinningRequirement loadWinning(JsonElement reqData) {
        return new WinningRequirement();
    }

    private List<Requirement> loadSubReqs(JsonObject reqData) {
        return reqData.entrySet().stream().map(e -> loadRequirement(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
    }

}
