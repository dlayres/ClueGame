package clueGame;

/**
 * Solution class will be used to make the 3 card game solution
 */
public class Solution {
	
	public String player; // The name of the correct player
	public String weapon; // The name of the correct weapon
	public String room; // The name of the correct room
	
	
	public Solution() {
		super();
	}
	
	public Solution(String player, String weapon, String room) {
		this.player = player;
		this.weapon = weapon;
		this.room = room;
	}
}
