package com.uex.gameoflife;

import android.app.Activity;
import android.content.res.Configuration;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends Activity implements OnClickListener {
	final Handler myHandler = new Handler();
	static final int MAX_COL = 14;
	static final int MAX_FIL = 20;
	/** Called when the activity is first created. */
	LinearLayout linearLayout[] = new LinearLayout[MAX_FIL];
	ImageView graficos[][] = new ImageView[MAX_FIL][MAX_COL];
	boolean tablero[][] = new boolean[MAX_FIL][MAX_COL];
	boolean sigTablero[][] = new boolean[MAX_FIL][MAX_COL];
	int gen;
	boolean ejecucion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		gen = 0;
		ejecucion = false;

		// LinearLayout mainLayout = new LinearLayout(this);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < MAX_FIL; i++) {
			linearLayout[i] = new LinearLayout(this);
			linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
			linearLayout[i].setGravity(Gravity.CENTER_HORIZONTAL);

			for (int j = 0; j < MAX_COL; j++) {
				graficos[i][j] = new ImageView(this);
				graficos[i][j].setImageResource(R.drawable.muerta);
				graficos[i][j].setId((i * 1000) + j);
				graficos[i][j].setOnClickListener(this);
				tablero[i][j] = false;
				linearLayout[i].addView(graficos[i][j]);
			}

			mainLayout.addView(linearLayout[i]);

		}

		LinearLayout botonesLayout = new LinearLayout(this);
		botonesLayout.setOrientation(LinearLayout.HORIZONTAL);
		botonesLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		// Button botonNext = (Button) findViewById(R.id.botonNext);
		// Button botonStart = (Button) findViewById(R.id.botonStart);
		// TextView contador = new TextView(this);
		//
		// contador.setText("Generación nº: "+gen);
		// botonNext.setText(R.string.botonNext);
		// botonStart.setText(R.string.botonStart);
		//
		// botonesLayout.addView(botonNext);
		// botonesLayout.addView(botonStart);
		// botonesLayout.addView(contador);
		mainLayout.addView(botonesLayout);

		// setContentView(mainLayout);

	}

	public void hacer() {
		gen++;
	}

	public void onClick(View casilla) {

		int i = 0, j = 0;
		boolean encontrado = false;
		while (!encontrado && i < MAX_FIL) {
			j = 0;
			while (!encontrado && j < MAX_COL) {
				if (graficos[i][j].getId() == casilla.getId()) {
					encontrado = true;
				} else {
					j++;
				}
			}
			if (!encontrado)
				i++;
		}

		if (encontrado) {
			if (tablero[i][j]) {
				tablero[i][j] = false;
				graficos[i][j].setImageResource(R.drawable.muerta);
			} else {
				tablero[i][j] = true;
				graficos[i][j].setImageResource(R.drawable.viva);
			}
		}

	}

	public void onBotonStartClick(View button) {
		Button botonNext = (Button) findViewById(R.id.botonNext);
		if (!ejecucion) {
			((Button) button).setText(R.string.botonStop);

			botonNext.setEnabled(false);
			ejecucion = true;
			(new Thread(new Runnable() {

				public void run() {
					// final String res = doLongOperation();
					while (ejecucion) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						myHandler.post(new Runnable() {
							public void run() {

								hacerPaso();
							}

						});
					}
				}
			})).start();

		} else {
			ejecucion = false;
			botonNext.setEnabled(true);
			((Button) button).setText(R.string.botonStart);
		}
	}

	public void onBotonNextClick(View button) {
		hacerPaso();
	}

	private void hacerPaso() {
		for (int i = 0; i < MAX_FIL; i++) {
			for (int j = 0; j < MAX_COL; j++) {
				sigTablero[i][j] = calcularSiguiente(i, j);
			}
		}
		for (int i = 0; i < MAX_FIL; i++) {
			for (int j = 0; j < MAX_COL; j++) {
				if (tablero[i][j] != sigTablero[i][j]) {
					tablero[i][j] = sigTablero[i][j];
					if (tablero[i][j]) {
						graficos[i][j].setImageResource(R.drawable.viva);

					} else {
						graficos[i][j].setImageResource(R.drawable.muerta);

					}
				}

			}
		}
		gen++;
		TextView contador = (TextView) findViewById(R.id.TextoGeneracion);
		contador.setText("Gen. nº: " + gen);

	}

	private boolean calcularSiguiente(int i, int j) {
		int vecinos = vecinosVivos(i, j);
		if (tablero[i][j] == true) {
			if (vecinos < 2)
				return false;
			if (vecinos > 3)
				return false;
			return true;

		} else {
			if (vecinos == 3)
				return true;
			return false;
		}
	}

	private int vecinosVivos(int i, int j) {
		int res = 0;
		boolean arriba, abajo, izquierda, derecha;

		arriba = (i - 1) >= 0;
		abajo = (i + 1) < MAX_FIL;
		izquierda = (j - 1) >= 0;
		derecha = (j + 1) < MAX_COL;

		if (arriba) {
			if (tablero[i - 1][j])
				res++;
		}
		if (abajo) {
			if (tablero[i + 1][j])
				res++;
		}
		if (izquierda) {
			if (tablero[i][j - 1])
				res++;
		}
		if (derecha) {
			if (tablero[i][j + 1])
				res++;
		}

		if (arriba && izquierda) {
			if (tablero[i - 1][j - 1])
				res++;
		}
		if (arriba && derecha) {
			if (tablero[i - 1][j + 1])
				res++;
		}
		if (abajo && izquierda) {
			if (tablero[i + 1][j - 1])
				res++;
		}
		if (abajo && derecha) {
			if (tablero[i + 1][j + 1])
				res++;
		}

		return res;
	}

	public void onConfigurationChanged(Configuration nuevaconfig) {
		super.onConfigurationChanged(nuevaconfig);
	}
}