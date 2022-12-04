package vn.edu.stu.doan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import vn.edu.stu.doan.adapter.BookAdapter;
import vn.edu.stu.doan.model.Book;
import vn.edu.stu.doan.model.Category;
import vn.edu.stu.doan.util.DBUtil;

public class BookDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final String DB_NAME = "dbBook.db";
    final String DB_PATH_SUFIX = "/databases/";

    Button btnEdit,btnImage;
    EditText etBid,etBname,etBauthor,etBprice;
    ImageView ivBimage;
    Spinner spinBcategory;

    BookAdapter adapter;
    ArrayList<Book> ds;
    ArrayList<Category> ds2;
    ArrayList<String> dsloai;
    String imgSelected="";
    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        addControls();
        copyDatabase();
        getDataFromDB();
        spinnerManage();
        dataManage();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long l){}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void spinnerManage(){
        dsloai = new ArrayList<>();
        for (Category c:ds2) {
            dsloai.add(c.getcName());
        }
        spinBcategory.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ds2);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBcategory.setAdapter(aa);
    }

    private int dataManage() {
        Intent intent=getIntent();
        int id=intent.getIntExtra("ma",-1);
        for (Book book:ds) {
            if(id==book.getbID()){
                int ma=book.getbID();
                String ten=book.getbName();
                String tacgia= book.getbAuthor();
                int gia= book.getbPrice();
                String hinh=book.getbImage();
                imgSelected=hinh;
                Category c= (Category) spinBcategory.getSelectedItem();
                //Toast.makeText(this, hinh, Toast.LENGTH_SHORT).show();
                etBid.setText(ma+"");
                etBname.setText(ten);
                etBauthor.setText(tacgia);
                etBprice.setText(gia+"");
                getImage(hinh);
                int index =0;
                for (int i =0 ;i < ds2.size();i++){
                    if(book.getbCategory().getcId() == ds2.get(i).getcId())
                        index =i;
                }
                spinBcategory.setSelection(index);
                btnEdit.setText(R.string.edit);
            }
        }
        return id;
    }


    private void addEvents() {
        String data1 = etBid.getText().toString();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!data1.isEmpty()){
                    if(checkFields()){
                        xuLySua();
                        finish();
                    }
                }else {
                    if (checkFields()){
                        xuLyThem();
                        finish();
                    }
                }
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    private void addControls() {
        etBid=findViewById(R.id.etBid);
        etBname=findViewById(R.id.etBname);
        etBauthor=findViewById(R.id.etBauthor);
        etBprice=findViewById(R.id.etBprice);
        ivBimage=findViewById(R.id.ivBimage);
        spinBcategory= findViewById(R.id.spinBcategory);
        btnEdit=findViewById(R.id.btnEdit);
        btnImage=findViewById(R.id.btnImage);

        ds2=new ArrayList<>();
        ds=new ArrayList<>();
        adapter=new BookAdapter(BookDetailActivity.this, R.layout.book_item,ds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_mini_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miAbout:
                Intent intent1=new Intent(this,AboutActivity.class);
                intent1.putExtra("isLogin",1);
                startActivity(intent1);
                return true;
            case R.id.miQuit:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(
                BookDetailActivity.this,
                DB_NAME,
                DB_PATH_SUFIX
        );
    }
    private void getDataFromDB() {
        ds.clear();
        ds2.clear();
        SQLiteDatabase database=openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        Cursor c2 =database.query("Category",null,null,null,null,null,null);
        while (c2.moveToNext()){
            int ma = c2.getInt(0);
            String ten = c2.getString(1);
            ds2.add(new Category(ma,ten));
        }
        c2.close();
        Cursor c1 =database.query("Book",null,null,null,null,null,null);
        while (c1.moveToNext()){
            int ma = c1.getInt(0);
            String ten = c1.getString(1);
            Category loai=new Category();
            int id = c1.getInt(2);
            String hinh=c1.getString(3);
            String tacGia=c1.getString(4);
            int gia=c1.getInt(5);
            for (Category l:ds2) {
                if (l.getcId()==id)
                    loai=l;
            }
            ds.add(new Book(ma,ten,loai,hinh,tacGia,gia));
        }
        c1.close();
        database.close();
    }
    private void xuLyThem() {
        String ten = etBname.getText().toString();
        int loai=ds2.get(spinBcategory.getSelectedItemPosition()).getcId();
        String hinh=getImage(imgSelected);
        String tacGia=etBauthor.getText().toString();
        int gia= Integer.parseInt(etBprice.getText().toString());

        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues row = new ContentValues();
        row.put("bName", ten);
        row.put("bCategory", loai);
        row.put("bImage", hinh);
        row.put("bAuthor", tacGia);
        row.put("bPrice", gia);
        database.insert("Book",null,row);
        adapter.notifyDataSetChanged();
        database.close();
        Toast.makeText(BookDetailActivity.this,R.string.n_add,Toast.LENGTH_SHORT).show();
    }

    private void xuLySua() {
        String ten = etBname.getText().toString();

        int loai=ds2.get(spinBcategory.getSelectedItemPosition()).getcId();
        String hinh = imgSelected;
        String tacGia=etBauthor.getText().toString();
        int gia= Integer.parseInt(etBprice.getText().toString());
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues row = new ContentValues();
        row.put("bName", ten);
        row.put("bCategory", loai);
        row.put("bImage", hinh);
        row.put("bAuthor", tacGia);
        row.put("bPrice", gia);
        database.update("Book",row,"bId = ?",new String[]{String.valueOf(dataManage())});
        adapter.notifyDataSetChanged();
        database.close();
        Toast.makeText(BookDetailActivity.this,R.string.n_edit,Toast.LENGTH_SHORT).show();
    }

    private String getImage(String img){
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/Demo");
        File file = new File(directory, ""+img); //or any other format supported
        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
        ivBimage.setImageBitmap(bitmap);
        return img;
    }
    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivBimage.setImageURI(selectedImageUri);
                    File dir = new File(Environment.getExternalStorageDirectory(),"Demo");
                    if (!dir.exists()){
                        dir.mkdir();
                    }
                    BitmapDrawable drawable = (BitmapDrawable) ivBimage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    File file = new File(dir,System.currentTimeMillis()+".jpg");
                    imgSelected=System.currentTimeMillis()+".jpg";
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getImage(imgSelected);
                    //Toast.makeText(this, getImage(imgSelected), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private boolean checkFields(){
        if(etBname.getText().toString().trim().length()==0){
            etBname.setError(getText(R.string.check_bname));
            return false;
        }
        if(etBauthor.getText().toString().trim().length()==0){
            etBauthor.setError(getText(R.string.check_bauthor));
            return false;
        }
        if (etBprice.getText().toString().trim().length()==0){
            etBprice.setError(getText(R.string.check_bprice));
            return false;
        }
        if(imgSelected.trim().length()==0){
            Toast.makeText(this, R.string.check_bimg, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}