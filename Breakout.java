/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	/** Color of bricks */
	private static final Color[] colors={Color.red,Color.red,Color.orange,Color.orange,
			Color.yellow,Color.yellow,Color.green,Color.green,Color.blue,Color.blue};

	public void run() {
		createPaddle();
		createBricks();
		createBallAndBounce();
		

	}

	private GRect paddle;
	
	/** coordinates of the paddle */
	private static int paddleX = (WIDTH - PADDLE_WIDTH)/2;
	private static int paddleY = (HEIGHT - PADDLE_HEIGHT-PADDLE_Y_OFFSET);
	
	/** create a paddle */
	private void createPaddle() {
		
		paddle = new GRect(paddleX,paddleY,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		
		addMouseListeners();
	}
	
	/** when mouse moves the paddle moves horizontally , too*/
	public void mouseMoved(MouseEvent e){
		paddleX = e.getX();
		if(paddleX > WIDTH - PADDLE_WIDTH) paddleX = WIDTH - PADDLE_WIDTH;
		paddle.setLocation(paddleX,paddleY);
	}
	
	/** create several rows bricks */
	public void createBricks(){
		for(int i = 0;i<NBRICK_ROWS;i++){
			createRowBricks(colors[i],i);
		}
	}
	
	private GRect brick ;
	
	/** create a row of bricks */
	private void createRowBricks(Color color,int row) {
		for(int i = 0;i<NBRICKS_PER_ROW;i++){
			
			//coordinates of the bricks
			int x = BRICK_SEP*(i+1) +i * BRICK_WIDTH;
			int y = BRICK_Y_OFFSET + row*BRICK_HEIGHT + BRICK_SEP*row;
			brick = new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
			brick.setColor(getBackground());;
			brick.setFilled(true);
			brick.setFillColor(color);
			add(brick);
		}
	}

	/** create a ball in the central window and bounce the ball */
	private void createBallAndBounce(){
		GOval ball = createBall();
		add(ball);
		bounceBall(ball);
	}

	/** coordinates of the ball */
	private static int ballX = (WIDTH - BALL_RADIUS*2)/2;
	private static int ballY = (WIDTH - BALL_RADIUS*2)/2;
	
	private GOval createBall() {
		GOval ball = new GOval(ballX,ballY,BALL_RADIUS*2,BALL_RADIUS*2);
		ball.setFilled(true);
		return ball;
	}
	
	private static final double PAUSE_TIME = 1000.0 / 48;

	private double vx,vy;
	
	private void bounceBall(GOval ball){
		RandomGenerator rgen = RandomGenerator.getInstance();
		vx = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean(0.5)) vx = -vx;
		vy = 3.0;
		
		while(true){
			ball.move(vx, vy);
			turn(ball);
			GObject collider = getCollidingObject(ball);
			if(collider == paddle){
				vy = -vy;
			}else if(collider == null){
				
			}else{
				remove(collider);
				vy = -vy;
			}
			pause(PAUSE_TIME);
		}
	}

	private void turn(GOval ball) {
		// TODO Auto-generated method stub
		if(ballBelowFloor(ball)){
			vy = -vy;
		}
		if(ballAboveFloor(ball)){
			vy = -vy;
		}
		if(ballLeftWall(ball)){
			vx = -vx;
		}
		if(ballRightWall(ball)){
			vx = -vx;
		}
	}

	private GObject getCollidingObject(GOval ball) {
		// TODO Auto-generated method stub
		GObject collider = getElementAt(ball.getX(),ball.getY());
		if(collider!=null) return collider;
		else {
			collider = getElementAt(ball.getX() + BALL_RADIUS*2,ball.getY());
			if(collider!=null) return collider;
			else{
				collider = getElementAt(ball.getX() + BALL_RADIUS*2,ball.getY()+ BALL_RADIUS*2);
				if(collider!=null) return collider;
				else{
					collider = getElementAt(ball.getX(),ball.getY()+ BALL_RADIUS*2);
					if(collider!=null) return collider;
				}
			}
		}
		return null;
	}

	private boolean ballBelowFloor(GOval ball) {
		// TODO Auto-generated method stub
		return (ball.getY()+BALL_RADIUS*2)>=HEIGHT;
	}

	private boolean ballAboveFloor(GOval ball) {
		// TODO Auto-generated method stub
		return ball.getY()<=0;
	}

	private boolean ballLeftWall(GOval ball) {
		// TODO Auto-generated method stub
		return ball.getX()<=0;
	}

	private boolean ballRightWall(GOval ball) {
		// TODO Auto-generated method stub
		return ball.getX()+BALL_RADIUS*2>=WIDTH;
	}
}
