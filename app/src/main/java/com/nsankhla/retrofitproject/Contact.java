package com.nsankhla.retrofitproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nsankhla.retrofitproject.Contacts.ContactVO;
import com.nsankhla.retrofitproject.Contacts.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Contact extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    Button btn_save;
    CoordinatorLayout coordinatorLayout;
    ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        coordinatorLayout = findViewById(R.id.cordinatorLayout);


        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //checkAndRequestPermissions();

                if (checkAndRequestPermissions()) {
                    initExportTask();

                }

            }
        });


    }

    private void initExportTask() {
        getAllContacts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<ContactVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        displayProgress("Please Wait contact exporting to csv");
                    }

                    @Override
                    public void onSuccess(List<ContactVO> contactVOS) {
                        saveFile(contactVOS);
                        hideProgress();

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();

                    }
                });
    }

    private boolean checkAndRequestPermissions() {


        int storagePermission = ContextCompat.checkSelfPermission(this,


                Manifest.permission.READ_EXTERNAL_STORAGE);
        int writestoragePermission = ContextCompat.checkSelfPermission(this,


                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int writeContact = ContextCompat.checkSelfPermission(this,


                Manifest.permission.WRITE_CONTACTS);

        int ReadContact = ContextCompat.checkSelfPermission(this,


                Manifest.permission.READ_CONTACTS);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (writestoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (writeContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (ReadContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_READ_EXTERNAL_STORAGE);


            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initExportTask();
                    //Permission Granted Successfully. Write working code here.
                } else {
                    checkAndRequestPermissions();
                }
                break;
        }
    }

    private Maybe<List<ContactVO>> getAllContacts() {

        List<ContactVO> contactVOList = new ArrayList();
        ContactVO contactVO;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Log.e("iffffffff", "000000000");
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactVO();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    contactVOList.add(contactVO);

                }

            }


        } else {

            showSnakbar("You have no contact in your phone");
        }

        return Maybe.just(contactVOList);

    }

    public boolean saveFile(List<ContactVO> contact) {

        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataPath = SDPath + "/Contact/data/";
        String zipPath = SDPath + "/Contact/zipfile/";


        File myFile;
        try {

            File dir = new File(dataPath);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            myFile = new File(dataPath + "/myfile.csv");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);


            Iterator itr = contact.iterator();
            int i = 0;
            while (itr.hasNext()) {
                if (i != contact.size() && contact.size() != 0) {
                    String name = contact.get(i).getContactName().toString();
                    String phone = contact.get(i).getContactNumber().toString();

                    myOutWriter.append(name + "," + phone);
                    myOutWriter.append("\n");
                    i++;
                } else {
                    myOutWriter.close();
                    fOut.close();

                    FileHelper.zip(dataPath, zipPath, "Contact.zip", false);


                    showSnakbar("\"Contact Export to 'sdCard/Contact'\"");


                    break;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("00000000", "File write failed: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("00000000", "File write failed: " + e.toString());
        }

        return false;
    }

    private void showSnakbar(String s) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void displayProgress(String mes) {

        pd = new ProgressDialog(this, R.style.MyAlertDialogStyle);


        pd.setMessage(mes);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

    }

    public void hideProgress() {
        pd.dismiss();
    }

}
