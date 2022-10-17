import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds;
    private LocalDateTime endTime;

    Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
        super.setStartTime(LocalDateTime.MIN);
        super.setDuration(Duration.ofMinutes(00));
        endTime = LocalDateTime.MIN;
        type = TypeTask.EPIC;
    }

    Epic(int id, TypeTask type, String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        super(id, type, name, description, status, startTime, duration);
        subtaskIds = new ArrayList<>();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtaskToEpic(Integer subtask) {
        subtaskIds.add(subtask);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public String subtaskIdToString() {
        StringBuilder sb = new StringBuilder();
        for (Integer id : subtaskIds) {
            sb.append(id);
            sb.append(' ');
        }
        return sb.toString().strip();
    }

    @Override
    public String toString() {
        return "Epic{" + "name=" + '\'' + name + '\'' +
                " description=" + '\'' + description + '\'' +
                " status=" + '\'' + status.name() + '\'' +
                " id=" + id +
                " type=" + type.name() +
                " startTime=" + startTime.toString() +
                " duration=" + duration.toString() +
                " endTime=" + endTime.toString() +
                " subtaskIds=" + subtaskIds.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherTask = (Epic) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                (id == otherTask.id) &&
                Objects.equals(type, otherTask.type) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(duration, otherTask.duration) &&
                Objects.equals(subtaskIds, otherTask.subtaskIds) &&
                Objects.equals(endTime, otherTask.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, type, startTime, duration, subtaskIds, endTime);
    }
}