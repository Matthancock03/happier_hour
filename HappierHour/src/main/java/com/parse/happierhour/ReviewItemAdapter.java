package com.parse.happierhour;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


/**
 * Created by jianganson72 on 1/8/16.
 */
public class ReviewItemAdapter extends ParseQueryAdapter<ParseObject> {
    public ReviewItemAdapter(Context context){
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Reviews");
                query.whereEqualTo("Name", DisplayLocationFragment.namefield.getText().toString());
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.reviewitem, null);
        }

        super.getItemView(object, v, parent);

        // Add the title view
        TextView reviewitems = (TextView) v.findViewById(R.id.reviewitem);
        reviewitems.setText(object.getString("title"));

        // Add a reminder of how long this item has been outstanding
        return v;
    }

}
