package practicaltest01.eim.systems.cs.pub.ro.practicaltest01;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;

import java.util.Date;
import java.util.Random;

/**
 * Created by michelcojocaru on 4/1/17.
 */

public class ProcessingThread extends Thread {

    private Context context = null;
    private boolean isRunning = true;

    private Random random = new Random();

    private double arithmeticMean;
    private double geometricMean;

    public ProcessingThread(Context context,int firstNumber,int secondNumber){
        this.context = context;
        arithmeticMean = (firstNumber + secondNumber)/2;
        geometricMean = Math.sqrt(firstNumber + secondNumber);
    }

    @Override
    public void run(){
        Log.d("[ProcessingThread]", "Thread has started!");
        while(isRunning){
            sendMessage();
            sleep();
        }
        Log.d("[ProcessingThread]", "Thread has stopped!");
    }

    private void sendMessage(){
        Intent intent = new Intent();
        intent.setAction(Constants.actionTypes[random.nextInt(Constants.actionTypes.length)]);
        intent.putExtra("message",new Date(System.currentTimeMillis()) + " " + arithmeticMean + " " + geometricMean);
        context.sendBroadcast(intent);
    }

    private void sleep(){
        try{
            Thread.sleep(10000);
        }catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }
    }

    public void stopThread(){
        isRunning = false;
    }
}