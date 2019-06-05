package ua.gov.mva.vfaces.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.firebase.database.Exclude;
import ua.gov.mva.vfaces.data.entity.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block implements Parcelable {

    @Exclude
    private static final String TAG = "Block";

    private String id;
    private String title;
    private List<Item> items;

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public Block() {
    }

    public Block(String id, String title, List<Item> items) {
        this.id = id;
        this.title = title;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Exclude
    public boolean isCompleted() {
        if (items == null) {
            Log.e(TAG, "items == null");
            return false;
        }
        for (Item i : items) {
            if (i.isNotAnswered()) {
                return false;
            }
        }
        return true;
    }

    @Exclude
    public boolean isNotCompleted() {
        if (items == null) {
            Log.e(TAG, "items == null");
            return false;
        }
        for (Item i : items) {
            if (i.isNotAnswered()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if Block contains items with specified type.
     * @param type - type of block.
     * @param position - start position in the loop to iterate from.
     *
     * @return - true if Block contains more items, false - if Block doesn't.
     */
    @Exclude
    public boolean hasMoreItemsOfType(BlockType type, int position) {
        if (items == null) {
            Log.e(TAG, "items == null");
            return false;
        }
        if (position < 0 || position > items.size() - 1) {
            Log.d(TAG, "Wrong position. position = $position");
            return false;
        }
        // Start from specified position
        for (int i = position; i <= items.size() - 1; i++) {
            Item item = items.get(i);
            if (item.getType() == type) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return Objects.equals(getId(), block.getId()) &&
                Objects.equals(getTitle(), block.getTitle()) &&
                Objects.equals(getItems(), block.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getItems());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeList(this.items);
    }

    protected Block(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.items = new ArrayList<Item>();
        in.readList(this.items, Item.class.getClassLoader());
    }

    public static final Parcelable.Creator<Block> CREATOR = new Parcelable.Creator<Block>() {
        @Override
        public Block createFromParcel(Parcel source) {
            return new Block(source);
        }

        @Override
        public Block[] newArray(int size) {
            return new Block[size];
        }
    };
}