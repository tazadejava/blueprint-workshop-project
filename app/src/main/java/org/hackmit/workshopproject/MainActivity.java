package org.hackmit.workshopproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView myText1 = findViewById(R.id.myText1);
        TextView myText2 = findViewById(R.id.myText2);
        TextView myText3 = findViewById(R.id.myText3);
        TextView myText4 = findViewById(R.id.myText4);

        myText1.setText("This image makes me feel happy, because as a child, I would pick rainer cherries and the ones in the picture reminded me of them.");
        myText2.setText("This image represents the complexities of the peach and its seed, perfectly capturing the \"bleed\" effect that occurs within the peach itself.");
        myText3.setText("This image has a lot of individual grapes. It must have taken the artist quite the time to draw each and every one of them!");
        myText4.setText("This image disgusts me because it is a moldy lemon, but it also impresses me because the artist did just a good job drawing its features!");
    }
}