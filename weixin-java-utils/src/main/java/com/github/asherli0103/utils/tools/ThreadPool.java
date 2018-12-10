/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author AsherLi0103
 * @version 1.0.00
 */
public class ThreadPool<T extends Runnable> extends Thread {
    public static final int DEFAULT_SIZE = 50;
    protected final boolean useThread;
    private final ExecutorService services;
    private final List<T> threadRunables;

    public ThreadPool() {
        this(10);
    }

    /**
     * 可以指定是否采用多线程执行工作,默认线程池大小为50
     *
     * @param useThread exception
     */
    public ThreadPool(final boolean useThread) {
        this(useThread, DEFAULT_SIZE);
    }

    public ThreadPool(final int size) {
        this(true, size);
    }

    /**
     * 可以指定是否采用多线程执行工作,并且需要指定线程池大小
     *
     * @param useThread 是否多线程
     * @param size      线程池大小
     */
    public ThreadPool(final boolean useThread, final int size) {
        super();
        this.useThread = useThread;
        this.services = Executors.newFixedThreadPool(size);
        this.threadRunables = new ArrayList<T>(size);
    }

    public void execute(final T threadRunable) {
        this.threadRunables.add(threadRunable);
        if (useThread)
            this.services.execute(threadRunable);
        else {
            final Thread innerThread = new Thread(threadRunable);
            innerThread.start();
            try {
                innerThread.join();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the threads
     */
    public List<T> getThreadRunables() {
        return Collections.unmodifiableList(threadRunables);
    }

    public void shutdown() {
        this.services.shutdown();
    }

    public boolean shutdown(final long timeOut) throws InterruptedException {
        return shutdown(timeOut, TimeUnit.SECONDS);
    }

    public boolean shutdown(final long timeOut, final TimeUnit timeUnit) throws InterruptedException {
        this.services.shutdown();
        return this.services.awaitTermination(timeOut, timeUnit);
    }

    /**
     * 等待多少秒后结束所有线程
     *
     * @param timeOut 超时时间
     * @return true/false
     * @throws InterruptedException exception
     */
    public boolean awaitTermination(final long timeOut) throws InterruptedException {
        return awaitTermination(timeOut, TimeUnit.SECONDS);
    }

    /**
     * 等待多少单位时间后结束所有线程
     *
     * @param timeOut  超时时间
     * @param timeUnit 关闭时间
     * @return true/false
     * @throws InterruptedException exception
     */
    public boolean awaitTermination(final long timeOut, final TimeUnit timeUnit) throws InterruptedException {
        return this.services.awaitTermination(timeOut, timeUnit);
    }
}
