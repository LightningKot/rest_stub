package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class StubDelay {

    @Value("${stub.delay.enabled:true}")
    private boolean delayEnabled;

    @Value(("${stub.delay.min:1000}"))
    private int min_delay;

    @Value(("${stub.delay.max:2000}"))
    private int max_delay;

    public void sleep_ms(){

        if(!delayEnabled){
            return;
        }

        int delay = ThreadLocalRandom.current().nextInt(min_delay, max_delay+1);

        try{
            Thread.sleep(delay);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
