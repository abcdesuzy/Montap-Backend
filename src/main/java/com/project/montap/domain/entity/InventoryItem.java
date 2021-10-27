package com.project.montap.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class InventoryItem {

    @Id
    @GeneratedValue
    @Column(name = "INVENTORY_ITEM_IDX")
    Long idx;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime dropDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_IDX")
    @JsonBackReference
    User user;

    @ManyToOne
    @JoinColumn(name = "ITEM_IDX")
    Item item;

    public InventoryItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
