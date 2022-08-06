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

        int numberOfTask = manager.createATask(task1);
        numberOfTask = manager.createATask(task2);
        System.out.println(manager.getListOfAllTasks());
        System.out.println();

        int numberEpic = manager.createAEpic(epic1);
        subtask1.setNumberOfEpic(numberEpic);
        subtask2.setNumberOfEpic(numberEpic);
        int numberSubtask = manager.createASubtask(subtask1);
        epic1.addSubtaskToEpic(numberSubtask);
        numberSubtask = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(numberSubtask);
        System.out.println(manager.getEpicById(numberEpic));
        System.out.println(manager.getListFromEpic2(1));
        System.out.println();

        numberEpic = manager.createAEpic(epic2);
        numberSubtask = manager.createASubtask(subtask3);
        epic2.addSubtaskToEpic(numberSubtask);
        System.out.println(manager.getEpicById(numberEpic));

        subtask1.setStatus("DONE");
        manager.overwriteSubtask(1, subtask1);
        subtask2.setStatus("DONE");
        manager.overwriteSubtask(2, subtask2);
        manager.checkStatusOfTheEpic(1);
        System.out.println(manager.getEpicById(1));
        System.out.println();

        manager.deleteTask(1);
        System.out.println(manager.getListOfAllTasks());
        System.out.println();

        manager.deleteEpic(2);
        System.out.println(manager.getListOfAllEpics());
    }
}
