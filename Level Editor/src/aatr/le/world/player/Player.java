package aatr.le.world.player;

import org.lwjgl.input.Keyboard;


//import sk.stb.STB;
//import aatr.engine.gamestate.GameState;
//import aatr.engine.gamestate.GameStateWorld;

import aatr.le.gfx.renderer.QuadRenderer;
import aatr.le.world.Chunk;
import aatr.le.world.World;
import aatr.le.world.tile.TileProperty;

public class Player /*extends QuadRenderer*/ {
	/*
	public static final String STB_WALK_COUNTER = "Player Walk Counter";

	private int x = 0, y = 0;
	private int cx = 0, cy = 0;
	private int layer = 0;
	
	private float speed = 500;

	private boolean isWalking;

	private Direction dir;

	private GameStateWorld gs;

	public static final float SIDE = World.GRID_SIZE;

	public Player(GameStateWorld gs) {
		super(0, 0, SIDE, SIDE);
		this.gs = gs;
		dir = Direction.DOWN;
		isWalking = false;

		STB.start(STB_WALK_COUNTER, .1f);
	}

	public void checkKeyboard(int key, boolean pressed) {
		if (pressed && !isWalking) {
			switch (key) {
			case Keyboard.KEY_A:
				if(dir != Direction.LEFT) {
					faceDir(Direction.LEFT);
					STB.reset(STB_WALK_COUNTER);
				}
				break;
			case Keyboard.KEY_D:
				if(dir != Direction.RIGHT) {
					faceDir(Direction.RIGHT);
					STB.reset(STB_WALK_COUNTER);
				}
				break;
			case Keyboard.KEY_W:
				if(dir != Direction.UP) {
					faceDir(Direction.UP);
					STB.reset(STB_WALK_COUNTER);
				}
				break;
			case Keyboard.KEY_S:
				if(dir != Direction.DOWN) {
					faceDir(Direction.DOWN);
					STB.reset(STB_WALK_COUNTER);
				}
				break;

			}
		}
	}

	public void update(double tick) {
		
		STB.update(tick, STB_WALK_COUNTER);
		
		if (isWalking) {
			switch (dir) {
			case RIGHT:
				super.translate((float) (speed * tick), 0);
				if ((x + 1) * World.GRID_SIZE < super.getTransform().position.x) {
					super.getTransform().position.x = (x + 1) * World.GRID_SIZE;
					x++;
					isWalking = false;
				}
				break;
			case LEFT:
				super.translate((float) (-speed * tick), 0);
				if ((x - 1) * World.GRID_SIZE > super.getTransform().position.x) {
					super.getTransform().position.x = (x - 1) * World.GRID_SIZE;
					x--;
					isWalking = false;
				}
				break;
			case DOWN:
				super.translate(0, (float) (speed * tick));
				if ((y + 1) * World.GRID_SIZE < super.getTransform().position.y) {
					super.getTransform().position.y = (y + 1) * World.GRID_SIZE;
					y++;
					isWalking = false;
				}
				break;
			case UP:
				super.translate(0, (float) (-speed * tick));
				if ((y - 1) * World.GRID_SIZE > super.getTransform().position.y) {
					super.getTransform().position.y = (y - 1) * World.GRID_SIZE;
					y--;
					isWalking = false;
				}
				break;
			}

		} else if (STB.done(STB_WALK_COUNTER)) {

			boolean w = Keyboard.isKeyDown(Keyboard.KEY_W);
			boolean a = Keyboard.isKeyDown(Keyboard.KEY_A);
			boolean s = Keyboard.isKeyDown(Keyboard.KEY_S);
			boolean d = Keyboard.isKeyDown(Keyboard.KEY_D);

			switch (dir) {
			case UP:
				if (w
						&& !gs.getWorld(layer).getTile(x, y - 1)
								.is(TileProperty.SOLID))
					walk(Direction.UP);
				break;
			case DOWN:
				if (s
						&& !gs.getWorld(layer).getTile(x, y + 1)
								.is(TileProperty.SOLID))
					walk(Direction.DOWN);
				break;
			case RIGHT:
				if (d
						&& !gs.getWorld(layer).getTile(x + 1, y)
								.is(TileProperty.SOLID))
					walk(Direction.RIGHT);
				break;
			case LEFT:
				if (a
						&& !gs.getWorld(layer).getTile(x - 1, y)
								.is(TileProperty.SOLID))
					walk(Direction.LEFT);
				break;
			}
			if (!isWalking) {
				if (w
						&& !gs.getWorld(layer).getTile(x, y - 1)
								.is(TileProperty.SOLID))
					walk(Direction.UP);
				else if (a
						&& !gs.getWorld(layer).getTile(x - 1, y)
								.is(TileProperty.SOLID))
					walk(Direction.LEFT);
				else if (s
						&& !gs.getWorld(layer).getTile(x, y + 1)
								.is(TileProperty.SOLID))
					walk(Direction.DOWN);
				else if (d
						&& !gs.getWorld(layer).getTile(x + 1, y)
								.is(TileProperty.SOLID))
					walk(Direction.RIGHT);
			}
		}
	}

	public void faceDir(Direction dir) {
		this.dir = dir;
	}

	public void walk(Direction dir) {
		if (isWalking)
			return;

		if (this.dir == dir)
			this.isWalking = true;

		this.dir = dir;
	}

	public int getLayer() {
		return layer;
	}

	public Player setLayer(int layer) {
		this.layer = layer;
		return this;
	}

	public Player setPosition(int x, int y) {
		this.getTransform().position.set(x * World.GRID_SIZE, y
				* World.GRID_SIZE);
		this.x = x;
		this.y = y;
		return this;
	}

	public int getX() {
		return x;
	}

	public void placeX(int x) {
		this.getTransform().position.setX(x * World.GRID_SIZE);
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.getTransform().position.setX(y * World.GRID_SIZE);
		this.y = y;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Direction getDir() {
		return dir;
	}

	public Player setChunkCoords(int cx, int cy) {
		this.cx = cx;
		this.cy = cy;
		return this;
	}
	
	*/
	
}