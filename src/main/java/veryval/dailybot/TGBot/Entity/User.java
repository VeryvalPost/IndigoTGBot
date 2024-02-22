package veryval.dailybot.TGBot.Entity;


import lombok.Data;


import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user")

public class User {

    @Id
    private Long chatId;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private String telephone;
    @Column
    private Language language;
    @Column
    private Purpose purpose;
    @Column
    private Date datetime;

    public Boolean getStud_already() {
        return stud_already;
    }

    public void setStud_already(Boolean stud_already) {
        this.stud_already = stud_already;
    }

    @Column
    private Boolean stud_already;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
