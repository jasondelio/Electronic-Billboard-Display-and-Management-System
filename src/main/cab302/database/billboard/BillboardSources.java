package cab302.database.billboard;

import java.util.*;

public interface BillboardSources {
    /**
     */
    void createBillboard(BillboardInfo b);

    /**
     */
    BillboardInfo getBillboard(String title);

    /**
     */
    int getSize();

    /**
     */
    void deleteBillboard(String title);

    /**
     *
     * @return
     */
    BillboardInfo editBillboard(String title);

    /**
     */
    void close();

    /**
     */
    Set<String> titleSet();
}
