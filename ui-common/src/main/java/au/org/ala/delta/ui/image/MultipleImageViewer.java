package au.org.ala.delta.ui.image;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;

import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.ui.image.ImagePanel.ScalingMode;

public class MultipleImageViewer extends JPanel {

	private static final long serialVersionUID = 6901754518169951771L;
	private int _selectedIndex;
    private CardLayout _layout;
    private ScalingMode _scalingMode;
    private List<ImageViewer> _imageViewers;
    private ImageSettings _imageSettings;
    private JPanel _contentPanel;

    public MultipleImageViewer(ImageSettings imageSettings) {
        _imageSettings = imageSettings;
        _layout = new CardLayout();
        _contentPanel = new JPanel();
        _contentPanel.setLayout(_layout);
        this.setLayout(new BorderLayout());
        this.add(_contentPanel, BorderLayout.CENTER);
        _imageViewers = new ArrayList<ImageViewer>();
        _selectedIndex = 0;
        _scalingMode = ScalingMode.FIXED_ASPECT_RATIO;
    }

    public void addImageViewer(ImageViewer viewer) {
        _imageViewers.add(viewer);
        _contentPanel.add(viewer, ImageUtils.subjectTextOrFileName(viewer.getViewedImage()));
    }

    /**
     * Displays the next image of the current subject (Character or Item)
     * @return false if there is no next image to move to, otherwise true
     */
    public boolean nextImage() {
        int nextIndex = _selectedIndex + 1;
        if (nextIndex < _imageViewers.size()) {
            _layout.next(_contentPanel);
            _selectedIndex = nextIndex;
            return true;
        }
        return false;
    }

    /**
     * Displays the previous image of the current subject (Character or Item)
     * @return false if there is no previous image to move to, otherwise true
     */
    public boolean previousImage() {
        int prevIndex = _selectedIndex - 1;
        if (prevIndex >= 0) {
            _layout.previous(_contentPanel);
            _selectedIndex = prevIndex;
            return true;
        }
        return false;
    }

    public void replaySound() {
        List<ImageOverlay> sounds = getVisibleViewer().getViewedImage().getSounds();
        for (ImageOverlay sound : sounds) {

            try {
                URL soundUrl = getVisibleViewer().getViewedImage().soundToURL(sound, _imageSettings.getImagePath());
                AudioPlayer.playClip(soundUrl);
            } catch (Exception e) {
                // TODO _messageHelper.errorPlayingSound(sound.overlayText);
                e.printStackTrace();
            }
        }
    }

    public void replayVideo() {
    }

    public void toggleScaling() {
        if (_scalingMode == ScalingMode.NO_SCALING) {
            setScalingMode(ScalingMode.FIXED_ASPECT_RATIO);
        } else {
            setScalingMode(ScalingMode.NO_SCALING);
        }
    }

    private void setScalingMode(ScalingMode mode) {
        if (mode == ScalingMode.NO_SCALING) {
            this.remove(_contentPanel);
            this.add(new JScrollPane(_contentPanel), BorderLayout.CENTER);
        } else if (_scalingMode == ScalingMode.NO_SCALING) {
            this.removeAll();
            this.add(_contentPanel, BorderLayout.CENTER);
        }
        _scalingMode = mode;
        for (ImageViewer viewer : _imageViewers) {
            viewer.setScalingMode(mode);
        }
        revalidate();
    }

    /**
     * Resizes this JInternalFrame so that the image is displayed at it's
     * natural size.
     */
    @Action
    public void fitToImage() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        parentWindow.pack();
    }

    /**
     * Displays the image in a full screen window. Clicking the mouse will
     * dismiss the window and return to normal mode.
     */
    @Action
    public void fullScreen() {
        // The assumption here is that this component has been added to a
        // dialog. We need the parent window of the dialog.
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Window ancestorWindow = SwingUtilities.getWindowAncestor(parentWindow);
        final Window w = new Window(ancestorWindow);
        w.setLayout(new BorderLayout());

        final ImageViewer viewer = getVisibleViewer();
        w.add(viewer, BorderLayout.CENTER);
        final GraphicsDevice gd = ancestorWindow.getGraphicsConfiguration().getDevice();

        w.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                w.dispose();
                gd.setFullScreenWindow(null);
                // add(viewer, key);
                // _layout.show(_contentPanel, key);
                revalidate();
            }
        });

        gd.setFullScreenWindow(w);
    }

    public ImageViewer getVisibleViewer() {
        if (_selectedIndex < _imageViewers.size()) {
            return _imageViewers.get(_selectedIndex);
        } else {
            return null;
        }
    }

}