package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;

import java.util.Stack;

public class ScreenManager {
	private Stack<ManageableScreen> screenStack;
	private ManageableScreen currentScreen;

	public ScreenManager() {
		screenStack = new Stack<ManageableScreen>();
	}

	public void push(ManageableScreen screen) {
		if (currentScreen != null) {
			currentScreen.pause();
			screenStack.push(currentScreen);
		}
		currentScreen = screen;
		currentScreen.show();
	}

	public void pop() {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.dispose();
			currentScreen = screenStack.pop();
			currentScreen.resume();
		}
	}

	public void dispose() {
		while (!screenStack.isEmpty()) {
			screenStack.pop().dispose();
		}
	}

	public ManageableScreen getCurrentScreen() {
		return currentScreen;
	}

	public void render() {
		if (currentScreen != null)
			currentScreen.render(Gdx.graphics.getDeltaTime());
	}
}
