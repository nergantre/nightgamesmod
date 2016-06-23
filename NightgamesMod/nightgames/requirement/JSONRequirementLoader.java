package nightgames.requirement;

import nightgames.global.JSONUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ryplinn on 6/21/2016.
 */
public class JSONRequirementLoader implements RequirementLoader<JSONValue> {
    @Override public Requirement loadRequirement(String key, JSONValue reqData) {
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
                req = loadMood(reqData);
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

    @Override public AnalRequirement loadAnal(JSONValue reqData) {
        return new AnalRequirement();
    }

    @Override public AndRequirement loadAnd(JSONValue reqData) {
        //return new AndRequirement(loadSubReqs((JSONObject) reqData));
        return null;
    }

    @Override public AttributeRequirement loadAttribute(JSONValue reqData) {
        //String att = JSONUtils.readString(reqData, "att");
        //int amount = JSONUtils.readInteger(reqData, "amount");
        //return new AttributeRequirement(att, amount);
        return null;
    }

    @Override public BodyPartRequirement loadBodyPart(JSONValue reqData) {
        //String type = JSONUtils.readString(reqData, "type");
        //return new BodyPartRequirement(type);
        return null;
    }

    @Override public DomRequirement loadDom(JSONValue reqData) {
        return new DomRequirement();
    }

    @Override public DurationRequirement loadDuration(JSONValue reqData) {
        //int duration = JSONUtils.readInteger(reqData, "duration");
        //return new DurationRequirement(duration);
        return null;
    }

    @Override public InsertedRequirement loadInserted(JSONValue reqData) {
        return new InsertedRequirement();
    }

    @Override public ItemRequirement loadItem(JSONValue reqData) {
        //String item = JSONUtils.readString(reqData, "item");
        //int amount = JSONUtils.readInteger(reqData, "amount");
        //return new ItemRequirement(item, amount);
        return null;
    }

    @Override public LevelRequirement loadLevel(JSONValue reqData) {
        //int level = JSONUtils.readInteger(reqData, "level");
        return null;
    }

    @Override public MoodRequirement loadMood(JSONValue reqData) {
        return null;
    }

    @Override public NoRequirement loadNo(JSONValue reqData) {
        return null;
    }

    @Override public NotRequirement loadNot(JSONValue reqData) {
        return null;
    }

    @Override public OrgasmRequirement loadOrgasm(JSONValue reqData) {
        return null;
    }

    @Override public OrRequirement loadOr(JSONValue reqData) {
        return null;
    }

    @Override public ProneRequirement loadProne(JSONValue reqData) {
        return null;
    }

    @Override public RandomRequirement loadRandom(JSONValue reqData) {
        return null;
    }

    @Override public ResultRequirement loadResult(JSONValue reqData) {
        return null;
    }

    @Override public ReverseRequirement loadReverse(JSONValue reqData) {
        return null;
    }

    @Override public SpecificBodyPartRequirement loadSpecificBodyPart(JSONValue reqData) {
        return null;
    }

    @Override public StanceRequirement loadStance(JSONValue reqData) {
        return null;
    }

    @Override public StatusRequirement loadStatus(JSONValue reqData) {
        return null;
    }

    @Override public SubRequirement loadSub(JSONValue reqData) {
        return null;
    }

    @Override public TraitRequirement loadTrait(JSONValue reqData) {
        return null;
    }

    @Override public WinningRequirement loadWinning(JSONValue reqData) {
        return null;
    }

    private List<Requirement> loadSubReqs(JSONObject reqData) {
        List<Requirement> reqs = new ArrayList<Requirement>();
        for (Object keyObj : reqData.keySet()) {
            String key = (String) keyObj;
            //reqs.add(loadRequirement(key, (JSONObject) reqData.get(key)));
        }

        return null;
    }
}
