package nightgames.requirements;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonRequirementSaver implements RequirementSaver<JsonElement> {

    @Override
    public RequirementSaver.SavedRequirement<JsonElement> saveRequirement(Requirement req) {
        JsonElement element;
        String name = ((BaseRequirement) req).getName();

        switch (name) {
            case "anal":
            case "dom":
            case "inserted":
            case "none":
            case "prone":
            case "sub":
            case "winning":
                element = new JsonObject();
                break;

            case "not":
                JsonObject sub = new JsonObject();
                RequirementSaver.SavedRequirement<JsonElement> subreq =
                                saveRequirement(((NotRequirement) req).getNegatedRequirement());
                sub.add(subreq.key, subreq.data);
                element = sub;
                break;
            case "reverse":
                sub = new JsonObject();
                subreq = saveRequirement(((ReverseRequirement) req).getReversedRequirement());
                sub.add(subreq.key, subreq.data);
                element = sub;
                break;

            case "and":
                List<Requirement> subReqs = ((AndRequirement) req).getSubRequirements();
                JsonArray arr = new JsonArray();
                subReqs.stream()
                       .map(r -> saveRequirement(r).data)
                       .forEach(arr::add);
                element = arr;
                break;
            case "or":
                subReqs = ((OrRequirement) req).getSubRequirements();
                arr = new JsonArray();
                subReqs.stream()
                       .map(r -> saveRequirement(r).data)
                       .forEach(arr::add);
                element = arr;
                break;

            case "attribute":
                JsonObject obj = new JsonObject();
                AttributeRequirement attReq = (AttributeRequirement) req;
                obj.addProperty("att", attReq.getAtt()
                                             .name());
                obj.addProperty("amount", attReq.getAmount());
                element = obj;
                break;
            case "bodypart":
                element = new JsonPrimitive(((BodyPartRequirement) req).getType());
                break;
            case "duration":
                element = new JsonPrimitive(((DurationRequirement) req).remaining());
                break;
            case "item":
                obj = new JsonObject();
                ItemRequirement itemReq = (ItemRequirement) req;
                obj.addProperty("item", itemReq.getItemAmount().item.name());
                obj.addProperty("amount", itemReq.getItemAmount().amount);
                element = obj;
                break;
            case "level":
                element = new JsonPrimitive(((LevelRequirement) req).getLevel());
                break;
            case "mood":
                element = new JsonPrimitive(((MoodRequirement) req).getMood()
                                                                   .name());
                break;
            case "orgasm":
                element = new JsonPrimitive(((OrgasmRequirement) req).getCount());
                break;
            case "random":
                element = new JsonPrimitive(((RandomRequirement) req).getThreshold());
                break;
            case "result":
                element = new JsonPrimitive(((ResultRequirement) req).getResult()
                                                                     .name());
                break;
            case "specificbodypart":
                throw new RuntimeException("Saving SpecificBodyPart is not implemented");
            case "status":
                element = new JsonPrimitive(((StatusRequirement) req).getFlag()
                                                                     .name());
                break;
            case "trait":
                element = new JsonPrimitive(((TraitRequirement) req).getTrait()
                                                                    .name());
                break;
            default:
                throw new RuntimeException("Saving requirement of type " + req.getClass()
                                                                              .getName()
                                + " not implemented");
        }

        if (name.equals("orgasm")) {
            name = "orgasms";
        }

        return new RequirementSaver.SavedRequirement<JsonElement>(element, name);
    }

}
