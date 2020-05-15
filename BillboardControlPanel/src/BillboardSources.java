import java.sql.SQLException;
import java.util.*;

public interface BillboardSources {

    /**
     */
    void addBillboard(Billboard b);

    /**
     */
    Billboard getBillboard(String name);


    /**
     */
    int getSize();

    /**
     */
    void deleteBillboard(String name);

    /**
     *
     * @return
     */
    void editBillboard(String name, String XMLContent, String previousName);

    /**
     */
    void close();

    /**
     */
    Set<String> nameSet();

}