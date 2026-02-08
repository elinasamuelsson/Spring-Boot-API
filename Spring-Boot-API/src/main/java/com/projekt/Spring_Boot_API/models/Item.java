package com.projekt.Spring_Boot_API.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_contents", nullable = false)
    private byte[] itemContents;

    @Column(name = "item_size", nullable = false)
    private int itemSizeBytes;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private User user;

    public Item(String itemName, byte[] itemContents, int itemSizeBytes, Folder folder, User user) {
        this.itemName = itemName;
        this.itemContents = itemContents;
        this.itemSizeBytes = itemSizeBytes;
        this.folder = folder;
        this.user = user;
    }
}
