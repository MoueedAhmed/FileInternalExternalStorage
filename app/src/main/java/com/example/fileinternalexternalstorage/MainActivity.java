package com.example.fileinternalexternalstorage;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText readView;
    EditText writeView;

    FileOutputStream outputStream;
    FileInputStream inputStream;
    String filename = "temp.txt";

    File myExternalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readView = (EditText) findViewById(R.id.outputEditTextMain);
        writeView = (EditText) findViewById(R.id.inputEditTextMain);

        Button externalWrite = (Button) findViewById(R.id.writeExternalButtonMain);
        Button externalRead = (Button) findViewById(R.id.readExternalButtonMain);
        Button externalDelete = (Button) findViewById(R.id.deleteExternalButtonMain);

        // if not able to write to external storage, disable buttons
        if (!isExternalStorageWritable() && isExternalStorageReadable()) {
            externalWrite.setEnabled(false);
            externalRead.setEnabled(false);
            externalDelete.setEnabled(false);
        }

        Button writeInternalButton = findViewById(R.id.writeInternalButtonMain);
        Button readInternalButton = findViewById(R.id.readInternalButtonMain);
        Button deleteInternalButton = findViewById(R.id.deleteInternalButtonMain);

        writeInternalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = writeView.getText().toString();

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(data.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        readInternalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream instream = openFileInput(filename);
                    ReadData(instream);

                } catch (java.io.FileNotFoundException e) {
                    // do something if the filename does not exits
                }
            }
        });

        deleteInternalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile("temp.txt");
            }
        });
    }


    /* Helper Methods */

    public File getDocumentDir(String name) {
        // Get the directory for the user's public directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), name);

        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void ReadData(InputStream instream)
    {
        try {
            InputStreamReader inputreader = new InputStreamReader(instream);

            BufferedReader buffreader = new BufferedReader(inputreader);

            String line = new String();
            String allLines = new String();

            // read every line of the file into the line-variable, on line at the time

            while ((line = buffreader.readLine()) != null) {
                allLines += line;
            }

            readView.setText(allLines);

            // close the file again
            instream.close();

        } catch (Exception e) {

            // do something if the filename does not exits
        }
    }

}
