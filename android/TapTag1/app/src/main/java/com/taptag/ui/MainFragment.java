package com.taptag.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taptag.R;
import com.taptag.custom.CustomFragment;

// TODO: Auto-generated Javadoc
/**
 * The current implementation of MainFragment class holds the various customized
 * components like custom Switch etc
 */
public class MainFragment extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main_container, null);

		setTouchNClick(v.findViewById(R.id.p1));
		setTouchNClick(v.findViewById(R.id.p2));
		setTouchNClick(v.findViewById(R.id.p3));

		return v;
	}

}
