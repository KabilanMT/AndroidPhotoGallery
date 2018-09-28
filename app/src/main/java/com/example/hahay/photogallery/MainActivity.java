package com.example.hahay.photogallery;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView cameraView;
    File imgFile;
    File[] files;
    public static int currentpic = 0;
    Button nextButt;
    Button backButt;
    public static String filter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Filter is: " + filter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button PicButton = (Button) findViewById(R.id.picButton);
        cameraView = (ImageView) findViewById(R.id.cameraView);

        File file = new File("/storage/emulated/0/Pictures/PhotoGallery/");
        if(filter == null || filter == ""){
            files = file.listFiles();
        }
        else{
            files = file.listFiles();
            if(files.length > 0){
                System.out.println(files.length);
                ArrayList<File> arrayList = new ArrayList<File>();
                for (int i = 0; i < files.length; ++i)
                {
                    System.out.println("Testing1");
                    if(files[i].getName().toString().contains(filter)){
                        arrayList.add(files[i]);
                        System.out.println("Testing2");
                    }

                    System.out.println("Testing3");

                }
                System.out.println("Testing4");
                if(arrayList.isEmpty()){
                    files = file.listFiles();
                }
                else{
                    files = arrayList.toArray(files);
                }
                System.out.println(arrayList);
            }

        }
        backButt = (Button) findViewById(R.id.backButton);
        backButt.setEnabled(false);
        nextButt = (Button) findViewById(R.id.nextButton);
        if(files.length > 0){
            currentpic = 0;
            Bitmap defaultBitmap = BitmapFactory.decodeFile(String.valueOf(files[currentpic]));
            defaultBitmap = crupAndScale(defaultBitmap, 400); // if you mind scaling
            cameraView.setImageBitmap(defaultBitmap);
            String newtimestamp = String.valueOf(files[currentpic]).replace("/storage/emulated/0/Pictures/PhotoGallery/", "");
            newtimestamp = String.valueOf(newtimestamp.replace(".jpg", ""));
            int index=newtimestamp.lastIndexOf(':');
            String caption = newtimestamp.substring(0,index);
            newtimestamp = newtimestamp.replace(caption + ":", "");
            DateFormat df=new SimpleDateFormat("yyMMdd_HHmmss");
            Date d= null;
            try {
                d = df.parse(newtimestamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            df=new SimpleDateFormat("yyyy-MMM-dd hh:mm");
            ((TextView)findViewById(R.id.timeStamp)).setText(df.format(d));
            TextInputEditText userInp = findViewById(R.id.captionInput);
            userInp.setText(caption);
        }



        //Disable button if user has no camera
        if(!hasCamera())
            PicButton.setEnabled(false);
    }

    //Check if the user has a camera
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/PhotoGallery");
        String picName = getPictureName();
        File imagefile = new File(picDir, picName);
        Uri pictureUri = Uri.fromFile(imagefile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        //Take a picture and pass results along to onActivityResult
        imgFile = new File(picDir, picName);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(imgFile.getAbsolutePath()));
        myBitmap = crupAndScale(myBitmap, 400); // if you mind scaling
        cameraView.setImageBitmap(myBitmap);
        File file = new File("/storage/emulated/0/Pictures/PhotoGallery/");
        files = file.listFiles();
        String newtimestamp = String.valueOf(files[currentpic]).replace("/storage/emulated/0/Pictures/PhotoGallery/", "");
        newtimestamp = String.valueOf(newtimestamp.replace(".jpg", ""));
        int index=newtimestamp.lastIndexOf(':');
        newtimestamp = newtimestamp.replace(newtimestamp.substring(0,index) + ":", "");
        DateFormat df=new SimpleDateFormat("yyMMdd_HHmmss");
        try {
            Date d=df.parse(newtimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df=new SimpleDateFormat("yyyy-MMM-dd hh:mm");
    }

    //resize image and rotate by 90 degrees
    public static  Bitmap crupAndScale (Bitmap source,int scale){
        Matrix matrix = new Matrix ();
        matrix.postRotate(90);
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createBitmap(source, 0 ,0, source.getWidth(), source.getHeight(), matrix, true);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    //set images name
    public String getPictureName() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "nocaption:" + timestamp+".jpg";
    }

    //Go to next image
    public void nextImage(View view) throws ParseException {
        File file = new File("/storage/emulated/0/Pictures/PhotoGallery/");
        files = file.listFiles();
        nextButt = (Button) findViewById(R.id.nextButton);
        if(currentpic+1 < files.length){
            currentpic += 1;
            Bitmap defaultBitmap = BitmapFactory.decodeFile(String.valueOf(files[currentpic]));
            defaultBitmap = crupAndScale(defaultBitmap, 400); // if you mind scaling
            cameraView.setImageBitmap(defaultBitmap);
            backButt.setEnabled(true);
            String newtimestamp = String.valueOf(files[currentpic]).replace("/storage/emulated/0/Pictures/PhotoGallery/", "");
            newtimestamp = String.valueOf(newtimestamp.replace(".jpg", ""));
            int index=newtimestamp.lastIndexOf(':');
            String caption = newtimestamp.substring(0,index);
            newtimestamp = newtimestamp.replace(caption + ":", "");
            DateFormat df=new SimpleDateFormat("yyMMdd_HHmmss");
            Date d=df.parse(newtimestamp);
            df=new SimpleDateFormat("yyyy-MMM-dd hh:mm");
            ((TextView)findViewById(R.id.timeStamp)).setText(df.format(d));
            TextInputEditText userInp = findViewById(R.id.captionInput);
            userInp.setText(caption);
            if(currentpic == files.length-1){
                nextButt.setEnabled(false);
            }
        }
    }

    //Go back an image
    public void backImage(View view) throws ParseException {
        File file = new File("/storage/emulated/0/Pictures/PhotoGallery/");
        files = file.listFiles();
        if(currentpic > 0){
            currentpic -= 1;
            Bitmap defaultBitmap = BitmapFactory.decodeFile(String.valueOf(files[currentpic]));
            defaultBitmap = crupAndScale(defaultBitmap, 400); // if you mind scaling
            cameraView.setImageBitmap(defaultBitmap);
            nextButt.setEnabled(true);
            String newtimestamp = String.valueOf(files[currentpic]).replace("/storage/emulated/0/Pictures/PhotoGallery/", "");
            newtimestamp = String.valueOf(newtimestamp.replace(".jpg", ""));
            int index=newtimestamp.lastIndexOf(':');
            String caption = newtimestamp.substring(0,index);
            newtimestamp = newtimestamp.replace(caption + ":", "");
            DateFormat df=new SimpleDateFormat("yyMMdd_HHmmss");
            Date d=df.parse(newtimestamp);
            df=new SimpleDateFormat("yyyy-MMM-dd hh:mm");
            ((TextView)findViewById(R.id.timeStamp)).setText(df.format(d));
            TextInputEditText userInp = findViewById(R.id.captionInput);
            userInp.setText(caption);
            if(currentpic == 0){
                backButt.setEnabled(false);
            }
        }
    }

    //Redirect to search view
    public void searchView(View view) {
        Intent searchIntent = new Intent(this, Search.class);
        startActivity(searchIntent);
    }


    //Save caption
    public void saveCaption(View view) {
        if (files.length > 0) {
            TextInputEditText userInp = findViewById(R.id.captionInput);
            String newtimestamp = String.valueOf(files[currentpic]).replace("/storage/emulated/0/Pictures/PhotoGallery/", "");
            String ogpicname = newtimestamp;
            newtimestamp = String.valueOf(newtimestamp.replace(".jpg", ""));
            int index=newtimestamp.lastIndexOf(':');
            newtimestamp = newtimestamp.replace(newtimestamp.substring(0,index), "");
            String caption = userInp.getText().toString();
            File tempFile = new File(caption+newtimestamp);
            files[currentpic].renameTo(tempFile);
            File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/PhotoGallery");
            String picName = "/"+caption+newtimestamp+".jpg";
            File from = new File(picDir,ogpicname);
            File to = new File(picDir, picName);
            from.renameTo(to);
            System.out.println("Worked");

        }
    }
}
