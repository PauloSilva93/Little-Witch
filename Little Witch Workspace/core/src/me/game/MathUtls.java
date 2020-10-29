package me.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MathUtls {
	
	Vector2 cubicBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float t)
	{
		Vector2 finalPnt = new Vector2();
		finalPnt.x = (float)(Math.pow((1 - t), 3) * p0.x) +
		(float)Math.pow((1 - t), 2) * 3 * t * p1.x	+
		(1 - t) * 3 * t * t * p2.x +
		t * t * t * p3.x;
		
		finalPnt.y = (float)(Math.pow((1 - t), 3) * p0.y) +
		(float)Math.pow((1 - t), 2) * 3 * t * p1.y	+
		(1 - t) * 3 * t * t * p2.y +
		t * t * t * p3.y;

		return finalPnt;
	}
}
