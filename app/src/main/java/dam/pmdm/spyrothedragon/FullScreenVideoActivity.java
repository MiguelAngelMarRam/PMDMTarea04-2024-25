package dam.pmdm.spyrothedragon;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Eliminar la barra de título y poner la ventana en pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_screen_video);

        VideoView videoView = findViewById(R.id.videoViewFull);

        String packageName = getPackageName();
        Uri videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.easter_egg_video);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(1.0f, 1.0f); // Asegurar que el volumen está activado (izquierda y derecha al 100%)
                mp.start();
            }
        });

        // Opcional: Agregar controles para reproducir/pausar
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Comenzar la reproducción del vídeo
        videoView.start();

        // Cuando finalice el vídeo, cerrar la Activity
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Finaliza la actividad para volver a collectibles
                finish();
            }
        });
    }

}
