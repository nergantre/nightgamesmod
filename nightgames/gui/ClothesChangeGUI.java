package nightgames.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import nightgames.characters.Character;
import nightgames.daytime.Activity;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSorter;

public class ClothesChangeGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -912778444912041408L;
	private Character character;
	private Activity resume;
	private JLabel appearanceLabel;
	private JLabel exposureLabel;
	DefaultListModel<Clothing> closetListModel;
	DefaultListModel<Clothing> outfitListModel;

	private void removeAllClothing() {
		character.closet.addAll(character.outfitPlan);
		character.outfitPlan.clear();
		character.change(Modifier.normal);
		refreshLists();
	}

	private void remove(Clothing article) {
		if (article == null) { return; }
		if (!character.outfitPlan.contains(article)) {
			System.err.println("Error: tried to remove nonexistent article: " + article.getName());
			return;
		}
		character.outfitPlan.remove(article);
		character.closet.add(article);
		character.change(Modifier.normal);
		refreshLists();
	}

	private void add(Clothing article) {
		if (article == null) { return; }
		if (!character.closet.contains(article)) {
			System.err.println("Error: tried to equip nonexistent article: " + article.getName());
			return;
		}
		// remove the article from the closet
		character.closet.remove(article);
		// change to make sure everything is equipped correctly
		character.change(Modifier.normal);
		// get the currently equipped items
		Set<Clothing> unequipped = new HashSet<Clothing>(character.outfit.getEquipped());
		// equip the new item
		character.outfit.equip(article);
		// get {previously equipped} - {currently equipped} to see what was unequipped
		unequipped.removeAll(character.outfit.getEquipped());
		// add all the unequipped items back into the closet
		character.closet.addAll(unequipped);
		// make the outfit plan the currently equipped items
		character.outfitPlan.clear();
		character.outfitPlan.addAll(character.outfit.getEquipped());
		// make sure the player is dressed correctly
		character.change(Modifier.normal);
		// refresh the ClothingLists
		refreshLists();
	}

	private void refreshLists() {
		closetListModel.clear();
		List<Clothing> tempList = new ArrayList<>(character.closet);
		tempList.sort(new ClothingSorter());
		tempList.forEach(article -> closetListModel.addElement(article));
		outfitListModel.clear();
		tempList = new ArrayList<>(character.outfit.getEquipped());
		tempList.sort(new ClothingSorter());
		tempList.forEach(article -> outfitListModel.addElement(article));
		DecimalFormat format = new DecimalFormat("#.##");
		appearanceLabel.setText("Appearance: " + format.format(character.outfit.getHotness()));
		exposureLabel.setText("Exposure: " + format.format(character.outfit.getExposure()));
		Global.gui().refresh();
	}

	public ClothesChangeGUI(Character character, Activity event, String doneOption){
		this.character = character;
		this.resume = event;
		setLayout(new BorderLayout());

		Box closetBox = Box.createVerticalBox();
		closetListModel = new DefaultListModel<>();
		JList<Clothing> closetList = new ClothingList(closetListModel);
		closetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		closetBox.add(new JLabel("Closet"));
		JScrollPane closetListPane = new JScrollPane(closetList);
		closetListPane.setMinimumSize(new Dimension(1000, 200));
		closetBox.add(closetListPane);

		JButton removeall = new JButton("Remove All");
		Box centerChangePanel = Box.createVerticalBox();
		JButton addButton = new JButton("Add ->");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButton.setMaximumSize(new Dimension(100, 50));
		JButton removeButton = new JButton("<- Remove");
		removeButton.setMaximumSize(new Dimension(100, 50));
		removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerChangePanel.add(Box.createVerticalStrut(200));
		centerChangePanel.add(addButton);
		centerChangePanel.add(Box.createVerticalStrut(200));
		centerChangePanel.add(removeButton);
		centerChangePanel.add(Box.createVerticalStrut(200));
		centerChangePanel.setOpaque(false);

		Box outfitBox = Box.createVerticalBox();
		outfitListModel = new DefaultListModel<>();
		JList<Clothing> outfitList = new ClothingList(outfitListModel);
		outfitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane outfitListPane = new JScrollPane(outfitList);
		outfitListPane.setMinimumSize(new Dimension(1000, 200));

		outfitBox.add(new JLabel("Outfit"));
		outfitBox.add(outfitListPane);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClothesChangeGUI.this.character.change(Modifier.normal);
				Global.gui().removeClosetGUI();
				ClothesChangeGUI.this.resume.visit(doneOption);
			}
		});
		btnOk.setFont(new Font("Sylfaen", Font.PLAIN, 24));
		btnOk.setAlignmentX(CENTER_ALIGNMENT);
		addButton.addActionListener(aevent -> add(closetList.getSelectedValue()));
		removeButton.addActionListener(aevent -> remove(outfitList.getSelectedValue()));
		removeall.addActionListener(aevent -> removeAllClothing());


		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(closetBox, BorderLayout.CENTER);
		leftPanel.add(new JLabel(), BorderLayout.SOUTH);
		leftPanel.setPreferredSize(new Dimension(500, 100));
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(outfitBox, BorderLayout.CENTER);
		rightPanel.add(removeall, BorderLayout.SOUTH);
		rightPanel.setPreferredSize(new Dimension(500, 100));
		JPanel centerPanel = new JPanel(new BorderLayout());
		Box cBPanel = Box.createHorizontalBox();
		cBPanel.add(centerChangePanel);
		cBPanel.setOpaque(false);
		centerPanel.add(cBPanel, BorderLayout.CENTER);
		Box labelPanel = Box.createVerticalBox();
		appearanceLabel = new JLabel("Appearance: ");
		exposureLabel = new JLabel("Exposure: ");
		labelPanel.add(appearanceLabel);
		labelPanel.add(exposureLabel);
		Box miscPanel = Box.createHorizontalBox();
		miscPanel.add(labelPanel);
		miscPanel.add(Box.createHorizontalStrut(20));
		miscPanel.add(btnOk);
		miscPanel.setAlignmentX(CENTER_ALIGNMENT);
		centerChangePanel.add(miscPanel);
		centerPanel.setOpaque(false);
		add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		refreshLists();
	}
}
