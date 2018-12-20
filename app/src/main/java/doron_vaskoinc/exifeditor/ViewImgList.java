package doron_vaskoinc.exifeditor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class ViewImgList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private static final int TAKE_PHOTO_CODE = 1;


    private ImageView imageView2;

    private static final int PICK_IMAGE = 100;

    ImageDatabase imageDB;

    Uri imageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img_list);
        imageDB = new ImageDatabase(this);
        //////////////////////////////////////////////////////

        imageView2 = findViewById(R.id.standard_list_imageview2);
        requestPermission1();
        requestPermission2();


        //////////////////////////////////////////////////////

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton cam_fab = findViewById(R.id.cam_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Opening local folders...", Snackbar.LENGTH_LONG)
                        .setAction("none", null).show();
                openGallery();
                configureSelectedImg();
            }
        });
        cam_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Opening camera...", Snackbar.LENGTH_LONG)
                        .setAction("none", null).show();
                openCamera();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /////////////////////////////////////////////////////////////////////

    private void requestPermission1() {
        if (ContextCompat.checkSelfPermission(ViewImgList.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewImgList.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void requestPermission2() {
        if (ContextCompat.checkSelfPermission(ViewImgList.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewImgList.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void requestPermission3() {
        if (ContextCompat.checkSelfPermission(ViewImgList.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewImgList.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void requestPermission4() {
        if (ContextCompat.checkSelfPermission(ViewImgList.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewImgList.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void requestPermission5() {
        if (ContextCompat.checkSelfPermission(ViewImgList.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewImgList.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                }
            }
        }
    }


    /////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_img_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            openCamera();

        } else if (id == R.id.nav_map) {
            openMap();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configureSelectedImg(){
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewImgList.this, ViewImg.class);
                if (imageUri != null) {
                    i.putExtra("imagePath", imageUri.toString());
                    startActivity(i);
                }
            }
        });

    }

    private void openMap(){
        requestPermission4();
        requestPermission5();
        Intent m = new Intent(ViewImgList.this, MapsActivity.class);
        startActivity(m);
    }

    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    public void openCamera() {
        requestPermission3();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String n1 = Integer.toString(new Random().nextInt(1000));
        String n2 = Integer.toString(new Random().nextInt(1000));
        String picname = "ExifTool_"+n1+"_"+n2+".jpg";
        File photo = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/",  picname);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        Log.v("URI", imageUri.toString());
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView2.setImageURI(imageUri);
            String uri_string = imageUri.toString();
            addImageToDB(imageUri, uri_string);
        }

        Log.v("resultCode", Integer.toString(resultCode));

        if(resultCode == RESULT_OK && requestCode == TAKE_PHOTO_CODE){
            Uri selectedImage = imageUri;
            Log.v("selectedImage", selectedImage.toString());
            Log.v("getRealPathFromURI", getRealPathFromURI(selectedImage));
        }
    }

    public void addImageToDB(Uri U, String uri_string) {
        try {

            String filepath = getRealPathFromURI(U);

            File file = new File(filepath);
            ExifInterface exifInterface = new ExifInterface(filepath);
            String strFileName = file.getName();
            String date_time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

            Double curLat = null;
            Double curLon = null;
            if ((exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null) && (exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null)) {
                curLat = convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
                curLon = convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            }

            String lat_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String long_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);

            boolean isInserted = imageDB.insertData( uri_string, filepath, strFileName, curLat, lat_ref, curLon, long_ref, date_time, make, model);
            if (isInserted) {
                Log.v("SQLite", "Inserted data.");
            } else {
                Log.v("SQLite", "Error inserting data.");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private String getRealPathFromURI(Uri contentURI) {
        String filePath;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

    private static Double convertToDegree(String stringDMS){
        Double result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        Double FloatS = S0/S1;

        result = FloatD + (FloatM / 60) + (FloatS / 3600);


        return result;

    }


}
