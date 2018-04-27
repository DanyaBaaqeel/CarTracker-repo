package nyx.com.cartracker.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nyx.com.cartracker.R;
import nyx.com.cartracker.models.Car;
import nyx.com.cartracker.models.Driver;


/**
 * Created by Luminance on 2/17/2018.
 */

public
class CarsSpinnerAdAPTER extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<Car> cities;
    public CarsSpinnerAdAPTER(Activity con , ArrayList<Car> cities) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(con);
        this.cities = cities;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cities==null?0:cities.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.my_spinner_style, null);
            holder = new ListContent();

            holder.name = (TextView) v.findViewById(R.id.textView1);

            v.setTag(holder);
        } else {

            holder = (ListContent) v.getTag();
        }


        holder.name.setText("" + cities.get(position).getName() + "  , # : ("+ cities.get(position).getNumber()+")");
        return v;
    }
    static class ListContent {
        TextView name;
    }
}
