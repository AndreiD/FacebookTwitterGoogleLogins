package androidadvance.com.facebooktwittergooglelogins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button_twitter = (Button) findViewById(R.id.button_twitter);
        Button button_facebook = (Button) findViewById(R.id.button_facebook);
        Button button_google = (Button) findViewById(R.id.button_google);
        Button button_email = (Button) findViewById(R.id.button_email);


        button_twitter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TwitterActivity.class));
            }
        });

        button_facebook.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FacebookActivity.class));
            }
        });

        button_google.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoogleActivity.class));
            }
        });

        button_email.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EmailActivity.class));
            }
        });
    }

}
