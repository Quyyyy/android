package vn.edu.stu.doan.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.stu.doan.R;
import vn.edu.stu.doan.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    Activity context;
    int resource;
    List<Category> object;

    public CategoryAdapter(@NonNull Activity context, int resource,@NonNull List<Category> object){
        super(context,resource,object);
        this.context = context;
        this.resource = resource;
        this.object = object;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convert, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater=this.context.getLayoutInflater();
        view=inflater.inflate(R.layout.category_item,null);
        TextView tvMaC=view.findViewById(R.id.tvMaC);
        TextView tvTenC=view.findViewById(R.id.tvTenC);
        Category c=this.object.get(pos);

        tvMaC.setText(Integer.toString(c.getcId()));
        tvTenC.setText(c.getcName());
        return  view;
    }
}
