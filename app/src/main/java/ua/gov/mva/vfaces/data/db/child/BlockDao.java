package ua.gov.mva.vfaces.data.db.child;

import java.util.List;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.database.DataSnapshot#getValue(Class)}.
 */
public class BlockDao {

    private String id;
    private String title;
    private List<ItemDao> items;

    public BlockDao(String id, String title, List<ItemDao> items) {
        this.id = id;
        this.title = title;
        this.items = items;
    }

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public BlockDao() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ItemDao> getItems() {
        return items;
    }
}
