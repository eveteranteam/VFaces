package ua.gov.mva.vfaces.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;
import ua.gov.mva.vfaces.data.entity.BlockType;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {

    @Exclude
    public static final String TAG = "Item";

    private BlockType type;
    private String name;

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

    public Item(BlockType type, String name, List<String> choices) {
        this.type = type;
        this.name = name;
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
    public boolean isNotAnswered(){
        return answers.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.name);
        dest.writeStringList(this.choices);
        dest.writeStringList(this.answers);
    }

    protected Item(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : BlockType.values()[tmpType];
        this.name = in.readString();
        this.choices = in.createStringArrayList();
        this.answers = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
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
