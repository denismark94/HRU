import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        AccessMatrix M = new AccessMatrix();
        M.addSubject("Alice");
        M.addSubject("Bob");
        M.addObject("Afolder");
        M.addObject("Bfolder");
        M.enterRight("Alice","Afolder",'o');
        M.enterRight("Alice","Bfolder",'r');
        M.enterRight("Bob","Bfolder",'o');
        M.enterRight("Bob","Afolder",'r');
        M.showSubjectsRights("Alice");
        M.showSubjectsRights("Alice");
    }
}
