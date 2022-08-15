public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Почта", "Сходить на почту и получить посылку");
        Task task2 = new Task("Налоги", "Заплатить налоги и спать спокойно");
        Subtask subtask1 = new Subtask("Спиисок покупок", "Купить макароны", 1);
        Subtask subtask2 = new Subtask("Спиисок покупок", "Купить хлеб", 2);
        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты");
        Epic epic2 = new Epic("Сходить в магазин", "Купить бытовую химию");
        Subtask subtask3 = new Subtask("Спиисок покупок", "Купить средство для мытья посуды");

        int task1Id = manager.createATask(task1);
        int task2Id = manager.createATask(task2);
        System.out.println(manager.getListOfAllTasks());
        System.out.println(manager.getTaskById(task1Id));
        System.out.println(manager.getTaskById(task2Id));
        System.out.println();

        int epic1Id = manager.createAEpic(epic1);
        subtask1.setNumberOfEpic(epic1Id);
        subtask2.setNumberOfEpic(epic1Id);
        int subtask1Id = manager.createASubtask(subtask1);
        epic1.addSubtaskToEpic(subtask1Id);
        int subtask2Id = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(subtask2Id);
        System.out.println(manager.getEpicById(epic1Id));
        System.out.println(manager.getListFromEpic(epic1Id));
        System.out.println(manager.getSubtaskById(subtask1Id));
        System.out.println(manager.getSubtaskById(subtask2Id));
        System.out.println();

        int epic2Id = manager.createAEpic(epic2);
        int subtask3Id = manager.createASubtask(subtask3);
        epic2.addSubtaskToEpic(subtask3Id);
        System.out.println(manager.getEpicById(epic2Id));
        System.out.println();

        subtask1.setStatus(TaskStatus.DONE);
        manager.overwriteSubtask(subtask1Id, subtask1);
        subtask2.setStatus(TaskStatus.DONE);
        manager.overwriteSubtask(subtask2Id, subtask2);
        manager.checkStatusOfTheEpic(epic1Id);
        System.out.println(manager.getEpicById(epic1Id));
        System.out.println();

        manager.deleteTask(task1Id);
        System.out.println(manager.getListOfAllTasks());
        System.out.println();

        manager.deleteEpic(epic1Id);
        System.out.println(manager.getListOfAllEpics());

        InMemoryTaskManager taskManager = (InMemoryTaskManager) manager;
        System.out.println(taskManager.getHistory());
    }
}
