package com.project.montap.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORY_ITEM_IDX")
    Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_IDX")
    @JsonBackReference
    User user;

    @ManyToOne
    @JoinColumn(name = "ITEM_IDX")
    Item item;

    @Column(columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    int equipYn = 0; // 아이템 장착 여부 : 0(미장착), 1(장착)

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",name = "DROP_DATE")
    LocalDateTime dropDate; // 아이템 획득 Log

    public InventoryItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }

}
