package io.whitegoldlabs.wiseguys.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets
{
	public AssetManager manager = new AssetManager();
	
	// ---------------------------------------------------------------------------------|
	// Sprites                                                                          |
	// ---------------------------------------------------------------------------------|
	public static final AssetDescriptor<Texture> title =
			new AssetDescriptor<Texture>(Gdx.files.internal("title.png"), Texture.class);
	
	public static final AssetDescriptor<Texture> spriteSheet =
			new AssetDescriptor<Texture>(Gdx.files.internal("sprites.png"), Texture.class);
	
	public static final AssetDescriptor<Texture> swapSewer =
			new AssetDescriptor<Texture>(Gdx.files.internal("swap_sewer.png"), Texture.class);
	
	// ---------------------------------------------------------------------------------|
	// Music                                                                            |
	// ---------------------------------------------------------------------------------|
	public static final AssetDescriptor<Music> bgmTrueSurvivor =
			new AssetDescriptor<Music>(Gdx.files.internal("bgm\\true_survivor.wav"), Music.class);
	
	// ---------------------------------------------------------------------------------|
	// Sound Effects                                                                    |
	// ---------------------------------------------------------------------------------|
	public static final AssetDescriptor<Sound> sfxCoin =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\coin.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxFireball =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\fireball.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxGameOver =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\game_over.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxJulesDeath =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\jules_death.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxJump =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\jump.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxStomp =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\stomp.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxKick =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\kick.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxBump =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\bump.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxBrickSmash =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\brick_smash.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxPause =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\pause.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxPipe =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\pipe.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxPop =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\pop.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxPowerupAppears =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\powerup_appears.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxPowerup =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\powerup.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfx1up =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\1-up.wav"), Sound.class);
	
	public static final AssetDescriptor<Sound> sfxStageClear =
			new AssetDescriptor<Sound>(Gdx.files.internal("sfx\\stage_clear.wav"), Sound.class);
	
	public void load()
	{
		manager.load(title);
		manager.load(spriteSheet);
		manager.load(swapSewer);
		manager.load(bgmTrueSurvivor);
		manager.load(sfxCoin);
		manager.load(sfxFireball);
		manager.load(sfxGameOver);
		manager.load(sfxJulesDeath);
		manager.load(sfxJump);
		manager.load(sfxStomp);
		manager.load(sfxKick);
		manager.load(sfxBump);
		manager.load(sfxBrickSmash);
		manager.load(sfxPause);
		manager.load(sfxPipe);
		manager.load(sfxPop);
		manager.load(sfxPowerupAppears);
		manager.load(sfxPowerup);
		manager.load(sfx1up);
		manager.load(sfxStageClear);
	}
	
	public void dispose()
	{
		manager.dispose();
	}
}
