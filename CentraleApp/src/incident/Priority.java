package incident;

/**
 * Enums be cray.
 * Created by Etienne on 16-6-2015.
 */
public class Priority {
    public static final int UNSET = 0; // Default value for incidents.
    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int HIGH = 3;

    public static int compare(Incident x, Incident y){
        return Integer.compare(x.getPriority(), y.getPriority());
    }
}
