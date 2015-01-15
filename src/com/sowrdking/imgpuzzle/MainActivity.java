package com.sowrdking.imgpuzzle;

import java.util.ArrayList;
import java.util.Collections;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

public class MainActivity extends Activity {

	private GridView gridview;
	private ArrayList<ImageView> arraylist_imageview;
	private ArrayList<Integer> shuffle_arraylist;
	private ImageViewAdapter imageViewadapter;
	private ImageView[] imageviewactivity;
	private Button restart;
	private int[] imageViewinitid;
	private int[] imageViewshuffleid;
	private int gridViewbackgroundcolor = Color.WHITE;
	private int imageview = R.drawable.ic_launcher;
	private int imageViewsize3_3;

	private int row = 3;
	private int col = 3;
	private ImageView answerimageView;
	private ImageView magicimageView;
	private ImageView selectimageView;
	private TextView mSteps;
	private int mCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageViewsize3_3 = (int) ((getResources().getDisplayMetrics().widthPixels - (20 * getResources()
				.getDisplayMetrics().density)) / 3);
		initGridViewUI(col, row * col, imageViewsize3_3, gridViewbackgroundcolor);
		setImageViewInitId();
		setImageViewShuffleId();
		mSteps = (TextView)findViewById(R.id.step_count);
		drawActivityBackground();
		restart = (Button) this.findViewById(R.id.res_but);

		restart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mCount = 0;
				mSteps.setText("0");
				initGridViewUI(col, row * col, imageViewsize3_3, gridViewbackgroundcolor);
				setImageViewInitId();
				setImageViewShuffleId();
			}
		});
	}

	public void initGridViewUI(int col, int count, int size, int gridview_background) {

		initImageViewUI(col, count, size);
		gridview = (GridView) this.findViewById(R.id.fragment1);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridview.setNumColumns(col);
		gridview.setLayoutParams(new RelativeLayout.LayoutParams(col * size, col * size));
		gridview.setBackgroundColor(gridview_background);
		imageViewadapter = new ImageViewAdapter(this, arraylist_imageview);
		imageViewadapter.setImageViewWidth(size);
		imageViewadapter.setImageViewHeight(size);
		gridview.setAdapter(imageViewadapter);
		
		gridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
	                GridView parent = (GridView)v;	                
	                int x = (int) event.getX();
	                int y = (int) event.getY();

	                int position = parent.pointToPosition(x, y);
	                
	                if (position > AdapterView.INVALID_POSITION) {
	                    int count = parent.getChildCount();
	                    
	                    for (int i = 0; i < count; i++) {
	                        View curr = parent.getChildAt(i);
	                        curr.setOnDragListener(new View.OnDragListener() {

	                            @Override
	                            public boolean onDrag(View v, DragEvent event) {
	                            	
	                            	magicimageView = (ImageView)v;	                            	
	                            	
	                                boolean result = true;
	                                int action = event.getAction();
	                                
	                                switch (action) {
		                                case DragEvent.ACTION_DRAG_STARTED:		                                	
		                                    break;
		                                case DragEvent.ACTION_DRAG_LOCATION:
		                                    break;
		                                case DragEvent.ACTION_DRAG_ENTERED:
		                                	v.setScaleX((float)1.1);
		                                	v.setScaleY((float)1.1);
		                                	if (event.getLocalState() == v) {
		                                        result = false;
		                                    }
		                                    int select = selectimageView.getId();
	                    					int magic = magicimageView.getId();
	                    					swap(selectimageView, magicimageView);
	                    					completePuzzle(select, magic);
		                                    break;
		                                case DragEvent.ACTION_DRAG_EXITED:	 
		                                	v.setScaleX((float)1.0);
		                                	v.setScaleY((float)1.0);
		                                    break;
		                                case DragEvent.ACTION_DROP:
		                                    
		                                    break;
		                                case DragEvent.ACTION_DRAG_ENDED:	                                    
		                                	v.setScaleX((float)1.0);
		                                	v.setScaleY((float)1.0);
		                                	mSteps.setText(Integer.toString(mCount));
		                                	break;
		                                default:
		                                    result = false;
		                                    break;
	                                }
	                                return result;
	                            }
	                        });
	                    }
	                    mCount++;
	                    int relativePosition = position - parent.getFirstVisiblePosition();
	                    View target = (View)parent.getChildAt(relativePosition);
	                    selectimageView = (ImageView)target;
	                    ClipData data = ClipData.newPlainText("DragData", "");
	                    target.startDrag(data, new View.DragShadowBuilder(target), target, 0);
	                }
	            }
				return true;
			}
			
		});
	}

	public void initImageViewUI(int divisor, int number, int size) {

		int count = -1;
		int proportion = size * divisor;
		arraylist_imageview = new ArrayList<ImageView>();
		setShuffleArrayList(number);
		Bitmap mBitmap = Bitmap.createScaledBitmap(
				((BitmapDrawable) getResources().getDrawable(imageview))
				.getBitmap(), proportion, proportion, true);

		imageviewactivity = new ImageView[number];

		for (int i = 0; i < imageviewactivity.length; i++) {
			imageviewactivity[i] = new ImageView(this);
			arraylist_imageview.add(imageviewactivity[i]);
			imageviewactivity[i].setId(i);
		}

		for (int h = 0; h < proportion; h += proportion / divisor) {
			for (int w = 0; w < proportion; w += proportion / divisor) {
				imageviewactivity[shuffle_arraylist.get(++count)]
						.setImageBitmap(Bitmap.createBitmap(mBitmap, w, h,
								(proportion / divisor), (proportion / divisor),
								null, true));
			}

		}

		answerimageView = new ImageView(this);
		answerimageView.setImageBitmap(mBitmap);

	}

	public void setImageViewInitId() {

		imageViewinitid = new int[imageviewactivity.length];
		
		for (int i = 0; i < imageViewinitid.length; i++) {
			imageViewinitid[i] = i;
		}
	}

	public void setImageViewShuffleId() {

		imageViewshuffleid = new int[imageviewactivity.length];
		
		for (int i = 0; i < imageViewinitid.length; i++) {
			imageViewshuffleid[shuffle_arraylist.get(i)] = i;
		}
	}

	public void setShuffleArrayList(int number) {

		shuffle_arraylist = new ArrayList<Integer>();
		
		for (int shuffle = 0; shuffle < number; shuffle++) {
			shuffle_arraylist.add(shuffle);
		}

		Collections.shuffle(shuffle_arraylist);
	}

	private void swap(ImageView v1, ImageView v2) {
		Drawable tempDraw = v2.getDrawable();
		v2.setImageDrawable(v1.getDrawable());
		v1.setImageDrawable(tempDraw);
		selectimageView = v2;
	}

	public void completePuzzle(int selectid, int magicbuttonid) {

		int result = 0;
		int select = imageViewshuffleid[selectid];
		imageViewshuffleid[selectid] = imageViewshuffleid[magicbuttonid];
		imageViewshuffleid[magicbuttonid] = select;

		for (int i = 0; i < imageViewshuffleid.length; i++) {
			if (imageViewinitid[i] == imageViewshuffleid[i]) {
				result++;
			}
		}

		if (result == imageViewinitid.length) {
			gridview.setBackground(answerimageView.getDrawable());
						
			new AlertDialog.Builder(this)			
			.setMessage("Finish")
	        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
				}
			})
	        .show();
		}
	}

	public void drawActivityBackground() {
		GradientDrawable grad = new GradientDrawable(Orientation.TL_BR,
				new int[] { Color.rgb(0, 0, 127), Color.rgb(0, 0, 255),
				Color.rgb(127, 0, 255), Color.rgb(127, 127, 255),
				Color.rgb(127, 255, 255), Color.rgb(255, 255, 255) });
		this.getWindow().setBackgroundDrawable(grad);

	}

}
