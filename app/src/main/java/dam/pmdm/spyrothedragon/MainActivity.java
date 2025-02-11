package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;
    NavController navController = null;

    private Boolean needToStartGuide = true;
    private int currentStep = 0; // Para rastrear el paso actual
    private final int totalSteps = 6; // Número total de pasos de la guía

    private SharedPreferences sharedPreferences;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);


        // Recuperar el estado de needToStartGuide
        needToStartGuide = sharedPreferences.getBoolean("needToStartGuide", true);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        guideBinding = binding.includeLayout;
        setContentView(binding.getRoot());


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
        initializeGuide();

    }

    private void initializeGuide() {
        guideBinding.exitGuide.setOnClickListener(this::onExitGuide);
        guideBinding.nextGuide.setOnClickListener(this::animateFadeOut);

        if (needToStartGuide) {
            guideBinding.guideLayout.setVisibility(View.VISIBLE);
            binding.navView.setEnabled(false); // Deshabilitar el menú
            showStep(currentStep);
        }
    }

    private void animateFadeOut(View view) {
        // Establecer el fondo purple y hacer que el View sea visible
        guideBinding.overlayView.setVisibility(View.VISIBLE); // Hacer visible el fondo
        ObjectAnimator fadeInBackground = ObjectAnimator.ofFloat(guideBinding.overlayView, "alpha", 0f, 1f); // Fade in para el fondo
        ObjectAnimator fadeOutBackground = ObjectAnimator.ofFloat(guideBinding.overlayView, "alpha", 1f, 0f); // Fade out para el fondo

        // Animación de desvanecimiento para los elementos visibles
        ObjectAnimator fadeOutText = ObjectAnimator.ofFloat(guideBinding.textStep, "alpha", 1f, 0f);
        ObjectAnimator fadeOutNext = ObjectAnimator.ofFloat(guideBinding.nextGuide, "alpha", 1f, 0f);
        ObjectAnimator fadeOutExit = ObjectAnimator.ofFloat(guideBinding.exitGuide, "alpha", 1f, 0f);

        // Establecer duración de las animaciones
        fadeInBackground.setDuration(2000);  // 2 segundos para el fade in del fondo
        fadeOutText.setDuration(1000);       // 1 segundo para los fade outs
        fadeOutNext.setDuration(1000);
        fadeOutExit.setDuration(1000);
        fadeOutBackground.setDuration(3000);  // 3 segundos para el fade out del fondo

        // Iniciar todas las animaciones al mismo tiempo
        AnimatorSet fadeSet = new AnimatorSet();
        fadeSet.playTogether(fadeInBackground, fadeOutText, fadeOutNext, fadeOutExit);

        // Iniciar la animación
        fadeSet.start();

        // Después de que la animación termine, realizar otras acciones
        fadeSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Hacer que los elementos se desvanezcan completamente
                guideBinding.textStep.setAlpha(0);
                guideBinding.nextGuide.setAlpha(0);
                guideBinding.exitGuide.setAlpha(0);

                // Puedes realizar otras acciones después de la animación
                fadeOutBackground.setDuration(3000);  // 3 segundos para el fade out del fondo

                onNextGuide(view);  // Continuar con el siguiente paso de la guía
                fadeOutBackground.start();
            }
        });
    }
    private void animatePulseImage() {
        // Ocultamos los textos y el boton
        guideBinding.textStep.setAlpha(0);
        guideBinding.nextGuide.setAlpha(0);
        guideBinding.exitGuide.setAlpha(0);
        // Animación inicial: ocupar toda la pantalla y luego reducirse
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(
                guideBinding.pulseImage, "scaleX", 5f, 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(
                guideBinding.pulseImage, "scaleY", 5f, 1f);
        scaleUpX.setDuration(1000);
        scaleUpY.setDuration(1000);

        // Animación de pulso: aumentar y disminuir en ambos ejes
        ObjectAnimator pulseX = ObjectAnimator.ofFloat(
                guideBinding.pulseImage, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator pulseY = ObjectAnimator.ofFloat(
                guideBinding.pulseImage, "scaleY", 1f, 1.5f, 1f);
        pulseX.setRepeatCount(2);
        pulseY.setRepeatCount(2);
        pulseX.setDuration(1000);
        pulseY.setDuration(1000);

        // Animación de desvanecimiento para el texto (fade in lento)
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                guideBinding.textStep, "alpha", 0f, 1f);
        fadeIn.setDuration(2000);  // Duración más lenta para un fade in más largo
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(
                guideBinding.nextGuide, "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(
                guideBinding.exitGuide, "alpha", 0f, 1f);
        fadeIn2.setDuration(6000);  // Duración más lenta para un fade in más largo
        fadeIn3.setDuration(6000);  // Duración más lenta para un fade in más largo

        // Crear y configurar el conjunto de animaciones
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleUpX).with(scaleUpY) // Primera etapa: expandir y reducir
                .before(pulseX).before(pulseY) // Segunda etapa: pulso
                .before(fadeIn)   // Tercera etapa: desvanecer el texto para que aparezca
                .before(fadeIn2).before(fadeIn3); // Cuarta etapa: desvanecer los botones para que aparezca al terminar

        animatorSet.start();
        playSound2();

        // Listener para manejar la visibilidad al finalizar
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (needToStartGuide) {
                    super.onAnimationEnd(animation);
                    guideBinding.pulseImage.setVisibility(View.VISIBLE);
                    guideBinding.textStep.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.campfire_ignite);
        }
        mediaPlayer.start();
    }
    private void playSound2() {
        if (mediaPlayer2 == null) {
            mediaPlayer2 = MediaPlayer.create(this, R.raw.level_up);
        }
        mediaPlayer2.start();
    }

    private void releaseMediaPlayer(MediaPlayer player) {
        if (player != null) {
            player.release();
        }
    }

    private void showStep(int step) {
        switch (step) {
            case 0:
                // Paso inicial
                // Mostrar la imagen del primer paso
                guideBinding.imageGuideStep.setVisibility(View.VISIBLE);
                ViewCompat.setElevation(guideBinding.guideLayout, 1f);
                binding.navView.setElevation(0);
                guideBinding.imageGuideStep.setImageResource(R.drawable.spyro);
                guideBinding.guideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
                highlightButton(R.id.nav_characters);
                guideBinding.textStep.setText(R.string.text_initial_guide);
                guideBinding.pulseImage.setAlpha(0f);
                guideBinding.nextGuide.setText(R.string.start);
                break;
            case 1:
                // Paso 1: Primer paso
                guideBinding.pulseImage.bringToFront();
                guideBinding.imageGuideStep.setVisibility(View.GONE);
                guideBinding.pulseImage.setAlpha(1f);
                guideBinding.guideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white_transparent));
                highlightButton(R.id.nav_characters);
                guideBinding.textStep.setText(R.string.text_button_1);
                guideBinding.nextGuide.setText(R.string.next); // Botón "Siguiente"
                break;
            case 2:
                // Paso 2: Segundo paso
                if (navController != null && navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() != R.id.navigation_worlds) {
                    navController.navigate(R.id.navigation_worlds);
                }
                highlightButton(R.id.nav_worlds);
                guideBinding.textStep.setText(R.string.text_button_2);
                break;
            case 3:
                // Paso 3: Tercer paso
                if (navController != null && navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() != R.id.navigation_collectibles) {
                    navController.navigate(R.id.navigation_collectibles);
                }
                highlightButton(R.id.nav_collectibles);
                guideBinding.textStep.setText(R.string.text_button_3);
                break;

            case 4:

                // Paso 4: Cuarto paso
                if (navController != null && navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() != R.id.navigation_characters) {
                    navController.navigate(R.id.navigation_characters);
                }
                // Resaltar el ícono de "Información" en la barra de acción
                highlightInfoButton();
                guideBinding.textStep.setText(R.string.text_button_info);
                break;

            case 5:
                // Paso final
                guideBinding.imageGuideStep.setVisibility(View.VISIBLE);
                // Animamos directamente la escena para que se muestren los botones
                animatePulseImage();
                guideBinding.guideLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
                guideBinding.textStep.setText(R.string.text_final_guide);
                guideBinding.nextGuide.setText(R.string.finish); // Botón "Finalizar"
                guideBinding.pulseImage.setAlpha(0f);
                break;
        }
    }

    private void highlightButton(int buttonId) {
        BottomNavigationView navView = binding.navView;

        // Obtener la vista del primer botón en el BottomNavigationView
        View button = navView.findViewById(buttonId);
        if (button != null) {
            // Obtener la ubicación del ícono en la pantalla
            int[] location = new int[2];
            button.getLocationOnScreen(location);

            // Ajustar la posición del pulso sobre el ícono
            guideBinding.pulseImage.setX(location[0] + button.getWidth() / 2f - guideBinding.pulseImage.getWidth() / 2f);
            guideBinding.pulseImage.setY(location[1] - guideBinding.pulseImage.getHeight());

            // Hacer visible el pulso y aplicarle la animación
            guideBinding.pulseImage.setVisibility(View.VISIBLE);
            animatePulseImage();  // Llamamos a la animación para resaltar el ícono
        }
    }
    // Metodo para resaltar el botón de información
    private void highlightInfoButton() {

        View infoButton = findViewById(R.id.action_info);
        if (infoButton != null) {
            // Obtener la ubicación del ícono en la pantalla
            int[] location = new int[2];
            infoButton.getLocationOnScreen(location);

            // Ajustar la posición del pulso sobre el ícono
            guideBinding.pulseImage.setX(location[0] + infoButton.getWidth() / 2f - guideBinding.pulseImage.getWidth() / 2f);
            guideBinding.pulseImage.setY(location[1] - guideBinding.pulseImage.getHeight());

            // Hacer visible el pulso y aplicarle la animación
            guideBinding.pulseImage.setVisibility(View.VISIBLE);
            animatePulseImage();  // Llamamos a la animación para resaltar el ícono
        }
    }

    private void onNextGuide(View view) {
        if (currentStep < totalSteps - 1) {
            currentStep++;
            showStep(currentStep);
            playSound();
        } else {
            onExitGuide(view); // Finaliza la guía
            playSound();
        }
    }

    private void onExitGuide(View view) {
        // Ocultar la guía
        guideBinding.guideLayout.setVisibility(View.GONE);
        binding.navView.setEnabled(true); // Habilitar el menú

        // Marcar que la guía ya no debe iniciarse
        needToStartGuide = false;

        // Guardar el estado en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("needToStartGuide", false);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer(mediaPlayer);
        releaseMediaPlayer(mediaPlayer2);
    }


    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (!binding.navView.isEnabled()) {
            return false; // Ignorar clics si el menú está deshabilitado
        }

        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }


}