package com.digitale.lullabyepro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import com.digitale.SoundGenerator.Birds;
import com.digitale.SoundGenerator.Farm;
import com.digitale.SoundGenerator.Nature;
import com.digitale.SoundGenerator.Noise;

import com.digitale.lullabyepro.AnimationView.GraphicObject;


class AnimationThread extends Thread {

	/**
	 * 
	 */
	private final AnimationView animationView;
	private String tag="AnimationThread";
	public AnimationThread(AnimationView animationView, SurfaceHolder surfaceHolder) {
		this.animationView = animationView;
		this.animationView.mSurfaceHolder = surfaceHolder;

		/** Initiate the text painter */
		this.animationView.textPaint = new Paint();
		this.animationView.textPaint.setARGB(255, 255, 255, 255);
		this.animationView.textPaint.setTextSize(32);
	}

	/**
	 * The actual application loop
	 */
	@Override
	public void run() {
		if (Lullabye.debug) Log.d(tag, "mrun  =" + this.animationView.mRun);
		while (this.animationView.mRun) {
			Canvas c = null;

			try {
				c = this.animationView.mSurfaceHolder.lockCanvas(null);
				synchronized (this.animationView.mSurfaceHolder) {

					synchronized (this.animationView.mPauseLock) {
						this.animationView.mPauseLock.notifyAll();
						while (this.animationView.mRun == false) {
							try {
								this.animationView.mPauseLock.wait();
							} catch (InterruptedException e) {
							}
						}
					}
					if(Lullabye.deepDebug) Log.d(tag,"doing main loop");
					updatePhysics();
					if (this.animationView.mdoDraw == true && c != null) {
						doDraw(c);
					
					}
					if (c != null) {
					this.animationView.mSurfaceHolder.unlockCanvasAndPost(c);
					}
					if (Lullabye.ismPlaying() == true) {

						doSound(this.animationView);
					}
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}catch (IllegalArgumentException e){
				
			} finally {
				if (c != null) {
				//	this.animationView.mSurfaceHolder.unlockCanvasAndPost(c);
				}

			}
			
		}

	}

	// Two methods for Runnable/Thread class to manage the Thread
	// properly.
	public void onPause() {

		synchronized (this.animationView.mPauseLock) {
			this.animationView.mRun = false;
			if(Lullabye.debug) Log.d(tag, "on Pause " + this.animationView.mRun);
			SoundManager.stopAllSound(animationView);
			animationView.setMloopedSoundRunning(false);
		}
	}

	public void onResume() {
		this.animationView.mRun = true;
		if(Lullabye.debug) Log.d(tag, "on resume " + this.animationView.mRun);
		synchronized (this.animationView.mPauseLock) {
			run();

			this.animationView.mPauseLock.notifyAll();
		}
	}

	/**
	 * Figures the graphics state based on the passage of realtime. Called at the
	 * start of draw(). 
	 */
	private void updatePhysics() {
		this.animationView.mnow = System.currentTimeMillis();
		this.animationView.setMtimeTenths((int) ((this.animationView.mnow / 10)% 100));
		this.animationView.setMtimeSeconds((int) (this.animationView.mnow / 1000));
		if (this.animationView.mLastTime != 0) {

			this.animationView.time = (this.animationView.mnow - this.animationView.mLastTime);
			
			this.animationView.clousdsx = (this.animationView.clousdsx + (this.animationView.time / 60));

			// reset clouds x co ord if it reaches upper limit
			if (this.animationView.clousdsx >= (this.animationView.mclouds.getWidth() * 4)) {
				this.animationView.clousdsx = 0;
			}
			this.animationView.clousdsx2 = (this.animationView.clousdsx2 + (this.animationView.time / 40));
			// reset clouds x co ord if it reaches upper limit
			if (this.animationView.clousdsx2 >= (this.animationView.mclouds.getWidth() * 4)) {
				this.animationView.clousdsx2 = 0;
			}
			// if lullabye is not playing reset play icon x co-ord
			if (Lullabye.ismPlaying() == false) {
				AnimationView.playx = (Lullabye.getMtransportLeftMargin());
			}
			// set x coordinate for play icon
			if (AnimationView.playx <= Lullabye.getMtransportLeftMargin()) {
				AnimationView.playx = Lullabye.getMtransportLeftMargin();
			}
			AnimationView.playx = (AnimationView.playx + 5);
			int width = (int) (this.animationView.mplayIcon.getWidth() * 1.5);
			// if play icon goes beyond right bound, reset to left bound
			if (Lullabye.deepDebug) Log.d(tag,"rt margin "+Lullabye.getMtransportRightMargin());
			if (AnimationView.playx >= Lullabye.getMtransportRightMargin() - width) {
				AnimationView.playx = Lullabye.getMtransportLeftMargin();
			}
		}

		this.animationView.mLastTime = this.animationView.mnow;
	}

	/**
	 * Draws to the provided Canvas.
	 */
	private void doDraw(Canvas canvas) {

		// Draw the background image
		Bitmap bitmap;
		GraphicObject.Coordinates coords;
		drawBackground(canvas);

		// draw moon
		canvas.drawBitmap(this.animationView.mmoon, (this.animationView.getWidth() / 4), (this.animationView.getHeight() / 8), null);
		// draw clouds twice with offsets to give morphing cloud effect
		drawClouds(canvas, (int) this.animationView.clousdsx);
		drawClouds(canvas, (int) this.animationView.clousdsx2);
		// draw moonglow
		canvas.drawBitmap(this.animationView.mmoonglow, (this.animationView.getWidth() / 4) - 48,
				(this.animationView.getHeight() / 8) - 48, null);
		// if the app is playing display play icon else show stop icon
		if (Lullabye.ismPlaying()) {
			drawState(canvas, (int) AnimationView.playx);
		} else {
			drawStop(canvas);

		}
		for (GraphicObject graphic : this.animationView._graphics) {
			bitmap = graphic.getGraphic();
			coords = graphic.getCoordinates();
			canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
		}
		// check if the _currentGraphic is null or not, if it is not null we
		// will draw it
		if (this.animationView._currentGraphic != null) {
			bitmap = this.animationView._currentGraphic.getGraphic();
			coords = this.animationView._currentGraphic.getCoordinates();
			canvas.drawBitmap(bitmap, coords.getX(), coords.getY(), null);
		}
		canvas.restore();

	}

	private void drawState(Canvas canvas, int left) {
		// rect object to hold co-ords of play icon
		//  (left, top, right bottom)
		int width = (int) (this.animationView.mplayIcon.getWidth() * 1.5);
		int height = (int) (this.animationView.mplayIcon.getHeight() * 1.5);
		// calculate and assign top right cloud section
		AnimationView mAnimationView = (AnimationView) this.animationView.findViewById(R.id.aview);
		int bottom;
		// store animation view co-ords
		bottom = mAnimationView.getBottom();
		Rect rectangle = new Rect(left, (bottom - height - 10), left
				+ width, bottom - 10);
		// draw playicon scaled
		canvas.drawBitmap(this.animationView.mplayIcon, null, rectangle, null);

	}

	private void drawStop(Canvas canvas) {
		// rect object to hold co-ords of stopicon
		// (left, top, right bottom)
		int width = (int) (this.animationView.mstopIcon.getWidth() * 1.5);
		int height = (int) (this.animationView.mstopIcon.getHeight() * 1.5);
		// calculate and assign top right cloud section
		AnimationView mAnimationView = (AnimationView) this.animationView.findViewById(R.id.aview);
		int bottom;
		int aviewwidth;
		aviewwidth = mAnimationView.getWidth();
		// store animation view co-ords
		bottom = mAnimationView.getBottom();
		// centre stop icon at bottom of view
		Rect rectangle = new Rect(((aviewwidth / 2 - width / 2)), (bottom
				- height - 10), ((aviewwidth / 2) + (width / 2)),
				bottom - 10);
		// draw playicon scaled
		canvas.drawBitmap(this.animationView.mstopIcon, null, rectangle, null);

	}

	/**
	 * draw the background
	 * 
	 * @param canvas
	 */
	private void drawBackground(Canvas canvas) {
		// rect object to hold co-ords of animation view
		Rect rectangle = new Rect();
		// get handle to animation view
		AnimationView mAnimationView = (AnimationView) this.animationView.findViewById(R.id.aview);
		// store animation view co-ords
		mAnimationView.getLocalVisibleRect(rectangle);
		// draw BG scaled to fill animation view
		if (this.animationView.mbackground == null) {
			if(Lullabye.debug) Log.d(tag,"mbackground unset!");
			this.animationView.mbackground = (BitmapFactory.decodeResource(this.animationView.getResources(),
					R.drawable.soundactivitybgmain));
		}
		canvas.drawBitmap(this.animationView.mbackground, null, rectangle, null);
	}

	/**
	 * draw the clouds
	 * 
	 * @param canvas
	 */
	private void drawClouds(Canvas canvas, int left) {

		// rect object to hold co-ords of clouds
		//  (left, top, right bottom)
		int width = this.animationView.mclouds.getWidth();
		int height = this.animationView.mclouds.getHeight();
		// calculate and assign top right cloud section

		Rect rectangle = new Rect(left, 0, left + (width * 4),
				(int) (height * .725));
		// draw right clouds scaled
		canvas.drawBitmap(this.animationView.mclouds, null, rectangle, null);
		// recalculate rect to tile clouds and draw left clouds
		// Log.d(tag, "clouds left "+(left-(width*2)));
		rectangle = new Rect(left - (width * 4), 0, left,
				(int) (height * .725));
		// draw clouds scaled
		canvas.drawBitmap(this.animationView.mclouds, null, rectangle, null);

	}

	private void doSound(AnimationView animationView) {
		// Log.v(tag,"looped? "+isMloopedSoundRunning());
		// Log.v(tag,"catagory "+Lullabye.getSelectedCatagory() +
		if (Lullabye.getSelectedCatagory() == 3
				&& animationView.isMloopedSoundRunning() == false) {
			Noise.noise(animationView);
		} else if (Lullabye.getSelectedCatagory() == 0) {	
			Birds myBirds=new Birds();
			myBirds.birds(animationView);
		} else if (Lullabye.getSelectedCatagory() == 1) {
			Nature myNature=new Nature();
			myNature.nature(animationView);
		} else if (Lullabye.getSelectedCatagory() == 2) {
			Farm myFarm=new Farm();
			myFarm.farm(animationView);
		} else {
		}
		;
		// its all gone horribly wrong and no catagory is selected
	}
}