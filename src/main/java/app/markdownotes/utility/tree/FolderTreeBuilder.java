package app.markdownotes.utility.tree;

import app.markdownotes.data.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains static methods to construct a tree that represents a folder structure.
 * The String path attribute of a Folder record is in the format "parent.child.grandchild".
 */
public class FolderTreeBuilder {

    /**
     * Converts a list of Folder records into a list of FolderTree records that represent roots
     * containing a list of its children.
     *
     * @param folders Folder records mapped from database
     * @return List of root FolderTree records
     */
    public static List<app.markdownotes.data.FolderNode> buildFolderTree(List<Folder> folders) {
        Map<String, FolderNode> lookup = folders.stream()
                .collect(Collectors.toMap(Folder::path, FolderTreeBuilder.FolderNode::new));

        lookup.values().forEach(folderNode -> {
            int lastDotIndex = folderNode.path.lastIndexOf('.');
            if (lastDotIndex > 0) {
                FolderNode parentFolder = lookup.get(folderNode.path.substring(0, lastDotIndex));
                if (parentFolder != null) {
                    parentFolder.children.add(folderNode);
                }
            }
        });

        return lookup.values().stream()
                .filter(node -> {
                    int lastDotIndex = node.path.lastIndexOf('.');
                    return lastDotIndex >= 0 || !lookup.containsKey(node.path.substring(0, lastDotIndex));
                })
                .map(FolderTreeBuilder.FolderNode::toFolderTree)
                .collect(Collectors.toList());
    }

    /**
     * Private entity used only to build the tree.
     */
    private static class FolderNode {
        long id;
        UUID accountId;
        String name;
        String path;
        List<FolderNode> children = new ArrayList<>();

        /**
         * Constructor to create new FolderNodes from Folder records.
         * @param folder record of folder
         */
        FolderNode(Folder folder) {
            this.id = id;
            this.accountId = accountId;
            this.name = name;
            this.path = path;
        }

        /**
         * Convert FolderNode and its descendants to FolderTree record to ensure immutability.
         * @return FolderTree
         */
        app.markdownotes.data.FolderNode toFolderTree() {
            List<app.markdownotes.data.FolderNode> folderNodes = children.stream()
                    .map(FolderTreeBuilder.FolderNode::toFolderTree)
                    .collect(Collectors.toList());
            return new app.markdownotes.data.FolderNode(id, name, path, folderNodes);
        }
    }
}
