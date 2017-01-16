package io.whitegoldlabs.wiseguys.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.constant.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.WINDOW_WIDTH;
		config.height = Constants.WINDOW_HEIGHT;
		config.fullscreen = true;
		config.vSyncEnabled = true;
		
		
		new LwjglApplication(new WiseGuys(), config);
	}
}
