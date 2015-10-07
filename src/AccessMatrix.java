import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 07.10.15
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class AccessMatrix {
    HashMap<String,HashMap<String,Character>> matrix;
    HashMap<String, Character> objects;
    //r - read
    //w - write
    //o - own
    //d - deny all
    public AccessMatrix() {
        this.matrix = new HashMap<String, HashMap<String, Character>>();
    }

    public void addSubject(String name) {
        matrix.put(name, objects);
    }

    public void addObject (String name) {

    }
}
