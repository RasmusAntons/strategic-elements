package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import java.util.Stack;

public class ScreenManager {
	private Stack<ManageableScreen> screenStack;
	private ManageableScreen currentScreen;
	private InputMultiplexer inputMultiplexer;

	public ScreenManager() {
		screenStack = new Stack<>();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void push(ManageableScreen screen) {
		if (currentScreen != null) {
			currentScreen.pause();
			screenStack.push(currentScreen);
		}
		currentScreen = screen;
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(currentScreen);
		currentScreen.initInput(inputMultiplexer);
		currentScreen.show();
	}

	public void swap(ManageableScreen screen) {
		currentScreen.dispose();
		currentScreen = screen;
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(currentScreen);
		currentScreen.initInput(inputMultiplexer);
		currentScreen.show();
	}

	public void pop() {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.dispose();
			inputMultiplexer.clear();
			if (!screenStack.isEmpty()) {
				currentScreen = screenStack.pop();
				currentScreen.initInput(inputMultiplexer);
				currentScreen.resume();
			} else {
				Gdx.app.exit();
			}
		}
	}

	public void dispose() {
		while (!screenStack.isEmpty()) {
			screenStack.pop().dispose();
		}
		Gdx.input.setInputProcessor(null);
	}

	public ManageableScreen getCurrentScreen() {
		return currentScreen;
	}

	public void render() {
		if (currentScreen != null)
			currentScreen.render(Gdx.graphics.getDeltaTime());
	}
}
