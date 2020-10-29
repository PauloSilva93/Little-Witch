package me.game.desktop;

import java.util.Iterator;

import javax.swing.GroupLayout.Alignment;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.game.desktop.MathUtls;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Game extends ApplicationAdapter {
   // *** DECLARA VARIÁVEIS E OBJETOS DA CLASSE ***
   // Janela, renderizador e shader e utilidades
   int windowWidth;
   int windowHeight;
   Window window;
   Renderer renderer;
   ShaderProgram shader;
   MathUtls mu;
   BitmapFont font;
   // Texturas...
   Texture inicioMainTexture;
   Texture inicioOverTexture;
   Texture witchTexture;
   Texture batTexture;
   Texture backgroundTexture;
   Texture houseTexture;
   Texture lifeIconTexture;
   Texture sairTexture;
   Texture sairOverTexture;
   Texture tituloTexture;
   Texture backgroundMenuTexture;
   Texture controlsTexture;
   
   // Objetos 2D 
   Object2D witch;
   Object2D[] bats;
   Object2D background;
   Object2D house;
   Object2D lifeIcon;
   Object2D inicioMain;
   Object2D inicioOver;
   Object2D sair;
   Object2D sairOver;
   Object2D titulo;
   Object2D backgroundMenu;
   Object2D controls;
   
   // Atributos dos Objetos 
   Vector2 witchSize;
   Vector2 witchPosition;
   Vector2 witchVel;
   Vector2 witchAc;
   int witchLifes;
   float hitCounter = 0;
   boolean collidesFlag;
   Vector2 batSize;
   Vector2 []batPositions;
   Vector2 [][]batPaths;
   Vector2 knockBack;
   int totalBats;
   float []batTimer;
   Vector2 backgroundSize;
   Vector2 backgroundPosition;
   Vector2 controlsSize;
   Vector2 controlsPosition;
   Vector2 houseSize;
   Vector2 housePosition;
   Vector2 lifeIconSize;
   Vector2 lifeIconPosition;
   Vector2 inicioMainPosition;
   Vector2 inicioMainSize;
   Vector2 inicioOverPosition;
   Vector2 inicioOverSize;
   Vector2 sairSize;
   Vector2 sairPosition;
   Vector2 sairOverSize;
   Vector2 sairOverPosition;
   Vector2 tituloSize;
   Vector2 tituloPosition;
   Vector2 backgroundMenuSize;
   Vector2 backgroundMenuPosition;
   // Atributos gerais
   float gravity;
   boolean victory;
   boolean defeat;
   boolean gameEndSoundPlayed;
   boolean leftPressed;
   boolean rightPressed;
   boolean upPressed;
   boolean inMenu;
   boolean mouseOverInicio;
   boolean mouseOverSair;
   boolean leftClick;
  
   //Musica e sons
   private Music gamePlayMusic;
   private Music menuMusic;
   private Sound[] hitSounds;
   private Sound victorySound;
   private Sound defeatSound;
   private boolean menuMusicIsPlaying;
   private boolean gamePlayMusicIsPlaying;
   
   MathUtls mathUtls;
   Vector3 mousePos = new Vector3();
   Vector2 mouseVertexCoords;
   
   @Override
   public void create() 
   {
	  victory = false;
	  defeat = false;
	  gameEndSoundPlayed = false;
	  inMenu = true;
	  mouseOverInicio = false;
	  mouseOverSair = false;
	  leftClick = false;
	  menuMusicIsPlaying = false;
	  gamePlayMusicIsPlaying = false;
	   
	  mathUtls = new MathUtls();
	  // *** INICIALIZA VARIÁVEIS E OBJETOS GLOBAIS ***
	  windowWidth = DesktopLauncher.resolution[0];
	  windowHeight = DesktopLauncher.resolution[1];
	  window = new Window(windowWidth, windowHeight);
	  renderer = new Renderer();
	  renderer.CreateBatch();
	  renderer.CreateCamera(windowWidth, windowHeight);
      shader = new ShaderProgram(Gdx.files.internal("shaders/vs.vsh"), Gdx.files.internal("shaders/fs.fsh"));
      shader.pedantic = false;
      mu = new MathUtls();
      font = new BitmapFont();
      font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
      font.getData().setScale(3.0f);
      
      //Menu Principal
      //Background Menu
      backgroundTexture = new Texture(Gdx.files.internal("images/bgmenu.jpg"));
      backgroundMenu = new Object2D(window, backgroundTexture, true);
      backgroundMenuPosition = new Vector2(0.0f, 0.0f);
      backgroundMenuSize = new Vector2(1.0f, 1.0f);
      
      // Controles
      controlsTexture = new Texture(Gdx.files.internal("images/Controles.png"));
      controls = new Object2D(window, controlsTexture, true);
      controlsPosition = new Vector2(0.65f, -0.65f);
      controlsSize = new Vector2(0.35f, 0.35f);
      
      //Titulo do Jogo
      tituloTexture = new Texture(Gdx.files.internal("images/titulo.png"));
      titulo = new Object2D(window, tituloTexture, new Vector2(3,2),0,5,0);
      tituloPosition = new Vector2(0.0f, 0.4f);
      tituloSize = new Vector2(0.5f, 0.5f);
      
      //Opção Inicio
      inicioMainTexture = new Texture(Gdx.files.internal("images/Inicio.png"));
      inicioMain = new Object2D(window, inicioMainTexture, new Vector2(3,2),0,5,0);
      inicioMainPosition = new Vector2(0.0f, -0.4f);
      inicioMainSize = new Vector2(0.15f, 0.15f);
      
      inicioOverTexture = new Texture(Gdx.files.internal("images/Inicio_Over.png"));
      inicioOver = new Object2D(window, inicioOverTexture, true);
      inicioOverPosition = new Vector2(0.0f, -0.4f);
      inicioOverSize = new Vector2(0.19f, 0.24f);
      
      //Opção Sair
      sairTexture = new Texture(Gdx.files.internal("images/sair.png"));
      sair = new Object2D(window, sairTexture, new Vector2(3,2),0,5,0);
      sairPosition = new Vector2(0.0f, -0.8f);
      sairSize = new Vector2(0.15f, 0.15f);
      
      sairOverTexture = new Texture(Gdx.files.internal("images/Sair_Over.png"));
      sairOver = new Object2D(window, sairOverTexture, true);
      sairOverPosition = new Vector2(0.0f, -0.8f);
      sairOverSize = new Vector2(0.19f, 0.22f);
         
      //Carregando Musica
      gamePlayMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/MusicaFundo.mp3"));
      menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menusong.mp3"));
      //Reproduzir a Musica
      menuMusic.setLooping(true);

      gamePlayMusic.setLooping(true);

      //MusicaFundo.play();
       
      //Carregando efeitos sonoros
      //hitSounds = new Sound[3];
      hitSounds = new Sound[] {
    		  Gdx.audio.newSound(Gdx.files.internal("audio/oouch-1.mp3")),
    		  Gdx.audio.newSound(Gdx.files.internal("audio/oouch-2.mp3")),
    		  Gdx.audio.newSound(Gdx.files.internal("audio/oouch-3.mp3"))
      };
      victorySound = Gdx.audio.newSound(Gdx.files.internal("audio/victory.wav"));
      defeatSound = Gdx.audio.newSound(Gdx.files.internal("audio/defeat.wav"));

      witchTexture = new Texture(Gdx.files.internal("images/witch.png"));
      batTexture = new Texture(Gdx.files.internal("images/bat.png"));
      backgroundTexture = new Texture(Gdx.files.internal("images/background.jpg"));
      houseTexture = new Texture(Gdx.files.internal("images/house.png"));
      lifeIconTexture = new Texture(Gdx.files.internal("images/lifeIcon.png"));
      
      witch = new Object2D(window, witchTexture, new Vector2(3,2),0,5,0);
      witchSize = new Vector2(0.1f, 0.1f);
      witchPosition = new Vector2();
      witchVel = new Vector2(0.0f, 0.0f);
      witchAc = new Vector2(0.03f, 0.1f);
      witchLifes = 3;
      hitCounter = 0;
      collidesFlag = false;
      knockBack = new Vector2();

      totalBats = 30;
      batSize = new Vector2(0.1f, 0.1f); 
      batPositions = new Vector2[totalBats];
      batPaths = new Vector2[totalBats][4];
      knockBack = new Vector2(); 
      batTimer = new float[totalBats];
      
	  bats = new Object2D[totalBats];
      for (int i = 0; i < totalBats; i++)
      {
    	  bats[i] = new Object2D(window, batTexture, new Vector2(3, 3), 1, 60, MathUtils.random(0, 60));  
    	  
    	  for (int j = 0; j < 4; j++)
    	  {
    		  batPaths[i][j] = new Vector2(MathUtils.random(1.0f + i % 26, 3.0f + i % 26f), MathUtils.random(-1.1f, 1.1f));
    	  }
    	  
    	  batTimer[i] = MathUtils.random();
      }
      
      background = new Object2D(window, backgroundTexture, true);
      backgroundSize = new Vector2(13.33f, 1.1f);
      backgroundPosition = new Vector2(12.25f, 0.0f);
     
      house = new Object2D(window, houseTexture, true);
      houseSize = new Vector2(0.8f, 1.12f);
      housePosition = new Vector2(24.5f, 0.0f);
     
      lifeIcon = new Object2D(window, lifeIconTexture, false);
      lifeIconSize = new Vector2(0.08f, 0.08f);
      lifeIconPosition = new Vector2(-0.92f, 0.92f);
 
      gravity = 0.001f;
      victory = false;
      defeat = false;
      leftPressed = false;
      rightPressed = false;
      upPressed = false;
     
      System.out.println(shader.isCompiled() ? "Shader compiled." : shader.getLog());   
   }
   
   @Override
   public void render() 
   {
	   
	   getInput();
       processInput();
       update();
       
       if(inMenu == true)
    	   drawMenu();
       else
    	   drawGame();
   }
   
   @Override
   public void dispose() {
	   
	  witchTexture.dispose();
      batTexture.dispose();
      houseTexture.dispose();
      backgroundTexture.dispose();
      lifeIconTexture.dispose();
      renderer.DisposeBatch();
   } 
   
   public void getInput()
   {
	   mouseOverInicio = false;
	   mouseOverSair = false;
	   leftClick = false;
	   
	   mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	   mouseVertexCoords = mathUtls.ScreenToVertexCoords(window, new Vector2(mousePos.x, mousePos.y));
	      
	   if(Gdx.input.isKeyPressed(Keys.LEFT)) leftPressed = true;
	   else leftPressed = false;
	   if(Gdx.input.isKeyPressed(Keys.RIGHT)) rightPressed = true;
	   else rightPressed = false;
	   if(Gdx.input.isKeyPressed(Keys.UP)) upPressed = true;
	   else upPressed = false;
	   
	   //Sair do Jogo
	  if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
	  {
		  if (inMenu)
			  Gdx.app.exit();
		  else {
			  inMenu = true;
			  gamePlayMusic.stop();
			  create();
		  }
	  }
	  
	  if(inMenu == true && !menuMusicIsPlaying)
		  menuMusic.play();
	  
	  if (inicioOver.collidesPoint(mouseVertexCoords, inicioOverPosition, inicioOverSize)) {
		  mouseOverInicio = true;
	  }
	  
	  if (sairOver.collidesPoint(mouseVertexCoords, sairOverPosition, sairOverSize)) {
		  mouseOverSair = true;
	  }
	    
	  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
		  leftClick = true;
	  }
	  
   }
   
   public void processInput()
   {
	   if (!collidesFlag && !defeat && !victory)
		  {
			  if(leftPressed && witchPosition.x - (witchSize.x * 2.0) > -1.0) witchVel.x -= witchAc.x * Gdx.graphics.getDeltaTime();
			  
			  if(rightPressed) witchVel.x += witchAc.x * Gdx.graphics.getDeltaTime();
				  
		      if(upPressed && witchPosition.y + witchSize.y < 1.0) witchVel.y += witchAc.y * Gdx.graphics.getDeltaTime(); 
		  }
	   
	   if (mouseOverInicio && leftClick) {
		   inMenu = false;
		   menuMusic.stop();
		   gamePlayMusic.play();
	   }
	   
	   if (mouseOverSair && leftClick && inMenu) {
		   Gdx.app.exit();
	   }
   }
   
   public void update()
   {	  
	   	  if(witchPosition.y < -0.6f) { 
	    	  witchVel.y += 0.01f;
	    	  //witchPosition.y = -0.6f;	
	      }
	      witchVel.x *= 0.95;
	      witchVel.y *= 0.95;
	      witchVel.y -= gravity;
	      witchPosition.x += witchVel.x;
	      witchPosition.y += witchVel.y;
	      witch.Rotate(4000 * witchVel.x);
	     
	      
	      if (witchPosition.x > 0 && witchPosition.x < 24.5)
	          Renderer.setCameraPos(new Vector3(witchPosition.x, 0.0f, 0.0f));
	      
	      if (collidesFlag)
	    	  hitCounter += Gdx.graphics.getDeltaTime();
	      if (hitCounter > 1)
	      {
	    	  hitCounter = 0;
	    	  collidesFlag = false;
	      }
	      
	      if (victory == false && defeat == false)
	      {
	    	  if (witchPosition.x > 24.2f) victory = true;
	      	  if (witchLifes == 0) defeat = true;
	      }
	      
	      for (int i = 0; i < totalBats; i++)
	      {
	    	  
	    	  batTimer[i] += Gdx.graphics.getDeltaTime() / 3.0f;
	    	  
	    	  if (batTimer[i] > 1)
	    	  {
		    	  batPaths[i][0].x = batPaths[i][3].x;
		    	  batPaths[i][0].y = batPaths[i][3].y;
		    	  for(int j = 1; j < 4; j++)
		    	  {
		    		  batPaths[i][j].x = (MathUtils.random(1.0f + i % 26, 3.0f + i % 26));  
		    		  batPaths[i][j].y = (MathUtils.random(-1.1f, 1.1f));  
		    	  }
		    	  
	    	  batTimer[i] = 0.0f;
	    	  }
	    	  
	    	  batPositions[i] = new Vector2(mu.cubicBezier(batPaths[i][0], batPaths[i][1], batPaths[i][2], batPaths[i][3], batTimer[i]));
	    	  
	    	  if (witch.Collides(bats[i]) && !collidesFlag && !defeat && !victory)
		      {
		    	  knockBack.x = batPositions[i].x - witchPosition.x;
		    	  knockBack.y = batPositions[i].x - witchPosition.x;
		    	  witchVel.x -= knockBack.x / 10.0f;
		    	  witchVel.y -= knockBack.y / 10.0f;
		    	  collidesFlag = true;
		    	  witchLifes--;
		    	  int hitSound = MathUtils.random(0, 2);
		          hitSounds[hitSound].play();
		      }
	      }     
   }
   
   public void drawGame()
   {
	   	  shader.begin();
	      renderer.InitRender();   
	      renderer.getBatch().setShader(shader);
	      
	      background.Draw(renderer.getBatch(), backgroundSize, backgroundPosition);
	      witch.Draw(renderer.getBatch(), witchSize , witchPosition);
	      house.Draw(renderer.getBatch(), houseSize , housePosition);
	      for (int i = 0; i < totalBats; i++)
	      {
	    	  bats[i].DrawAnim(renderer.getBatch(), batSize, batPositions[i]);
	      }
	      	      
	      lifeIcon.Draw(renderer.getBatch(), lifeIconSize , lifeIconPosition);
	     
	      renderer.resetBatchTransform();
	      font.draw(renderer.getBatch(), String.valueOf(witchLifes), 80, windowHeight - 10);

	      if(victory)
	      {
	    	  if (!gameEndSoundPlayed) {
	    		  victorySound.play();
	    		  gameEndSoundPlayed = true;
	    	  }
	    	  font.draw(renderer.getBatch(), "Você chegou à casa da vovó! \nAperte ESC para voltar ao Menu", windowWidth / 2, windowHeight / 2, 0, Align.center, true);
	    	  if(Gdx.input.isKeyPressed(Keys.ESCAPE))
	    		  Gdx.app.exit();
	      }
	    	  
	      if (defeat) 
	      {
	    	  if (!gameEndSoundPlayed) {
	    		  defeatSound.play();
	    		  gameEndSoundPlayed = true;
	    	  }
	    	  font.draw(renderer.getBatch(), "Game Over :( \nAperte ESC para voltar ao Menu", windowWidth / 2, windowHeight / 2, 0, Align.center, true);
	    	  if(Gdx.input.isKeyPressed(Keys.ESCAPE))
	    		  Gdx.app.exit();   	  
	      }
	      
	      renderer.StopRender();
	      shader.end();
   }
   
   public void drawMenu()
   {
	   	  shader.begin();
	      renderer.InitRender();   
	      renderer.getBatch().setShader(shader);
	      
	      backgroundMenu.Draw(renderer.getBatch(),backgroundMenuSize, backgroundMenuPosition);
	      titulo.DrawAnim(renderer.getBatch(), tituloSize, tituloPosition);
	      inicioMain.DrawAnim(renderer.getBatch(), inicioMainSize, inicioMainPosition);
	      sair.DrawAnim(renderer.getBatch(), sairSize, sairPosition);

	      if (mouseOverInicio) inicioOver.Draw(renderer.getBatch(), inicioOverSize, inicioOverPosition);
	      else if (mouseOverSair) sairOver.Draw(renderer.getBatch(), sairOverSize, sairOverPosition);
	      
	      controls.Draw(renderer.getBatch(), controlsSize, controlsPosition);
	      
	      renderer.StopRender();
	      shader.end();
   }
}