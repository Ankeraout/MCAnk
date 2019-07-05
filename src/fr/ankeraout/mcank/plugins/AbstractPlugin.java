package fr.ankeraout.mcank.plugins;

/**
 * This class implements the {@link Plugin} interface. It contains empty code
 * for the {@link Plugin#onLoad()} and {@link Plugin#onUnload()} methods so that
 * any class that extends this class does not have to implement them if the code
 * is going to be empty.
 * 
 * @author Ankeraout
 *
 */
public abstract class AbstractPlugin implements Plugin {
	@Override
	public void onLoad() {

	}

	@Override
	public void onUnload() {

	}
}
