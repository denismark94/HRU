import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;

public class AccessMatrix {
    HashMap<String, HashMap<String, Integer>> subjects;
    HashMap<String, HashMap<String, Integer>> objects;

    //r - read
    //w - write
    //o - own
    //d - deny all
    public AccessMatrix() {
        this.subjects = new HashMap<String, HashMap<String, Integer>>();
        this.objects = new HashMap<String, HashMap<String, Integer>>();
    }

    public void addSubject(String name) {
        subjects.put(name, new HashMap<String, Integer>());
    }

    public void addObject(String name) {
        objects.put(name, new HashMap<String, Integer>());
    }

    public void destroySubject(String subjName) {
        HashMap<String, Integer> objs = subjects.get(subjName);
        for (String objName : objs.keySet())
            objects.get(objName).remove(subjName);
        subjects.remove(subjName);
    }

    public void destroyObject(String objName) {
        HashMap<String, Integer> subjs = objects.get(objName);
        for (String subjName : subjs.keySet())
            subjects.get(subjName).remove(objName);
        objects.remove(objName);
    }

    public void enterRight(String subject, String object, char right) {
        int marker = convert(right);
        if (subjects.get(subject).containsKey(object)) {
            marker = marker | subjects.get(subject).remove(object);
            subjects.get(subject).put(object, marker);
            objects.get(object).replace(subject, marker);
        } else {
            subjects.get(subject).put(object, marker);
            objects.get(object).put(subject, marker);
        }
    }

    public boolean deleteRight(String subject, String object, char right) {
        int marker = convert(right);
        if (subjects.get(subject).containsKey(object)) {
            int curMarker = subjects.get(subject).get(object);
            if ((marker & curMarker) == marker) {
                curMarker &= (~marker & 15);
                subjects.get(subject).replace(object, curMarker);
                objects.get(object).replace(subject, curMarker);
                return true;
            }
        }
        return false;
    }

    public void showSubjectsRights(String subject) {
        System.out.println("Субъект: " + subject);
        HashMap<String, Integer> objs = subjects.get(subject);
        for (String object : objs.keySet())
            System.out.println(" Права: " + convert(objs.get(object)) + " Объект: " + object);
    }

    public void showObjectsRights(String object) {
        System.out.println("Объект: " + object);
        HashMap<String, Integer> subjs = objects.get(object);
        for (String subject : subjs.keySet())
            System.out.println(" Права: " + convert(subjs.get(subject)) + " Субъект: " + subject);
    }

    public void enterRights(String subject, String object, String rights) {
        char[] r = rights.toLowerCase().toCharArray();
        for (int i = 0; i < r.length; i++)
            enterRight(subject, object, r[i]);
    }

    public void deleteRights(String subject, String object, String rights) {
        char[] r = rights.toLowerCase().toCharArray();
        for (int i = 0; i < r.length; i++)
            deleteRight(subject, object, r[i]);
    }

    public int convert(char access) {
        switch (access) {
            case 'o':
                return 8;
            case 'r':
                return 4;
            case 'w':
                return 2;
            case 'e':
                return 1;

        }
        return 0;
    }

    public String convert(int marker) {
        String result = "";
        if ((marker & 8) == 8)
            result += "o";
        if ((marker & 4) == 4)
            result += "r";
        if ((marker & 2) == 2)
            result += "w";
        if ((marker & 1) == 1)
            result += "e";
        return result;
    }

    public void printTable() {
        System.out.print("|\t\t|");
        for (String o : objects.keySet())
            System.out.print("\t" + o + "\t|");
        System.out.println();
        for (String s : subjects.keySet()) {
            System.out.print("|" + s + "\t|");
            for (String o : objects.keySet())
                if (subjects.get(s).containsKey(o))
                    System.out.print("" + convert(subjects.get(s).get(o)) + "\t|");
                else
                    System.out.print("\t-\t|");
            System.out.println();
        }
    }

    public void table(String tableName) {
        String[] columns = new String[objects.size() + 1];
        int i = 0,
                j = 0;
        columns[i++] = "subj\\obj";
        for (String o : objects.keySet())
            columns[i++] = o;
        String[][] data = new String[subjects.size()][columns.length];

        i = 0;
        for (String s : subjects.keySet()) {
            j = 0;
            data[i][j++] = s;
            for (String o: objects.keySet())
                if (subjects.get(s).containsKey(o))
                    data[i][j++] = convert(subjects.get(s).get(o));
            else
                    data[i][j++] = "-";
            i++;
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                TestFrame.createGUI(columns,data, tableName);
            }
        });
    }
}
