package ua.gov.mva.vfaces.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;
import ua.gov.mva.vfaces.data.entity.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Item implements Parcelable {

    @Exclude
    public static final String TAG = "Item";

    private BlockType type;
    private String name;
    private boolean isOptional;
    private boolean otherChoice;

    @NonNull
    private List<String> choices = new ArrayList<>();
    @NonNull
    private List<String> answers = new ArrayList<>();

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public Item() {
    }

    public Item(BlockType type, String name, boolean otherChoice, boolean isOptional, @NonNull List<String> choices,
                @NonNull List<String> answers) {
        this.type = type;
        this.name = name;
        this.otherChoice = otherChoice;
        this.isOptional = isOptional;
        this.choices = choices;
        this.answers = answers;
    }

    public Item(BlockType type, String name, boolean isOptional, @NonNull List<String> choices, boolean otherChoice) {
        this.type = type;
        this.name = name;
        this.isOptional = isOptional;
        this.choices = choices;
        this.otherChoice = otherChoice;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(@NonNull List<String> choices) {
        this.choices = choices;
    }

    @NonNull
    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(@NonNull List<String> answers) {
        this.answers = answers;
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

    @Exclude
    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean isOtherChoice() {
        return otherChoice;
    }

    public void setOtherChoice(boolean otherChoice) {
        this.otherChoice = otherChoice;
    }

    @Exclude
    public boolean isOptionalSelected() {
        return isOptional && !answers.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return isOptional() == item.isOptional() &&
                isOtherChoice() == item.isOtherChoice() &&
                getType() == item.getType() &&
                Objects.equals(getName(), item.getName()) &&
                Objects.equals(getChoices(), item.getChoices()) &&
                Objects.equals(getAnswers(), item.getAnswers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getName(), isOptional(), isOtherChoice(), getChoices(), getAnswers());
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
        dest.writeByte(this.otherChoice ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.choices);
        dest.writeStringList(this.answers);
    }

    protected Item(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : BlockType.values()[tmpType];
        this.name = in.readString();
        this.isOptional = in.readByte() != 0;
        this.otherChoice = in.readByte() != 0;
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
