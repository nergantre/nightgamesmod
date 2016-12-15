package nightgames.creator.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import nightgames.creator.req.CreatorRequirement;

class SceneStore {

	static final Collection<String> REQUIRED_LINES = Arrays.asList("hurt", "naked", "stunned", "taunt", "tempt",
			"orgasm", "makeOrgasm", "describe", "startBattle", "night", "victory", "draw", "defeat", "victory3p",
			"victory3pAssist", "intervene3p", "intervene3pAssist");

	Map<String, List<Scene>> scenes;

	SceneStore() {
		scenes = new HashMap<>();
		REQUIRED_LINES.forEach(s -> scenes.put(s, new ArrayList<>()));
	}

	boolean loadFromJson(JsonObject json) {
		return false;
	}

	boolean buildFromGui(String situation, String text, Optional<CreatorRequirement> reqs) {
		assert scenes.containsKey(situation);
		Scene scene = new Scene(situation, text, reqs);
		if (scenes.get(situation).contains(scene)) {
			return false;
		}
		scenes.get(situation).add(scene);
		return true;
	}

	void loadAllFromJson(JsonObject json) {
		scenes.clear();
		json.entrySet().forEach(e -> {
			List<Scene> scenes = new ArrayList<>();
			JsonArray arr = e.getValue().getAsJsonArray();
			arr.forEach(raw -> {
				JsonObject obj = raw.getAsJsonObject();
				Optional<CreatorRequirement> req;
				if (obj.has("requirements")) {
					req = Optional.of(CreatorRequirement.fromJson(obj.get("requirements").getAsJsonObject()));
				} else {
					req = Optional.empty();
				}
				Scene scene = new Scene(e.getKey(), obj.get("text").getAsString(), req);
				scenes.add(scene);
			});
			this.scenes.put(e.getKey(), scenes);
		});
	}

	void insertAllIntoJson(JsonObject json) {
		JsonObject lines = new JsonObject();
		REQUIRED_LINES.forEach(s -> {
			JsonArray arr = new JsonArray();
			List<Scene> scenes = this.scenes.get(s);
			if (scenes.isEmpty()) {
				JsonObject obj = new JsonObject();
				obj.addProperty("text", "!!NOT WRITTEN!!");
				arr.add(obj);
			} else {
				scenes.forEach(scene -> {
					JsonObject obj = new JsonObject();
					obj.addProperty("text", scene.getText());
					if (scene.getReqs().isPresent()) {
						JsonObject req = new JsonObject();
						scene.getReqs().get().toJson(req);
						obj.add("requirements", req);
					}
					arr.add(obj);
				});
			}
			lines.add(s, arr);
		});
	}

	public static void main(String[] args) throws IOException {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(Files.newBufferedReader(new File("characters/samantha.json").toPath()))
				.getAsJsonObject();
		SceneStore ss = new SceneStore();
		ss.loadAllFromJson(json.get("lines").getAsJsonObject());
		ss.scenes.values().stream().flatMap(List::stream).forEach(System.out::println);
	}

	class Scene {
		private String situation;
		private String text;
		private Optional<CreatorRequirement> reqs;

		Scene(String situation, String text, Optional<CreatorRequirement> reqs) {
			this.situation = situation;
			this.text = text;
			this.reqs = reqs;
		}

		String getBrief() {
			return text.length() < 20 ? text : text.substring(0, 20);
		}

		String getSituation() {
			return situation;
		}

		void setSituation(String situation) {
			this.situation = situation;
		}

		String getText() {
			return text;
		}

		void setText(String text) {
			this.text = text;
		}

		Optional<CreatorRequirement> getReqs() {
			return reqs;
		}

		void setReqs(Optional<CreatorRequirement> reqs) {
			this.reqs = reqs;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((reqs == null) ? 0 : reqs.hashCode());
			result = prime * result + ((situation == null) ? 0 : situation.hashCode());
			result = prime * result + ((text == null) ? 0 : text.hashCode());
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
			Scene other = (Scene) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (reqs == null) {
				if (other.reqs != null)
					return false;
			} else if (!reqs.equals(other.reqs))
				return false;
			if (situation == null) {
				if (other.situation != null)
					return false;
			} else if (!situation.equals(other.situation))
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}

		private SceneStore getOuterType() {
			return SceneStore.this;
		}
		
		public String toString() {
			return String.format("[Reqs: %s, sit: %s, text: %s]", reqs.toString(), situation, getBrief());
		}

	}
}
