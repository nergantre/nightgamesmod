package nightgames.creator.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.MouthPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.custom.CustomNPC;
import nightgames.characters.custom.JsonSourceNPCDataLoader;
import nightgames.characters.custom.NPCData;
import nightgames.characters.custom.effect.MoneyModEffect;
import nightgames.creator.model.AiMod;
import nightgames.creator.model.ItemAmount;
import nightgames.creator.model.MouthType;
import nightgames.creator.model.TraitBean;
import nightgames.creator.req.CreatorRequirement;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.requirements.JsonRequirementLoader;
import nightgames.requirements.JsonRequirementSaver;

public class CreatorGui extends Application {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	@SuppressWarnings("serial")
	private static final Map<String, String> FORMAT_HELP = new HashMap<String, String>() {
		{
			put("{<char>:subject}", "Name if <char> is an NPC, 'you' for the player.");
			put("{<char>:pronoun}", "Regular pronoun - he/she/you.");
			put("{<char>:possessive}", "Possessive pronoun - his/her/your.");
			put("{<char>:direct-object}", "Direct object - him/her/you.");
			put("{<char>:reflective}", "Reflective prounoun - himself/herself/yourself.");
			put("{<char>:name-possessive}", "Possessive pronoun for player, name + 's for NPC.");
			put("{<char>:name}", "Take a guess.");
			put("{<char>:name-do}", "Direct object for player, name for NPC.");
			put("{<char>:action:<player>:<npc>}", "<player> if <char> is the player, else <npc>.");
			put("{<char>:pronoun-action:<player>:<npc>}",
					"Shorthand for {<char>:pronoun}{<char>:action:<player>:<npc>}");
			put("{<char>:subject-action:<player>:<npc>}",
					"Shorthand for {<char>:subject}{<char>:action:<player>:<npc>}");
			put("{<char>:body-part:<type>}", "Description of a certain body part.");
			put("{<char>:main-genitals}", "'dick', 'pussy' or 'crotch', depending on what's avaiable.");

		}
	};

	private Stage stage;

	@FXML
	private AnchorPane mainPane;
	@FXML
	private MenuItem menuNew;
	@FXML
	private MenuItem menuDisplay;
	@FXML
	private ListView<Clothing> outfit;
	@FXML
	private ChoiceBox<Clothing> clothingChoice;

	private Parser parser = new Parser();

	@FXML
	private TextField name;
	@FXML
	private ChoiceBox<CharacterSex> sex;
	@FXML
	private ChoiceBox<Item> trophy;
	@FXML
	private TextField defaultPortrait, staminaStart, arousalStart, mojoStart, willpowerStart, level;

	@FXML
	private TextField power, seduction, cunning, perception, speed, science, arcane, ki, dark, fetish, animism, bio,
			divinity, willpower, medicine, technique, submissive, hypnosis, nymphomania, slime, ninjutsu, temporal;
	private Map<String, TextField> attrMap;

	@FXML
	private ChoiceBox<BreastsPart> breasts;
	@FXML
	private ChoiceBox<BasicCockPart> cockSize;
	@FXML
	private ChoiceBox<CockMod> cockType;
	@FXML
	private ChoiceBox<PussyPart> pussy;
	@FXML
	private ChoiceBox<TailPart> tail;
	@FXML
	private ChoiceBox<EarPart> ears;
	@FXML
	private ChoiceBox<WingsPart> wings;
	@FXML
	private ChoiceBox<MouthType> mouth;
	@FXML
	private TextField hotness;

	@FXML
	private ListView<Trait> startTraits;
	@FXML
	private ListView<TraitBean> growthTraits;
	private ObservableList<TraitBean> growthList;
	@FXML
	private ChoiceBox<Trait> traitChoice;
	@FXML
	private TextField staminaBase, staminaBonus, arousalBase, arousalBonus, mojoBase, mojoBonus, willpowerBase,
			willpowerBonus, attrBonus;

	@FXML
	private TextField recruitmentCost;
	@FXML
	private ChoiceBox<Item> startItem;
	@FXML
	private TextField startItemAmount;
	@FXML
	private Button startItemAdd;
	@FXML
	private Button startItemRemove;
	@FXML
	private ListView<ItemAmount> startItemList;
	@FXML
	private ChoiceBox<Item> purchaseItem;
	@FXML
	private TextField purchaseItemAmount;
	@FXML
	private Button purchaseItemAdd;
	@FXML
	private Button purchaseItemRemove;
	@FXML
	private ListView<ItemAmount> purchaseItemList;
	@FXML
	private ChoiceBox<AiMod.Type> modType;
	@FXML
	private ChoiceBox<String> modValue;
	@FXML
	private TextField modWeight;
	@FXML
	private Button modAdd;
	@FXML
	private Button modRemove;
	@FXML
	private ListView<AiMod<?>> modList;

	@FXML
	private ChoiceBox<String> sceneType;
	@FXML
	private ChoiceBox<Integer> sceneIdx;
	@FXML
	private ChoiceBox<Character> otherBox;
	@FXML
	private Button sceneAdd, sceneDelete, sceneMove;
	@FXML
	private TreeView<CreatorRequirement> reqTree;
	@FXML
	private TextArea rawText, parsedText;

	@FXML
	private Menu loadMenu;
	@FXML
	private Tab sceneTab, verificationTab;
	@FXML
	private AnchorPane verificationPane;
	private VerificationList verification;

	private ObjectProperty<NPC> currentChar = new SimpleObjectProperty<>();
	private SceneStore store = new SceneStore();

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		mainPane = loader.load(Files.newInputStream(new File("./CreatorGui.fxml").toPath()));

		reqTree.setCellFactory(t -> new ReqCell());
		reqTree.setRoot(new TreeItem<>(CreatorRequirement.ROOT));
		Global.rebuildCharacterPool(Optional.empty());
		setupScenes();
		setupGeneric();
		setupOutfit();
		setupBody();
		setupAttrs();
		setupTraits();
		setupLoad();
		setupItems();
		setupAiMods();
		setupVerification();

		/*
		 * Alert beta = new Alert(AlertType.INFORMATION,
		 * "Please note that this tool is far from finished." +
		 * " Proceed with caution, and please let me (dndw) know if you have any feedback so far."
		 * ); beta.setTitle("Beta Status"); beta.showAndWait();
		 */

		Scene scene = new Scene(mainPane);
		stage = primaryStage;
		primaryStage.setTitle("Night Games - Scene Creator");
		primaryStage.setScene(scene);
		primaryStage.setOnHiding(e -> System.exit(0));
		primaryStage.show();
	}

	private void setupGeneric() {
		sex.getItems().addAll(CharacterSex.values());
		sex.getSelectionModel().select(CharacterSex.female);
		trophy.getItems().addAll(Item.values());
		trophy.getSelectionModel().select(Item.Lubricant);
	}

	private void setupOutfit() {
		outfit.getItems().addAll(Clothing.getByID("Tshirt"), Clothing.getByID("jeans"), Clothing.getByID("bra"),
				Clothing.getByID("panties"));
		clothingChoice.getItems().addAll(Clothing.clothingTable.values());
		clothingChoice.getItems().sort(Comparator.comparing(Clothing::getName));
	}

	private void setupBody() {
		breasts.getItems().addAll(BreastsPart.values());
		breasts.getSelectionModel().select(BreastsPart.c);

		cockType.getItems().addAll(CockMod.values());
		cockType.getItems().add(null);
		cockType.getSelectionModel().select(null);

		cockSize.getItems().addAll(BasicCockPart.values());
		cockSize.getItems().add(null);
		cockSize.getSelectionModel().select(null);
		cockSize.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
			cockType.setDisable(nw == null);
		});

		pussy.getItems().addAll(PussyPart.values());
		pussy.getItems().add(null);
		pussy.getSelectionModel().select(PussyPart.normal);
		hackNormalPussy();

		ears.getItems().addAll(EarPart.values());
		ears.getSelectionModel().select(EarPart.normal);

		mouth.getItems().addAll(MouthType.values());
		mouth.getSelectionModel().select(MouthType.normal);

		tail.getItems().addAll(TailPart.values());
		tail.getItems().add(null);
		tail.getSelectionModel().select(null);

		wings.getItems().addAll(WingsPart.values());
		wings.getItems().add(null);
		wings.getSelectionModel().select(null);
	}

	private void setupAttrs() {
		attrMap = new HashMap<>();
		attrMap.put("power", power);
		attrMap.put("seduction", seduction);
		attrMap.put("cunning", cunning);
		attrMap.put("perception", perception);
		attrMap.put("speed", speed);
		attrMap.put("science", science);
		attrMap.put("dark", dark);
		attrMap.put("arcane", arcane);
		attrMap.put("ki", ki);
		attrMap.put("bio", bio);
		attrMap.put("slime", slime);
		attrMap.put("divinity", divinity);
		attrMap.put("animism", animism);
		attrMap.put("fetish", fetish);
		attrMap.put("medicine", medicine);
		attrMap.put("technique", technique);
		attrMap.put("temporal", temporal);
		attrMap.put("hypnosis", hypnosis);
		attrMap.put("nymphomania", nymphomania);
		attrMap.put("submissive", submissive);
		attrMap.put("ninjutsu", ninjutsu);
		attrMap.put("willpower", willpower);
		attrMap.values().forEach(CreatorGui::setFieldNumericOnly);
	}

	private void setupTraits() {
		growthTraits.setCellFactory(l -> new TraitCell());
		growthTraits.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		growthList = FXCollections.observableArrayList();
		growthTraits.setItems(new SortedList<TraitBean>(growthList, Comparator.comparing(TraitBean::getLevel)));
		startTraits.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		traitChoice.getItems().addAll(Trait.values());
		traitChoice.getItems().removeIf(t -> t.toString().trim().isEmpty());
		traitChoice.getItems().sort(Comparator.comparing(Trait::toString));
		traitChoice.getSelectionModel().select(0);
	}

	private void setupLoad() {
		Global.allNPCs().stream().map(c -> {
			MenuItem item = new MenuItem(c.getTrueName());
			item.setOnAction(e -> load(c));
			return item;
		}).forEach(loadMenu.getItems()::add);
	}

	private void setupItems() {
		startItem.getItems().addAll(Item.values());
		purchaseItem.getItems().addAll(Item.values());
		setFieldNumericOnly(startItemAmount);
		setFieldNumericOnly(purchaseItemAmount);
	}

	private void setupAiMods() {
		modType.getItems().addAll(AiMod.Type.values());
		modType.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
			modValue.getItems().clear();
			modValue.getItems()
					.addAll(nw.build().getUniverse().stream().map(Object::toString).collect(Collectors.toSet()));
			modValue.getSelectionModel().select(nw.build().getValue().toString());
			modValue.getItems().sort(Comparator.naturalOrder());
		});
		modType.getSelectionModel().select(AiMod.Type.SKILL);

	}

	private void setupScenes() {
		otherBox.setItems(FXCollections.observableArrayList(Global.allNPCs()));
		otherBox.getSelectionModel().select(1);
		otherBox.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
			parser.setOther(nw);
			parseText(null);
		});
		sceneTab.setOnSelectionChanged(this::updateCharacter);
		currentChar.addListener((obs, old, nw) -> parser.setSelf(nw));
		parser.setOther(otherBox.getSelectionModel().getSelectedItem());

		sceneIdx.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
			try {
				SceneStore.Scene scene = store.getScenes(sceneType.getValue()).get(nw);
				rawText.setText(scene.getText());
				scene.fillTree(reqTree);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		});

		sceneType.getItems().addAll(SceneStore.REQUIRED_LINES);
		sceneType.getItems().addAll("recruitment intro", "recruitment confirmation");
		sceneType.getItems().sort(Comparator.naturalOrder());
		sceneType.getSelectionModel().select(0);
		sceneType.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
			rawText.setText("");
			parsedText.setText("");
			sceneIdx.getItems().clear();
			if (store.getScenes(nw).isEmpty()) {
				addScene(null);
			} else {
				IntStream.range(0, store.getScenes(nw).size()).forEach(sceneIdx.getItems()::add);
				sceneIdx.getSelectionModel().selectFirst();
			}
		});

		rawText.setOnKeyTyped(e -> {
			store.getScenes(sceneType.getValue()).get(sceneIdx.getValue()).setText(rawText.getText());
		});
		reqTree.setOnMouseExited(e -> {
			JsonObject obj = CreatorRequirement.allToJson(reqTree.getRoot());
			JsonRequirementLoader loader = new JsonRequirementLoader();
			store.getScenes(sceneType.getValue()).get(sceneIdx.getValue()).setReqs(loader.loadRequirements(obj));
		});

	}

	private void setupVerification() {
		verificationTab.setOnSelectionChanged(this::updateCharacter);
		verification = new VerificationList();
		verificationPane.getChildren().add(verification);
		currentChar.addListener(o -> verification.update(createJson()));
	}

	@FXML
	public void reset(ActionEvent event) {
		rawText.setText("");
		parsedText.setText("");
		reqTree.setRoot(new TreeItem<>(CreatorRequirement.ROOT));
	}

	@FXML
	public void displayJson(ActionEvent event) {
		String json = buildJson();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("JSON");
		TextArea text = new TextArea();
		text.setText(json);
		alert.getDialogPane().setExpandableContent(text);
		alert.getDialogPane().setExpanded(true);
		alert.showAndWait();
	}

	@FXML
	public void showTags(ActionEvent evt) {
		GridPane grid = new GridPane();
		String base = "The list below shows all available format tags. In every case,"
				+ " <char> must be replaced by either 'self' or 'other'. In the 'action' tags,"
				+ " <player> and <npc> can be replaced by any text which does not contain '}' or ':'."
				+ " Nested tags (tags within tags) are not supported. The name of every tag can be written"
				+ " in capital letters, causing the first letter of the result to be capitalized. "
				+ " (e.g. {self:POSSESSIVE} legs -> Her legs)";
		int i = 0;
		for (Map.Entry<String, String> ent : FORMAT_HELP.entrySet()) {
			Label key = new Label(ent.getKey());
			Label val = new Label(ent.getValue());
			grid.add(key, 0, i);
			grid.add(val, 1, i++);
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Format Tags");
		alert.setContentText(base);
		alert.getDialogPane().setExpandableContent(grid);
		alert.getDialogPane().setExpanded(true);
		alert.showAndWait();
	}

	@FXML
	private void addClothing(ActionEvent evt) {
		Clothing selected = clothingChoice.getValue();
		if (selected != null) {
			outfit.getItems().add(selected);
		}
	}

	@FXML
	private void removeSelectedClothing(ActionEvent evt) {
		Collection<Clothing> selected = outfit.getSelectionModel().getSelectedItems();
		outfit.getItems().removeAll(selected);
	}

	@FXML
	private void addTraitAtStart(ActionEvent evt) {
		Trait selected = traitChoice.getValue();
		if (selected != null && !traitIsSelected(selected)) {
			startTraits.getItems().add(selected);
		}
	}

	@FXML
	private void addTraitAtLevel(ActionEvent evt) {
		Trait selected = traitChoice.getValue();
		if (selected != null && !traitIsSelected(selected)) {
			TraitBean bean = new TraitBean();
			bean.setTrait(selected);
			bean.setLevel(5);
			bean.levelProperty().addListener(o -> {
				growthList.add(new TraitBean());
				growthList.remove(new TraitBean());
			});
			growthList.add(bean);
		}
	}

	private boolean traitIsSelected(Trait t) {
		return startTraits.getItems().contains(t)
				|| growthTraits.getItems().stream().map(TraitBean::getTrait).anyMatch(t2 -> t == t2);
	}

	@FXML
	private void removeSelectedTraits(ActionEvent evt) {
		startTraits.getItems().removeAll(startTraits.getSelectionModel().getSelectedItems());
		growthList.removeAll(growthTraits.getSelectionModel().getSelectedItems());
	}

	private void showText(String txt) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setContentText(txt);
		alert.showAndWait();
	}

	private String buildJson() {
		JsonElement req = buildRequirements();
		JsonObject obj = new JsonObject();
		obj.add("requirements", req);
		obj.add("text", new JsonPrimitive(rawText.getText()));
		return GSON.toJson(obj);
	}

	@FXML
	public void close(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	public void parseText(KeyEvent event) {
		parsedText.setText(parser.parse(rawText.getText()));
	}

	@FXML
	public void validateRequirements(ActionEvent event) {
		List<String> errors = validate(reqTree.getRoot());
		AlertType type = errors.isEmpty() ? AlertType.INFORMATION : AlertType.WARNING;
		Alert alert = new Alert(type);
		alert.setHeaderText(errors.isEmpty() ? null : "Validation returned errors:");
		alert.setContentText(errors.isEmpty() ? "No errors found." : errors.stream().collect(Collectors.joining("\n")));
		alert.showAndWait();
	}

	private List<String> validate(TreeItem<CreatorRequirement> item) {
		List<String> errors = new ArrayList<>();
		item.getValue().getBox().getChildren().forEach(n -> {
			if (n instanceof TextField && ((TextField) n).getText().equals("")) {
				errors.add(item.getValue().get().name() + ": Enter a value in all fields.");
			}
			if (n instanceof ChoiceBox<?> && ((ChoiceBox<?>) n).getSelectionModel().getSelectedItem() == null) {
				errors.add(item.getValue().get().name() + ": Select a value in all fields.");
			}
		});
		int childCount = item.getChildren().size();
		if (!item.getValue().get().canHaveChildren(childCount)) {
			errors.add(item.getValue().get().name() + ": Invalid number of subrequirements.");
		}
		item.getChildren().stream().map(this::validate).forEach(errors::addAll);
		return errors;
	}

	private JsonObject buildRequirements() {
		JsonObject obj = new JsonObject();
		for (TreeItem<CreatorRequirement> item : reqTree.getRoot().getChildren()) {
			JsonObject sub = new JsonObject();
			// obj.add(item.getValue().getName(), sub);
			buildRequirements(item, obj);
		}
		return obj;
	}

	private void buildRequirements(TreeItem<CreatorRequirement> item, JsonObject toFill) {
		item.getValue().toJson(toFill);
		if (item.getChildren().size() > 0) {
			JsonElement arr = toFill.get(item.getValue().get().name().toLowerCase());
			for (TreeItem<CreatorRequirement> child : item.getChildren()) {
				if (arr.isJsonArray()) {
					JsonObject sub = new JsonObject();
					buildRequirements(child, sub);
					arr.getAsJsonArray().add(sub);
				} else {
					assert arr.isJsonObject();
					buildRequirements(child, arr.getAsJsonObject());
					toFill.add(item.getValue().get().name().toLowerCase(), arr);
				}
			}
		}
	}

	@FXML
	private void loadChar(ActionEvent evt) {
		FileChooser fc = new FileChooser();
		fc.setSelectedExtensionFilter(new ExtensionFilter("JSON Characters", ".json"));
		File file = fc.showOpenDialog(stage);
		if (file != null) {
			try {
				NPCData data = JsonSourceNPCDataLoader.load(new FileInputStream(file));
				Character ch = new CustomNPC(data).getCharacter();
				load(ch);
				MenuItem item = new MenuItem(data.getName());
				item.setOnAction(e -> load(ch));
				if (!loadMenu.getItems().contains(item)) {
					loadMenu.getItems().add(item);
				}
			} catch (JsonParseException | FileNotFoundException e) {
				Alert error = new Alert(AlertType.ERROR, "Error loading file: " + e.getMessage());
				error.showAndWait();
				e.printStackTrace();
			}
		}
	}

	private void load(Character ch) {
		store.clear();
		name.setText(ch.getTrueName());
		sex.getSelectionModel().select(ch.initialGender);
		trophy.getSelectionModel().select(ch.getTrophy());
		outfit.getItems().clear();
		outfit.getItems().addAll(ch.outfit.getAll());
		outfit.getItems().removeIf(c -> c == null);
		level.setText(ch.getLevel() + "");
		staminaStart.setText(ch.getStamina().max() + "");
		arousalStart.setText(ch.getArousal().max() + "");
		mojoStart.setText(ch.getMojo().max() + "");
		willpowerStart.setText(ch.getWillpower().max() + "");

		attrMap.entrySet().forEach(e -> e.getValue()
				.setText("" + ch.getPure(Attribute.valueOf(Global.capitalizeFirstLetter(e.getKey())))));
		breasts.getSelectionModel().select(ch.body.getRandomBreasts());

		CockPart cock = ch.body.getRandomCock();
		BasicCockPart size;
		CockMod mod;
		if (cock == null) {
			size = null;
			mod = null;
		} else if (cock instanceof BasicCockPart) {
			size = (BasicCockPart) cock;
			mod = null;
		} else {
			size = ((ModdedCockPart) cock).getBase();
			mod = (CockMod) ((ModdedCockPart) cock).getMod(ch);
		}
		cockSize.getSelectionModel().select(ch.hasDick() ? size : null);
		cockType.getSelectionModel().select(ch.hasDick() ? mod : null);

		pussy.getSelectionModel().select(ch.hasPussy() ? ch.body.getRandomPussy() : null);
		ears.getSelectionModel().select((EarPart) ch.body.getRandom("ears"));

		BodyPart mouthPart = ch.body.getRandom("mouth");
		MouthType type = mouthPart instanceof MouthPart ? MouthType.normal : MouthType.mouth_pussy;
		mouth.getSelectionModel().select(type);

		tail.getSelectionModel().select((TailPart) ch.body.getRandom("tail"));
		wings.getSelectionModel().select(ch.body.getRandomWings());
		hotness.setText(Math.round(ch.body.getHotness(ch)) + "");

		Collection<Trait> startTraits = ch.getTraits();
		this.startTraits.getItems().clear();
		this.startTraits.getItems().addAll(startTraits);
		growthList.clear();
		growthList.addAll(ch.getGrowth().getTraits().entrySet().stream().filter(e -> e.getKey() > 1)
				.flatMap(e -> e.getValue().stream().map(t -> {
					TraitBean bean = new TraitBean();
					bean.setLevel(e.getKey());
					bean.setTrait(t);
					// bean.levelProperty().addListener(o -> {
					// growthList.add(new TraitBean());
					// growthList.remove(new TraitBean());
					// });
					return bean;
				})).collect(Collectors.toSet()));
		staminaBase.setText(ch.getGrowth().stamina + "");
		staminaBonus.setText(ch.getGrowth().bonusStamina + "");
		arousalBase.setText(ch.getGrowth().arousal + "");
		arousalBonus.setText(ch.getGrowth().bonusArousal + "");
		mojoBase.setText("0");
		mojoBonus.setText("0");
		willpowerBase.setText(ch.getGrowth().willpower + "");
		willpowerBonus.setText(ch.getGrowth().bonusWillpower + "");

		NPC npc = ((NPC) ch);
		if (npc.ai instanceof CustomNPC) {
			NPCData data = ((CustomNPC) npc.ai).getData();
			data.getStartingItems().stream().map(ItemAmount::new).forEach(startItemList.getItems()::add);
			data.getPurchasedItems().stream().map(ItemAmount::new).forEach(purchaseItemList.getItems()::add);

			data.getAiModifiers().getAttackMods().entrySet().stream()
					.map(e -> new AiMod.Skill(e.getKey(), e.getValue())).forEach(modList.getItems()::add);
			data.getAiModifiers().getPositionMods().entrySet().stream()
					.map(e -> new AiMod.Position(e.getKey(), e.getValue())).forEach(modList.getItems()::add);
			data.getAiModifiers().getSelfStatusMods().entrySet().stream()
					.map(e -> new AiMod.SelfStatus(e.getKey(), e.getValue())).forEach(modList.getItems()::add);
			data.getAiModifiers().getOppStatusMods().entrySet().stream()
					.map(e -> new AiMod.OpponentStatus(e.getKey(), e.getValue())).forEach(modList.getItems()::add);

			data.getCharacterLines().forEach((key, scenes) -> scenes.forEach(ent -> {
				if (!sceneType.getItems().contains(key))
					sceneType.getItems().add(key);
				store.addScene(key, ent.getRawLine(), ent.getRequirements());
			}));
			Optional<SceneStore.Scene> initial = store.getAny();
			if (initial.isPresent()) {
				rawText.setText(initial.get().getText());
				initial.get().fillTree(reqTree);
			}
			if (data.getRecruitment().effects.size() > 0) {
				recruitmentCost.setText(-((MoneyModEffect) data.getRecruitment().effects.get(0)).getAmount() + "");
			}
			store.getScenes("recruitment intro").clear();
			store.addScene("recruitment intro", data.getRecruitment().introduction, data.getRecruitment().requirement);
			store.getScenes("recruitment confirmation").clear();
			store.addScene("recruitment confirmation", data.getRecruitment().confirm, Collections.emptyList());
		}
	}

	@FXML
	private void updateCharacter(Event evt) {
		CustomNPC pers = new CustomNPC(JsonSourceNPCDataLoader.load(createJson()));
		currentChar.set(new NPC(pers.getData().getName(), pers.getData().getStats().level, pers));
	}

	private JsonObject createJson() {
		JsonObject root = new JsonObject();
		root.addProperty("name", name.getText());
		root.addProperty("type", "CustomNPC" + name.getText());
		root.addProperty("sex", sex.getSelectionModel().getSelectedItem().name());
		root.addProperty("trophy", trophy.getSelectionModel().getSelectedItem().name());
		root.addProperty("plan", "hunting");
		root.addProperty("defaultPortrait", "angel_confident.jpg");

		JsonObject outfit = new JsonObject();
		JsonArray top = new JsonArray();
		this.outfit.getItems().forEach(c -> top.add(c.getID()));
		outfit.add("top", top);
		outfit.add("bottom", new JsonArray());
		root.add("outfit", outfit);

		JsonObject stats = new JsonObject();
		JsonObject base = new JsonObject();
		base.addProperty("level", Integer.parseInt(level.getText()));

		JsonObject attrs = new JsonObject();
		attrMap.entrySet().forEach(e -> attrs.addProperty(Global.capitalizeFirstLetter(e.getKey()),
				Integer.parseInt(e.getValue().getText())));
		base.add("attributes", attrs);

		JsonObject bresources = new JsonObject();
		bresources.addProperty("stamina", Integer.parseInt(staminaStart.getText()));
		bresources.addProperty("arousal", Integer.parseInt(arousalStart.getText()));
		bresources.addProperty("mojo", Integer.parseInt(mojoStart.getText()));
		bresources.addProperty("willpower", Integer.parseInt(willpowerStart.getText()));
		base.add("resources", bresources);

		JsonArray startTraits = new JsonArray();
		this.startTraits.getItems().stream().map(Trait::name).forEach(startTraits::add);
		base.add("traits", startTraits);
		stats.add("base", base);

		JsonObject growth = new JsonObject();
		JsonObject gresources = new JsonObject();
		gresources.addProperty("stamina", Double.parseDouble(staminaBase.getText()));
		gresources.addProperty("bonusStamina", Double.parseDouble(staminaBonus.getText()));
		gresources.addProperty("arousal", Double.parseDouble(arousalBase.getText()));
		gresources.addProperty("bonusArousal", Double.parseDouble(arousalBonus.getText()));
		gresources.addProperty("mojo", Double.parseDouble(mojoBase.getText()));
		gresources.addProperty("bonusMojo", Double.parseDouble(mojoBonus.getText()));
		gresources.addProperty("willpower", Double.parseDouble(willpowerBase.getText()));
		gresources.addProperty("bonusWillpower", Double.parseDouble(willpowerBonus.getText()));
		JsonArray points = new JsonArray();
		points.add(2);
		for (int i = 0; i < 5; i++)
			points.add(3);
		gresources.add("points", points);
		gresources.addProperty("bonusPoints", Integer.parseInt(attrBonus.getText()));
		growth.add("resources", gresources);

		JsonArray growthTraits = new JsonArray();
		this.growthList.stream().map(b -> {
			JsonObject obj = new JsonObject();
			obj.addProperty("level", b.getLevel());
			obj.addProperty("trait", b.getTrait().name());
			return obj;
		}).forEach(growthTraits::add);
		growth.add("traits", growthTraits);
		growth.add("preferredAttributes", new JsonArray());
		stats.add("growth", growth);
		root.add("stats", stats);

		JsonObject body = new JsonObject();
		JsonArray parts = new JsonArray();
		JsonObject breasts = new JsonObject();
		breasts.addProperty("class", "nightgames.characters.body.BreastsPart");
		breasts.addProperty("enum", this.breasts.getValue().name());
		parts.add(breasts);

		JsonObject ears = new JsonObject();
		ears.addProperty("class", "nightgames.characters.body.EarPart");
		ears.addProperty("enum", this.ears.getValue().name());
		parts.add(ears);

		JsonObject mouth = new JsonObject();
		MouthType type = this.mouth.getValue();
		mouth.addProperty("class", type == MouthType.normal ? "nightgames.characters.body.MouthPart"
				: "nightgames.characters.body.MouthPussyPart");
		mouth.addProperty("desc", type == MouthType.normal ? "mouth" : "mouth-pussy");
		mouth.addProperty("hotness", 0);
		mouth.addProperty("pleasure", 1);
		mouth.addProperty("sensitivity", 1);
		parts.add(mouth);

		JsonObject ass = new JsonObject();
		ass.addProperty("class", "nightgames.characters.body.AssPart");
		ass.addProperty("desc", "ass");
		ass.addProperty("hotness", 0.5);
		ass.addProperty("pleasure", 1.1);
		ass.addProperty("sensitivity", 1);
		parts.add(ass);

		BasicCockPart cockSize = this.cockSize.getValue();
		if (cockSize != null) {
			JsonObject cock = new JsonObject();
			CockMod mod = this.cockType.getValue();
			if (mod == null) {
				cock.addProperty("class", "nightgames.characters.body.BasicCockPart");
				cock.addProperty("enum", cockSize.name());
			} else {
				cock.addProperty("class", "nightgames.characters.body.ModdedCockPart");
				JsonObject cockBase = new JsonObject();
				cockBase.addProperty("enum", cockSize.name());
				JsonObject cockMod = new JsonObject();
				cockMod.addProperty("enum", mod.name());
				cock.add("base", cockBase);
				cock.add("mod", cockMod);
			}
			parts.add(cock);
		}

		PussyPart pussy = this.pussy.getValue();
		if (pussy != null) {
			JsonObject pussyObj = new JsonObject();
			pussyObj.addProperty("class", "nightgames.characters.body.PussyPart");
			pussyObj.addProperty("enum", pussy.name());
			parts.add(pussyObj);
		}
		body.add("parts", parts);
		body.addProperty("hotness", Double.parseDouble(hotness.getText()));
		root.add("body", body);

		JsonObject items = new JsonObject();
		JsonArray initial = new JsonArray();
		startItemList.getItems().stream().map(ia -> {
			JsonObject obj = new JsonObject();
			obj.addProperty("item", ia.getItem().name());
			obj.addProperty("amount", ia.getAmount());
			return obj;
		}).forEach(initial::add);
		items.add("initial", initial);

		JsonArray purchase = new JsonArray();
		purchaseItemList.getItems().stream().map(ia -> {
			JsonObject obj = new JsonObject();
			obj.addProperty("item", ia.getItem().name());
			obj.addProperty("amount", ia.getAmount());
			return obj;
		}).forEach(purchase::add);
		items.add("purchase", purchase);
		root.add("items", items);

		JsonArray aimods = new JsonArray();
		modList.getItems().stream().map(m -> {
			JsonObject obj = new JsonObject();
			obj.addProperty("type", m.getName());
			obj.addProperty("value", m.valueAsString());
			obj.addProperty("weight", m.getWeight());
			return obj;
		}).forEach(aimods::add);
		root.add("ai-modifiers", aimods);

		JsonObject lines = new JsonObject();
		SceneStore.REQUIRED_LINES.forEach(sit -> {
			JsonArray arr = new JsonArray();
			store.getScenes(sit).forEach(scene -> {
				JsonObject obj = new JsonObject();
				obj.addProperty("text", scene.getText());
				JsonObject reqs = new JsonObject();
				JsonRequirementSaver saver = new JsonRequirementSaver();
				scene.getReqs().stream().map(saver::saveRequirement).forEach(saved -> reqs.add(saved.key, saved.data));
				obj.add("requirements", reqs);
				arr.add(obj);
			});
			lines.add(sit, arr);
		});
		root.add("lines", lines);
		// root.add("mood", new JsonObject());
		JsonObject recruitment = new JsonObject();
		String intro, confirm;
		if (!store.getScenes("recruitment intro").isEmpty()) {
			intro = store.getScenes("recruitment intro").get(0).getText();
		} else {
			intro = "";
		}
		if (!store.getScenes("recruitment confirmation").isEmpty()) {
			confirm = store.getScenes("recruitment confirmation").get(0).getText();
		} else {
			confirm = "";
		}
		recruitment.addProperty("introduction", intro);
		recruitment.addProperty("confirm", confirm);
		JsonArray effects = new JsonArray();
		JsonObject effect = new JsonObject();
		int cost;
		try {
			cost = Integer.parseInt(recruitmentCost.getText());
		} catch (NumberFormatException e) {
			cost = 1000;
		}
		effect.addProperty("modMoney", cost);
		effects.add(effect);
		recruitment.add("cost", effects);
		JsonObject requirements = new JsonObject();
		JsonRequirementSaver saver = new JsonRequirementSaver();
		if (!store.getScenes("recruitment intro").isEmpty())
			store.getScenes("recruitment intro").get(0).getReqs().stream().map(saver::saveRequirement)
					.forEach(s -> requirements.add(s.key, s.data));
		recruitment.add("requirements", requirements);
		recruitment.addProperty("action", name.getText() + ": $" + cost);
		root.add("recruitment", recruitment);

		root.add("portraits", new JsonArray());

		return root;
	}

	@FXML
	private void save() {
		FileChooser fc = new FileChooser();
		fc.setSelectedExtensionFilter(new ExtensionFilter("JSON Characters", ".json"));
		File file = fc.showSaveDialog(stage);
		if (file != null) {
			try {
				try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
					writer.write(GSON.toJson(createJson()));
				}
			} catch (IOException e) {
				Alert a = new Alert(AlertType.ERROR, "Unable to save file! Error: " + e);
				a.showAndWait();
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void addAiMod(ActionEvent evt) {
		AiMod mod = modType.getValue().build();
		mod.setValue(mod.fromString(modValue.getValue()));
		mod.setWeight(Double.parseDouble(modWeight.getText()));
		modList.getItems().add(mod);
	}

	@FXML
	private void removeAiMod(ActionEvent evt) {
		AiMod<?> mod = modList.getSelectionModel().getSelectedItem();
		if (mod != null) {
			modList.getItems().remove(mod);
		}
	}

	@FXML
	private void addStartItem(ActionEvent evt) {
		Item item = startItem.getValue();
		int amt;
		try {
			amt = Integer.parseInt(startItemAmount.getText());
		} catch (NumberFormatException e) {
			return;
		}
		if (item != null) {
			ItemAmount ia = new ItemAmount(item, amt);
			startItemList.getItems().add(ia);
		}
	}

	@FXML
	private void removeStartItem(ActionEvent evt) {
		ItemAmount selected = startItemList.getSelectionModel().getSelectedItem();
		if (selected != null) {
			startItemList.getItems().remove(selected);
		}
	}

	@FXML
	private void addPurchaseItem(ActionEvent evt) {
		Item item = purchaseItem.getValue();
		int amt;
		try {
			amt = Integer.parseInt(purchaseItemAmount.getText());
		} catch (NumberFormatException e) {
			return;
		}
		if (item != null) {
			ItemAmount ia = new ItemAmount(item, amt);
			purchaseItemList.getItems().add(ia);
		}
	}

	@FXML
	private void removePurchaseItem(ActionEvent evt) {
		ItemAmount selected = purchaseItemList.getSelectionModel().getSelectedItem();
		if (selected != null) {
			purchaseItemList.getItems().remove(selected);
		}
	}

	@FXML
	private void addScene(ActionEvent evt) {
		store.addScene(sceneType.getValue(), "", new ArrayList<>());
		sceneIdx.getItems().add(sceneIdx.getItems().size());
		sceneIdx.getSelectionModel().select(sceneIdx.getItems().size() - 1);
	}

	@FXML
	private void removeScene(ActionEvent evt) {
		int idx = sceneIdx.getValue();
		List<SceneStore.Scene> scenes = store.getScenes(sceneType.getValue());
		if (scenes.size() < 1)
			return;
		scenes.remove(idx);
		sceneIdx.getItems().clear();
		IntStream.range(0, scenes.size()).forEach(sceneIdx.getItems()::add);
		sceneIdx.getSelectionModel().selectFirst();
	}

	@FXML
	private void moveScene(ActionEvent evt) {
		int idx = sceneIdx.getValue();
		if (idx > 0) {
			Collections.swap(store.getScenes(sceneType.getValue()), idx, idx - 1);
			sceneIdx.getSelectionModel().select(idx - 1);
		}
	}

	private class ReqCell extends TreeCell<CreatorRequirement> {

		private final ContextMenu context;
		private final MenuItem remove;

		ReqCell() {
			context = new ContextMenu();
			MenuItem add = new MenuItem("Add Subrequirement");
			add.setOnAction(e -> {
				if (getTreeItem() != null) {
					getTreeItem().getChildren().add(new TreeItem<>(new CreatorRequirement()));
					getTreeItem().setExpanded(true);
				}
			});
			context.getItems().add(add);

			remove = new MenuItem("Remove");
			remove.setOnAction(e -> {
				if (getTreeItem() != null && getTreeItem().getParent() != null) {
					getTreeItem().getParent().getChildren().remove(getTreeItem());
				}
			});
			context.getItems().add(remove);
			setContextMenu(context);
		}

		@Override
		protected void updateItem(CreatorRequirement item, boolean empty) {
			super.updateItem(item, empty);
			setGraphic(empty ? null : item.getBox());
			if (getTreeItem() != null) {
				if (getTreeItem().getParent() == null) {
					// context.getItems().remove(remove);
				}
				// this.setStyle("-fx-background-color:" +
				// (getTreeItem().getValue().isDirty() ? "red" : "white"));
			}

			if (!empty) {
				item.addListener(x -> updateItem(item, false));
			}
		}

	}

	static void setFieldNumericOnly(TextField field) {
		field.textProperty().addListener((obs, old, nw) -> {
			if (nw.chars().anyMatch(i -> !java.lang.Character.isDigit((char) i))) {
				field.setText("");
			}
		});
	}

	private static void hackNormalPussy() {
		// sorry
		try {
			Field field = PussyPart.class.getDeclaredField("desc");
			field.setAccessible(true);
			field.set(PussyPart.normal, "normal");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Global(true);
		Global.newGame("", Optional.empty(), Collections.emptyList(), CharacterSex.male, Collections.emptyMap());
		Global.rebuildCharacterPool(Optional.empty());
		System.out.println("asdf");
		launch(args);
	}
}
