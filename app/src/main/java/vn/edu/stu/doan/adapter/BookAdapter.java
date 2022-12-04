package vn.edu.stu.doan.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import vn.edu.stu.doan.R;
import vn.edu.stu.doan.model.Book;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Activity context, int resource,@NonNull List<Book> object) {
        super(context, resource,object);
    }
    @NonNull
    @Override
    public  View getView(int pos, @Nullable View convert, @NonNull ViewGroup parent){
        Book b = getItem(pos);
        if(convert==null){
            convert = LayoutInflater.from(getContext()).inflate(R.layout.book_item,null);
        }
        ImageView ivHinh=(ImageView) convert.findViewById(R.id.ivHinh);
        TextView tvMa=(TextView) convert.findViewById(R.id.tvMa);
        TextView tvTen=(TextView)convert.findViewById(R.id.tvTen);
        TextView tvLoai=(TextView)convert.findViewById(R.id.tvLoai);
        //TextView tvTacGia=(TextView)convert.findViewById(R.id.tvTacGia);
        //TextView tvGia=(TextView)convert.findViewById(R.id.tvGia);

        tvMa.setText(Integer.toString(b.getbID()));
        tvTen.setText(b.getbName());
        tvLoai.setText(b.getbCategory().getcName());
        //tvTacGia.setText(b.getbAuthor());
        //tvGia.setText(Integer.toString(b.getbPrice()));

        File sdCard = Environment.getExternalStorageDirectory();

        File directory = new File (sdCard.getAbsolutePath() + "/Demo");

        File file = new File(directory, b.getbImage()+""); //or any other format supported

        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image

        /*try {
            streamIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ivHinh.setImageBitmap(bitmap);

        return convert;
    }
}
