package me.game.desktop;

import com.badlogic.gdx.Gdx;

public class Window {

	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	public Window(int width, int height)
	{
		  this.width = width;
		  this.height = height;
		  centerX = (int) ((float)width / 2.0f);
		  centerY = (int) ((float)height / 2.0f);
		  Gdx.graphics.setWindowedMode(width, height);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
}
