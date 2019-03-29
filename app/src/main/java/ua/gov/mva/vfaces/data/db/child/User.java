package ua.gov.mva.vfaces.data.db.child;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.database.DataSnapshot#getValue(Class)}.
 *
 * @see ua.gov.mva.vfaces.presentation.ui.auth.signin.SignInViewModel#getUser(String) method.
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String work;

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public User() {
    }

    public User(String id, String name, String email, String phone, String work) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.work = work;
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
}