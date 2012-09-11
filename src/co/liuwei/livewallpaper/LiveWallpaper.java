package co.liuwei.livewallpaper;
import java.util.Random;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * @author liuwei
 * @version 2012-9-10
 */
@TargetApi(7)
public class LiveWallpaper extends WallpaperService
{
	//屏幕分辨率
	int screenWidth;
    int screenHeight; 
    
    //背景图片
    Bitmap bg = null;
    //气泡图片
	Bitmap bubble = null;
	Bitmap explode = null;
	
	public Engine onCreateEngine()
	{
		//DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = 480;//dm.widthPixels;
		screenHeight =854;//dm.heightPixels;
		
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);  
		bubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_004); 
		explode = BitmapFactory.decodeResource(getResources(), R.drawable.explode4); 
		// 返回自定义的Engine
		return new MyEngine();
	}

	class MyEngine extends Engine
	{
		// 记录程序界面是否可见
		private boolean mVisible;
		// 记录当前当前用户动作事件的发生位置
		private float mTouchX = -1;
		private float mTouchY = -1;
		
		//气泡
		Bubble b1 = new Bubble(0, screenHeight);
		Bubble b2 = new Bubble(48, screenHeight+350);
		Bubble b3 = new Bubble(96, screenHeight+200);
		Bubble b4 = new Bubble(144, screenHeight+510);
		Bubble b5 = new Bubble(192, screenHeight+30);
		Bubble b6 = new Bubble(240, screenHeight+390);
		Bubble b7 = new Bubble(288, screenHeight+200);
		Bubble b8 = new Bubble(336, screenHeight+310);
		Bubble b9 = new Bubble(384, screenHeight+90);
		Bubble b10 = new Bubble(432, screenHeight+150);
		Bubble b11 = new Bubble(480, screenHeight+100);
		
		// 定义画笔
		private Paint mPaint = new Paint();
		// 定义一个Handler
		Handler mHandler = new Handler();
		// 定义一个周期性执行的任务
		private final Runnable drawTarget = new Runnable()
		{
			public void run()
			{
				// 动态地绘制图形
				drawFrame();
			}
		};

		@Override
		public void onCreate(SurfaceHolder surfaceHolder)
		{
			super.onCreate(surfaceHolder);
			
			// 初始化画笔
			mPaint.setTextSize(24);
			mPaint.setColor(Color.RED);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.STROKE);
			// 设置处理触摸事件
			setTouchEventsEnabled(true);
			
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			// 删除回调
			mHandler.removeCallbacks(drawTarget);
		}

		@Override
		public void onVisibilityChanged(boolean visible)
		{
			mVisible = visible;
			// 当界面可见时候，执行drawFrame()方法。
			if (visible)
			{
				// 动态地绘制图形
				drawFrame();
			}
			else
			{
				// 如果界面不可见，删除回调
				mHandler.removeCallbacks(drawTarget);
			}
		}

		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels)
		{
			drawFrame();
		}


		public void onTouchEvent(MotionEvent event)
		{
			// 如果检测到滑动操作
			if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				mTouchX = event.getX();
				mTouchY = event.getY();
			}
			else
			{
				mTouchX = -1;
				mTouchY = -1;
			}
			super.onTouchEvent(event);
		}

		// 定义绘制图形的工具方法
		private void drawFrame()
		{
			// 获取该壁纸的SurfaceHolder
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try
			{
				// 对画布加锁
				c = holder.lockCanvas();
				if (c != null)
				{
					c.save();
					// 绘制背景图片
				    RectF rectF = new RectF(0, 0, screenWidth, screenHeight);   //w和h分别是屏幕的宽和高，也就是你想让图片显示的宽和高  
				    c.drawBitmap(bg, null, rectF, mPaint);  
					// 在触碰点绘制圆圈
					drawTouchPoint(c);
					// 绘制气泡
					drawBitmap(c, b1, mPaint);
					drawBitmap(c, b2, mPaint);
					drawBitmap(c, b3, mPaint);
					drawBitmap(c, b4, mPaint);
					drawBitmap(c, b5, mPaint);
					drawBitmap(c, b6, mPaint);
					drawBitmap(c, b7, mPaint);
					drawBitmap(c, b8, mPaint);
					drawBitmap(c, b9, mPaint);
					drawBitmap(c, b10, mPaint);
					drawBitmap(c, b11, mPaint);
					
					c.restore();
				}
			}
			finally
			{
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}
			mHandler.removeCallbacks(drawTarget);
			// 调度下一次重绘
			if (mVisible)
			{
				
				resetBubble(b1, 0, screenHeight);
				resetBubble(b2, 48, screenHeight);
				resetBubble(b3, 96, screenHeight);
				resetBubble(b4, 144, screenHeight);
				resetBubble(b5, 192, screenHeight);
				resetBubble(b6, 240, screenHeight);
				resetBubble(b7, 288, screenHeight);
				resetBubble(b8, 336, screenHeight);
				resetBubble(b9, 384, screenHeight);
				resetBubble(b10, 432, screenHeight);
				resetBubble(b11, 480, screenHeight);
				
				// 指定0.1秒后重新执行mDrawCube一次
				mHandler.postDelayed(drawTarget, 100);
			}
		}

		private void drawBitmap(Canvas c, Bubble b, Paint paint){
			if(b.type == 1){
				c.drawBitmap(bubble, b.x, b.y, paint);
			}else{
				c.drawBitmap(explode, b.x, b.y, paint);
			}
			
		}
		
		private void resetBubble(Bubble b,float x,float y){
			b.x += getRandomFalterValue();
			b.y -= getRandomUpValue();
			if (b.y < 0)
			{
				b.x = x;
				b.y = y;
				b.type = 1;
			}
		}
		// 在屏幕触碰点绘制圆圈
		private void drawTouchPoint(Canvas c)
		{
			changeType(b1);
			changeType(b2);
			changeType(b3);
			changeType(b4);
			changeType(b5);
			changeType(b6);
			changeType(b7);
			changeType(b8);
			changeType(b9);
			changeType(b10);
			changeType(b11);
		}
		
		private void changeType(Bubble b){
			if(Math.abs(b.x - mTouchX) < 60 && Math.abs(b.y - mTouchY) < 60){
				b.type = 2;
			}
		}
		//获取左右摇晃的随机值
		private float getRandomFalterValue(){
			Random rdm=new Random();  
			return (float)(rdm.nextInt()%15);
		}
		//获取0到10的随机数
		private float getRandomUpValue(){
			Random rdm=new Random();  
			return (float)((rdm.nextInt()& 0x7fffffff)%15);
		}
		
	}
	
	//气泡
	class Bubble {
		float x;
		float y;
		int type = 1;
		
		public Bubble(float x,float y){
			this.x = x;
			this.y = y;
		}
	}
	
}