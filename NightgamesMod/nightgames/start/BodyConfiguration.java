package nightgames.start;

import nightgames.characters.CharacterSex;
import nightgames.characters.body.*;
import nightgames.global.JSONUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class BodyConfiguration {

    private Archetype type;
    private Optional<GenitalConfiguration> genitals;
    private Optional<BreastsPart> breasts;
    private Optional<AssPart> ass;
    private Optional<EarPart> ears;
    private Optional<TailPart> tail;
    private Optional<WingsPart> wings;
    private List<TentaclePart> tentacles;
    private double hotness;

    BodyConfiguration() {
        type = Archetype.REGULAR;
        genitals = Optional.empty();
        breasts = Optional.empty();
        ass = Optional.empty();
        ears = Optional.empty();
        tail = Optional.empty();
        wings = Optional.empty();
        tentacles = new ArrayList<>();
        // TODO: This overwrites base character hotness if not specified in the StartConfiguration.
        hotness = 1.0;
    }

    static BodyConfiguration parse(JSONObject obj) {
        BodyConfiguration cfg = new BodyConfiguration();
        if (obj.containsKey("archetype"))
            cfg.type = Archetype.valueOf(JSONUtils.readString(obj, "archetype")
                                                  .toUpperCase());
        if (obj.containsKey("breasts"))
            cfg.breasts = Optional.of(BreastsPart.valueOf(JSONUtils.readString(obj, "breasts")
                                                                   .toLowerCase()));
        if (obj.containsKey("ass"))
            cfg.ass = Optional.of(JSONUtils.readString(obj, "ass")
                                           .equals("basic") ? AssPart.generic : new AnalPussyPart());

        if (obj.containsKey("ears"))
            cfg.ears = Optional.of(EarPart.valueOf(JSONUtils.readString(obj, "ears")
                                                            .toLowerCase()));
        if (obj.containsKey("tail") && !obj.get("tail").equals("none"))
            cfg.tail = Optional.of(TailPart.valueOf(JSONUtils.readString(obj, "tail")
                                                             .toLowerCase()));
        if (obj.containsKey("wings") && !obj.get("wings").equals("none"))
            cfg.wings = Optional.of(WingsPart.valueOf(JSONUtils.readString(obj, "wings")
                                                               .toLowerCase()));

        if (obj.containsKey("genitals"))
            cfg.genitals = Optional.of(GenitalConfiguration.parse((JSONObject) obj.get("genitals")));
        
        List<TentaclePart> list = new ArrayList<>();
        if (obj.containsKey("tentacles")) {
            JSONArray arr = (JSONArray) obj.get("tentacles");
            for (Object o : arr) {
                list.add(parseTentacle((JSONObject) o));
            }
        }
        cfg.tentacles = list;

        return cfg;
    }

    private static TentaclePart parseTentacle(JSONObject o) {
        String desc = JSONUtils.readString(o, "desc");
        String fluids = JSONUtils.readString(o, "fluids");
        String attachpoint = JSONUtils.readString(o, "attachpoint");
        double hotness = JSONUtils.readFloat(o, "hotness");
        double pleasure = JSONUtils.readFloat(o, "pleasure");
        double sensitivity = JSONUtils.readFloat(o, "sensitivity");
        return new TentaclePart(desc, attachpoint, fluids, hotness, pleasure, sensitivity);
    }

    void apply(Body body) {
/**/        genitals.ifPresent(gc -> gc.process(body));
        replaceIfPresent(body, breasts);
        replaceIfPresent(body, ass);
        replaceIfPresent(body, ears);
        replaceIfPresent(body, tail);
        replaceIfPresent(body, wings);
        tentacles.forEach(body::add);
        body.hotness = hotness;
    }

    private void replaceIfPresent(Body body, Optional<? extends BodyPart> part) {
        if (part.isPresent()) {
            body.addReplace(part.get(), 1);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ass == null) ? 0 : ass.hashCode());
        result = prime * result + ((breasts == null) ? 0 : breasts.hashCode());
        result = prime * result + ((ears == null) ? 0 : ears.hashCode());
        result = prime * result + ((genitals == null) ? 0 : genitals.hashCode());
        long temp;
        temp = Double.doubleToLongBits(hotness);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((tail == null) ? 0 : tail.hashCode());
        result = prime * result + ((tentacles == null) ? 0 : tentacles.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((wings == null) ? 0 : wings.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BodyConfiguration other = (BodyConfiguration) obj;
        if (ass == null) {
            if (other.ass != null)
                return false;
        } else if (!ass.equals(other.ass))
            return false;
        if (breasts == null) {
            if (other.breasts != null)
                return false;
        } else if (!breasts.equals(other.breasts))
            return false;
        if (ears == null) {
            if (other.ears != null)
                return false;
        } else if (!ears.equals(other.ears))
            return false;
        if (genitals == null) {
            if (other.genitals != null)
                return false;
        } else if (!genitals.equals(other.genitals))
            return false;
        if (Double.doubleToLongBits(hotness) != Double.doubleToLongBits(other.hotness))
            return false;
        if (tail == null) {
            if (other.tail != null)
                return false;
        } else if (!tail.equals(other.tail))
            return false;
        if (tentacles == null) {
            if (other.tentacles != null)
                return false;
        } else if (!tentacles.equals(other.tentacles))
            return false;
        if (type != other.type)
            return false;
        if (wings == null) {
            if (other.wings != null)
                return false;
        } else if (!wings.equals(other.wings))
            return false;
        return true;
    }



    static class GenitalConfiguration {
        Optional<CockConfiguration> cock;
        Optional<PussyPart> pussy;

        GenitalConfiguration() {
            cock = Optional.empty();
            pussy = Optional.empty();
        }

        public static GenitalConfiguration parse(JSONObject o) {
            GenitalConfiguration cfg = new GenitalConfiguration();
            if (o.containsKey("cock")) {
                CockConfiguration cock = new CockConfiguration();
                JSONObject co = (JSONObject) o.get("cock");
                if (co.containsKey("length")) {
                    cock.length = BasicCockPart.valueOf(JSONUtils.readString(co, "length").toLowerCase());
                }
                if (co.containsKey("type") && !co.get("type").equals("basic")) {
                    cock.type = Optional.of(CockMod.valueOf(JSONUtils.readString(co, "type").toLowerCase()));
                }
                cfg.cock = Optional.of(cock);
            }
            if (o.containsKey("pussy")) {
                cfg.pussy = Optional.of(PussyPart.valueOf(JSONUtils.readString(o, "pussy").toLowerCase()));
            }
            return cfg;
        }

        private void process(Body body) {
            body.removeAll("cock");
            body.removeAll("pussy");
            if (cock.isPresent())
                body.add(cock.get()
                             .build());
            if (pussy.isPresent())
                body.add(pussy.get());
        }
    }

    static class CockConfiguration {
        Optional<CockMod> type;
        BasicCockPart length;

        CockConfiguration() {
            type = Optional.empty();
            length = BasicCockPart.average;
        }

        private CockPart build() {
            return type.isPresent() ? new ModdedCockPart(length, type.get()) : length;
        }
    }


    enum Archetype {
        REGULAR(null, PussyPart.normal),
        DEMON(CockMod.incubus, PussyPart.succubus),
        CAT(CockMod.primal, PussyPart.feral),
        CYBORG(CockMod.bionic, PussyPart.cybernetic),
        ANGEL(CockMod.blessed, PussyPart.divine),
        WITCH(CockMod.runic, PussyPart.arcane),
        SLIME(CockMod.slimy, PussyPart.gooey);
        private CockMod cockMod;
        private PussyPart pussy;

        private Archetype(CockMod cockMod, PussyPart pussy) {
            this.cockMod = cockMod;
            this.pussy = pussy;
        }

        private void apply(Body body) {
            if (body.has("cock") && this != REGULAR)
                body.addReplace(new ModdedCockPart(BasicCockPart.average, cockMod), 1);
            if (body.has("pussy") && this != REGULAR)
                body.addReplace(pussy, 1);
            switch (this) {
                case ANGEL:
                    body.add(WingsPart.angelic);
                    break;
                case CAT:
                    body.add(TailPart.cat);
                    body.add(EarPart.cat);
                    break;
                case DEMON:
                    body.add(WingsPart.demonic);
                    body.add(TailPart.demonic);
                    body.add(EarPart.pointed);
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        String test = "{\"archetype\":\"regular\",\"genitals\":{\"cock\":{\"type\":\"bionic\",\"length\":\"huge"
                        + "\"}},\"breasts\":\"c\",\"ass\":\"AnalPussy\",\"ears\":\"pointed\",\"tail\":\"cat\",\"wings"
                        + "\":\"demonic\",\"tentacles\":[],\"hotness\":1.0}";
        BodyConfiguration cfg = parse((JSONObject) JSONValue.parse(test));
        Body body = new Body();
        cfg.apply(body);
        body.finishBody(CharacterSex.male);

        System.out.println(body);
    }
}
