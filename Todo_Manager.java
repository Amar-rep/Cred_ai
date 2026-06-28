package week;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Todo_Manager {
    public static void main(String[] args) {

        Todo todo = new Todo();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to TODOLIST -_- ");

        while (true) {
            System.out.println("\n1. Create new category");
            System.out.println("2. Create new task");
            System.out.println("3. Update Status");
            System.out.println("4. Show todo status");
            System.out.println("5. Sort tasks");
            System.out.println("6. Filter tasks");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createCategory(todo, scanner);
                    break;
                case 2:
                    createTask(todo, scanner);
                    break;
                case 3:
                    updateStatus(todo, scanner);
                    break;
                case 4:
                    showStatus(todo);
                    break;
                case 5:
                    sortTasks(todo, scanner);
                    break;
                case 6:
                    filterTasks(todo, scanner);
                    break;
                case 7:
                    System.out.println("Thank YOU!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println("---------------------------");
        }

    }

    private static void createCategory(Todo todo, Scanner scanner) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        todo.addCategory(categoryName);
        System.out.println("New Category : " + categoryName);
    }

    private static void createTask(Todo todo, Scanner scanner) {
        System.out.println("Enter The Task title :");
        String title = scanner.nextLine();
        System.out.println("Enter the Task description");
        String description = scanner.nextLine();
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            String dateString = scanner.nextLine();
            try {
                date = LocalDate.parse(dateString);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date format Please use yyyy-MM-dd");
            }
        }
        System.out.println("Choose category : ");
        for (int i = 0; i < todo.getCategories().size(); i++) {
            System.out.println((i + 1) + ". " + todo.getCategories().get(i));
        }
        int categoryIndex = scanner.nextInt();
        scanner.nextLine();

        if (!todo.isValidCategoryIndex(categoryIndex)) {
            System.out.println("Invalid category");
            return;
        }

        String selectedCategory = todo.getCategoryByIndex(categoryIndex);
        Task task = todo.addTask(title, description, date, selectedCategory);
        System.out.println("New task : ");
        todo.printTask(task);
    }

    private static void updateStatus(Todo todo, Scanner scanner) {
        if (!todo.hasTasks()) {
            System.out.println("No tasks available");
            return;
        }

        System.out.println("Select Task : ");
        for (int i = 0; i < todo.getTasks().size(); i++) {
            System.out.println(i + 1 + "." + todo.getTasks().get(i).getTitle());
        }
        int taskIndex = scanner.nextInt();
        scanner.nextLine();

        if (!todo.isValidTaskIndex(taskIndex)) {
            System.out.println("Invaid Task");
            return;
        }

        System.out.println("Choose status : ");
        System.out.println("1. COMPLETED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. NOT_STARTED");
        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        Status selectedStatus = null;
        switch (statusChoice) {
            case 1:
                selectedStatus = Status.COMPLETED;
                break;
            case 2:
                selectedStatus = Status.IN_PROGRESS;
                break;
            case 3:
                selectedStatus = Status.NOT_STARTED;
                break;
            default:
                System.out.println("Invalid status");
                break;
        }

        if (selectedStatus == null) {
            return;
        }

        todo.updateTaskStatus(taskIndex, selectedStatus);
        System.out.println("Task status updated");
    }

    private static void showStatus(Todo todo) {
        System.out.println("TODO LIST STATUS");
        HashMap<Status, Integer> map = todo.getStatusCount();

        System.out.println("Total tasks: " + todo.getTasks().size());
        for (Status status : Status.values()) {
            System.out.println(status + ": " + map.getOrDefault(status, 0));
        }
    }

    private static void sortTasks(Todo todo, Scanner scanner) {
        if (!todo.hasTasks()) {
            System.out.println("No tasks ");
            return;
        }

        System.out.println("Choose sort type:");
        System.out.println("1. Due date");
        System.out.println("2. Category");
        System.out.println("3. Status");
        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        switch (sortChoice) {
            case 1:
                todo.sortByDueDate();
                System.out.println("Tasks sorted by due date");
                break;
            case 2:
                todo.sortByCategory();
                System.out.println("Tasks sorted by category");
                break;
            case 3:
                todo.sortByStatus();
                System.out.println("Tasks sorted by status");
                break;
            default:
                System.out.println("Invalid sort selection");
                return;
        }
        for (int i = 0; i < todo.getTasks().size(); i++) {
            todo.printTask(todo.getTasks().get(i));
        }
    }

    private static void filterTasks(Todo todo, Scanner scanner) {
        if (!todo.hasTasks()) {
            System.out.println("No tasks available");
            return;
        }

        System.out.println("Choose filter type:");
        System.out.println("1. Category");
        System.out.println("2. Status");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();

        List<Task> filteredTasks = new ArrayList<>();
        switch (filterChoice) {
            case 1:
                System.out.println("Choose category : ");
                for (int i = 0; i < todo.getCategories().size(); i++) {
                    System.out.println((i + 1) + ". " + todo.getCategories().get(i));
                }
                int filterCategoryIndex = scanner.nextInt();
                scanner.nextLine();

                if (!todo.isValidCategoryIndex(filterCategoryIndex)) {
                    System.out.println("Invalid category.");
                    return;
                }

                String filterCategory = todo.getCategoryByIndex(filterCategoryIndex);
                System.out.println("FILTER CATEOGRY:" + filterCategory);
                filteredTasks = todo.filterByCategory(filterCategory);
                break;

            case 2:
                System.out.println("Choose status : ");
                System.out.println("1. COMPLETED");
                System.out.println("2. IN_PROGRESS");
                System.out.println("3. NOT_STARTED");
                int filterStatusChoice = scanner.nextInt();
                scanner.nextLine();

                Status filterStatus = null;
                switch (filterStatusChoice) {
                    case 1:
                        filterStatus = Status.COMPLETED;
                        break;
                    case 2:
                        filterStatus = Status.IN_PROGRESS;
                        break;
                    case 3:
                        filterStatus = Status.NOT_STARTED;
                        break;
                    default:
                        System.out.println("Invalid status.");
                        break;
                }

                if (filterStatus == null) {
                    return;
                }

                filteredTasks = todo.filterByStatus(filterStatus);
                break;

            default:
                System.out.println("Invalid filter Criteria");
                return;
        }
        if (filteredTasks.isEmpty()) {
            System.out.println("No matching tasks found");
        } else {
            for (Task tasks : filteredTasks) {
                todo.printTask(tasks);
            }
        }
    }
}

enum Status {
    COMPLETED,
    IN_PROGRESS,
    NOT_STARTED,
}

class Todo {
    private ArrayList<String> categoryList;
    private ArrayList<Task> taskList;

    public Todo() {
        this.categoryList = new ArrayList<>(Arrays.asList("Groceries", "Bill Payments"));
        this.taskList = new ArrayList<>();
    }

    public ArrayList<String> getCategories() {
        return categoryList;
    }

    public ArrayList<Task> getTasks() {
        return taskList;
    }

    public boolean hasTasks() {
        return !taskList.isEmpty();
    }

    public void addCategory(String categoryName) {
        categoryList.add(categoryName);
    }

    public Task addTask(String title, String description, LocalDate dueDate, String category) {
        Task newTask = new Task(title, description, dueDate, category);
        taskList.add(newTask);
        return newTask;
    }

    public boolean isValidCategoryIndex(int categoryIndex) {
        return categoryIndex >= 1 && categoryIndex <= categoryList.size();
    }

    public String getCategoryByIndex(int categoryIndex) {
        return categoryList.get(categoryIndex - 1);
    }

    public boolean isValidTaskIndex(int taskIndex) {
        return taskIndex >= 1 && taskIndex <= taskList.size();
    }

    public void updateTaskStatus(int taskIndex, Status status) {
        Task selectedTask = taskList.get(taskIndex - 1);
        selectedTask.setStatus(status);
    }

    public HashMap<Status, Integer> getStatusCount() {
        HashMap<Status, Integer> map = new HashMap<>();

        for (Task task : taskList) {
            map.put(task.getStatus(), map.getOrDefault(task.getStatus(), 0) + 1);
        }

        return map;
    }

    public void sortByDueDate() {
        taskList.sort(Comparator.comparing(Task::getDueDate));
    }

    public void sortByCategory() {
        taskList.sort(Comparator.comparing(Task::getCategory));
    }

    public void sortByStatus() {
        taskList.sort(Comparator.comparing(Task::getStatus));
    }

    public List<Task> filterByCategory(String category) {
        return taskList.stream()
                .filter(task -> task.getCategory().equals(category))
                .toList();
    }

    public List<Task> filterByStatus(Status status) {
        return taskList.stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    public void printTask(Task task) {
        System.out.println("------------------------------");
        System.out.println("Title       : " + task.getTitle());
        System.out.println("Description : " + task.getDescription());
        System.out.println("Due Date    : " + task.getDueDate());
        System.out.println("Category    : " + task.getCategory());
        System.out.println("Status      : " + task.getStatus());
        System.out.println("------------------------------");
    }
}

class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String category;
    private Status status;

    public Task(String title, String description, LocalDate dueDate, String category) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.status = Status.NOT_STARTED;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
