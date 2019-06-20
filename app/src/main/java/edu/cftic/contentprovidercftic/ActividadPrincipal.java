package edu.cftic.contentprovidercftic;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActividadPrincipal extends AppCompatActivity {

    private static final String[] PERMISOS = {Manifest.permission.READ_CONTACTS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        ActivityCompat.requestPermissions(this, PERMISOS, 300);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        startActivity(new Intent(this, ListarContactosActivity.class));
        //startActivity(new Intent(this, SeleccionContactoActivity.class));
    }
}
