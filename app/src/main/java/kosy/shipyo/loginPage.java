package kosy.shipyo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class loginPage extends Activity {


    public String username = "";
    public String password = "";
    EditText usernameBox;
    EditText passwordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage_layout);

        //old method:
        // passwordBox  = (EditText)findViewById(R.id.password_box);

        //ImageView kiscica = (ImageView) findViewById(R.id.imageView);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        editTextvalue("username",R.id.username_box);
        editTextvalue("password",R.id.password_box);

        Button login_Button = (Button) findViewById(R.id.login_button);
        login_Button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent inent = new Intent(this, dynamicGame.class);

                /*
                //JSON REQUEST
                String url = "http://kosysite.hol.es/webservices.php";
                TextView textView = (TextView) findViewById(R.id.textView);
                new jsonGet(textView).execute(url);*/

                try {
                    username = usernameBox.getText().toString();
                    password = passwordBox.getText().toString();
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "username / password incorrect. Please use Return button always.", Toast.LENGTH_LONG).show();
                }

                String result = "ures";
                try {

                    result = new jsonPost().execute(username, password).get();
                    //valami a végén ott van, gondolom \n és mindig elhalt miatta.
                    result = result.substring(0,result.length()-1);
                    if (result.equals("ok")){
                        Toast.makeText(getApplicationContext(), "Account is correct.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), RealTimeGame.class);
                        String[] passAccounts = new String[2];
                        passAccounts[0] = username;
                        passAccounts[1] = password;
                        intent.putExtra("account",passAccounts);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Account is not correct.", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex){ System.out.println("Authentication was not succesful. " + ex); }



            }

        }) ;

        Button reg_Button = (Button) findViewById(R.id.reg_button);
        reg_Button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO registert megcsinálni, jsonPost class alapján simple
            }
        });


        Button temp_Button = (Button) findViewById(R.id.temp_button);
        temp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RealTimeGame.class);
                startActivity(intent);
            }
        });





    }

    private void editTextvalue(final String modifiy, int input){

        final EditText edittext = (EditText) findViewById(input);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    if (modifiy.equals("username")) {
                        usernameBox = edittext;
                    }
                    if (modifiy.equals("password")) {
                        passwordBox = edittext;
                    }

                    InputMethodManager manager = (InputMethodManager) v.getContext()
                            .getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null)
                        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

}
