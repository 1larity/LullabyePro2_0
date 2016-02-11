package com.digitale.lullabyepro;


import java.util.ArrayList;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimationView extends SurfaceView implements SurfaceHolder.Callback {
	public static float playx = 100;
	public  boolean mRun = false;
	long mLastTime;
	ArrayList<GraphicObject> _graphics = new ArrayList<GraphicObject>();
	private AnimationThread thread;
	private boolean mspecialLoop = false;
//	private boolean mGameIsRunning = false;

	GraphicObject _currentGraphic = null;
	/** Variables for the counter */
	

	/** Handle to the surface manager object we interact with */
	SurfaceHolder mSurfaceHolder;
	Paint textPaint;
	Bitmap mmoonglow = (BitmapFactory.decodeResource(getResources(),
			R.drawable.moonglow));
	Bitmap mbackground = (BitmapFactory.decodeResource(getResources(),
			R.drawable.soundactivitybgmain));
	Bitmap mclouds = (BitmapFactory.decodeResource(getResources(),
			R.drawable.clouds));
	Bitmap mmoon = (BitmapFactory.decodeResource(getResources(),
			R.drawable.moon));
	Bitmap mplayIcon = (BitmapFactory.decodeResource(getResources(),
			android.R.drawable.ic_media_play));
	Bitmap mstopIcon = (BitmapFactory.decodeResource(getResources(),
			android.R.drawable.ic_media_pause));
	float time;
	float clousdsx;
	float clousdsx2;
	public long mnow;
	private int mtimeTenths;
	private int mtimeSeconds;
	Object mPauseLock = new Object();
	private int mwavesLoop = 1;
	boolean mdoDraw = true;
	static final String tag = "AnimationView";

	

	/**
	 * So we can stop/pause the game loop
	 */
	public void setRunning(boolean b) {
		mRun = b;
	}

	// graphic class containing a bitmap and the coordinates where it is
	// located.
	class GraphicObject {

		// Speed Class
		// we need the direction of the movement and the speed. For this we need
		// a new inner class comparable with the inner class Coordinates in our
		// class GraphicObject.
		public class Speed {
			private static final int _defaultSpeed = 20;
			public static final int X_DIRECTION_RIGHT = 1;
			public static final int X_DIRECTION_LEFT = -1;
			public static final int Y_DIRECTION_DOWN = -1;
			public static final int Y_DIRECTION_UP = 1;

			private int _x = _defaultSpeed;
			private int _y = _defaultSpeed;

			private int _xDirection = X_DIRECTION_RIGHT;
			private int _yDirection = Y_DIRECTION_DOWN;

			/**
			 * @return the _xDirection
			 */
			public int getXDirection() {
				return _xDirection;
			}

			/**
			 * @param direction
			 *            the _xDirection to set
			 */
			public void setXDirection(int direction) {
				_xDirection = direction;
			}

			public void toggleXDirection() {
				if (_xDirection == X_DIRECTION_RIGHT) {
					_xDirection = X_DIRECTION_LEFT;
				} else {
					_xDirection = X_DIRECTION_RIGHT;
				}
			}

			/**
			 * @return the _yDirection
			 */
			public int getYDirection() {
				return _yDirection;
			}

			/**
			 * @param direction
			 *            the _yDirection to set
			 */
			public void setYDirection(int direction) {
				_yDirection = direction;
			}

			public void toggleYDirection() {
				if (_yDirection == Y_DIRECTION_DOWN) {
					_yDirection = Y_DIRECTION_UP;
				} else {
					_yDirection = Y_DIRECTION_DOWN;
				}
			}

			/**
			 * @return the _x
			 */
			public int getX() {
				return _x;
			}

			/**
			 * @param speed
			 *            the _x to set
			 */
			public void setX(int speed) {
				_x = speed;
			}

			/**
			 * @return the _y
			 */
			public int getY() {
				return _y;
			}

			/**
			 * @param speed
			 *            the _y to set
			 */
			public void setY(int speed) {
				_y = speed;
			}

			@Override
			public String toString() {
				String xDirection;
				if (_xDirection == X_DIRECTION_RIGHT) {
					xDirection = "right";
				} else {
					xDirection = "left";
				}
				return "Speed: x: " + _x + " | y: " + _y + " | xDirection: "
						+ xDirection;
			}
		}

		/**
		 * Contains the coordinates of the graphic.
		 */
		public class Coordinates {
			private int _x = 100;
			private int _y = 0;

			public Speed getSpeed() {
				return _speed;
			}

			public int getX() {
				return _x + _bitmap.getWidth() / 2;
			}

			public void setX(int value) {
				_x = value - _bitmap.getWidth() / 2;
			}

			public int getY() {
				return _y + _bitmap.getHeight() / 2;
			}

			public void setY(int value) {
				_y = value - _bitmap.getHeight() / 2;
			}

			@Override
			public String toString() {
				return "Coordinates: (" + _x + "/" + _y + ")";
			}
		}

		private Bitmap _bitmap;
		private Coordinates _coordinates;
		private Speed _speed;

		public GraphicObject(Bitmap bitmap) {
			_bitmap = bitmap;
			_coordinates = new Coordinates();
			_speed = new Speed();
		}

		public Speed getSpeed() {
			return _speed;
		}

		public Bitmap getGraphic() {
			return _bitmap;
		}

		public Coordinates getCoordinates() {
			return _coordinates;
		}
	}

	/** The thread that actually draws the animation */
	static boolean mloopedSoundRunning = false;

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// create thread only; it's started in surfaceCreated()
		thread = new AnimationThread(this, holder);

	}

	/**
	 * Callback invoked when the surface dimensions change.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// added fix -->
		mdoDraw = true;
		// Log.d(tag,"thread state "+ thread.getState());
		if (thread.getState() == Thread.State.TERMINATED) {
			thread = new AnimationThread(this, mSurfaceHolder);
			mRun = true;
			thread.start();
			// <-- added fix
		} else if (thread.getState() == Thread.State.NEW) {
			thread = new AnimationThread(this, mSurfaceHolder);
			mRun = true;
			thread.start();
		} else {
			synchronized (mPauseLock) {
				mRun = true;
				mPauseLock.notifyAll();
			}
		}
	}

	/**
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mdoDraw = false;

	}

	/**
	 * @return the mloopedSoundRunning
	 */
	public boolean isMloopedSoundRunning() {
		return mloopedSoundRunning;
	}

	/**
	 * @param mloopedSoundRunning
	 *            the mloopedSoundRunning to set
	 */
	public void setMloopedSoundRunning(boolean mloopedSoundRunning) {
		AnimationView.mloopedSoundRunning = mloopedSoundRunning;
	}

	public int getMtimeTenths() {
		return mtimeTenths;
	}

	public void setMtimeTenths(int mtimeTenths) {
		this.mtimeTenths = mtimeTenths;
	}

	public boolean isMspecialLoop() {
		return mspecialLoop;
	}

	public void setMspecialLoop(boolean mspecialLoop) {
		this.mspecialLoop = mspecialLoop;
	}

	public int getMtimeSeconds() {
		return mtimeSeconds;
	}

	public void setMtimeSeconds(int mtimeSeconds) {
		this.mtimeSeconds = mtimeSeconds;
	}

	public int getMwavesLoop() {
		return mwavesLoop;
	}

	public void setMwavesLoop(int mwavesLoop) {
		this.mwavesLoop = mwavesLoop;
	}
}