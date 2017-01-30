package io.whitegoldlabs.wiseguys;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.ConsoleCommandExecutor;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.ScriptManager;
import io.whitegoldlabs.wiseguys.view.GameScreen;
import io.whitegoldlabs.wiseguys.view.MainMenuScreen;

public class WiseGuys extends Game
{
	public Assets assets;
	public ScriptManager scriptManager;
	
	public Screen currentScreen;
	public String currentWorld;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public BitmapFont font;
	public BitmapFont bigFont;
	
	public Engine engine;
	public Entity player;
	
	public Console console;
	
	public boolean eventProcessing;
	public boolean isRunning;
	public boolean wasSleeping;
	
	public String nextGameScreenDestination;
	private float nextGameScreenX;
	private float nextGameScreenY;
	
	@Override
	public void create()
	{
		this.assets = new Assets();
		assets.manager.load(Assets.spriteSheet);
		assets.manager.finishLoading();
		
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.7f;
		
		this.batch = new SpriteBatch();
		
		this.font = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		
		this.bigFont = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		bigFont.getData().setScale(2);
		
		this.scriptManager = new ScriptManager();
		this.engine = new Engine();
		initPlayer();
		
		this.eventProcessing = false;
		this.isRunning = true;
		this.wasSleeping = false;
		
		this.currentWorld = "world1-1";
		
		this.console = new GUIConsole();
		console.setCommandExecutor(new ConsoleCommandExecutor(this));
		console.setPosition(0, Gdx.graphics.getHeight() / 2);
		console.setSizePercent(100, 50);
		console.setDisplayKeyID(Input.Keys.GRAVE);
		
		
		MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
		this.currentScreen = mainMenuScreen;
		this.setScreen(currentScreen);
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}
	
	public void prepareNextGameScreen(String worldName, float x, float y)
	{
		this.nextGameScreenDestination = worldName;
		this.nextGameScreenX = x;
		this.nextGameScreenY = y;
	}
	
	public void setNextGameScreen()
	{
		GameScreen nextGameScreen = new GameScreen(this, nextGameScreenDestination, nextGameScreenX, nextGameScreenY);
		
		currentScreen.dispose();
		currentScreen = nextGameScreen;
		screen = nextGameScreen;
	
		nextGameScreenDestination = null;
	}
	
	private void initPlayer()
	{
		this.player = new Entity();
		
		Texture spriteSheet = assets.manager.get(Assets.spriteSheet);
		
		Sprite playerStillSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Array<Sprite> playerStillSprites = new Array<>(); 
		Array<Sprite> playerJumpingSprites = new Array<>();
		Array<Sprite> playerMovingSprites = new Array<>();
		Array<Sprite> playerSlowingSprites = new Array<>();
		Array<Sprite> playerDamagedSprites = new Array<>();
		
		playerStillSprites.add(playerStillSprite);
		playerJumpingSprites.add(new Sprite(spriteSheet, 64, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 16, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 32, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 48, 0, 16, 16));
		playerSlowingSprites.add(playerStillSprite);
		playerDamagedSprites.add(playerStillSprite);
		playerDamagedSprites.add(new Sprite(spriteSheet, 0, 0, 0, 0));
		
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		player.add(new SpriteComponent(playerStillSprite));
		
		StateComponent state = new StateComponent();
		state.directionState = StateComponent.DirectionState.RIGHT;
		state.airborneState = StateComponent.AirborneState.GROUNDED;
		player.add(state);
		
	    player.add(new PlayerComponent(0, 0, 3));
	    player.add(new TypeComponent(TypeComponent.Type.PLAYER));
		player.add(new PositionComponent(0, 0));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new HitboxComponent
		(
			0,
			0,
			playerStillSprite.getWidth(),
			playerStillSprite.getHeight(),
			playerHitboxSprite
		));
		
		AnimationComponent animation = new AnimationComponent();
		animation.animations.put("STILL", new Animation<Sprite>(1f/32f, playerStillSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("MOVING", new Animation<Sprite>(1f/32f, playerMovingSprites, Animation.PlayMode.LOOP));
		animation.animations.put("SLOWING", new Animation<Sprite>(1f/32f, playerSlowingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("JUMPING", new Animation<Sprite>(1f/32f, playerJumpingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("DAMAGED", new Animation<Sprite>(1f/32f, playerDamagedSprites, Animation.PlayMode.LOOP));
		player.add(animation);
	}
	
	public void powerupSuperJules()
	{
		Texture spriteSheet = assets.manager.get(Assets.spriteSheet);
		
		Sprite playerStillSprite = new Sprite(spriteSheet, 0, 16, 16, 32);
		Array<Sprite> playerStillSprites = new Array<>(); 
		Array<Sprite> playerJumpingSprites = new Array<>();
		Array<Sprite> playerMovingSprites = new Array<>();
		Array<Sprite> playerSlowingSprites = new Array<>();
		Array<Sprite> playerDamagedSprites = new Array<>();
		
		playerStillSprites.add(playerStillSprite);
		playerJumpingSprites.add(new Sprite(spriteSheet, 64, 16, 16, 32));
		playerMovingSprites.add(new Sprite(spriteSheet, 16, 16, 16, 32));
		playerMovingSprites.add(new Sprite(spriteSheet, 32, 16, 16, 32));
		playerMovingSprites.add(new Sprite(spriteSheet, 48, 16, 16, 32));
		playerSlowingSprites.add(playerStillSprite);
		playerDamagedSprites.add(playerStillSprite);
		playerDamagedSprites.add(new Sprite(spriteSheet, 0, 0, 0, 0));
		
		player.add(new SpriteComponent(playerStillSprite));
		
		AnimationComponent animation = new AnimationComponent();
		animation.animations.put("STILL", new Animation<Sprite>(1f/32f, playerStillSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("MOVING", new Animation<Sprite>(1f/32f, playerMovingSprites, Animation.PlayMode.LOOP));
		animation.animations.put("SLOWING", new Animation<Sprite>(1f/32f, playerSlowingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("JUMPING", new Animation<Sprite>(1f/32f, playerJumpingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("DAMAGED", new Animation<Sprite>(1f/32f, playerDamagedSprites, Animation.PlayMode.LOOP));
		player.add(animation);
		
		Mappers.hitbox.get(player).hitbox.height = 32;
		Mappers.player.get(player).playerState = PlayerComponent.PlayerState.SUPER;
	}
	
	public void powerdownJulesNormal()
	{
		Texture spriteSheet = assets.manager.get(Assets.spriteSheet);
		
		Sprite playerStillSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Array<Sprite> playerStillSprites = new Array<>(); 
		Array<Sprite> playerJumpingSprites = new Array<>();
		Array<Sprite> playerMovingSprites = new Array<>();
		Array<Sprite> playerSlowingSprites = new Array<>();
		Array<Sprite> playerDamagedSprites = new Array<>();
		
		playerStillSprites.add(playerStillSprite);
		playerJumpingSprites.add(new Sprite(spriteSheet, 64, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 16, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 32, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 48, 0, 16, 16));
		playerSlowingSprites.add(playerStillSprite);
		playerDamagedSprites.add(playerStillSprite);
		playerDamagedSprites.add(new Sprite(spriteSheet, 0, 0, 0, 0));
		
		player.add(new SpriteComponent(playerStillSprite));
		
		AnimationComponent animation = new AnimationComponent();
		animation.animations.put("STILL", new Animation<Sprite>(1f/32f, playerStillSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("MOVING", new Animation<Sprite>(1f/32f, playerMovingSprites, Animation.PlayMode.LOOP));
		animation.animations.put("SLOWING", new Animation<Sprite>(1f/32f, playerSlowingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("JUMPING", new Animation<Sprite>(1f/32f, playerJumpingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("DAMAGED", new Animation<Sprite>(1f/32f, playerDamagedSprites, Animation.PlayMode.LOOP));
		player.add(animation);
		
		Mappers.hitbox.get(player).hitbox.height = 16;
		
		PlayerComponent playerComponent = Mappers.player.get(player);
		
		playerComponent.playerState = PlayerComponent.PlayerState.NORMAL;
		playerComponent.damaged = true;
	}
}
