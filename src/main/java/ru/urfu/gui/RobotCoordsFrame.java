package ru.urfu.gui;

import ru.urfu.model.RobotModel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Окошко с координатами робота.
 * Координаты получаются из observable-объекта (модели).
 */
public class RobotCoordsFrame extends JInternalFrame implements Observer {
    final JLabel coordsLabel;
    public RobotCoordsFrame() {
        setTitle("Координаты робота");
        coordsLabel = new JLabel();
        coordsLabel.setVisible(true);
        add(coordsLabel);
    }

    /**
     * Метод, вызываемый, при обновлении observable.
     * Для получения информации об обновлении на observable-объект нужно
     * подписаться.
     */
    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof RobotModel) {
            final RobotModel robotModel = (RobotModel) observable;
            final String strCoords = String.format("%d %d",
                    (int)robotModel.getPositionX(),
                    (int)robotModel.getPositionY());
            coordsLabel.setText(strCoords);
        }
    }
}
