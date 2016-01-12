{
	"type" : "NPCTestMale",
	"name" : "TestMale",
	"outfit" : {
		"top" : ["shirt"],
		"bottom" : ["pants", "boxers"]
	},
	"trophy" : "MiscTrophy",
	"stats" : {
		"base" : {
			"level" : 10,
			"attributes" : {
				"Seduction" : 100,
				"Cunning" : 70,
				"Power" : 50,
				"Dark" : 50,
				"Animism" : 50,
				"Science" : 50,
				"Divinity" : 50,
				"Arcane" : 50,
				"Fetish" : 50,
				"Ki" : 50,
				"Bio" : 50,
			},
			"resources" : {
				"stamina" : 100,
				"mojo" : 100,
				"arousal" : 150,
				"willpower" : 100
			},
			"traits" : [
			]
		},
		"growth" : {
			"resources" : {
				"stamina" : 2,
				"bonusStamina" : 1,
				"mojo" : 2,
				"bonusMojo" : 1,
				"arousal" : 2,
				"bonusArousal" : 1,
				"willpower" : 1.5,
				"bonusWillpower" : 0.5,
				"points" : [2, 3, 3],
				"bonusPoints" : 1
			},
			"traits" : [{
					"level" : 12,
					"trait" : "silvertongue"
				}
			],
			"preferredAttributes" : [{
					"attribute" : "Seduction"
				}
			]
		}
	},
	"plan" : "hunting",
	"sex" : "male",
	"body":{
        "parts":[
          {
            "class":"nightgames.characters.body.BreastsPart",
            "enum":"flat"
          },
          {
            "class":"nightgames.characters.body.EarPart",
            "enum":"normal"
          },
          {
            "class":"nightgames.characters.body.BasicCockPart",
            "enum":"average"
          }
        ],
        "hotness":1.0
      },
	"items" : {
		"initial" : [{
				"item" : "nectar",
				"amount" : 10
			}
		],
		"purchase" : [{
				"item" : "nectar",
				"amount" : 10
			}
		]
	},
	"lines" : {
		"hurt" : [{
				"text" : "<b>\"Aaah, what a sweet voice, you're making my flowers drip.\"</b>"
			}
		],
		"naked" : [{
				"text" : "{self:subject} seems unperturbed. While making no effort to cover herself, she beckons to you, <b>\"Like what you see? Step a little closer and I'll show you a whole new world.\"</b>"
			}
		],
		"stunned" : [{
				"text" : "<b>{self:subject} crumples onto the ground, \"You will pay for this...\"</b>"
			}
		],
		"taunt" : [{
				"text" : "<b>\"Be still! I'm going to have a lot of fun tormenting you inside my flower...\"</b>"
			}
		],
		"tempt" : [
			{
				"requirements": {
					"inserted" : true
				},
				"text" : "<b>\"Don't worry cutie, I'll wring out your cum straight away.\"</b>"
			},
			{
				"text" : "<b>\"Come a little bit closer and partake in my scent...\"</b>"
			}
		],
		"orgasm" : [{
				"text" : "Hah... Hah... That was... unexpected. Now I'm even more interested in you. Come, let's try this again!"
			}
		],
		"makeOrgasm" : [
			{
				"requirements": {
					"reverse": {
						"orgasms" : 2
					}
				},
				"text" : "<b>\"Ahahaha! Amazing! At this rate you'll be little more than fertilizer for me you know?\"</b>"
			},
			{
				"requirements": {
					"reverse": {
						"orgasms" : 1
					}
				},
				"text" : "<b>\"Ahahaha! that's the second! Are your balls dry yet?\"</b>"
			},
			{
				"text" : "<b>\"And that's the first! Let's see if there's any more for me, hmm?\"</b>"
			}
		],
		"describe" : [{
				"text" : "{self:name} can only be described as an green amazoness sprouting from a tree.<br>While her body beneath her thighs are invisible to you, her voluptuous upper body extends out from a giant flower blooming on the side of the large tree in the courtyard. Her long hair is a deeper shade of green and extends down to the small of her back. Numerous fleshy red flowers adorn her body, each one lewdly slightly opening and closing as if breathing. <br>She looks at you with a hungry look in her eyes."
			}
		],
		"startBattle" : [{
				"text" : "<b>\"Come, scatter your seed and make my flowers bloom.\"</b>"
			}
		],
		"night" : [{
				"text" : "Tired after the night's games, you start trudging back to your place. As you're about to leave the grounds however, you smell a sweet scene in the air. The aroma seems to arouse you, but at the same time making your accumulated fatigue disappear like magic. You grin and thank {self:name-do} in your mind."
			}
		],
		"victory" : [{
				"requirements" :
				{
					"result" : "anal",
					"inserted" : true
				},
				"text" : "She wins by fucking your ass"
			}, {
				"requirements" :
				{
					"result" : "anal",
					"reverse" : {"inserted" : true}
				},
				"text" : "She wins by you fucking her ass"
			}, {
				"requirements" :
				{
						"result" : "intercourse"
					}
				,
				"text" : "She wins by fucking you"
			}, {
				"requirements" :
				{
					"and" : [
							{"bodypart" : "pussy"},
							{"bodypart" : "tail"}
						]
				}
				,
				"text" : "She wins by... having a pussy and a tail?"
			}, {
				"requirements" :
				{
					"or": [
						{"dom" : ""},
						{"item" : {
								"item" : "Dildo",
								"amount" : 1
							}
						}
					]
				},
				"text" : "She wins by... either being in a dominant position OR having a dildo?"
			}, {
				"requirements" :
				{
					"not": {"stance" : "Neutral"}
				},
				"text" : "She wins while not being in the neutral stance."
			}, {
				"text" : "Default NPC victory scene"
			}
		],
		"defeat" : [{
				"text" : "Default player victory scene"
			}
		],
		"draw" : [{
				"text" : "Default draw scene"
			}
		],
		"victory3p" : [{
				"text" : "Rosea brings you to an orgasm while {other:subject} holds you down."
			}],
		"victory3pAssist" :[{
				"text" : "Rosea brings {other:name-do} to an orgasm while you hold {other:direct-object} down."
			}
		],
		"intervene3p" : [{
				"text" : "While the fight is happening between you and {other:subject}, Rosea sneaks up behind you and holds you down."
			}
		],
		"intervene3pAssist": [{
				"text" : "While the fight is happening, Rosea sneaks behind {other:name-do} and holds {other:direct-object} down."
			}
		]
	},
	"mood" : {
		"dominant" : 100,
		"nervous" : 50,
		"angry" : 100,
		"confident" : 100,
		"horny" : 100,
		"desperate" : 50
	},
	"recruitment" : {
		"action" : "Rosea: $1000",
		"requirements" : {
			"level" : 10
		},
		"cost" : [
			{"modMoney" : -1000}
		],
		"introduction" : "Rosea's a tree. If you want, I can plant her in the middle of the front lawn.",
		"confirm" : "Alright, you'll see her tomorrow at dusk"
	},
	"portraits" : [{
			"requirements" : {
					"mood" : "dominant"
				}
			,
			"text" : "rosea_dominant.png"
		}, {
			"requirements" : {
					"mood" : "nervous"
				}
			,
			"text" : "rosea_nervous.png"
		}, {
			"requirements" : {
					"mood" : "angry"
				}
			,
			"text" : "rosea_angry.png"
		}, {
			"requirements" : {
					"mood" : "confident"
				}
			,
			"text" : "rosea_confident.png"
		}, {
			"requirements" : {
					"mood" : "horny"
				}
			,
			"text" : "rosea_horny.png"
		}, {
			"requirements" : {
					"mood" : "desperate"
				}
			,
			"text" : "rosea_desperate.png"
		}
	],
	"defaultPortrait" : "rosea_confident.png"
}
