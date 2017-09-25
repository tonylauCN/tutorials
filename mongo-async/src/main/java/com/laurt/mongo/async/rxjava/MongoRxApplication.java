package com.laurt.mongo.async.rxjava;

import com.laurt.mongo.async.core.Constants;

import java.util.concurrent.ExecutionException;

public class MongoRxApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Processor processor = new Processor();
        processor.process("demo", Constants.MONGO_COLLECTION);

        Thread.sleep(1000000);
    }
}
