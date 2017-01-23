package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.constant.Constants;

public class MainMenuScreen implements Screen
{
	final WiseGuys game;
	OrthographicCamera camera;
	
	Stage stage;
	Table table;
	
	Texture titleTexture;
	Image title;
	
	// ---------------------------------------------------------------------------------|
	// Constructors                                                                     |
	// ---------------------------------------------------------------------------------|
	public MainMenuScreen(final WiseGuys game)
	{
		this.game = game;
		
		stage = new Stage();
		table = new Table();
		
		table.setFillParent(true);
	    stage.addActor(table);
	    
	    // Title Image
	    titleTexture = new Texture(Gdx.files.internal("title.png"));
	    title = new Image(titleTexture);
	    title.setSize(title.getWidth()*3, title.getHeight()*3);
	    title.setPosition((Gdx.graphics.getWidth() - title.getWidth()) / 2, (Gdx.graphics.getHeight() - title.getHeight()) / 2);
	    stage.addActor(title);
	    
	    Skin skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	    
	    // Labels
	    Label lblArcade = new Label("Arcade Mode", skin, "default");
	    lblArcade.setPosition((Gdx.graphics.getWidth() - lblArcade.getWidth()) / 2, 180);
	    stage.addActor(lblArcade);
	    
	    Label lblStageSelect = new Label("Select Stage", skin, "default");
	    lblStageSelect.setPosition((Gdx.graphics.getWidth() - lblStageSelect.getWidth()) / 2, 150);
	    stage.addActor(lblStageSelect);
	    
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

	// ---------------------------------------------------------------------------------|
	// Implemented Methods                                                              |
	// ---------------------------------------------------------------------------------|
	@Override
	public void show()
	{
		
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
		
//		camera.update();
//		game.batch.setProjectionMatrix(camera.combined);
//		
//		game.batch.begin();
//		game.font.draw(game.batch, "Wise Guys", 5, Constants.WINDOW_HEIGHT - 8);
//		
//		game.batch.end();
		
		// For now, clicking anywhere in the menu will bring you to the game screen.
		if(Gdx.input.isTouched())
		{
			GameScreen newGameScreen = new GameScreen(game, "world1-1", 16, 32);
			game.currentScreen = newGameScreen;
			game.setScreen(game.currentScreen);
			dispose();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause()
	{
		
	}

	@Override
	public void resume()
	{
		
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}
