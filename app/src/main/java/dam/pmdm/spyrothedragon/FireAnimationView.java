package dam.pmdm.spyrothedragon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

public class FireAnimationView extends View {

    private Paint firePaint;
    private Path firePath;
    private final Random random = new Random();
    private int frameCount = 0;

    public FireAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        firePaint = new Paint();
        firePaint.setAntiAlias(true);
        firePaint.setStyle(Paint.Style.FILL);
        firePath = new Path();
    }

    public void startAnimation() {
        invalidate(); // Vuelve a dibujar la vista para actualizar la animación
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Tamaño y posición de la llama
        int flameBaseX = width / 2;
        int flameBaseY = height - 200;  // Empieza un poco arriba del borde inferior
        int flameWidth = 180 + random.nextInt(500);  // Variación para hacerlo más natural
        int flameHeight = 350 + random.nextInt(400);

        // Movimiento ondulante
        int waveOffset = (int) (Math.sin(frameCount * 0.2) * 30);

        // Crear la forma de la llama
        firePath.reset();
        firePath.moveTo(flameBaseX, flameBaseY);
        firePath.quadTo(flameBaseX - (float) flameWidth / 2 + waveOffset, flameBaseY - flameHeight / 2,
                flameBaseX, flameBaseY - flameHeight);
        firePath.quadTo(flameBaseX + (float) flameWidth / 2 - waveOffset, flameBaseY - flameHeight / 2,
                flameBaseX, flameBaseY);
        firePath.close();

        // Gradiente de color para simular fuego
        @SuppressLint("DrawAllocation") LinearGradient gradient = new LinearGradient(
                flameBaseX, flameBaseY - flameHeight,
                flameBaseX, flameBaseY,
                new int[]{Color.WHITE, Color.YELLOW, Color.RED},
                new float[]{0.1f, 0.5f, 1f},
                Shader.TileMode.CLAMP
        );
        firePaint.setShader(gradient);

        // Dibujar la llama
        canvas.drawPath(firePath, firePaint);

        // Incrementar el contador de frames para animación
        frameCount++;

        // Volver a dibujar (animación)
        postInvalidateDelayed(50);
    }
}

