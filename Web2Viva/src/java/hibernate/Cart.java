/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "gift_box_boxId")
    private GiftBox giftBox;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "added_at")
    private Date added_at;
    
    @Column(name = "status",nullable = false)
    private int status;
    
    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Date added_at) {
        this.added_at = added_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftBox getGiftBox() {
        return giftBox;
    }
    public void setGiftBox(GiftBox giftBox) {
        this.giftBox = giftBox;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
