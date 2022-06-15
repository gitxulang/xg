package iie.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xczhao
 * @date 2020/10/25 - 17:16
 */
public class NmsUser implements Serializable {

    private Integer id;
    private String name;
    private String card;
    private NmsDepartment nmsDepartment;
    private String education;
    private String nationality;
    private String sex;
    private String birthDate;
    private Integer deled;
    private String createtime;
    private Timestamp itime;

    private String reserver1;
    private String reserver2;

    public NmsUser() {
    }

    public NmsUser(Integer deled, Timestamp itime) {
        this.deled = deled;
        this.itime = itime;
    }

    public NmsUser(String name, String card, NmsDepartment nmsDepartment, String education, String nationality, String sex, String birthDate, Integer deled, String createtime, Timestamp itime, String reserver1, String reserver2) {
        this.name = name;
        this.card = card;
        this.nmsDepartment = nmsDepartment;
        this.education = education;
        this.nationality = nationality;
        this.sex = sex;
        this.birthDate = birthDate;
        this.deled = deled;
        this.createtime = createtime;
        this.itime = itime;
        this.reserver1 = reserver1;
        this.reserver2 = reserver2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public NmsDepartment getNmsDepartment() {
        return nmsDepartment;
    }

    public void setNmsDepartment(NmsDepartment nmsDepartment) {
        this.nmsDepartment = nmsDepartment;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getDeled() {
        return deled;
    }

    public void setDeled(Integer deled) {
        this.deled = deled;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Timestamp getItime() {
        return itime;
    }

    public void setItime(Timestamp itime) {
        this.itime = itime;
    }

    public String getReserver1() {
        return reserver1;
    }

    public void setReserver1(String reserver1) {
        this.reserver1 = reserver1;
    }

    public String getReserver2() {
        return reserver2;
    }

    public void setReserver2(String reserver2) {
        this.reserver2 = reserver2;
    }
}
