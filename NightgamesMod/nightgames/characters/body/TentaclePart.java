package nightgames.characters.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.characters.DummyCharacter;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class TentaclePart extends GenericBodyPart {
    private static final BodyPartMod TentacleMod = () -> "TentacleMod";
    public static final BodyPart DUMMY_PART = new GenericBodyPart("tentacles", 1.0, 1.0, 0.0, "tentacles", "");
    public static final Character DUMMY_CHARACTER = new DummyCharacter("summoned tentacles", "tentaclesdummy", 1, DUMMY_PART);
    public String attachpoint;
    String fluids;
    private boolean printSynonym;
    static String allowedAttachTypes[] = {"ass", "mouth", "pussy", "hands", "feet", "tail", "cock"};

    public static void pleasureWithTentacles(Combat c, Character target, int strength, BodyPart targetPart) {
        target.body.pleasure(DUMMY_CHARACTER, DUMMY_PART, targetPart, strength, c);
    }
    
    public static TentaclePart randomTentacle(String desc, Body body, String fluids, double hotness, double pleasure,
                    double sensitivity) {
        Set<String> avail = new HashSet<String>(Arrays.asList(allowedAttachTypes));
        Set<String> parts = new HashSet<String>();
        for (BodyPart p : body.getCurrentParts()) {
            if (p instanceof TentaclePart) {
                avail.remove(((TentaclePart) p).attachpoint);
            }
            parts.add(p.getType());
        }

        avail.retainAll(parts);
        String type;
        ArrayList<String> availList = new ArrayList<String>(avail);
        if (avail.size() > 0) {
            type = availList.get(Global.random(availList.size()));
        } else {
            type = "back";
        }
        TentaclePart part = new TentaclePart(desc, type, fluids, hotness, pleasure, sensitivity);
        return part;
    }

    public TentaclePart(String desc, String attachpoint, String fluids, double hotness, double pleasure,
                    double sensitivity) {
        this(desc, attachpoint, fluids, hotness, pleasure, sensitivity, true);
    }

    public TentaclePart(String desc, String attachpoint, String fluids, double hotness, double pleasure,
                    double sensitivity, boolean printSynonym) {
        super(desc, "", hotness, pleasure, sensitivity, true, "tentacles", "");
        this.attachpoint = attachpoint;
        this.fluids = fluids;
        this.printSynonym = printSynonym;
    }

    public static String synonyms[] = {"mass", "clump", "nest", "group",};

    @Override
    public void describeLong(StringBuilder b, Character c) {
        if (printSynonym)
            b.append("A " + Global.pickRandom(synonyms) + " of ");
        else
            b.append("A ");
        b.append(describe(c));
        if (c.body.has(attachpoint)) {
            b.append(" sprouts from " + c.nameOrPossessivePronoun() + " " + attachpoint + ".");
        } else {
            b.append(" sprouts from " + c.nameOrPossessivePronoun() + " back.");
        }
    }

    @Override
    public String describe(Character c) {
        return desc;
    }

    @Override
    public String fullDescribe(Character c) {
        return attachpoint + " " + desc;
    }

    @Override
    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c) {
        if (with.isType(attachpoint) && Global.random(3) > -1) {
            c.write(self, Global.format("Additionally, {self:name-possessive} " + fullDescribe(self)
                            + " takes the opportunity to squirm against {other:name-possessive} "
                            + target.fullDescribe(opponent), self, opponent));
            opponent.body.pleasure(self, this, target, 5, c);
        }
        return 0;
    }

    @Override
    public boolean isReady(Character c) {
        return true;
    }

    @Override
    public String getFluids(Character c) {
        return fluids;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject saveToDict() {
        JSONObject res = new JSONObject();
        res.put("desc", desc);
        res.put("attachpoint", attachpoint);
        res.put("hotness", hotness);
        res.put("pleasure", pleasure);
        res.put("sensitivity", sensitivity);
        res.put("fluids", fluids);

        return res;
    }

    @Override
    public BodyPart loadFromDict(JSONObject dict) {
        try {
            GenericBodyPart part = new TentaclePart((String) dict.get("desc"), (String) dict.get("attachpoint"),
                            (String) dict.get("fluids"), ((Number) dict.get("hotness")).doubleValue(),
                            ((Number) dict.get("pleasure")).doubleValue(),
                            ((Number) dict.get("sensitivity")).doubleValue());
            return part;
        } catch (ClassCastException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public BodyPartMod getMod(Character self) {
        return TentacleMod;
    }
}
