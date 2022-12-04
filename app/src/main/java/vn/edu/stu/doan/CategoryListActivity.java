package vn.edu.stu.doan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.stu.doan.adapter.CategoryAdapter;
import vn.edu.stu.doan.model.Book;
import vn.edu.stu.doan.model.Category;
import vn.edu.stu.doan.util.DBUtil;

public class CategoryListActivity extends AppCompatActivity {

    final String DB_NAME = "dbBook.db";
    final String DB_PATH_SUFIX = "/databases/";

    Button btnAdd;
    ListView lvCategoryList;
    CategoryAdapter adapter;
    ArrayList<Book> ds;
    ArrayList<Category> ds2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        addControls();
        addEvents();
        copyDatabase();
        getDataFromDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miBookList:
                Intent intent=new Intent(this,BookListActivity.class);
                startActivity(intent);
                return true;
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
    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(CategoryListActivity.this,CategoryDetailActivity.class);
                startActivity(intent1);
            }
        });

        lvCategoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
                builder.setMessage("Do you want to delete?");
                builder.setTitle("Delete Confirm!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    xuLyXoa(index);
                    getDataFromDB();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        lvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long l) {
                xuLyChon(index);
            }
        });
    }

    private void addControls() {
        btnAdd=findViewById(R.id.btnAdd);
        lvCategoryList = findViewById(R.id.lvCategoryList);
        ds = new ArrayList<>();
        ds2=new ArrayList<>();
        adapter=new CategoryAdapter(CategoryListActivity.this, R.layout.category_item,ds2);
        lvCategoryList.setAdapter(adapter);
    }

    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(
                CategoryListActivity.this,
                DB_NAME,
                DB_PATH_SUFIX
        );
    }

    private void getDataFromDB() {
        ds.clear();
        ds2.clear();
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
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
            for (Category l:ds2) {
                if (l.getcId()==id)
                    loai=l;
            }
            ds.add(new Book(ma,ten,loai,hinh));
        }
        c1.close();
        database.close();
    }

    private void xuLyXoa(int index) {
        if (index >= 0 && index < adapter.getCount()) {
            Category c = adapter.getItem(index);
            SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            Cursor c2 = database.rawQuery("SELECT * FROM Book where bCategory=?", new String[]{String.valueOf(c.getcId())});
            if(c2.getCount()==0){
                database.delete("Category", "cId = ?", new String[]{c.getcId() + ""});
                Toast.makeText(CategoryListActivity.this, R.string.n_del_s, Toast.LENGTH_SHORT).show();}
            else
                Toast.makeText(CategoryListActivity.this, R.string.n_del_f, Toast.LENGTH_SHORT).show();
            c2.close();
            adapter.notifyDataSetChanged();
            database.close();
        }
    }

    private void xuLyChon(int index){
        if (index >= 0 && index < adapter.getCount()) {
            Category c = adapter.getItem(index);
            Intent intent2=new Intent(CategoryListActivity.this,CategoryDetailActivity.class);
            intent2.putExtra("ma",c.getcId());
            startActivity(intent2);
        }
    }
}