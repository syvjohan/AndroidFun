package johan.laboration2;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by johan on 2014-12-09.
 */
public class AdapterList extends ArrayAdapter<Group> {

    private LayoutInflater mLayoutInflater;

    public StudentAdapter(Context context, ArrayList<Group> objects) {
        super(context, R.layout.fragment_item_list, objects);

        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_item_list, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.group_name);
            holder.username = (TextView) convertView.findViewById(R.id.student_username);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).name);
        holder.username.setText(getItem(position).username);

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView username;
    }



}
