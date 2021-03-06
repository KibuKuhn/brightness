package kibu.kuhn.brightness.ui.component;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.event.ActionEvent.ACTION_PERFORMED;
import static java.awt.event.KeyEvent.VK_ENTER;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class XButton extends JButton
{

    private static final long serialVersionUID = 1L;

    public XButton(Action action) {
        super(action);
        setBorderPainted(false);
        setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));
        setContentAreaFilled(false);
        addKeyListener(new EnterAction());
    }

    @Override
    public Dimension getPreferredSize() {
        var preferredSize = super.getPreferredSize();
        preferredSize.height = 32;
        if (preferredSize.width < 32) {
            preferredSize.width = 32;
        }
        return preferredSize;
    }

    @Override
    public Insets getInsets(Insets insets) {
        insets.left = insets.bottom = insets.right = insets.top = 2;
        return insets;
    }

    private class EnterAction extends KeyAdapter
    {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() != VK_ENTER) {
                return;
            }
            performEnterAction();
        }

    }

    private void performEnterAction() {
        getAction().actionPerformed(new ActionEvent(this, ACTION_PERFORMED, getActionCommand()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasFocus()) {
            getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }
    }

}