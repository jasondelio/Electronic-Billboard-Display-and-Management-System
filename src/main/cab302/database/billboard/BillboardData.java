package cab302.database.billboard;

import javax.swing.*;

/**
 * This version uses an BillboardDataSource and its methods to retrieve data
 */
public class BillboardData {
    DefaultListModel listModel;
    BillboardDataSource billboardData;

    /**
     * Constructor initializes the list model that holds billboard names as Strings and
     * attempts to read any data saved from previous invocations of the
     * application.
     */
    public BillboardData() {
        listModel = new DefaultListModel();
        billboardData = new BillboardDataSource();

        // add the retrieved data to the list model
        for (String name : billboardData.nameSet()) {
            if(!name.equals("NotScheduled")) {
                listModel.addElement(name);
            }
        }
    }

    /**
     * Adds only the billboard with name "NotScheduled" to the billboards table.
     *
     * @param b Billboard to add to billboards table.
     */
    public void addNotScheduledBoard(BillboardInfo b) {

        // check if starting up of billboard database or not with
        // using get size
        if (billboardData.getSize() == 0){
            billboardData.addNotScheduledBillboard(b);
        }
    }
    /**
     * Adds a billboard to the billboards table.
     *
     * @param b Billboard to add to billboards table.
     */
    public void add(BillboardInfo b) {

        // check to see if the billboard is already in the list model
        // if not add to the billboard table and the list model
        if (!listModel.contains(b.getName())) {
            listModel.addElement(b.getName());
            billboardData.addBillboard(b);
        }
    }

    /**
     * Based on the name of the billboard in the billboards table, delete the billboard.
     *
     * @param key the billboard's name
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        billboardData.deleteBillboard((String) key);
    }

    /**
     * Retrieves billboard details from the model.
     *
     * @param key the billboard's name to retrieve.
     * @return the billboard object related to the name.
     */
    public BillboardInfo get(Object key) {
        return billboardData.getBillboard((String) key);
    }

    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     * @return the number of billboards in the billboards table.
     */
    public int getSize() {
        return billboardData.getSize();
    }

    /**
     * Edit the billboard's information in the billboards table and edit the billboard's name from list model
     * if the billboard's name is edited
     *
     * @param name         the billboard's name
     * @param xmlContent   the billboard's xml content
     * @param previousName the billboard's name before edited
     */
    public void edit(String name, String xmlContent, String previousName) {
        billboardData.editBillboard(name, xmlContent, previousName);
        if (previousName != name) {
            listModel.removeElement(previousName);
            listModel.addElement(name);
        }

    }

}
