/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screencapture.utils;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author Xmair
 */
public class TransferableImage implements Transferable {
    Image image;

    public TransferableImage(Image i) {
        this.image = i;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.imageFlavor) && this.image != null) {
            return this.image;
        }
        else {
            Utils.handleError(new UnsupportedFlavorException(flavor));
        }
        
        return null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = DataFlavor.imageFlavor;
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported( DataFlavor flavor ) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor flavor1 : flavors) {
            if (flavor.equals(flavor1)) {
                return true;
            }
        }

        return false;
    }
}
