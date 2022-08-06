import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task{
    private ArrayList<Integer> contentOfTheEpic;

    Epic(String name, String description) {
        super(name, description);
        contentOfTheEpic = new ArrayList<>();
    }

    void addSubtaskToEpic(Integer subtask) {
        contentOfTheEpic.add(subtask);
    }

    ArrayList<Integer> getContentOfTheEpic() {
        return this.contentOfTheEpic;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 31;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash += status.hashCode();
        }
        hash *= 31;
        if (contentOfTheEpic != null) {
            hash += contentOfTheEpic.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Epic{" + "name=" + '\'' + name + '\'' +
                " description=" + '\'' + description + '\'' +
                " status=" + '\'' + status + '\'' +
                " contentOfTheEpic=" + Arrays.toString(new ArrayList[]{contentOfTheEpic}) + "}";
    }
}