import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.Paths;
import org.junit.Test;

/**
 * User: Administrator
 * Date: 11-4-26 Time: 下午9:07
 *
 * @author Basten Gao
 */
public class PathsTest {

    @Test
    public void test() {
        String path = "/a/b/c.java";

        String[] names = Paths.breaks(path);
        for (int i = 0; i < names.length; i++) {
            System.out.printf("%5s:%-5s%n", names[i], i);
        }
    }
}
