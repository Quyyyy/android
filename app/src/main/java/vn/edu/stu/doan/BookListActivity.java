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

import vn.edu.stu.doan.adapter.BookAdapter;
import vn.edu.stu.doan.model.Book;
import vn.edu.stu.doan.model.Category;
import vn.edu.stu.doan.util.DBUtil;

public class BookListActivity extends AppCompatActivity {
    final String DB_NAME = "dbBook.db";
    final String DB_PATH_SUFIX = "/databases/";

    ListView lvBookList;
    Button btnAdd;
    ArrayList<Book> ds;
    ArrayList<Category> ds2;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        addControls();
        copyDatabase();
        getDataFromDB();
        addEvents();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(BookListActivity.this,BookDetailActivity.class);
                startActivity(intent1);
            }
        });

        lvBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xuLyChon(position);
            }
        });

        lvBookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);
                builder.setMessage("Do you want to delete?");
                builder.setTitle("Delete Confirm!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    xuLyXoa(position);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_side_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miCategoryList:
                Intent intent=new Intent(this,CategoryListActivity.class);
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
            for (Category l:ds2) {
                if (l.getcId()==id)
                    loai=l;
            }
            ds.add(new Book(ma,ten,loai,hinh));
        }
        c1.close();
        database.close();
    }

    private void copyDatabase() {
        DBUtil.copyDBFileFromAssets(
                BookListActivity.this,
                DB_NAME,
                DB_PATH_SUFIX
        );
    }

    private void addControls() {
        btnAdd=findViewById(R.id.btnAdd);
        lvBookList = findViewById(R.id.lvBookList);
        ds = new ArrayList<>();
        ds2=new ArrayList<>();
        adapter=new BookAdapter(BookListActivity.this,R.layout.book_item,ds);
        lvBookList.setAdapter(adapter);
    }

    private void xuLyXoa(int index) {
        if (index >= 0 && index < adapter.getCount()) {
            Book b = adapter.getItem(index);
            SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
            database.delete("Book","bId = ?",new String[]{b.getbID() + ""});
            adapter.notifyDataSetChanged();
            database.close();
            Toast.makeText(this,R.string.n_del_s,Toast.LENGTH_SHORT).show();}
    }

    private void xuLyChon(int index){
        if (index >= 0 && index < adapter.getCount()) {
            Book b = adapter.getItem(index);
            Intent intent2=new Intent(BookListActivity.this,BookDetailActivity.class);
            intent2.putExtra("ma",b.getbID());
            startActivity(intent2);
        }
    }
}