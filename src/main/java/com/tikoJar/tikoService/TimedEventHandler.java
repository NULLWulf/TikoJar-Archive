package com.tikoJar.tikoService;

import com.tikoJar.DTO.QueryHandler;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimedEventHandler {

    public TimedEventHandler(){

        // Set QueryHandler.checkTimeLimits() to run repeatedly on a schedule
        TimerTask repeatedTask = new TimerTask() {

            public void run() {
//                QueryHandler queryHandler = new QueryHandler();
//                queryHandler.checkExpiredJars();
            }

        };

        // Set the schedule for 9am
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 9);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        // Execute the repeatedTask daily at the set time
        Timer timer = new Timer();
        timer.schedule(repeatedTask, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

    }

}
