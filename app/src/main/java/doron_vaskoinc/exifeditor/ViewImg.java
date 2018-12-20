package doron_vaskoinc.exifeditor;

import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

public class ViewImg extends AppCompatActivity {

    ImageView i1;

    String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img);
        i1 = findViewById(R.id.imageView1);


        String imgPath = Objects.requireNonNull( getIntent().getExtras() ).getString("imagePath");

        try {
            final Uri imageUri1 = Uri.parse(imgPath);
            if (imageUri1 != null) {
                i1.setImageURI(imageUri1);
            }
            filePath = getRealPathFromURI(imageUri1);


            initExif(filePath);


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.v("Debug", e.getMessage());
            }
        configureDoneButton();
    }


    private void initExif(String filepath) throws IOException{
        DecimalFormat df = new DecimalFormat("###.###");
        File file = new File(filepath);
        String strFileName = file.getName();

        TextView current_filename_text = findViewById(R.id.current_filename_text);
        TextView current_date_time_text = findViewById(R.id.current_date_time_text);
        TextView current_latitude_text = findViewById(R.id.current_latitude_text);
        TextView current_latitude_ref_text = findViewById(R.id.current_latitude_ref_text);
        TextView current_longitude_text = findViewById(R.id.current_longitude_text);
        TextView current_longitude_ref_text = findViewById(R.id.current_longitude_ref_text);
        TextView current_make_text = findViewById(R.id.current_make_text);
        TextView current_model_text = findViewById(R.id.current_model_text);

        ExifInterface exifInterface = new ExifInterface(filepath);

        if (strFileName.length() > 40) {
            strFileName = "..." + strFileName.substring(strFileName.length() - 37);

            Log.v("Debug", ""+Integer.toString(strFileName.length()));
        }
        current_filename_text.setText(strFileName);


        current_date_time_text.setText(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
        Double curLat;
        Double curLon;
        String strLat = "null";
        String strLon = "null";
        if ((exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null) && (exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null)) {
            curLat = convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            curLon = convertToDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            strLat =df.format(curLat);
            strLon =df.format(curLon);
        }

        current_latitude_text.setText(strLat);

        current_latitude_ref_text.setText(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));

        current_longitude_text.setText(strLon);

        current_longitude_ref_text.setText(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));

        current_make_text.setText(exifInterface.getAttribute(ExifInterface.TAG_MAKE));

        current_model_text.setText(exifInterface.getAttribute(ExifInterface.TAG_MODEL));
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

    private static String converttoDMS(double latitude) {
        latitude=Math.abs(latitude);
        int degree = (int) latitude;
        latitude *= 60;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= 60;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude*1000.0d);

        return String.valueOf( degree ) +
                "/1," +
                minute +
                "/1," +
                second +
                "/1000";
    }


    public void setDateTime(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.current_date_time_input);
        TextView current_make_text = findViewById(R.id.current_date_time_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                String dt = input_field.getText().toString();
                exif.setAttribute(ExifInterface.TAG_DATETIME, dt);
                exif.saveAttributes();
                current_make_text.setText(dt);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setLat(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.latitude_input);
        TextView current_make_text = findViewById(R.id.current_latitude_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                Double l = Math.abs(Double.parseDouble(input_field.getText().toString()));
                if (l <= 180.0) {
                    String ref = input_field.getText().toString();
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, converttoDMS(l));
                    exif.saveAttributes();
                    current_make_text.setText(ref);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setLon(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.longitude_input);
        TextView current_make_text = findViewById(R.id.current_longitude_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                Double l = Math.abs(Double.parseDouble(input_field.getText().toString()));
                if (l <= 180.0) {
                    String ref = input_field.getText().toString();
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, converttoDMS(l));
                    exif.saveAttributes();
                    current_make_text.setText(ref);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void setLatRef(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.latitude_ref_input);
        TextView current_make_text = findViewById(R.id.current_latitude_ref_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                if (input_field.getText().toString().toUpperCase().equals("N") || input_field.getText().toString().toUpperCase().equals("S")) {
                    String ref = input_field.getText().toString().toUpperCase();
                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, ref);
                    exif.saveAttributes();
                    current_make_text.setText(ref);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setLonRef(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.longitude_ref_input);
        TextView current_make_text = findViewById(R.id.current_longitude_ref_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                if (input_field.getText().toString().toUpperCase().equals("W") || input_field.getText().toString().toUpperCase().equals("E")) {
                    String ref = input_field.getText().toString().toUpperCase();
                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, ref);
                    exif.saveAttributes();
                    current_make_text.setText(ref);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setMake(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.make_input);
        TextView current_make_text = findViewById(R.id.current_make_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                String make = input_field.getText().toString();
                exif.setAttribute(ExifInterface.TAG_MAKE, make);
                exif.saveAttributes();
                current_make_text.setText(make);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setModel(View view) {
        ExifInterface exif;
        EditText input_field = findViewById(R.id.model_input);
        TextView current_make_text = findViewById(R.id.current_model_text);
        // Checks that a user input something into field, ie input != empty
        if (input_field.getText().length() < 1) {
        }
        // If input is not empty, convert.
        else {
            try {
                exif = new ExifInterface(filePath);
                String model = input_field.getText().toString();
                exif.setAttribute(ExifInterface.TAG_MODEL, model);
                exif.saveAttributes();
                current_make_text.setText(model);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    private void configureDoneButton() {
        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
