package org.example.neptuneojserver.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

public class ClipboardListener implements ClipboardOwner {

    public static void main(String[] args) {
        ClipboardListener listener = new ClipboardListener();
        listener.startListening();
    }

    public void startListening() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(this);
        regainOwnership(contents);
        System.out.println("Clipboard listening started...");
        while (true) {
            // Keep the program running
        }
    }

    public void regainOwnership(Transferable t) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(t, this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        try {
            Thread.sleep(100); // Wait a little for the contents to be updated
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String text = (String) clipboard.getData(DataFlavor.stringFlavor);
                System.out.println("Copied text: " + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        regainOwnership(clipboard.getContents(this));
    }
}
