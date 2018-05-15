package com.example.frankjunior.pinceldesom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ToggleButton;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int MIN_SAMPLE_RATE = 44100;

    private static final String TRIGGER_VERDE = "trigger_verde";
    private static final String TRIGGER_AZUL = "trigger_azul";
    private static final String TRIGGER_VERMELHO = "trigger_vermelho";
    private static final String TRIGGER_AMARELO = "trigger_amarelo";
    private static final String TRIGGER_X_MAX = "x_max";
    private static final String TRIGGER_Y_MAX = "y_max";

    private static final int AMARELO = 0xFFFFC048;
    private static final int VERMELHO = 0xFFE82126;
    private static final int VERDE = 0xFF00B87F;
    private static final int AZUL = 0xFF3666DE;

    private static final int BTN_AMARELO = R.id.btn_amarelo;
    private static final int BTN_VERMELHO = R.id.btn_vermelho;
    private static final int BTN_VERDE = R.id.btn_verde;
    private static final int BTN_AZUL = R.id.btn_azul;

    private static final String TAG = "PincelDeSom";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1000;

    private static int MAX_X = 0;
    private static int MAX_Y = 0;

    private static final int ON = 1;
    private static final int OFF = 0;

    private static final String PINCEL_DE_SOM_MAIN_PD = "pincel_de_som/pincel_de_som_main.pd";
    private static final int PINCEL_DE_SOM_ZIP = R.raw.pincel_de_som;

    private DrawingView drawingView;
    private ToggleButton btnAmarelo = null;
    private ToggleButton btnVermelho = null;
    private ToggleButton btnVerde = null;
    private ToggleButton btnAzul = null;
    private PdUiDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TODO: Improve this permission handle
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "permission is not granted");

            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);

        } else {
            Log.d(TAG,"Permission has already been granted");
            initApp();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"permission was granted");
                    initApp();
                } else {
                    Log.d(TAG,"permission denied");
                    finish();
                }
                return;
            }
        }
    }

    private void initApp() {
        setContentView(R.layout.activity_main);

        btnAmarelo = (ToggleButton) findViewById(BTN_AMARELO);
        btnVermelho = (ToggleButton) findViewById(BTN_VERMELHO);
        btnVerde = (ToggleButton) findViewById(BTN_VERDE);
        btnAzul = (ToggleButton) findViewById(BTN_AZUL);

        getScreenSize();

        try {
            initPd();
            loadPatch();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            finish();
        }

        drawingView = (DrawingView) findViewById(R.id.drawing);

        PdBase.sendFloat(TRIGGER_X_MAX, MAX_X);
        PdBase.sendFloat(TRIGGER_Y_MAX, MAX_Y);
        triggerEffect(BTN_AMARELO);
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MAX_X = size.x;
        MAX_Y = size.y;
    }

    private void initPd() throws IOException {
        // Configure the audio glue
        AudioParameters.init(this);
        int srate = Math.max(MIN_SAMPLE_RATE,
            AudioParameters.suggestSampleRate());
        PdAudio.initAudio(srate, 1, 2, 8, true);
        // Create and install the dispatcher
        dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);

    }

    private void loadPatch() throws IOException {
        File dir = getFilesDir();
        IoUtils.extractZipResource(
            getResources().openRawResource(PINCEL_DE_SOM_ZIP), dir, true);
        File patchFile = new File(dir, PINCEL_DE_SOM_MAIN_PD);
        PdBase.openPatch(patchFile.getAbsolutePath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PdAudio.stopAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PdAudio.release();
        PdBase.release();
    }

    public void clickButtonAmarelo(View view) {
        if (btnAmarelo.isChecked()) {

            btnVermelho.setChecked(false);
            btnAzul.setChecked(false);
            btnVerde.setChecked(false);

            drawingView.colorChanged(AMARELO);
            triggerEffect(BTN_AMARELO);
        }
    }

    public void clickButtonVermelho(View view) {
        if (btnVermelho.isChecked()) {

            btnAmarelo.setChecked(false);
            btnAzul.setChecked(false);
            btnVerde.setChecked(false);

            drawingView.colorChanged(VERMELHO);
            triggerEffect(BTN_VERMELHO);
        }
    }

    public void clickButtonVerde(View view) {
        if (btnVerde.isChecked()) {

            btnAmarelo.setChecked(false);
            btnAzul.setChecked(false);
            btnVermelho.setChecked(false);

            drawingView.colorChanged(VERDE);
            triggerEffect(BTN_VERDE);
        }
    }

    public void clickButtonAzul(View view) {
        if (btnAzul.isChecked()) {

            btnAmarelo.setChecked(false);
            btnVerde.setChecked(false);
            btnVermelho.setChecked(false);

            drawingView.colorChanged(AZUL);
            triggerEffect(BTN_AZUL);
        }
    }

    private void triggerEffect(int botao) {
        switch (botao) {
            case BTN_AMARELO:
                PdBase.sendFloat(TRIGGER_VERMELHO, OFF);
                PdBase.sendFloat(TRIGGER_VERDE, OFF);
                PdBase.sendFloat(TRIGGER_AZUL, OFF);
                PdBase.sendFloat(TRIGGER_AMARELO, ON);
                break;
            case BTN_VERMELHO:
                PdBase.sendFloat(TRIGGER_AMARELO, OFF);
                PdBase.sendFloat(TRIGGER_VERDE, OFF);
                PdBase.sendFloat(TRIGGER_AZUL, OFF);
                PdBase.sendFloat(TRIGGER_VERMELHO, ON);
                break;
            case BTN_VERDE:
                PdBase.sendFloat(TRIGGER_AMARELO, OFF);
                PdBase.sendFloat(TRIGGER_VERMELHO, OFF);
                PdBase.sendFloat(TRIGGER_AZUL, OFF);
                PdBase.sendFloat(TRIGGER_VERDE, ON);
                break;
            case BTN_AZUL:
                PdBase.sendFloat(TRIGGER_AMARELO, OFF);
                PdBase.sendFloat(TRIGGER_VERMELHO, OFF);
                PdBase.sendFloat(TRIGGER_VERDE, OFF);
                PdBase.sendFloat(TRIGGER_AZUL, ON);
                break;

            default:
                break;
        }
    }
}
