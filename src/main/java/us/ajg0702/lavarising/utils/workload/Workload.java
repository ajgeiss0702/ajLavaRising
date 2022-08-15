package us.ajg0702.lavarising.utils.workload;

public interface Workload {
    void compute();

    default boolean shouldBeRescheduled() {
        return false;
    }
}
