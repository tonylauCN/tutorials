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
package com.laurt.base.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>Title: ExecutorServiceSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/30 上午8:56
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.concurrent
 *
 * @version v1.0.0
 */
public class ExecutorServiceSample {

    public static void main(String[] args) {
        ExecutorServiceSample executorServiceSample = new ExecutorServiceSample();
        executorServiceSample.sample();
        executorServiceSample.sample2(1);
        executorServiceSample.sample3();
    }

    /************************************************
     * ExecutorService使用技巧
     * sample1: 使用线程工厂修改线程名称(默认：pool-N-thread-M)
     * sample2: 多线程跟踪调试困难,使用线程名方便跟踪线程在哪个方法中出错
     * sample3: 限制任务排队数,并且监控任务的等待时间
     ************************************************/

    private void sample() {

        // 修改线程名称,设置线程为守护线程,
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    System.out.println(thread.getName() + " Exception: " + throwable.getMessage());
                })
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(1, threadFactory);

        // Runable
        Future<?> future = executorService.submit(() -> {
            int i = 1;
            System.out.println(i / 0);
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Callable 可以抛出异常,需要在Future.get()时获取
        future = executorService.submit(() -> {
            int i = 1;
            return i / 0;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    public void sample2(int i) {

        // 方便跟踪线程
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {

            final String threadOldName = Thread.currentThread().getName();
            final String threadNewName = "thread-trace-" + i;

            Thread.currentThread().setName(threadNewName);

            try {
                // real logic ...
                System.out.println(Thread.currentThread().getName());
            } finally {
                Thread.currentThread().setName(threadOldName);
            }
        });
        executorService.shutdown();
    }

    public void sample3() {
        // 保证线程有界,原生LinkedBlockingQueue导致任务量无限

        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        ExecutorService executorService = new ThreadPoolExecutor(10
                , 10
                , 0L
                , TimeUnit.MILLISECONDS
                , queue);

        // 使用代理,获得线程等待执行的时间
        ExecutorService delegateExecotor = new WaitTimeMonitoringExecutorService(executorService);
        try {
            for (int i = 0; i < 100; i++) {
                delegateExecotor.submit(() -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            delegateExecotor.shutdown();
        }
    }

    /**
     * 线程池代理,获得线程执行的等待时间
     */
    private class WaitTimeMonitoringExecutorService implements ExecutorService {

        final ExecutorService target;

        WaitTimeMonitoringExecutorService(ExecutorService target) {
            this.target = target;
        }

        /**
         * Initiates an orderly shutdown in which previously submitted
         * tasks are executed, but no new tasks will be accepted.
         * Invocation has no additional effect if already shut down.
         * <p>
         * <p>This method does not wait for previously submitted tasks to
         * complete execution.  Use {@link #awaitTermination awaitTermination}
         * to do that.
         *
         * @throws SecurityException if a security manager exists and
         *                           shutting down this ExecutorService may manipulate
         *                           threads that the caller is not permitted to modify
         *                           because it does not hold {@link
         *                           RuntimePermission}{@code ("modifyThread")},
         *                           or the security manager's {@code checkAccess} method
         *                           denies access.
         */
        @Override
        public void shutdown() {
            target.shutdown();
        }

        /**
         * Attempts to stop all actively executing tasks, halts the
         * processing of waiting tasks, and returns a list of the tasks
         * that were awaiting execution.
         * <p>
         * <p>This method does not wait for actively executing tasks to
         * terminate.  Use {@link #awaitTermination awaitTermination} to
         * do that.
         * <p>
         * <p>There are no guarantees beyond best-effort attempts to stop
         * processing actively executing tasks.  For example, typical
         * implementations will cancel via {@link Thread#interrupt}, so any
         * task that fails to respond to interrupts may never terminate.
         *
         * @return list of tasks that never commenced execution
         *
         * @throws SecurityException if a security manager exists and
         *                           shutting down this ExecutorService may manipulate
         *                           threads that the caller is not permitted to modify
         *                           because it does not hold {@link
         *                           RuntimePermission}{@code ("modifyThread")},
         *                           or the security manager's {@code checkAccess} method
         *                           denies access.
         */
        @Override
        public List<Runnable> shutdownNow() {
            return target.shutdownNow();
        }

        /**
         * Returns {@code true} if this executor has been shut down.
         *
         * @return {@code true} if this executor has been shut down
         */
        @Override
        public boolean isShutdown() {
            return target.isShutdown();
        }

        /**
         * Returns {@code true} if all tasks have completed following shut down.
         * Note that {@code isTerminated} is never {@code true} unless
         * either {@code shutdown} or {@code shutdownNow} was called first.
         *
         * @return {@code true} if all tasks have completed following shut down
         */
        @Override
        public boolean isTerminated() {
            return target.isTerminated();
        }

        /**
         * Blocks until all tasks have completed execution after a shutdown
         * request, or the timeout occurs, or the current thread is
         * interrupted, whichever happens first.
         *
         * @param timeout the maximum time to wait
         * @param unit    the time unit of the timeout argument
         * @return {@code true} if this executor terminated and
         * {@code false} if the timeout elapsed before termination
         *
         * @throws InterruptedException if interrupted while waiting
         */
        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return target.awaitTermination(timeout, unit);
        }

        /**
         * Submits a value-returning task for execution and returns a
         * Future representing the pending results of the task. The
         * Future's {@code get} method will return the task's result upon
         * successful completion.
         * <p>
         * <p>
         * If you would like to immediately block waiting
         * for a task, you can use constructions of the form
         * {@code result = exec.submit(aCallable).get();}
         * <p>
         * <p>Note: The {@link Executors} class includes a set of methods
         * that can convert some other common closure-like objects,
         * for example, {@link PrivilegedAction} to
         * {@link Callable} form so they can be submitted.
         *
         * @param task the task to submit
         * @return a Future representing pending completion of the task
         *
         * @throws RejectedExecutionException if the task cannot be
         *                                    scheduled for execution
         * @throws NullPointerException       if the task is null
         */
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            final long startTime = System.currentTimeMillis();
            return target.submit(() -> {
                final long queueDuration = System.currentTimeMillis() - startTime;
                System.out.println(Thread.currentThread().getName() + " - wait:" + queueDuration + "ms");
                return task.call();
            });
        }

        /**
         * Submits a Runnable task for execution and returns a Future
         * representing that task. The Future's {@code get} method will
         * return the given result upon successful completion.
         *
         * @param task   the task to submit
         * @param result the result to return
         * @return a Future representing pending completion of the task
         *
         * @throws RejectedExecutionException if the task cannot be
         *                                    scheduled for execution
         * @throws NullPointerException       if the task is null
         */
        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return submit(() -> {
                task.run();
                return result;
            });
        }

        /**
         * Submits a Runnable task for execution and returns a Future
         * representing that task. The Future's {@code get} method will
         * return {@code null} upon <em>successful</em> completion.
         *
         * @param task the task to submit
         * @return a Future representing pending completion of the task
         *
         * @throws RejectedExecutionException if the task cannot be
         *                                    scheduled for execution
         * @throws NullPointerException       if the task is null
         */
        @Override
        public Future<?> submit(Runnable task) {
            return submit(new Callable<Void>() {
                /**
                 * Computes a result, or throws an exception if unable to do so.
                 *
                 * @return computed result
                 *
                 * @throws Exception if unable to compute a result
                 */
                @Override
                public Void call() throws Exception {
                    task.run();
                    return null;
                }
            });
        }

        /**
         * Executes the given tasks, returning a list of Futures holding
         * their status and results when all complete.
         * {@link Future#isDone} is {@code true} for each
         * element of the returned list.
         * Note that a <em>completed</em> task could have
         * terminated either normally or by throwing an exception.
         * The results of this method are undefined if the given
         * collection is modified while this operation is in progress.
         *
         * @param tasks the collection of tasks
         * @return a list of Futures representing the tasks, in the same
         * sequential order as produced by the iterator for the
         * given task list, each of which has completed
         *
         * @throws InterruptedException       if interrupted while waiting, in
         *                                    which case unfinished tasks are cancelled
         * @throws NullPointerException       if tasks or any of its elements are {@code null}
         * @throws RejectedExecutionException if any task cannot be
         *                                    scheduled for execution
         */
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return target.invokeAll(tasks);
        }

        /**
         * Executes the given tasks, returning a list of Futures holding
         * their status and results
         * when all complete or the timeout expires, whichever happens first.
         * {@link Future#isDone} is {@code true} for each
         * element of the returned list.
         * Upon return, tasks that have not completed are cancelled.
         * Note that a <em>completed</em> task could have
         * terminated either normally or by throwing an exception.
         * The results of this method are undefined if the given
         * collection is modified while this operation is in progress.
         *
         * @param tasks   the collection of tasks
         * @param timeout the maximum time to wait
         * @param unit    the time unit of the timeout argument
         * @return a list of Futures representing the tasks, in the same
         * sequential order as produced by the iterator for the
         * given task list. If the operation did not time out,
         * each task will have completed. If it did time out, some
         * of these tasks will not have completed.
         *
         * @throws InterruptedException       if interrupted while waiting, in
         *                                    which case unfinished tasks are cancelled
         * @throws NullPointerException       if tasks, any of its elements, or
         *                                    unit are {@code null}
         * @throws RejectedExecutionException if any task cannot be scheduled
         *                                    for execution
         */
        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return target.invokeAll(tasks, timeout, unit);
        }

        /**
         * Executes the given tasks, returning the result
         * of one that has completed successfully (i.e., without throwing
         * an exception), if any do. Upon normal or exceptional return,
         * tasks that have not completed are cancelled.
         * The results of this method are undefined if the given
         * collection is modified while this operation is in progress.
         *
         * @param tasks the collection of tasks
         * @return the result returned by one of the tasks
         *
         * @throws InterruptedException       if interrupted while waiting
         * @throws NullPointerException       if tasks or any element task
         *                                    subject to execution is {@code null}
         * @throws IllegalArgumentException   if tasks is empty
         * @throws ExecutionException         if no task successfully completes
         * @throws RejectedExecutionException if tasks cannot be scheduled
         *                                    for execution
         */
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return target.invokeAny(tasks);
        }

        /**
         * Executes the given tasks, returning the result
         * of one that has completed successfully (i.e., without throwing
         * an exception), if any do before the given timeout elapses.
         * Upon normal or exceptional return, tasks that have not
         * completed are cancelled.
         * The results of this method are undefined if the given
         * collection is modified while this operation is in progress.
         *
         * @param tasks   the collection of tasks
         * @param timeout the maximum time to wait
         * @param unit    the time unit of the timeout argument
         * @return the result returned by one of the tasks
         *
         * @throws InterruptedException       if interrupted while waiting
         * @throws NullPointerException       if tasks, or unit, or any element
         *                                    task subject to execution is {@code null}
         * @throws TimeoutException           if the given timeout elapses before
         *                                    any task successfully completes
         * @throws ExecutionException         if no task successfully completes
         * @throws RejectedExecutionException if tasks cannot be scheduled
         *                                    for execution
         */
        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return target.invokeAny(tasks, timeout, unit);
        }

        /**
         * Executes the given command at some time in the future.  The command
         * may execute in a new thread, in a pooled thread, or in the calling
         * thread, at the discretion of the {@code Executor} implementation.
         *
         * @param command the runnable task
         * @throws RejectedExecutionException if this task cannot be
         *                                    accepted for execution
         * @throws NullPointerException       if command is null
         */
        @Override
        public void execute(Runnable command) {
            target.execute(command);
        }
    }
}
