package com.project.montap.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Equipment {
    @Id
    @GeneratedValue
    @Column(name = "EQUIPMENT_IDX")
    Long idx;

    @ManyToOne
    @JoinColumn(name = "USER_IDX")
    User user;

    @ManyToOne
    @JoinColumn(name = "ITEM_IDX")
    Item item;

    public Equipment(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
