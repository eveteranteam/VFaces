package ua.gov.mva.vfaces.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import ua.gov.mva.vfaces.data.entity.BlockType;

public class Item implements Parcelable {

    @Exclude
    public static final String TAG = "Item";

    private BlockType type;
    private String name;
    private boolean isOptional;

    @NonNull
    private List<String> choices = new ArrayList<>();
    @NonNull
    private List<String> answers = new ArrayList<>();

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public Item() {
    }

    public Item(BlockType type, String name, @NonNull List<String> choices, @NonNull List<String> answers) {
        this.type = type;
        this.name = name;
        this.choices = choices;
        this.answers = answers;
    }

    public Item(BlockType type, String name, boolean isOptional, @NonNull List<String> choices) {
        this.type = type;
        this.name = name;
        this.isOptional = isOptional;
        this.choices = choices;
    }

    public BlockType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Exclude
    public boolean isAnswered() {
        boolean result = !answers.isEmpty();
        Log.d(TAG, "!answers.isEmpty() == " + result);
        return result;
    }

    @Exclude
    public boolean isNotAnswered() {
        if (isOptional) {
            return false;
        }
        return answers.isEmpty();
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isOptionalSelected() {
        return isOptional && !answers.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.name);
        dest.writeByte(this.isOptional ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.choices);
        dest.writeStringList(this.answers);
    }

    protected Item(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : BlockType.values()[tmpType];
        this.name = in.readString();
        this.isOptional = in.readByte() != 0;
        this.choices = in.createStringArrayList();
        this.answers = in.createStringArrayList();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
