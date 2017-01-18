package nightgames.start;

import static nightgames.start.ConfigurationUtils.mergeOptionals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nightgames.characters.CharacterSex;
import nightgames.characters.body.AssPart;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.GenericCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.body.mods.ArcaneMod;
import nightgames.characters.body.mods.CyberneticMod;
import nightgames.characters.body.mods.DivineMod;
import nightgames.characters.body.mods.FeralMod;
import nightgames.characters.body.mods.GooeyMod;
import nightgames.characters.body.mods.PartMod;
import nightgames.characters.body.mods.SecondPussyMod;
import nightgames.characters.body.mods.DemonicMod;
import nightgames.json.JsonUtils;

class BodyConfiguration {

    protected Optional<Archetype> type;
    protected Optional<GenitalConfiguration> genitals;
    protected Optional<BreastsPart> breasts;
    protected Optional<AssPart> ass;
    protected Optional<EarPart> ears;
    protected Optional<TailPart> tail;
    protected Optional<WingsPart> wings;
    protected Optional<List<TentaclePart>> tentacles;
    protected Optional<Double> hotness;
    protected Optional<Double> faceFemininity;

    BodyConfiguration() {
        type = Optional.empty();
        genitals = Optional.empty();
        breasts = Optional.empty();
        faceFemininity = Optional.empty();
        ass = Optional.empty();
        ears = Optional.empty();
        tail = Optional.empty();
        wings = Optional.empty();
        tentacles = Optional.empty();
        hotness = Optional.empty();
    }
    
    BodyConfiguration(BodyConfiguration primaryConfig, BodyConfiguration secondaryConfig) {
        type = primaryConfig.type;
        genitals = mergeOptionals(primaryConfig.genitals, secondaryConfig.genitals);
        breasts = mergeOptionals(primaryConfig.breasts, secondaryConfig.breasts);
        ass = mergeOptionals(primaryConfig.ass, secondaryConfig.ass);
        faceFemininity = mergeOptionals(primaryConfig.faceFemininity, secondaryConfig.faceFemininity);
        ears = mergeOptionals(primaryConfig.ears, secondaryConfig.ears);
        tail = mergeOptionals(primaryConfig.tail, secondaryConfig.tail);
        wings = mergeOptionals(primaryConfig.wings, secondaryConfig.wings);
        tentacles = mergeOptionals(primaryConfig.tentacles, secondaryConfig.tentacles);
        hotness = mergeOptionals(primaryConfig.hotness, secondaryConfig.hotness);
    }

    static BodyConfiguration parse(JsonObject obj) {
        BodyConfiguration config = new BodyConfiguration();
        if (obj.has("archetype"))
            config.type = Optional.of(Archetype.valueOf(obj.get("archetype").getAsString().toUpperCase()));
        if (obj.has("breasts"))
            config.breasts = Optional.of(BreastsPart.valueOf(obj.get("breasts").getAsString()
                                                                   .toLowerCase()));
        if (obj.has("ass"))
            config.ass = Optional.of(obj.get("ass").getAsString()
                                           .equals("basic") ? AssPart.generateGeneric() : (AssPart)AssPart.generateGeneric().applyMod(new SecondPussyMod()));

        if (obj.has("ears"))
            config.ears = Optional.of(EarPart.valueOf(obj.get("ears").getAsString()
                                                            .toLowerCase()));
        if (obj.has("tail") && !obj.get("tail").getAsString().equals("none"))
            config.tail = Optional.of(TailPart.valueOf(obj.get("tail").getAsString()
                                                             .toLowerCase()));
        if (obj.has("wings") && !obj.get("wings").getAsString().equals("none"))
            config.wings = Optional.of(WingsPart.valueOf(obj.get("wings").getAsString()
                                                               .toLowerCase()));

        if (obj.has("genitals"))
            config.genitals = Optional.of(GenitalConfiguration.parse(obj.getAsJsonObject("genitals")));
        
        List<TentaclePart> list = new ArrayList<>();
        if (obj.has("tentacles")) {
            JsonArray arr = obj.getAsJsonArray("tentacles");
            for (Object o : arr) {
                list.add(parseTentacle((JsonObject) o));
            }
        }
        config.tentacles = Optional.of(list);

        if (obj.has("hotness")) {
            config.hotness = Optional.of((double) obj.get("hotness").getAsFloat());
        }

        if (obj.has("faceFemininity")) {
            config.faceFemininity = Optional.of((double) obj.get("faceFemininity").getAsFloat());
        }
        return config;
    }

    private static TentaclePart parseTentacle(JsonObject o) {
        String desc = o.get("desc").getAsString();
        String fluids = o.get("fluids").getAsString();
        String attachpoint = o.get("attachpoint").getAsString();
        double hotness = o.get("hotness").getAsFloat();
        double pleasure = o.get("pleasure").getAsFloat();
        double sensitivity = o.get("sensitivity").getAsFloat();
        return new TentaclePart(desc, attachpoint, fluids, hotness, pleasure, sensitivity);
    }

    void apply(Body body) {
        type.ifPresent(t -> t.apply(body));
        genitals.ifPresent(gc -> gc.apply(body));
        replaceIfPresent(body, breasts);
        replaceIfPresent(body, ass);
        replaceIfPresent(body, ears);
        replaceIfPresent(body, tail);
        replaceIfPresent(body, wings);
        replaceIfPresent(body, faceFemininity.map(fem -> new FacePart(Optional.ofNullable(body.getFace()).map(face -> face.hotness).orElse(0.0), fem)));
        applyTentacles(body);
        hotness.ifPresent(h -> body.hotness = h);
    }
    
    private void replaceIfPresent(Body body, Optional<? extends BodyPart> part) {
        if (part.isPresent()) {
            body.addReplace(part.get(), 1);
        }
    }

    private void applyTentacles(Body body) {
        if (tentacles.isPresent()) {
            body.removeAll("tentacles");
            tentacles.get().forEach(body::add);

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
        result = prime * result + ((hotness == null) ? 0 : hotness.hashCode());
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
        if (hotness == null) {
            if (other.hotness != null) {
                return false;
            }
        } else if (!hotness.equals(other.hotness))
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

        public static GenitalConfiguration parse(JsonObject object) {
            GenitalConfiguration config = new GenitalConfiguration();
            if (object.has("cock")) {
                CockConfiguration cock = new CockConfiguration();
                JsonObject cockJson = object.getAsJsonObject("cock");
                JsonUtils.getOptional(cockJson, "length").map(JsonElement::getAsInt)
                                .ifPresent(length -> cock.length = length);
                cock.type = JsonUtils.getOptional(cockJson, "type").flatMap(json -> CockMod.getFromType(json.getAsString()));
                config.cock = Optional.of(cock);
            }

            JsonUtils.getOptional(object, "pussy").ifPresent(modClass -> {
                if (modClass.getAsString().equals("normal")) {
                    config.pussy = Optional.of(PussyPart.generic);
                } else {
                    PartMod pussyMod = JsonUtils.gson.fromJson(modClass, PartMod.class);
                    config.pussy = Optional.of((PussyPart)PussyPart.generic.applyMod(pussyMod));
                }
            });
            return config;
        }

        private void apply(Body body) {
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
        int length;

        CockConfiguration() {
            type = Optional.empty();
            length = 6;
        }

        private CockPart build() {
            GenericCockPart generic = new GenericCockPart(length);
            return type.isPresent() ? (CockPart) generic.applyMod(type.get()) : generic;
        }
    }

    enum Archetype {
        REGULAR(null, PussyPart.generic),
        DEMON(CockMod.incubus, PussyPart.generic.applyMod(DemonicMod.INSTANCE)),
        CAT(CockMod.primal, PussyPart.generic.applyMod(FeralMod.INSTANCE)),
        CYBORG(CockMod.bionic, PussyPart.generic.applyMod(CyberneticMod.INSTANCE)),
        ANGEL(CockMod.blessed, PussyPart.generic.applyMod(DivineMod.INSTANCE)),
        WITCH(CockMod.runic, PussyPart.generic.applyMod(ArcaneMod.INSTANCE)),
        SLIME(CockMod.slimy, PussyPart.generic.applyMod(GooeyMod.INSTANCE));
        private final CockMod cockMod;
        private final BodyPart pussy;

        Archetype(CockMod cockMod, BodyPart pussy) {
            this.cockMod = cockMod;
            this.pussy = pussy;
        }

        private void apply(Body body) {
            if (body.has("cock") && this != REGULAR) {
                body.addReplace(body.getRandomCock().applyMod(cockMod), 1);
            }
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
}
