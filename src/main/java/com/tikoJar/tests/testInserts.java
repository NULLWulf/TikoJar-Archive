package com.tikoJar.tests;

import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.DAO.OpeningCondition;

import java.time.LocalDate;
import java.util.ArrayList;

public class testInserts {

    public testInserts(){};

    public void run(){

        OpeningCondition openingCondition = new OpeningCondition(true, 10, LocalDate.now(), null,"ABC123");

        ArrayList<Message> messages = new ArrayList<>();

        Message message1 = new Message("Bob", LocalDate.now(),"Something positive");
        Message message2 = new Message("Samander", LocalDate.now(),"Something positive again");
        Message message3 = new Message("Jake", LocalDate.now(),"Something negative");

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);

        Jar jar = new Jar("ABC123",openingCondition,messages);
    }
}
