/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laurt.tutorials.reactivestream;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import reactor.core.publisher.Flux;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * <p>Title: ReactiveStreamSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/30 下午2:46
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.reactivestream
 *
 * @version v1.0.0
 */
public class ReactiveStreamSample {


    public static void main(String[] args) {
        ReactiveStreamSample streamSample = new ReactiveStreamSample();
        streamSample.doReactive();
        System.exit(0);
    }

    public void doReactive() {

        int size = 10000000;
        long ts;
        AtomicInteger integer = new AtomicInteger();
        Float value;

        //step1: // 基准 测试1  ----------------------------------------------------
        ts = System.currentTimeMillis();
        Stream<Long> data = LongStream.range(0, size).boxed();
        value = data
                .filter(predicate -> predicate % 2 == 0)
                .reduce(0L, Long::sum).floatValue();
        System.out.println("1.Origin Stream " + value + " took3 " + (System.currentTimeMillis() - ts) + "ms");

        //step2: reactivex 测试2  ----------------------------------------------------
        long ts2 = System.currentTimeMillis();
        Observable.rangeLong(0, size).reduce(Long::sum).toSingle().subscribe((v, t) -> {
            System.out.println("2.ReactiveX " + v.floatValue() + " took " + (System.currentTimeMillis() - ts2) + "ms");

        });

        //step3: reactivex 测试3  ----------------------------------------------------
        long ts3 = System.currentTimeMillis();

        LongAdder adder = new LongAdder();
        Observable.rangeLong(0L, size)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .subscribe((v) -> {
                            adder.add(v);
                        }, (throwable) -> {
                        },
                        () -> {
                            System.out.println("3.ReactiveX " + adder.floatValue() + " took " + (System.currentTimeMillis() - ts3) + "ms");
                        });

        //step4: reactivex 测试4  ----------------------------------------------------
        long ts4 = System.currentTimeMillis();

        Observable.rangeLong(0L, size).buffer(10000).reduce((a, b) -> {
            b.add(a.stream().reduce(Long::sum).orElse(1L));
            return b;
        }).toSingle().subscribe((v, t) -> {
            System.out.println("4.ReactiveX " + v.stream().reduce(Long::sum).get().floatValue() + " took " + (System.currentTimeMillis() - ts4) + "ms");
        });

        //step5: reactor 3.x 测试 ----------------------------------------------------
        long ts5 = System.currentTimeMillis();
        LongStream longStream = LongStream.range(0, size);
        LongAdder adder5 = new LongAdder();
        Flux.fromStream(longStream.boxed())
                //.publishOn(reactor.core.scheduler.Schedulers.parallel())
                .subscribeOn(reactor.core.scheduler.Schedulers.elastic())
                .subscribe((v) -> {
                    adder5.add(v);
                }, (throwable) -> {

                }, () -> {
                    System.out.println("5.Reactor " + adder5.floatValue() + " took " + (System.currentTimeMillis() - ts5) + "ms");
                });


        //step6: reactor 3.x 测试 ----------------------------------------------------
        long ts6 = System.currentTimeMillis();
        LongAdder adder6 = new LongAdder();
        Flux.fromStream((LongStream.range(0, size).boxed()))
                .reduce(Long::sum)
                .toProcessor().subscribe((v) -> {
            adder6.add(v);
        }, (throwable) -> {

        }, () -> {
            System.out.println("6.Reactor " + adder6.floatValue() + " took " + (System.currentTimeMillis() - ts6) + "ms");
        });

        //step7: reactor 3.x 测试 ----------------------------------------------------
        long ts7 = System.currentTimeMillis();
        LongAdder adder7 = new LongAdder();
        Flux.range(0, size).map(Long::new)
                .reduce(Long::sum)
                .toProcessor().subscribe((v) -> {
            adder7.add(v);
        }, (throwable) -> {

        }, () -> {
            System.out.println("7.Reactor " + adder7.floatValue() + " took " + (System.currentTimeMillis() - ts7) + "ms");
        });


        long ts8 = System.currentTimeMillis();
        LongAdder adder8 = new LongAdder();
        CountDownLatch latch = new CountDownLatch(1);
        final ActorSystem actorSystem = ActorSystem.create("Stream-service-actor");
        final Materializer materIalizer = ActorMaterializer.create(actorSystem);
        Source source = akka.stream.javadsl.Source.range(0, size)
                .map(Long::new)
                .reduce(Long::sum);

        Sink<Long, CompletionStage<Done>> sink =
                Sink.foreach(i -> {
                    adder8.add(i);
                    latch.countDown();
                });
        RunnableGraph<SourceQueueWithComplete<Long>> run = source.toMat(sink, Keep.none());
        run.run(materIalizer);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("8.Akka " + adder8.floatValue() + " took " + (System.currentTimeMillis() - ts8) + "ms");

        //stepN: reactivex 测试N  ----------------------------------------------------
        long tsN = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(() -> {
            Thread.sleep(5000);
            return 1L;
        });


        //other: reactivex Future  ----------------------------------------------------
        Observable.fromFuture(future, Schedulers.computation()).subscribe((v) -> {
                    System.out.print("N.ReactiveX Future -> " + v);
                }, (throwable -> {
                    System.err.println(throwable.getMessage());
                }),
                () -> {
                    System.out.println(" took " + (System.currentTimeMillis() - tsN) + "ms");
                });


        Observable.just(1, 2, 3, 4)
                .doOnEach((v) -> { // 监听发送事件
                    if (v.isOnComplete()) {
                        System.out.println("finished!");
                    }
                    if (v.isOnNext()) {
                        System.out.println("send next " + v.getValue());
                    }
                    if (v.isOnError()) {
                        System.err.println("had some error");
                    }
                })
                .doOnSubscribe((s) -> {  // 监听订阅
                    System.out.println(s.toString());
                }).serialize()
                .subscribe((v) -> { // 订阅
                    if (v > 2) {
                        throw new RuntimeException("a runtime exception");
                    }
                    Thread.sleep(1000);
                    System.out.println("receive value " + v);
                }, (throwable -> {
                    System.err.println("receive error: " + throwable.getMessage());
                }), () -> {
                    System.out.println("receive finished!");
                }, (subscript) -> {

                });

        executorService.shutdown();
        System.out.println("execute over!");

    }
}
