package com.projekt.Spring_Boot_API.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "folders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    @Column (name = "folder_id")
    private UUID folderId;

    @Column (name = "folder_name", nullable = false)
    private String folderName;

    @ManyToOne
    @JoinColumn (name = "parent_folder_id")
    @JsonBackReference
    private Folder parentFolder;

    @OneToMany(
            mappedBy = "parentFolder",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Folder> subFolders;

    @OneToMany (mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    @ManyToOne
    @JoinColumn (name = "owner_id")
    @JsonBackReference
    private User user;

    public Folder(String name, Folder parentFolder, User owner) {
        this.folderName = name;
        this.parentFolder = parentFolder;
        this.user = owner;
    }
}
