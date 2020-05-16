package cab302.controlpanel.data;

import cab302.controlpanel.dataobjects.Billboard;
import cab302.controlpanel.datasources.BillboardDataSource;

import javax.swing.*;

public class BillboardData {
    DefaultListModel listModel;

    BillboardDataSource billboardData;

    /**
     * Constructor initializes the list model that holds names as Strings and
     * attempts to read any data saved from previous invocations of the
     * application.
     *
     */
    public BillboardData() {
        listModel = new DefaultListModel();
        /* BEGIN MISSING CODE */
        billboardData = new BillboardDataSource();
        /* END MISSING CODE */

        // add the retrieved data to the list model
        for (String name : billboardData.nameSet()) {
            listModel.addElement(name);
        }
    }

    /**
     * Adds a person to the address book.
     *
     * @param u A User to add to the address book.
     */
    public void add(Billboard b) {

        // check to see if the person is already in the book
        // if not add to the address book and the list model
        if (!listModel.contains(b.getName())) {
            listModel.addElement(b.getName());
            billboardData.addBillboard(b);
        }
    }

    /**
     * Based on the name of the person in the address book, delete the person.
     *
     * @param key
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        billboardData.deleteBillboard((String) key);
    }

    /**
     * Saves the data in the address book using a persistence
     * mechanism.
     */
    public void persist() { billboardData.close();
    }

    /**
     * Retrieves Person details from the model.
     *
     * @param key the name to retrieve.
     * @return the Person object related to the name.
     */
    public Billboard get(Object key) {
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
     * @return the number of names in the Address Book.
     */
    public int getSize() {
        return billboardData.getSize();
    }

    public void edit(String name, String xmlContent, String previousName)
    {
        billboardData.editBillboard(name, xmlContent, previousName);
        if(previousName != name) {
            listModel.removeElement(previousName);
            listModel.addElement(name);
        }

    }

}
