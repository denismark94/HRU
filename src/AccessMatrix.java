import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 07.10.15
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class AccessMatrix {
    HashMap<String,HashMap<String,Integer>> subjects;
    HashMap<String, HashMap<String,Integer>> objects;
    //r - read
    //w - write
    //o - own
    //d - deny all
    public AccessMatrix() {
        this.subjects = new HashMap<String, HashMap<String, Integer>>();
        this.objects = new HashMap<String, HashMap<String, Integer>>();
    }

    public void addSubject(String name) {
        subjects.put(name,new HashMap<String,Integer>());
    }

    public void addObject (String name) {
        objects.put(name,new HashMap<String,Integer>());
    }

    public void destroySubject (String subjName) {
        HashMap<String, Integer> objs = subjects.get(subjName);
        for (String objName: objs.keySet())
            objects.get(objName).remove(subjName);
    }

    public void destroyObject (String objName) {
        HashMap<String, Integer> subjs = objects.get(objName);
        for (String subjName: subjs.keySet())
            subjects.get(subjName).remove(objName);
    }

    public void enterRight (String subject, String object, char right) {
        int marker = convert(right);
        if (subjects.get(subject).containsKey(object))
        {
            marker = marker | subjects.get(subject).remove(object);
            subjects.get(subject).put(object, marker);
            objects.get(object).replace(subject,marker);
        }
        else
        {
            subjects.get(subject).put(object,marker);
            objects.get(object).put(subject,marker);
        }
    }

    public boolean deleteRight (String subject, String object, char right) {
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

    public void showSubjectsRights (String subject) {
        System.out.println("Субъект: "+ subject);
        HashMap<String,Integer> objs = subjects.get(subject);
        for (String object: objs.keySet())
            System.out.println(" Права: " + convert(objs.get(object))+" Объект: " + object);
    }

    public void showObjectsRights (String object) {
        System.out.println("Объект: "+ object);
        HashMap<String,Integer> subjs = objects.get(object);
        for (String subject: subjs.keySet())
            System.out.println(" Права: " + convert(subjs.get(subject))+" Субъект: " + subject);
    }

    public int convert(char access){
        switch (access) {
            case 'r':
                return 8;
            case 'w':
                return 4;
            case 'x':
                return 2;
            case 'o':
                return 1;
        }
        return 0;
    }
    public String convert(int marker){
        String result = "";
        if ((marker & 8) == 8)
                result+="r";
        if ((marker & 4) == 4)
            result+="w";
        if ((marker & 2) == 2)
            result+="x";
        if ((marker & 1) == 1)
            result+="o";
        return result;
    }
}
