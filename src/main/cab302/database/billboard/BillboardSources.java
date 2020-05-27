package cab302.database.billboard;

import java.util.Set;

public interface BillboardSources {

    /**
     * Add a billboard to the billboards table in database
     *
     * @param b Billboard to add
     */
    void addBillboard(BillboardInfo b);

    /**
     * Extracts all the details of a billboard from the billboards table based on the
     * name passed in.
     *
     * @param name the billboard's name which wants to be extracted
     * @return
     */
    BillboardInfo getBillboard(String name);


    /**
     */
    int getSize();

    /**
     * Deletes a billboard from the billboards table
     *
     * @param name the billboard's name to delete from billboards table
     */
    void deleteBillboard(String name);

    /**
     * Edit a billboard from the billboard table
     *
     * @param name         billboard's name after edited
     * @param XMLContent   billboard's name after edited
     * @param previousName billboard's name before edited
     */
    void editBillboard(String name, String XMLContent, String previousName);

    /**
     */
    void close();

    /**
     * Retrieves a set of billboard names from the data source that are used in
     * the billboard name list.
     *
     * @return set of billboard names.
     */
    Set<String> nameSet();

}