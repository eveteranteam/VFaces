package ua.gov.mva.vfaces.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire implements Parcelable {

    private static final String TAG = "Questionnaire";

    private String key;
    private String id;
    private String userId;
    private List<Block> blocks = new ArrayList<>();
    private String name;
    private String settlement;
    private int progress;
    private long lastEditTime = 0;
    private boolean isRefusedToAnswer;
    private boolean isVeteranAbsent;

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public Questionnaire() {
    }

    public Questionnaire(String key, String id, String userId, List<Block> blocks, String name, String settlement,
                         int progress, long lastEditTime, boolean isRefusedToAnswer, boolean isVeteranAbsent) {
        this.key = key;
        this.id = id;
        this.userId = userId;
        this.blocks = blocks;
        this.name = name;
        this.settlement = settlement;
        this.progress = progress;
        this.lastEditTime = lastEditTime;
        this.isRefusedToAnswer = isRefusedToAnswer;
        this.isVeteranAbsent = isVeteranAbsent;
    }

    public Questionnaire(String id, List<Block> blocks) {
        this.id = id;
        this.blocks = blocks;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void setBlockAt(Block block, int position) {
        if (blocks == null) {
            return;
        }
        blocks.set(position, block);
        calculateProgress();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public boolean isRefusedToAnswer() {
        return isRefusedToAnswer;
    }

    public void setRefusedToAnswer(boolean refusedToAnswer) {
        this.isRefusedToAnswer = refusedToAnswer;
    }

    public boolean isVeteranAbsent() {
        return isVeteranAbsent;
    }

    public void setVeteranAbsent(boolean veteranAbsent) {
        isVeteranAbsent = veteranAbsent;
    }

    @Exclude
    public boolean isFinished() {
        if (blocks == null) {
            Log.e(TAG, "blocks == null");
            return false;
        }
        for (Block b : blocks) {
            if (b.isNotCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Exclude
    public boolean isNotFinished() {
        if (blocks == null) {
            Log.e(TAG, "blocks == null");
            return false;
        }
        for (Block b : blocks) {
            if (b.isNotCompleted()) {
                return true;
            }
        }
        return false;
    }

    @Exclude
    private int calculateProgress() {
        int result;
        if (blocks == null) {
            Log.e(TAG, "blocks == null");
            return 0;
        }
        int completedCount = 0;
        for (Block b : blocks) {
            if (b.isCompleted()) {
                completedCount++;
            }
        }
        result = completedCount * 100 / blocks.size();
        Log.d(TAG, "progress == " + result);
        progress = result;
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeTypedList(this.blocks);
        dest.writeString(this.name);
        dest.writeString(this.settlement);
        dest.writeInt(this.progress);
        dest.writeLong(this.lastEditTime);
        dest.writeByte(this.isRefusedToAnswer ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVeteranAbsent ? (byte) 1 : (byte) 0);
    }

    protected Questionnaire(Parcel in) {
        this.key = in.readString();
        this.id = in.readString();
        this.userId = in.readString();
        this.blocks = in.createTypedArrayList(Block.CREATOR);
        this.name = in.readString();
        this.settlement = in.readString();
        this.progress = in.readInt();
        this.lastEditTime = in.readLong();
        this.isRefusedToAnswer = in.readByte() != 0;
        this.isVeteranAbsent = in.readByte() != 0;
    }

    public static final Creator<Questionnaire> CREATOR = new Creator<Questionnaire>() {
        @Override
        public Questionnaire createFromParcel(Parcel source) {
            return new Questionnaire(source);
        }

        @Override
        public Questionnaire[] newArray(int size) {
            return new Questionnaire[size];
        }
    };
}
