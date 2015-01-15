/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author billw
 * @version 1.0.5
 */
package com.sowrdking.imgpuzzle;

/**
 * @author billw
 *
 */
import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageViewAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<ImageView> arraylist_imageview;
	private int imageviewwidth, imageviewheight;
	
	public ImageViewAdapter(Context context,
				ArrayList<ImageView> arraylist_imageview) {

		this.context = context;
		this.arraylist_imageview = arraylist_imageview;

	}

	@Override
	public int getCount() {
		return arraylist_imageview.size();
	}

	@Override
	public Object getItem(int position) {
		return arraylist_imageview.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = arraylist_imageview.get(position);
		convertView.setLayoutParams(new AbsListView.LayoutParams(
				imageviewwidth, imageviewheight));
		
		return convertView;

	}

	public void setImageViewWidth(int width) {
		this.imageviewwidth = width;
	}

	public void setImageViewHeight(int height) {
		this.imageviewheight = height;
	}


}