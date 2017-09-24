package com.laurt.mongo;

public class MongoApplication {
	public static void main(String[] args) throws InterruptedException {

	    Processor processor = new Processor();
	    processor.process("demo", Constants.MONGO_COLLECTION);

	}
}
