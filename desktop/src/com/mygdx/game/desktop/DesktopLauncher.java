package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.StateManager;

public class DesktopLauncher
{
	public static final String TITLE = "AgentSurveillance";

	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = TITLE;
		config.width = StateManager.WIDTH;
		config.height = StateManager.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new StateManager(), config);
	}
}
