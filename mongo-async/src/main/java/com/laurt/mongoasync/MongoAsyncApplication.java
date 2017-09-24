package com.laurt.mongoasync;

public class MongoAsyncApplication {

    public static void main(String[] args) throws InterruptedException {


        Processor processor = new Processor();
        processor.process("demo", Constants.MONGO_COLLECTION);

        Thread.sleep(1000000);
    }
}
