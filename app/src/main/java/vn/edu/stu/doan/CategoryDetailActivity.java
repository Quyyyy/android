package vn.edu.stu.doan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import vn.edu.stu.doan.adapter.CategoryAdapter;
import vn.edu.stu.doan.model.Category;
import vn.edu.stu.doan.util.DBUtil;

public class CategoryDetailActivity extends AppCompatActivity {

    final String DB_NAME = "dbBook.db";
    final String DB_PATH_SUFIX = "/databases/";

    EditText etCid,etCname;
    Button btnEdit;

    CategoryAdapter adapter;
    ArrayList<Category> ds2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        addControls();
        copyDatabase();
        getDataFromDB();
        dataManage();
        addEvents();
    }

    private int dataManage() {
        Intent intent=getIntent();
        int ma=intent.getIntExtra("ma",-1);
        for (Category category:ds2) {
            if(ma==category.getcId()){
                int id=category.getcId();
                String name=category.getcName();
                etCid.setText(id+"");
                etCname.setText(name);
                btnEdit.setText(R.string.edit);
            }
        }
        return ma;
    }

    private void addEvents() {
        String data1 = etCid.getText().toString();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!data1.isEmpty()){
                    if(checkFields()){
                        xuLySua();
                        finish();
                    }
                }else{
                    if(checkFields()){
                        xuLyThem();
                        finish();
                    }
                }
            }
        });
    }

    private void addControls() {
        etCid=findViewById(R.id.etCid);
        etCname=findViewById(R.id.etCname);
        btnEdit= findViewById(R.id.btnEdit);
        ds2=new ArrayList<>();
        adapter=new CategoryAdapter(CategoryDetailActivity.this, R.layout.category_item,ds2);
    }

    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(
                CategoryDetailActivity.this,
                DB_NAME,
                DB_PATH_SUFIX
        );
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
    private void getDataFromDB() {
        ds2.clear();
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        Cursor c2 =database.query("Category",null,null,null,null,null,null);
        while (c2.moveToNext()){
            int ma = c2.getInt(0);
            String ten = c2.getString(1);
            ds2.add(new Category(ma,ten));
        }
        c2.close();
        database.close();
    }

    private void xuLyThem() {
        Category c=new Category();
        String ten = etCname.getText().toString();
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues row = new ContentValues();
        row.put("cName", ten);
        database.insert("Category",null,row);
        adapter.notifyDataSetChanged();
        database.close();
        Toast.makeText(this,R.string.n_add,Toast.LENGTH_SHORT).show();
    }

    private void xuLySua() {
        String ten = etCname.getText().toString();
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues row = new ContentValues();
        row.put("cName", ten);
        database.update("Category",row,"cId = ?",new String[]{String.valueOf(dataManage())});
        adapter.notifyDataSetChanged();
        database.close();
        Toast.makeText(this,R.string.n_edit,Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields(){
        if(etCname.getText().toString().trim().length()<=0){
            etCname.setError(getText(R.string.check_cname));
            return false;
        }
        return true;
    }
}