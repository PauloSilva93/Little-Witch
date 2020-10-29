package me.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Object2D {
	
	private Vector3 sVec;
	private Vector3 tVec;
	private Matrix4 model;
	private Rectangle rect;
	private Window window;
	private Renderer renderer = new Renderer();
	private Texture texture;
	private Vector2 size; 
	private Vector2 position;
	private float angle;
	private Vector2 totalFrames = new Vector2();
	private int totalFramesN;
	private int fps;
	private float timer = 0;
	private int[][] frames;
	private boolean firstAnim = true;
	private int firstFrame = 0;
	private boolean firstSpawn = true;
	private boolean cameraRelative = true;
	
	public Object2D(Window window, Texture texture, boolean cameraRelative)
	{
		size = new Vector2(0.0f, 0.0f); 
		position = new Vector2(0.0f, 0.0f);
		this.window = window;
		this.texture = texture;
		this.cameraRelative = cameraRelative;
		rect = new Rectangle();
		rect.width = texture.getWidth();
		rect.height = texture.getHeight();
		angle = 0;
	    model = new Matrix4();   
	}
	
	public Object2D(Window window, Texture texture, Vector2 totalFrames, int blankFrames, int fps, int firstFrame)
	{
		size = new Vector2(0.0f, 0.0f); 
		position = new Vector2(0.0f, 0.0f);
		this.totalFrames = totalFrames;
		this.fps = fps;
		this.window = window;
		this.texture = texture;
		this.firstFrame = firstFrame;
		rect = new Rectangle();
		rect.width = texture.getWidth();
		rect.height = texture.getHeight();
		angle = 0;
	    model = new Matrix4();   
	    
		totalFramesN = (int)totalFrames.x * (int)totalFrames.y - blankFrames;
		frames = new int[totalFramesN][4];
		int frameX = 0;
		int frameY = 0;
		for (int i = 0; i < totalFramesN; i++)
		{
			if(frameX % totalFrames.x == 0 && frameX != 0)
			{
				frameX = 0;
				frameY++;
			}
			
			int []frameLayout = { (int)(rect.width / totalFrames.x) * frameX, (int)(rect.height / totalFrames.y) * frameY, (int)(rect.width / totalFrames.x), (int)(rect.height / totalFrames.y) }; 
			
			frameX++;
			
			for (int j = 0; j < 4; j++)
			{
				frames[i][j] = frameLayout[j];
			}	
		}
		
		rect = new Rectangle();
		rect.width = texture.getWidth();
		rect.height = texture.getHeight();
		angle = 0;
	    model = new Matrix4();   
	}
	
	
	public void Draw(SpriteBatch batch, Vector2 size, Vector2 position)
	{
		this.size = size;
		this.position = position;
		rect.x = position.x;
		rect.y = position.y;
		
		sVec = new Vector3((window.getWidth() / rect.width) * size.x, (window.getHeight() / rect.height) * size.y, 0.0f);
		
		if (cameraRelative)
		tVec = new Vector3(window.getCenterX() + window.getCenterX() * (rect.x - Renderer._cameraPos.x) - (rect.width / 2) * sVec.x, window.getCenterY() + window.getCenterY() * (rect.y - Renderer._cameraPos.y) - (rect.height / 2) * sVec.y, 0.0f);
		else tVec = new Vector3(window.getCenterX() + window.getCenterX() * (rect.x) - (rect.width / 2) * sVec.x, window.getCenterY() + window.getCenterY() * (rect.y) - (rect.height / 2) * sVec.y, 0.0f);
		
        model = model.idt();
        model = model.translate(tVec);
        model = model.scale(sVec.x, sVec.y, sVec.z);
        model = model.rotate(new Vector3(0.0f, 0.0f, 1.0f), angle);
        batch.setTransformMatrix(model);
        batch.draw(texture, rect.x, rect.y);
        firstSpawn = false;
	}
	
	public void DrawAnim(SpriteBatch batch, Vector2 size, Vector2 position)
	{
		this.size = size;
		this.position = position;
		rect.x = position.x;
		rect.y = position.y;
		
		sVec = new Vector3(((window.getWidth() / rect.width) * size.x) * totalFrames.x, ((window.getHeight() / rect.height) * size.y) * totalFrames.y, 0.0f);
		tVec = new Vector3(window.getCenterX() + window.getCenterX() * (rect.x - Renderer._cameraPos.x) - (rect.width / 2) * sVec.x / totalFrames.x, window.getCenterY() + window.getCenterY() * (rect.y - Renderer._cameraPos.y) - (rect.height / 2) * sVec.y / totalFrames.y, 0.0f);
		
        model = model.idt();
        model = model.translate(tVec);
        model = model.scale(sVec.x , sVec.y , sVec.z);
        model = model.rotate(new Vector3(0.0f, 0.0f, 1.0f), angle);
        batch.setTransformMatrix(model);
        
        if (firstAnim)
        {
        	timer = firstFrame;
        	firstAnim = false;
        }
        timer += Gdx.graphics.getDeltaTime() * 20;
        if (timer >= totalFramesN) timer = 0;
        int []currentFrame = { frames[(int)timer][0], frames[(int)timer][1], frames[(int)timer][2], frames[(int)timer][3]};
        float []frameOrigin = { (rect.width / totalFrames.x) * currentFrame[0], (rect.height / totalFrames.y) * currentFrame[1] }; 
        batch.draw(texture, (float)rect.x, (float)rect.y, currentFrame[0], currentFrame[1], currentFrame[2], currentFrame[3]);
        firstSpawn = false;
	}
	
	public void Rotate(float degrees)
	{
		this.angle = degrees;
	}
	
	public boolean Collides(Object2D object2D)
	{
		float[] rect1P1 = {this.position.x, this.position.y}; 
		float[] rect1P2 = {this.position.x, this.position.y + this.size.y}; 
		float[] rect1P3 = {this.position.x + this.size.x, this.position.y}; 
		float[] rect1P4 = {this.position.x + this.size.x, this.position.y + this.size.y}; 
		float[][] rect1 = {rect1P1, rect1P2, rect1P3, rect1P4};
		
		float[] rect2P1 = {object2D.position.x, object2D.position.y}; 
		float[] rect2P2 = {object2D.position.x, object2D.position.y + object2D.size.y}; 
		float[] rect2P3 = {object2D.position.x + object2D.size.x, object2D.position.y}; 
		float[] rect2P4 = {object2D.position.x + object2D.size.x, object2D.position.y + object2D.size.y}; 
		float[][] rect2 = {rect2P1, rect2P2, rect2P3, rect2P4};
		
		for (int i = 0; i < 4; i++)
		{
			if ((rect1[0][0] <= rect2[i][0] && rect2[i][0] <= rect1[3][0]) && (rect1[0][1] <= rect2[i][1] && rect2[i][1] <= rect1[3][1]))
				if (!firstSpawn)
					return true;
			if ((rect2[0][0] <= rect1[i][0] && rect1[i][0] <= rect2[3][0]) && (rect2[0][1] <= rect1[i][1] && rect1[i][1] <= rect2[3][1]))
				if (!firstSpawn)
					return true;
		}
		return false;
	}
	
	public boolean collidesPoint(Vector2 point, Vector2 rectPos, Vector2 rectSize)
	{
		if ((rectPos.x <= point.x && point.x <= rectPos.x + rectSize.x) && (rectPos.y <= point.y && point.y <= rectPos.y + rectSize.y))
		return true;
		else return false;
	}
}
