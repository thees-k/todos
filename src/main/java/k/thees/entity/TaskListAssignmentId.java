package k.thees.entity;

import java.io.Serializable;
import java.util.Objects;

public class TaskListAssignmentId implements Serializable {
    private Long task;
    private Long list;

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskListAssignmentId that)) return false;
        return Objects.equals(task, that.task) && Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, list);
    }
}
