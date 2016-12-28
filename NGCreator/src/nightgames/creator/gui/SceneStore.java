package nightgames.creator.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import nightgames.creator.req.CreatorRequirement;
import nightgames.requirements.AndRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.OrRequirement;
import nightgames.requirements.Requirement;
import nightgames.requirements.ReverseRequirement;

public class SceneStore {

	public static final Collection<String> REQUIRED_LINES = Arrays.asList("hurt", "naked", "stunned", "taunt", "tempt",
			"orgasm", "makeOrgasm", "describe", "startBattle", "night", "victory", "draw", "defeat", "victory3p",
			"victory3pAssist", "intervene3p", "intervene3pAssist");

	private Map<String, List<Scene>> scenes;

	SceneStore() {
		scenes = new HashMap<>();
		REQUIRED_LINES.forEach(s -> scenes.put(s, new ArrayList<>()));
		scenes.put("recruitment intro", new ArrayList<>());
		scenes.put("recruitment confirmation", new ArrayList<>());
	}

	void addScene(String situation, String text, List<Requirement> reqs) {
		List<Scene> list = scenes.putIfAbsent(situation, new ArrayList<>());
		list.add(new Scene(situation, text, reqs));
	}

	List<Scene> getScenes(String situation) {
		return scenes.get(situation);
	}
	
	public Optional<Scene> getAny() {
		for (String sit : REQUIRED_LINES) {
			if (scenes.get(sit).size() > 0) {
				return Optional.of(scenes.get(sit).get(0));
			}
		}
		return Optional.empty();
	}

	class Scene {
		private String situation;
		private String text;
		private List<Requirement> reqs;

		Scene(String situation, String text, List<Requirement> reqs) {
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

		List<Requirement> getReqs() {
			return reqs;
		}

		void setReqs(List<Requirement> reqs) {
			this.reqs = reqs;
		}

		void fillTree(TreeView<CreatorRequirement> tree) {
			TreeItem<CreatorRequirement> root = new TreeItem<>(CreatorRequirement.ROOT);
			reqs.stream().map(r -> itemFor(r, false)).forEach(root.getChildren()::add);
			root.getChildren().forEach(i -> i.setExpanded(true));
			root.setExpanded(true);
			tree.setRoot(root);
		}

		private TreeItem<CreatorRequirement> itemFor(Requirement req, boolean reversed) {
			if (req instanceof ReverseRequirement) {
				Requirement sub = ((ReverseRequirement) req).getReversedRequirement();
				return itemFor(sub, !reversed);
			} else if (req instanceof NotRequirement) {
				Requirement sub = ((NotRequirement) req).getNegatedRequirement();
				return itemFor(sub, reversed);
			} else if (req instanceof AndRequirement) {
				TreeItem<CreatorRequirement> root = new TreeItem<>(CreatorRequirement.fromReqs(req, reversed));
				((AndRequirement) req).getSubRequirements().stream().map(r -> CreatorRequirement.fromReqs(r, reversed))
						.map(TreeItem<CreatorRequirement>::new).forEach(root.getChildren()::add);
				return root;
			} else if (req instanceof OrRequirement) {
				TreeItem<CreatorRequirement> root = new TreeItem<>(CreatorRequirement.fromReqs(req, reversed));
				((OrRequirement) req).getSubRequirements().stream().map(r -> CreatorRequirement.fromReqs(r, reversed))
						.map(TreeItem<CreatorRequirement>::new).forEach(root.getChildren()::add);
				return root;
			} else {
				return new TreeItem<>(CreatorRequirement.fromReqs(req, reversed));
			}
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
			return getBrief();
		}

	}

}
