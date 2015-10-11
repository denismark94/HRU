import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Model {

    private static HashMap<String, String> users = new HashMap<String, String>();
    private static String curUser = null;
    private static ArrayList<String> safe = new ArrayList<String>();
    public static boolean security = false;

    public static boolean logon(String name, String pass) {
        if (users.containsKey(name))
            if (users.get(name).equals(pass)) {
                curUser = name;
                System.out.println("Вход в систему произведен успешно");
                return true;
            } else
                System.out.println("Неправильный пароль");
        else
            System.out.println("Пользователь с таким именем не зарегистрирован в системе");
        return false;
    }

    public static void check(String object) {
        //only administrator have right to check files and add it to white list
        if (curUser.contains("admin"))
            //anti-virus magic
            if (object.contains("tr"))
                System.out.println("Файл " + object + " является потенциально опасным. " +
                        "Добавление в список безопасных запрещено");
            else {
                safe.add(object);
                System.out.println("Добавление в белый список файла " + object + " успешно");
            }
        else
            System.out.println("Только admin может добавлять файлы в список безопасных");
    }

    public static void InitializeThreatScenario(int number) {
        Scanner scan = new Scanner(System.in);
        AccessMatrix M = initialize();
        String login, pass;
        boolean passed = false;
        switch (number) {
            case 1:
                //Случай, при котором admin имеет полные права на o2
                System.out.println("admin имеет полные права на о2");
                M.enterRights("admin", "o2", "rwe");
                M.table("Инициализация");
                System.out.println("Введите логин и пароль злоумышленника");
                login = "";
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                //user создает троян o_тр
                System.out.println("Пользователь " + curUser + " создает вредоносный файл");
                M = createFile(M, curUser, "otr", "o2");
                //user, как создатель otr, дает права на выполнение трояна другому пользователю
                System.out.println(curUser + " выдает права на выполнение вредоносного файла");
                M = giveRightForFile(M, "admin", "otr", "o2");
                //admin выполняет троян, тот забирает права admin'а
                System.out.println(curUser + " выходит из системы.\n" +
                        "Зайдите в систему от пользователя - цели для продолжения");
                curUser = null;
                passed = false;
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                System.out.println(curUser + " запускает вредоносный файл");
                M = launch(M, curUser, "otr");
                if (security) {
                    System.out.println("Проверка файла текущим пользователем\n" +
                            "Худший случай - запуск инициировал admin");
                    check("otr");
                }
                M = copy(M, "str", "o3", "o2");
                // Взлом успешен, лишний пользователь str удален
                M.table("Результат");
                break;
            case 2:
                //В этом случае admin может только читать o2
                System.out.println("admin имеет только права на чтение o2");
                M.enterRight("admin", "o2", 'r');
                M.table("Инициализация");
                System.out.println("Введите логин и пароль злоумышленника");
                login = "";
                passed = false;
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                //user дает полные права admin'у на o2
                System.out.println(curUser + " добавляет admin'у недостающие права");
                M = giveRightForFolder(M, "user", "admin", "o2");
                //user создает троян o_тр
                System.out.println(curUser + " cоздает вредоносный файл");
                M = createFile(M, "user", "otr", "o2");
                //user, как создатель otr, дает права на выполнение трояна другому пользователю
                System.out.println(curUser + " выдает права на выполнение вредоносного файла");
                M = giveRightForFile(M, "admin", "otr", "o2");
                //admin выполняет троян, тот забирает права admin'а
                System.out.println(curUser + " выходит из системы.\n" +
                        "Зайдите в систему от пользователя - цели для продолжения");
                curUser = null;
                passed = false;
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                System.out.println(curUser + " запускает вредоносный файл");
                M = launch(M, curUser, "otr");
                if (security) {
                    System.out.println("Проверка файла текущим пользователем\n" +
                            "Худший случай - запуск инициировал admin");
                    check("otr");
                }
                M = copy(M, "str", "o3", "o2");
                // Взлом успешен, лишний пользователь str удален
                M.table("Результат");
                break;
            case 3:
                //admin не имеет прав на выполнение o2,user не может дать на нее права напрямую
                System.out.println("admin не имеет прав на выполнение o2,user не может дать на нее права напрямую");
                M.enterRight("admin", "o2", 'r');
                M.table("Инициализация");
                System.out.println("Введите логин и пароль злоумышленника");
                login = "";
                passed = false;
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                //создание папки и выдача прав
                System.out.println(curUser + " Создает папку o4 и выдает admin'у права на нее");
                createNewFolder(M, "user", "admin", "o4");
                //user создает троян o_тр
                System.out.println(curUser + " cоздает вредоносный файл");
                M = createFile(M, "user", "otr", "o4");
                //user, как создатель otr, дает права на выполнение трояна другому пользователю
                System.out.println(curUser + " выдает права на выполнение вредоносного файла");
                M = giveRightForFile(M, "admin", "otr", "o4");
                //admin выполняет троян, тот забирает права admin'а
                System.out.println(curUser + " выходит из системы.\n" +
                        "Зайдите в систему от пользователя - цели для продолжения");
                curUser = null;
                passed = false;
                while (!passed) {
                    System.out.print("Логин:");
                    login = scan.nextLine();
                    System.out.print("Пароль:");
                    pass = scan.nextLine();
                    passed = logon(login, pass);
                }
                System.out.println(curUser + " запускает вредоносный файл");
                M = launch(M, curUser, "otr");
                if (security) {
                    System.out.println("Проверка файла текущим пользователем\n" +
                            "Худший случай - запуск инициировал admin");
                    check("otr");
                }
                M = copy(M, "str", "o3", "o4");
                // Взлом успешен, лишний пользователь str удален
                M.table("Результат");
                break;

        }
        if (M.objects.containsKey("o'") && M.objects.get("o'").containsKey("user"))
            System.out.println("Атака увенчалась успехом");
        else
            System.out.println("Атака не увенчалась успехом");
    }

    public static AccessMatrix createNewFolder(AccessMatrix M, String owner, String target, String folder) {
        M.addObject(folder);
        M.enterRights(owner, folder, "orwe");
        M.enterRights(target, folder, "rwe");
        return M;
    }

    public static AccessMatrix initialize() {
        AccessMatrix M = new AccessMatrix();
        M.addSubject("admin");
        users.put("admin", "password");
        M.addSubject("user");
        users.put("user", "123456");
        M.addObject("o1");//secret
        M.addObject("o2");//public
        M.addObject("o3");//secret
        M.enterRights("admin", "o1", "orwe");
        M.enterRights("admin", "o3", "orwe");
        M.enterRights("user", "o2", "orwe");
        return M;
    }

    public static AccessMatrix createFile(AccessMatrix M, String subject, String file, String folder) {
        if (M.subjects.get(subject).containsKey(folder))
            if ((M.subjects.get(subject).get(folder) & 4) == 4) {
                M.addObject(file);
                M.enterRights(subject, file, "orwe");
                System.out.println("Операция выполнена успешно");
                return M;
            }
        System.out.println("Операция не выполнена. Недостаточно прав");
        return M;
    }

    public static AccessMatrix giveRightForFile(AccessMatrix M, String subject, String file, String folder) {
        if (M.subjects.get(subject).containsKey(folder))
            if ((M.subjects.get(subject).get(folder) & 6) == 6) {
                M.enterRights(subject, file, "rwe");
                System.out.println("Операция выполнена успешно");
                return M;
            }
        System.out.println("Операция не выполнена. Недостаточно прав");
        return M;
    }

    public static AccessMatrix giveRightForFolder(AccessMatrix M, String owner, String target, String folder) {
        if (M.subjects.get(owner).containsKey(folder))
            if ((M.subjects.get(owner).get(folder) & 8) == 8) {
                M.enterRights(target, folder, "we");
                System.out.println("Операция выполненая успешно");
                return M;
            }
        System.out.println("Ошибка. Недостаточно прав");
        return M;
    }

    public static AccessMatrix launch(AccessMatrix M, String subject, String file) {
        if (security) {
            if (!safe.contains(file)) {
                System.out.println("Файл не является безопасным.\n" +
                        "Обратитесь к администратору для проверки файла");
                return M;
            }
        }
        if (file.equals("otr")) {
            if (M.subjects.get(subject).containsKey(file))
                if ((M.subjects.get(subject).get(file) & 7) == 7) {
                    M.addSubject("str");
                    for (String objs : M.subjects.get("user").keySet())
                        M.enterRights("str", objs, "rwe");
                    for (String obj : M.subjects.get(subject).keySet())
                        if ((M.subjects.get(subject).get(obj) & 15) == 15)
                            M.enterRights("str", obj, "rwe");
                    System.out.println("Вредоносная программа выполнена. Секретные файлы скомпрометированы");
                    return M;
                }
            System.out.println("Выполнение вредоносной программы не выполнено. Недостаточно прав");
            return M;
        } else {
            System.out.println("Выполнена обычная программа");
            return M;
        }
    }

    public static AccessMatrix copy(AccessMatrix M, String subject, String file, String destFolder) {
        if (!M.subjects.containsKey(subject))
        {
            System.out.println("Вредоносный файл не был запущен. Ошибка");
            return M;
        }
        if (M.subjects.get(subject).containsKey(file) && M.subjects.get(subject).containsKey(destFolder)) {
            int fileRights = M.subjects.get(subject).get(file);
            int folderRights = M.subjects.get(subject).get(destFolder);
            if (((fileRights & 4) == 4) && ((folderRights & 2) == 2)) {
                M.addObject("o'");
                M.enterRights(subject, "o'", "orwe");
                M.enterRight("user", "o'", 'r');
            }
        }
        M.destroySubject("str");
        System.out.println("Копирование секретных файлов, удаление следов вредоносной программы");
        return M;
    }
}

