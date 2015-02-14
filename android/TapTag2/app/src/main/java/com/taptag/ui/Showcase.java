package com.taptag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.taptag.R;
import com.taptag.custom.CustomFragment;

/**
 * The Class Showcase is the Fragment class that is launched when the user
 * clicks on Showcase button in Left navigation drawer.
 */
public class Showcase extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.showcase, null);

		GridView grid = (GridView) v.findViewById(R.id.grid);
		grid.setAdapter(new GridAdapter());
		return v;
	}

	/**
	 * The Class GridAdapter is the adpater for displaying Products in GridView.
	 * The current implementation simply display dummy product images. You need
	 * to change it as per your needs.
	 */
	private class GridAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return 20;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			if (v == null)
				v = LayoutInflater.from(getActivity()).inflate(
						R.layout.profile_item, null);

			ImageView img = (ImageView) v.findViewById(R.id.img);
			if (pos % 6 == 0)
				img.setImageResource(R.drawable.showcase1);
			else if ((pos - 1) % 6 == 0)
				img.setImageResource(R.drawable.showcase2);
			else if ((pos - 2) % 6 == 0)
				img.setImageResource(R.drawable.showcase3);
			else if ((pos - 3) % 6 == 0)
				img.setImageResource(R.drawable.showcase4);
			else if ((pos - 4) % 6 == 0)
				img.setImageResource(R.drawable.showcase5);
			else if ((pos - 5) % 6 == 0)
				img.setImageResource(R.drawable.showcase6);

			return v;
		}

	}
}
