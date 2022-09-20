import java.util.ArrayList;
import java.util.List;

public interface HistoryManager<T> {

    void add(Integer id, T task);

    void remove(int id);

    List<T> getHistory();

    List<Integer> getHistoryId();
}
