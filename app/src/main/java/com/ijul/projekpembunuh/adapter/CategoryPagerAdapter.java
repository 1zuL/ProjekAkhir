package com.ijul.projekpembunuh.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.ijul.projekpembunuh.R;
import com.ijul.projekpembunuh.fragment.ItemFragment;
import com.ijul.projekpembunuh.model.Solution;

import java.util.ArrayList;
import java.util.List;


public class CategoryPagerAdapter extends FragmentPagerAdapter
{
    private List<Solution> solutionList = new ArrayList<>();
    private Context context;

    public CategoryPagerAdapter(FragmentManager manager, Context context, List<Solution> solutionList)
    {
        super(manager);
        this.solutionList = solutionList;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return ItemFragment.newInstance(solutionList.get(position));
    }

    @Override
    public int getCount()
    {
        return solutionList.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return solutionList.get(position).category.name;
    }

    public View getTabView(final int position)
    {
        // Given you have a custom layout in `res/layout/tab_item.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.tab_item, null);

        TextView txtCategoryName = (TextView) v.findViewById(R.id.txtCategoryName);
        ImageView imgCategoryIcon = (ImageView) v.findViewById(R.id.imgCategoryIcon);

        txtCategoryName.setText(solutionList.get(position).category.name);
        imgCategoryIcon.setImageResource(solutionList.get(position).category.resourceId);

        return v;
    }
}
