public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Почта", "Сходить на почту и получить посылку");
        Task task2 = new Task("Налоги", "Заплатить налоги и спать спокойно");
        Subtask subtask1 = new Subtask("Спиисок покупок", "Купить макароны", 1);
        Subtask subtask2 = new Subtask("Спиисок покупок", "Купить хлеб", 2);
        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты");
        Epic epic2 = new Epic("Сходить в магазин", "Купить бытовую химию");
        Subtask subtask3 = new Subtask("Спиисок покупок", "Купить средство для мытья посуды");

        int id;
        id = manager.createATask(task1);
        id = manager.createATask(task2);
        System.out.println(manager.getListOfAllTasks());
        System.out.println();

        id = manager.createAEpic(epic1);
        subtask1.setNumberOfEpic(id);
        subtask2.setNumberOfEpic(id);
        id = manager.createASubtask(subtask1);
        epic1.addSubtaskToEpic(id);
        id = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(id);
        System.out.println(manager.getEpicById(2));
        System.out.println(manager.getListFromEpic(2));
        System.out.println();

        id = manager.createAEpic(epic2);
        id = manager.createASubtask(subtask3);
        epic2.addSubtaskToEpic(id);
        System.out.println(manager.getEpicById(5));
        System.out.println();

        subtask1.setStatus("DONE");
        manager.overwriteSubtask(3, subtask1);
        subtask2.setStatus("DONE");
        manager.overwriteSubtask(4, subtask2);
        manager.checkStatusOfTheEpic(2);
        System.out.println(manager.getEpicById(2));
        System.out.println();

        manager.deleteTask(0);
        System.out.println(manager.getListOfAllTasks());
        System.out.println();

        manager.deleteEpic(2);
        System.out.println(manager.getListOfAllEpics());
    }
}
