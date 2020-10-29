package me.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Renderer {
	
	private SpriteBatch batch;

	private OrthographicCamera camera;

	static Vector3 _cameraPos;
	
	public void CreateCamera(int cameraWidth, int cameraHeight)
	{
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, cameraWidth, cameraHeight);
	      
	      _cameraPos = new Vector3(0.0f, 0.0f, 0.0f);
	}
	
	public static void setCameraPos(Vector3 cameraPos)
	{
		_cameraPos = cameraPos;
	}
	
	public void CreateBatch()
	{
		 batch = new SpriteBatch();
	}
	
	public void DisposeBatch()
	{
		batch.dispose();
	}
	
	public void InitRender()
	{
		camera.update();
		batch.begin();
	}
	
	public void StopRender()
	{
		batch.end();
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public void resetBatchTransform()
	{
		Matrix4 idt = new Matrix4().idt();
		this.batch.setTransformMatrix(idt);
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
}
