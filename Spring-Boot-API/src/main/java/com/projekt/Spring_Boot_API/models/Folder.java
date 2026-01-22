package com.projekt.Spring_Boot_API.models;

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
    private UUID folder_id;

    @Column (nullable = false)
    private String folder_name;

    @ManyToOne
    @JoinColumn (name = "parent_folder_id")
    private Folder parent_folder;

    @OneToMany (mappedBy = "parent_folder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Folder> sub_folders;

    @OneToMany (mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
}
