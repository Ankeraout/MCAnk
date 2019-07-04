package fr.ankeraout.mcank.commands;

import fr.ankeraout.mcank.Player;

public interface Command {
	public void onCall(Player caller, String[] args);
}
