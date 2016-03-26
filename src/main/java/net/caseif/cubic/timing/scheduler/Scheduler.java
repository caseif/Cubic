/*
 * Cubic
 * Copyright (c) 2016, Maxim Roncace <me@caseif.net>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.caseif.cubic.timing.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Scheduler {

    private Set<Task> syncTasks = new HashSet<>();
    private Set<Task> asyncTasks = new HashSet<>();

    public void runTask(Runnable runnable, long delay) {
        runRepeatingTask(runnable, delay, -1);
    }

    public void runAsyncTask(Runnable runnable, long delay) {
        runRepeatingAsyncTask(runnable, delay, -1);
    }

    public void runRepeatingTask(Runnable runnable, long delay, long interval) {
        syncTasks.add(new Task(runnable, delay, interval, false));
    }

    public void runRepeatingAsyncTask(Runnable runnable, long delay, long interval) {
        asyncTasks.add(new Task(runnable, delay, interval, false));
    }

    public void pollTasks() {
        runTasks(asyncTasks, true);
        runTasks(syncTasks, false);
    }

    private void runTasks(Set<Task> tasks, boolean async) {
        Set<Task> newTasks = new HashSet<>();
        Set<Task> removeTasks = new HashSet<>();
        tasks.stream().filter(task -> System.currentTimeMillis() - task.getTimeScheduled() >= task.getDelay())
                .forEach(task -> {
                    if (async) {
                        new Thread(task.getRunnable()).run();
                    } else {
                        task.getRunnable().run();
                    }
                    if (task.getRepeatInterval() != -1) {
                        newTasks.add(new Task(task.getRunnable(), task.getRepeatInterval(), task.getRepeatInterval(),
                                task.isAsync()));
                    }
                    removeTasks.add(task);
                });
        removeTasks.forEach(tasks::remove);
        newTasks.forEach(tasks::add);
    }

    private class Task {

        private UUID id = UUID.randomUUID();
        private long scheduledAt = System.currentTimeMillis();

        private Runnable runnable;
        private long delay;
        private long interval;
        private boolean async;

        private Task(Runnable runnable, long delay, long interval, boolean async) {
            this.runnable = runnable;
            this.delay = delay;
            this.interval = interval;
            this.async = async;
        }

        private UUID getUniqueId() {
            return id;
        }

        private long getTimeScheduled() {
            return scheduledAt;
        }

        private Runnable getRunnable() {
            return runnable;
        }

        private long getDelay() {
            return delay;
        }

        private long getRepeatInterval() {
            return interval;
        }

        private boolean isAsync() {
            return async;
        }

    }

}
