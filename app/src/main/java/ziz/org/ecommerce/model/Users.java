package ziz.org.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Users {

    private String name;
    private String phone;
    private String password;
    private String image;
    private String addresse;

    public Users(String name, String phone, String image, String addresse) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.addresse = addresse;
    }

    public Users(String name, String phone, String addresse) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.addresse = addresse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }
}
