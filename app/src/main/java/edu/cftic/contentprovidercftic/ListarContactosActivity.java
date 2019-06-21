package edu.cftic.contentprovidercftic;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ListarContactosActivity extends AppCompatActivity {

    private static final String[] PERMISOS = {Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ContentResolver contentResolver = getContentResolver();
        Uri uri_contactos = ContactsContract.Contacts.CONTENT_URI;////content://com.android.contacts/contacts

        String[] prefijo = {"J%"};
        //Cursor cursor_contactos = contentResolver.query(uri_contactos, null, null, null, null); //Selecciono todas las columnas, de todos
        //Cursor cursor_contactos = contentResolver.query(uri_contactos, null, null, null, ContactsContract.Contacts.DISPLAY_NAME +" ASC"); //Selecciono todas las columnas, de todos
        // Cursor cursor_contactos = contentResolver.query(uri_contactos, null, ContactsContract.Contacts.HAS_PHONE_NUMBER +" = 1" ,null, null); //Selecciono todas las columnas, de todos
         Cursor cursor_contactos = contentResolver.query(uri_contactos, null, ContactsContract.Contacts.DISPLAY_NAME +" LIKE ?" ,prefijo, null); //Selecciono todas las columnas, de todos
        //Cursor cursor_contactos = contentResolver.query(uri_contactos, null, ContactsContract.Contacts.DISPLAY_NAME + " = 'Evutxa'", null, null); //Selecciono todas las columnas, de todos

        Cursor cursor_raw = null;

        Cursor cursor_data = null;

        if (cursor_contactos.moveToFirst()) {
            Log.d("MIAPP", "NUM CONTACTOS = " + cursor_contactos.getCount());

            long id_aux = 0;
            String str_aux = null;

            int num_columna_id = cursor_contactos.getColumnIndex(ContactsContract.Contacts._ID);
            int num_columna_name = cursor_contactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {

                id_aux = cursor_contactos.getLong(num_columna_id);
                str_aux = cursor_contactos.getString(num_columna_name);


                Log.d("MIAPP", "(CONTACT)ID CONTACT = " + id_aux + " NOMBRE CONTACT = " + str_aux);

                cursor_raw = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID + " = " + id_aux, null, null);

                if (cursor_raw.moveToFirst()) {
                    do {

                        long id_raw_aux = cursor_raw.getLong(cursor_raw.getColumnIndex(ContactsContract.RawContacts._ID));
                        String nombre_cuenta = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                        String tipo_cuenta = cursor_raw.getString(cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));

                        Log.d("MIAPP", " (RAW)NOMBRE CUENTA = " + nombre_cuenta + " TIPO CUENTA RAW = " + tipo_cuenta);


                        cursor_data = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.RAW_CONTACT_ID + " = " + id_raw_aux, null, null);

                        if (cursor_data.moveToFirst()) {
                            do {

                                String mime_type = cursor_data.getString(cursor_data.getColumnIndex(ContactsContract.Data.MIMETYPE));
                                String data = cursor_data.getString(cursor_data.getColumnIndex(ContactsContract.Data.DATA1));

                                Log.d("MIAPP", "  (DATA)MIME = " + mime_type + " DATA = " + data);

                            } while (cursor_data.moveToNext());
                        }
                        cursor_data.close();


                    } while (cursor_raw.moveToNext());
                }
                cursor_raw.close();


            } while (cursor_contactos.moveToNext());
        }
        cursor_contactos.close();

        //Selección de todos los telefonos

        Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);

        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
            int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
            Log.d("MIAPP", "Telefono = " + number);
        }
        phones.close();
    }
}
