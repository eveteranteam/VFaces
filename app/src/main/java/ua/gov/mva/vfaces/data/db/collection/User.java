package ua.gov.mva.vfaces.data.db.collection;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.firestore.FirebaseFirestore}.
 * @see ua.gov.mva.vfaces.presentation.ui.auth.signin.SignInViewModel#getUser(String) method.
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String work;

    /**
     * Required by {@link com.google.firebase.firestore.FirebaseFirestore} empty constructor.
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