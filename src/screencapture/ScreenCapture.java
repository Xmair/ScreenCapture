/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screencapture;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import screencapture.utils.TransferableImage;
import screencapture.utils.Utils;

/**
 *
 * @author Xmair
 */
public class ScreenCapture implements ClipboardOwner {
    private static TrayIcon trayIcon;
    private static ScreenCapture screenCapture;
    
    public static void main(String[] args) {
        if (SystemTray.isSupported()) {
            ScreenCapture.screenCapture = new ScreenCapture();
            ScreenCapture.screenCapture.createTrayIcon();
            ScreenCapture.screenCapture.prepareKeyCapture();
        } else {
            Utils.handleError(new Exception("System tray not supported"));
            System.exit(0);
        }
    }
    
    public void createTrayIcon() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("resources/images/comedy.png"));
            ScreenCapture.trayIcon = new TrayIcon(image, "Screen Capturer");
            ScreenCapture.trayIcon.setImageAutoSize(true);
            tray.add(ScreenCapture.trayIcon);
            ScreenCapture.trayIcon.displayMessage("Screen Capturer", "Press F8 to capture a screenshot.", MessageType.INFO);
            
            MenuItem capture = new MenuItem("Capture Screenshot");
            capture.addActionListener((ActionEvent e) -> {
                this.capture();
            });
            
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener((ActionEvent e) -> {
                System.exit(0);
            });
            
            PopupMenu popupMenu = new PopupMenu("Capture");
            popupMenu.add(capture);
            popupMenu.add(exit);
            trayIcon.setPopupMenu(popupMenu);
        }
        catch(AWTException | HeadlessException e) {
            Utils.handleError(e);
        }
    }
    
    public void capture() {
        try {
            File screenshot = new File(Utils.generateScreenshotName());
            BufferedImage img = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(img, "png", screenshot);
            trayIcon.displayMessage("Screen Capturer", String.format("Screenshot saved to %s!", screenshot.getAbsolutePath()), MessageType.INFO);
            
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(new TransferableImage(img), this);
            System.gc();
        }
        catch (AWTException | HeadlessException | IOException ex) {
            Utils.handleError(ex);
        }
    }
    
    public void prepareKeyCapture() {
        try {
            LogManager.getLogManager().reset();
            Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
            
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new KeyBoardHook());
        }
        catch (NativeHookException ex) {
            Utils.handleError(ex);
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        
    }
    
    private class KeyBoardHook implements NativeKeyListener {
        @Override
	public void nativeKeyPressed(NativeKeyEvent e) {
            if (e.getKeyCode() == NativeKeyEvent.VC_F8) {
                ScreenCapture.screenCapture.capture();
            }
	}

        @Override
        public void nativeKeyTyped(NativeKeyEvent nke) {
            
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nke) {
            
        }
    }
}
