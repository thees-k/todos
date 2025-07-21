package k.thees.entity;

import java.io.Serializable;
import java.util.Objects;

public class TaskListAssignmentId implements Serializable {
    private Long task;
    private Long list;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskListAssignmentId that = (TaskListAssignmentId) o;
        return Objects.equals(task, that.task) && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, list);
    }
}
