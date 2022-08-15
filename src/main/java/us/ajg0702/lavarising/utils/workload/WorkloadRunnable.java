package us.ajg0702.lavarising.utils.workload;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class WorkloadRunnable implements Runnable {

    private static final double MAX_MILLIS_PER_TICK = 20;
    private static final int MAX_NANOS_PER_TICK = (int) (MAX_MILLIS_PER_TICK * 1E6);

    private final Deque<Workload> workloadDeque = new ArrayDeque<>();

    public void addWorkload(Workload workload) {
        this.workloadDeque.add(workload);
    }

    public void addWorkloads(List<? extends Workload> workloads) {
        this.workloadDeque.addAll(workloads);
    }


    @Override
    public void run() {
        long stopTime = System.nanoTime() + MAX_NANOS_PER_TICK;

        Workload lastElement = this.workloadDeque.peekLast();
        Workload nextLoad = null;

        // Note: Don't permute the conditions because sometimes the time will be over but the queue will still be polled then.
        while (System.nanoTime() <= stopTime && !this.workloadDeque.isEmpty() && nextLoad != lastElement) {
            nextLoad = this.workloadDeque.poll();
            nextLoad.compute();
            if (nextLoad.shouldBeRescheduled()) {
                this.addWorkload(nextLoad);
            }
        }
    }

    public int getNumberOfTasksRemaining() {
        int remaining = 0;
        for (Workload workload : workloadDeque) {
            if(!workload.shouldBeRescheduled()) remaining++;
        }
        return remaining;
    }
}
