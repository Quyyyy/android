package vn.edu.stu.doan;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

public class AboutActivity extends AppCompatActivity {

    TextView tvSDT;
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addControls();
        addEvents();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        MenuInflater inflater = getMenuInflater();
        if(intent.hasExtra("isLogin")){
            inflater.inflate(R.menu.activity_extra_menu, menu);
        }else{
            inflater.inflate(R.menu.activity_mini_menu, menu);
        }
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miBookList:
                Intent intent=new Intent(this,BookListActivity.class);
                startActivity(intent);
                return true;
            case R.id.miCategoryList:
                Intent intent1=new Intent(this,CategoryListActivity.class);
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
        String phoneNumb = tvSDT.getText().toString();

        tvSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + phoneNumb));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        tvSDT=findViewById(R.id.tvSDT);
        mapView=findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(
                Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        xuLyNapBanDo();
                    }
                }
        );
    }

    private void xuLyNapBanDo() {
        // STU: 10.738242625107251, 106.67794694095484
        Point pointSTU = Point.fromLngLat(106.67794694095484, 10.738242625107251);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager manager = PointAnnotationManagerKt.createPointAnnotationManager(
                annotationPlugin,
                new AnnotationConfig()
        );
        PointAnnotationOptions optionsSTU = new PointAnnotationOptions()
                .withPoint(pointSTU)
                .withIconImage(BitmapFactory.decodeResource(this.getResources(), R.drawable.red_marker));
        manager.create(optionsSTU);

        CameraOptions cameraOptions = new CameraOptions.Builder()
                .center(pointSTU)
                .zoom(16.0)
                .pitch(0.0)
                .bearing(0.0)
                .build();
        mapView.getMapboxMap().setCamera(cameraOptions);
    }
}