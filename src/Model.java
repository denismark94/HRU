/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 09.10.15
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class Model {
    public static void InitializeThreatScenario(int number) {
        AccessMatrix M = initialize();
        for (String s : M.subjects.keySet())
            M.showSubjectsRights(s);
        System.out.println("Обработка");
        switch (number) {
            case 1:
                //user создает троян o_тр
                M = createFile(M, "user", "otr", "o2");
                //user, как создатель otr, дает права на выполнение трояна другому пользователю
                M = giveRight(M, "admin", "otr", "o2");
                //admin выполняет троян, тот забирает права admin'а
                M = launch(M, "admin", "otr");
                //Троян копирует o3 в o2
                M = copy(M, "str", "o3", "o2");
                // Взлом успешен, лишний пользователь str удален
                for (String s : M.subjects.keySet())
                    M.showSubjectsRights(s);
                break;
        }
    }

    public static AccessMatrix initialize() {
        AccessMatrix M = new AccessMatrix();
        M.addSubject("admin");
        M.addSubject("user");
        M.addObject("o1");//secret
        M.addObject("o2");//public
        M.addObject("o3");//secret
        M.enterRights("admin", "o1", "orwe");
        M.enterRights("admin", "o2", "rwe");
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

    public static AccessMatrix giveRight(AccessMatrix M, String subject, String file, String folder) {
        if (M.subjects.get(subject).containsKey(folder))
            if ((M.subjects.get(subject).get(folder) & 6) == 6) {
                M.enterRights(subject, file, "rwe");
                System.out.println("Операция выполнена успешно");
                return M;
            }
        System.out.println("Операция не выполнена. Недостаточно прав");
        return M;
    }

    public static AccessMatrix launch(AccessMatrix M, String subject, String file) {
        if (file.equals("otr")) {
            if (M.subjects.get(subject).containsKey(file))
                if ((M.subjects.get(subject).get(file) & 7) == 7) {
                    M.addSubject("str");
                    M.enterRights("str", "o2", "rwe");
                    M.enterRights("str", "otr", "rwe");
                    for (String obj : M.subjects.get(subject).keySet())
                        if ((M.subjects.get(subject).get(obj) & 15) == 15)
                            M.enterRights("str", obj, "rwe");
                    System.out.println("Вредоносная программа выполнена");
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
        return M;
    }
}

