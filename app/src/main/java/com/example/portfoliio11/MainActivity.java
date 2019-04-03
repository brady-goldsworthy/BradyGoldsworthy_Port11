package com.example.portfoliio11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button[][] buttons;

    private TicTacToe game;

    private TextView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //we aren't using activity_main.xml

        //Create TicTacToe game

        game = new TicTacToe();

        buildGUI();



    } //end onCreate

    public void buildGUI() {
        //Get width of screen
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        //Buttons will be 1/3 size of screen
        int width = size.x / TicTacToe.SIDE;

        //Create a grid layout
        GridLayout gridLayout = new GridLayout(this);

        //Set rows and cols of grid
        gridLayout.setRowCount(TicTacToe.SIDE + 1); //Version 4 includes row for banner
        gridLayout.setColumnCount(TicTacToe.SIDE);

        //Create 2D array of buttons
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];

        //handle Button clicks
        ButtonHandler buttonHandler = new ButtonHandler();

        //Place buttons in grid
        for (int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                //generate button for each spot in button array
                buttons[row][col] = new Button(this);

                //set textSize for X's and O's
                buttons[row][col].setTextSize((int)(width * 0.2));

                buttons[row][col].setOnClickListener(buttonHandler);

                //Add button to gridLayout
                gridLayout.addView(buttons[row][col], width, width); //button is a width x width square
            }
        }

        //Create textview for banner
        banner = new TextView(this);

        //Set the size for the textview
        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);
        GridLayout.Spec colSpec = GridLayout.spec(0, TicTacToe.SIDE);

        GridLayout.LayoutParams layoutParamsBanner = new GridLayout.LayoutParams(rowSpec, colSpec);

        //attach the layout params to the textview
        banner.setLayoutParams(layoutParamsBanner);

        banner.setWidth(TicTacToe.SIDE * width);
        banner.setHeight(width);

        //Center the textView
        banner.setGravity(Gravity.CENTER);

        banner.setBackgroundColor(Color.LTGRAY);

        //Set text size and text
        banner.setTextSize((int) (width * 0.05));

        banner.setText(game.result());

        //add banner to grid layout
        gridLayout.addView(banner);

        setContentView(gridLayout); //Render button array





    } //end buildGUI

    //The  button handler
    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Display message on click
            //Toast.makeText(MainActivity.this, "ButtonHandler onCLick", Toast.LENGTH_SHORT).show();

            //determine which button was clicked
            for (int row = 0; row < TicTacToe.SIDE; row++) {
                for (int col = 0; col < TicTacToe.SIDE; col++) {
                    if (v == buttons[row][col]) {
                        update(row, col);
                    }

                }
            }

        } //end onCLick



    } //end buttonhandler


    public void update(int row, int col) {
        //Display message on update
        //Toast.makeText(this, "Button updated, row: " + row + " col: " + col, Toast.LENGTH_SHORT).show();

        //find the current player
        int currentPlayer = game.play(row, col);

        if (currentPlayer == 1) {
            //Put symbol onto button
            buttons[row][col].setText("X");
        }
        else if (currentPlayer == 2){
            buttons[row][col].setText("O");
        }

        //check if game is over
        if (game.isGameOver()) {
            //Change background color of the textView
            banner.setBackgroundColor(Color.CYAN);

            //Update banner text
            banner.setText(game.result());

            enableButtons(false);

            showNewGameDialog();
        }


    } //end update


    public void enableButtons(boolean enabled) {
        for (int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                buttons[row][col].setEnabled(enabled);
            }
        }
    }

    public void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Tic Tac Toe");
        alert.setMessage("Start a new game?");

        //Create positive message
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //reset game
                game.resetGame();

                //and re enable buttons
                enableButtons(true);
                clearBoard();

                banner.setBackgroundColor(Color.LTGRAY);
                banner.setText(game.result());
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish(); //close application
            }
        });

        alert.show(); //display dialog

    } //End newGameDialog

    public void clearBoard() {
        for (int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                buttons[row][col].setText("");
            }
        }
    }



}
