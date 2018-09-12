# BowAndArrow
2 player game with bow and arrows shooting at one another in example of learning about game creation

No functional JAR deployment yet, IDE required to run. Bow and Arrow 2 player game. Will take turns shooting arrows at one another,
hitting the opponent reduces health, first to reduce the other player's health to 0 wins.
Rocks are randomly generated polygons spaced a set distance apart, map features a wall halfway between the players,
and a custom created camera system and grid keeps track of shots by the players and their movement, and minimize
the amount of spawning required on screen for optimization. Additionally arrows despawn after a certain amount of time to minimize load.
Damage is based on the speed of the arrow at the time of impact, with custom collision detecting associated with the game engine.
