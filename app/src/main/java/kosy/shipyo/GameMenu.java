package kosy.shipyo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ákos on 2017. 05. 05..
 */

public class GameMenu extends Activity{

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemenu_layout);
        Intent intent = getIntent();


        Button newGame = (Button) findViewById(R.id.newGame_button);
        newGame.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "http://kosysite.hol.es/webservices.php";
                TextView textView = (TextView) findViewById(R.id.textView);
                System.out.println(new jsonGet(textView).execute(url));

                Intent intent = new Intent(v.getContext(), RealTimeGame.class);
                startActivity(intent);



            }

        });
    }





}
