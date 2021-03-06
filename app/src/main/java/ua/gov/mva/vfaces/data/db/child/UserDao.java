package ua.gov.mva.vfaces.data.db.child;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.database.DataSnapshot#getValue(Class)}.
 *
 * @see ua.gov.mva.vfaces.presentation.ui.auth.signin.SignInViewModel#getUser(String) method.
 */
public class UserDao {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String work;
    private int selectedPosition;

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public UserDao() {
    }

    public UserDao(String id, String name, String email, String phone, String work, int selectedPosition) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.work = work;
        this.selectedPosition = selectedPosition;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWork() {
        return work;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}