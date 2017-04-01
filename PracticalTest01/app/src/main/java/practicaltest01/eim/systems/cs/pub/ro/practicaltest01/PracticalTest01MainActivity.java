package practicaltest01.eim.systems.cs.pub.ro.practicaltest01;


import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    private IntentFilter intentFilter = new IntentFilter();
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private TextView textView1 = null;
    private TextView textView2 = null;
    private Button pressme1 = null;
    private Button pressme2 = null;
    private Button second = null;
    private boolean serviceStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        serviceStatus = Constants.SERVICE_STOPPED;

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(String.valueOf(0));
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(String.valueOf(0));

        pressme1 = (Button) findViewById(R.id.pressme1);
        pressme2 = (Button) findViewById(R.id.pressme2);

        second = (Button) findViewById(R.id.second);

        if ((savedInstanceState != null)) {
            if (savedInstanceState.containsKey("textView1")) {
                textView1.setText(savedInstanceState.getString("textView1"));
            } else {
                textView1.setText(String.valueOf(0));
            }
        }


        if ((savedInstanceState != null)) {
            if (savedInstanceState.containsKey("textView2")) {
                textView2.setText(savedInstanceState.getString("textView2"));
            } else {
                textView2.setText(String.valueOf(0));
            }
        }



        pressme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1 = (TextView) findViewById(R.id.textView1);
                int aux = Integer.valueOf(textView1.getText().toString());
                aux++;
                textView1.setText(String.valueOf(aux));

                int leftNumber = aux;
                int rightNumber = Integer.parseInt(textView2.getText().toString());

                if(leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THREASHOLD && serviceStatus == Constants.SERVICE_STOPPED){
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                    intent.putExtra("firstNumber",leftNumber);
                    intent.putExtra("secondNumber",rightNumber);
                    getApplicationContext().startService(intent);
                    serviceStatus = Constants.SERVICE_STARTED;
                }
            }
        });



        pressme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2 = (TextView) findViewById(R.id.textView2);
                int aux = Integer.valueOf(textView2.getText().toString());
                aux++;
                textView2.setText(String.valueOf(aux));

                int leftNumber = Integer.parseInt(textView1.getText().toString());
                int rightNumber = aux;

                if(leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THREASHOLD && serviceStatus == Constants.SERVICE_STOPPED){
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                    intent.putExtra("firstNumber",leftNumber);
                    intent.putExtra("secondNumber",rightNumber);
                    getApplicationContext().startService(intent);
                    serviceStatus = Constants.SERVICE_STARTED;
                }

            }

        });



        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PracticalTest01SecondaryActivity.class);
                int counter = Integer.parseInt(textView1.getText().toString()) +
                                Integer.parseInt(textView2.getText().toString());
                intent.putExtra("numberOfClicks",counter);
                startActivityForResult(intent,SECONDARY_ACTIVITY_REQUEST_CODE);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent){
        if(requestCode == SECONDARY_ACTIVITY_REQUEST_CODE){
            Toast.makeText(this,"The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("textView1", textView1.getText().toString());
        savedInstanceState.putString("textView2", textView2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("textView1")) {
            textView1.setText(savedInstanceState.getString("textView1"));
        }else{
            textView1.setText(String.valueOf(0));
        }

        if (savedInstanceState.containsKey("textView2")) {
            textView2.setText(savedInstanceState.getString("textView2"));
        }else{
            textView2.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(messageBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause(){
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        Intent intent = new Intent(this,PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }




}
