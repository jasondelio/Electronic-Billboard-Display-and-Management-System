package cab302.database.billboard;

import java.util.Set;

public interface BillboardSources {

    /**
     */
    void addBillboard(BillboardInfo b);

    /**
     */
    BillboardInfo getBillboard(String name);


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